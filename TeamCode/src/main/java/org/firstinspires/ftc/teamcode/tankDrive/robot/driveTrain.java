package org.firstinspires.ftc.teamcode.tankDrive.robot;

import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.tankDrive.constants;
import org.firstinspires.ftc.teamcode.tankDrive.helper.slewRateLimiter;
import org.firstinspires.ftc.teamcode.tankDrive.helper.inputShaper;

public class driveTrain {
    private final DcMotor leftMotor, rightMotor;
    private final Telemetry telemetry;
    private IMU imu;
    private final slewRateLimiter leftLimiter, rightLimiter;

    public driveTrain(HardwareMap hardwareMap, Telemetry telemetry) {
        leftMotor = hardwareMap.get(DcMotor.class, constants.LEFT_MOTOR_NAME);
        rightMotor = hardwareMap.get(DcMotor.class, constants.RIGHT_MOTOR_NAME);
        leftMotor.setDirection(constants.LEFT_MOTOR_DIRECTION);
        rightMotor.setDirection(constants.RIGHT_MOTOR_DIRECTION);
        this.telemetry = telemetry;
        leftLimiter = new slewRateLimiter();
        rightLimiter = new slewRateLimiter();
        if(constants.FIELD_ORIENTED) {
            imu = hardwareMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                    RevHubOrientationOnRobot.LogoFacingDirection.UP;

            RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                    RevHubOrientationOnRobot.UsbFacingDirection.LEFT;

            RevHubOrientationOnRobot orientationOnRobot =
                    new RevHubOrientationOnRobot(logoDirection, usbDirection);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
        }
    }

    public void loop(Gamepad gamepad, double runtime) {
        double forward = -gamepad.left_stick_y;
        double turn = constants.DRIVE_TYPE ? gamepad.left_stick_x : gamepad.right_stick_x;
        telemetry.addData("joystick y", forward);
        telemetry.addData("joystick x", turn);

        if(constants.DO_DEADZONE) {
            if(abs(forward) < constants.DEADZONE_SIZE) forward = 0;
            if(abs(turn) < constants.DEADZONE_SIZE) turn = 0;
        }

        if(constants.INPUT_SHAPING) {
            forward = inputShaper.shape(forward);
            turn = inputShaper.shape(turn);
            telemetry.addData("input shaped y", forward);
            telemetry.addData("input shaped x", turn);
        }

        if(constants.FIELD_ORIENTED) {
            double theta = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            telemetry.addData("theta", theta);

            double cos = Math.cos(theta);
            double sin = Math.sin(theta);

            double xField = turn * cos + forward * sin;
            double yField = -turn * sin + forward * cos;

            turn = -xField;
            forward = -yField;

            telemetry.addData("field oriented y", forward);
            telemetry.addData("field oriented x", turn);
        }

        double leftPower = forward + turn;
        double rightPower = forward - turn;

        double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
        double left = leftPower / max;
        double right = rightPower / max;

        // TODO: replace max limits with linear transforming
        double big = 1.0;
        if(constants.TRIGGER_BOOST && constants.TRIGGER_SLOWDOWN) {
            double neutral = 1 - constants.BOOST_TRIGGER_LENGTH;
            double add = gamepad.right_trigger * (1 - constants.BOOST_TRIGGER_LENGTH);
            double reduce = gamepad.left_trigger * (1 - constants.BOOST_TRIGGER_LENGTH - constants.SLOWDOWN_TRIGGER_DISTANCE);
            big = neutral + add - reduce;
        }
        else if(constants.TRIGGER_BOOST) {
            big = (1 - constants.BOOST_TRIGGER_LENGTH) + constants.BOOST_TRIGGER_LENGTH * gamepad.right_trigger;
        }
        else if(constants.TRIGGER_SLOWDOWN) {
            double neutral = 1.0;
            double range = neutral - constants.SLOWDOWN_TRIGGER_DISTANCE;
            big = neutral - range * gamepad.left_trigger;
        }
        big = Math.min(constants.MAX_MOTOR_POWER, Math.abs(big));
        left = Math.max(-big, Math.min(big, left));
        right = Math.max(-big, Math.min(big, right));


        if(constants.SLEW_RATE_LIMITING) {
            left = leftLimiter.shape(left, runtime);
            right = rightLimiter.shape(right, runtime);
        }
        leftMotor.setPower(left);
        rightMotor.setPower(right);
        telemetry.addData("left power", left);
        telemetry.addData("right power", right);
    }
}