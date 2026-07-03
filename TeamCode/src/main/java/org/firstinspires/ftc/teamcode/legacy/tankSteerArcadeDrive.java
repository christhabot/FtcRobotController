package org.firstinspires.ftc.teamcode.legacy;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@TeleOp(name = "Field oriented Tank steer Arcade")

public class tankSteerArcadeDrive extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    
    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "left");
        rightMotor = hardwareMap.get(DcMotor.class, "right");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() { 
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;

        double leftPower = y + x;
        double rightPower = y - x;

        double max = Math.max(1.0, Math.abs(leftPower) + Math.abs(rightPower));

        leftMotor.setPower(leftPower / max);
        rightMotor.setPower(rightPower / max);
    }
}