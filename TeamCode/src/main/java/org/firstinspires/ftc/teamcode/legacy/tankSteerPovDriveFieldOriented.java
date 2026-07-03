package org.firstinspires.ftc.teamcode.legacy;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@TeleOp(name = "Field oriented Tank steer Arcade")

public class tankSteerPovDriveFieldOriented extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private BNO055IMU imu;
    
    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "left");
        rightMotor = hardwareMap.get(DcMotor.class, "right");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);
    }

    @Override
    public void loop() { 
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.left_stick_y;
        double trigger = gamepad1.right_trigger;
        double theta = imu.getAngularOrientation().firstAngle;

        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        double xField = x * cos - y * sin;
        double yField = x * sin + y * cos;

        double leftPower = yField + xField;
        double rightPower = yField - xField;

        double max = Math.max(1.0, Math.abs(leftPower) + Math.abs(rightPower));

        if(max > 1.0) {
            leftPower /= max;
            rightPower /= max;
        }

        trigger = min(1.0, max(0.0, trigger));
        leftPower = max(0.0, min(leftPower, 0.6 + trigger*0.4));
        rightPower = max(0.0, min(rightPower, 0.6 + trigger*0.4));

        leftMotor.setPower(leftPower / max);
        rightMotor.setPower(rightPower / max);
    }
}