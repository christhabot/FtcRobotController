package org.firstinspires.ftc.teamcode.legacy;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.*;

@TeleOp(name = "tank steer ultimate")
public class tankSteerUltimate extends OpMode {
    private final boolean driveType = true; // false: joystick drive, true: pov drive
    private final boolean fieldOriented = false;
    private final boolean accelShaping = true;
    private final double shapingDur = 0.1;
    private final boolean trigger = true;
    private final double triggerLength = 0.4; // no trigger = 1 - x
    private final boolean sharpTurn = false;
    private final boolean inputShaping = true;
    private final double shapinge = 0.55;
    private final boolean hexMotors = true;
    private final double hexPower = 1.0;
    private final boolean servos = true;

    private boolean intakeActive = false;

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private IMU imu;
    private DcMotor intakeMotor;
    private DcMotor outtakeMotor;
    private CRServo servo2;
    private Servo servo1;

    private static final int amount = 5;
    private final double[] curSpeeds = new double[amount];
    private final double[] lastTargets = new double[amount];
    private final double[] rampStartSpeeds = new double[amount];
    private final double[] rampStartTimes = new double[amount];

    {
        for (int i = 0; i < amount; i++) {
            lastTargets[i] = -1.0;
        }
    }

    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        if(fieldOriented) {
            imu = hardwareMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                    RevHubOrientationOnRobot.LogoFacingDirection.UP;

            RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                    RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

            RevHubOrientationOnRobot orientationOnRobot =
                    new RevHubOrientationOnRobot(logoDirection, usbDirection);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
        }

        if(hexMotors) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
            outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");
        }
        
        if(servos) {
            servo1 = hardwareMap.get(Servo.class, "servo_motor1");
            servo2 = hardwareMap.get(CRServo.class, "servo_motor2");
        }
    }

    public double shape(int index, double target, double time) {
        if (abs(target - lastTargets[index]) >= 0.1) {
            rampStartTimes[index] = time;
            rampStartSpeeds[index] = curSpeeds[index];
            lastTargets[index] = target;
        }

        double progress = (time - rampStartTimes[index]) / (index >= 2 ? 0.6 : shapingDur);
        if (progress >= 1.0) {
            curSpeeds[index] = target;
            lastTargets[index] = target;
            return target;
        }

        double shaped = rampStartSpeeds[index] + (target - rampStartSpeeds[index]) * progress * progress * (3 - 2 * progress);
        double result = max(-1.0, min(1.0, shaped));
        curSpeeds[index] = result;
        return result;
    }

    @Override
    public void loop() {
        boolean button = gamepad1.a;
        if (button) {
            intakeActive = !intakeActive;
        }
        boolean l1 = gamepad1.left_bumper;
        boolean r1 = gamepad1.right_bumper;

        double front = r1 ? -1 : intakeActive ? 1 : 0;
        double back = l1 ? -1 : intakeActive ? 1 : 0;

        if(hexMotors) {
            intakeMotor.setPower(shape(2, front * hexPower, getRuntime()));
            outtakeMotor.setPower(shape(3, back * hexPower, getRuntime()));
        }

        telemetry.addData("front", front);
        telemetry.addData("back", back);

        boolean servoButton = gamepad1.dpad_up;
        boolean servoButton2 = gamepad1.dpad_down;
        int pos = servoButton2 ? -1 : servoButton ? 1 : 0;
        boolean servoButton3 = gamepad1.dpad_left;
        boolean servoButton4 = gamepad1.dpad_right;
        int pos2 = servoButton3 ? -1 : servoButton4 ? 1 : 0;
        if(gamepad1.x) {
            pos = 1;
            pos2 = 1;
        }
        else if(gamepad1.y) {
            pos = -1;
            pos2 = -1;
        }
        if(servos) {
            //servo1.SetPosition(pos);
            servo2.setPower(pos2);
        }
        telemetry.addData("servo1", pos);
        telemetry.addData("servo2", pos2);

        double x = 0;
        if(driveType) x = gamepad1.right_stick_x;
        else x = gamepad1.left_stick_x;
        x = -x;
        double y = 0;
        y = gamepad1.left_stick_y;
        telemetry.addData("x", x);
        telemetry.addData("y", y);

        if(inputShaping) {
            x = (1 - shapinge) * x + shapinge * x * x * x;
            y = (1 - shapinge) * y + shapinge * y * y * y;
            telemetry.addData("shaped x", x);
            telemetry.addData("shaped y", y);
        }

//        if(fieldOriented) {
//            double theta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
//
//            double cos = Math.cos(theta);
//            double sin = Math.sin(theta);
//
//            double xField = x * cos + y * sin;
//            double yField = -x * sin + y * cos;
//            x = xField;
//            y = yField;
//        }

        if(sharpTurn) {
            if(abs(x) >= 0.5) {
                y = 0;
            }
        }

        double leftPower = y + x;
        double rightPower = y - x;

        double maxPower = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
        if (maxPower > 1.0) {
            leftPower /= maxPower;
            rightPower /= maxPower;
        }

        if(accelShaping) {
            leftPower = shape(0, leftPower, getRuntime());
            rightPower = shape(1, rightPower, getRuntime());
        }

        if(trigger) {
            double big = 1.0 - triggerLength + gamepad1.right_trigger * triggerLength;
            leftPower = min(max(leftPower, -big), big);
            rightPower = min(max(rightPower, -big), big);
        }

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.update();
    }
}