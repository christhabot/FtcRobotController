package org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain;

import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.robot.constants.DriveTrainConstants.*;
import org.firstinspires.ftc.teamcode.robot.helper.Accelerator;
import org.firstinspires.ftc.teamcode.robot.helper.InputShaper;
import org.firstinspires.ftc.teamcode.robot.helper.MotorBiasCompensation;

public class TankDriveTrain {
    private final DcMotor leftMotor, rightMotor;
    private final Telemetry telemetry;
    private final IMU imu;
    private final Accelerator leftAccelerator, rightAccelerator;
    private final MotorBiasCompensation compensator;

    public TankDriveTrain(HardwareMap hardwareMap, Telemetry telemetry) {
        leftMotor = hardwareMap.get(DcMotor.class, LEFT_MOTOR_NAME);
        rightMotor = hardwareMap.get(DcMotor.class, RIGHT_MOTOR_NAME);
        leftMotor.setDirection(LEFT_MOTOR_DIRECTION);
        rightMotor.setDirection(RIGHT_MOTOR_DIRECTION);
        this.telemetry = telemetry;
        leftAccelerator = new Accelerator();
        rightAccelerator = new Accelerator();
        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;

        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

        RevHubOrientationOnRobot orientationOnRobot =
                new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
        compensator = new MotorBiasCompensation(imu, telemetry);
    }

    public void loop(Gamepad gamepad, double runtime) {
        double forward = gamepad.left_stick_y;
        double turn = DRIVE_TYPE ? gamepad.left_stick_x : gamepad.right_stick_x;
        turn = -turn;
        telemetry.addData("joystick y", forward);
        telemetry.addData("joystick x", turn);

        if(DO_DEADZONE) {
            if(abs(forward) < DEADZONE_SIZE) forward = 0;
            if(abs(turn) < DEADZONE_SIZE) turn = 0;
        }

        if(INPUT_SHAPING) {
            forward = InputShaper.shape(forward);
            turn = InputShaper.shape(turn);
            telemetry.addData("input shaped y", forward);
            telemetry.addData("input shaped x", turn);
        }

        if(FIELD_ORIENTED) {
            double theta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            telemetry.addData("theta", theta);

            double cos = Math.cos(theta);
            double sin = Math.sin(theta);

            double xField = turn * cos + forward * sin;
            double yField = -turn * sin + forward * cos;

            turn = xField;
            forward = yField;

            telemetry.addData("imu", theta);
            telemetry.addData("field oriented y", forward);
            telemetry.addData("field oriented x", turn);
        }
        else if(MOTOR_BIAS_COMPENSATION) {
            double[] result = compensator.compensate(turn, forward);
            turn = result[0];
            forward = result[1];

            telemetry.addData("compensated y", forward);
            telemetry.addData("compensated x", turn);
        }

        double leftPower = forward + turn;
        double rightPower = forward - turn;

        double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
        double left = leftPower / max;
        double right = rightPower / max;

        double big = 1.0;
        if(TRIGGER_BOOST && TRIGGER_SLOWDOWN) {
            double neutral = 1 - BOOST_TRIGGER_LENGTH;
            double add = gamepad.right_trigger * (1 - BOOST_TRIGGER_LENGTH);
            double reduce = gamepad.left_trigger * (1 - BOOST_TRIGGER_LENGTH - SLOWDOWN_TRIGGER_DISTANCE);
            big = neutral + add - reduce;
        }
        else if(TRIGGER_BOOST) {
            big = (1 - BOOST_TRIGGER_LENGTH) + BOOST_TRIGGER_LENGTH * gamepad.right_trigger;
        }
        else if(TRIGGER_SLOWDOWN) {
            double neutral = 1.0;
            double range = neutral - SLOWDOWN_TRIGGER_DISTANCE;
            big = neutral - range * gamepad.left_trigger;
        }
        big = Math.min(MAX_MOTOR_POWER, Math.abs(big));
        telemetry.addData("max drive speed", big);

        left *= big;
        right *= big;

        if(ACCELERATOR) {
            left = leftAccelerator.shape(left, runtime);
            right = rightAccelerator.shape(right, runtime);
        }

        // right *= 0.8; // systematic motor error simulation

        leftMotor.setPower(left);
        rightMotor.setPower(right);
        telemetry.addData("left power", left);
        telemetry.addData("right power", right);
    }
}