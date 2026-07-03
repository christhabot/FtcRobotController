package org.firstinspires.ftc.teamcode.legacy;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@TeleOp(name = "Field oriented Tank steer Arcade")

public class tankSteerPovDriveAccelShaping extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    
    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "left_drive");
        rightMotor = hardwareMap.get(DcMotor.class, "right_drive ");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    private double curSpeed = 0.0;
    private double lastTarget = -1.0;
    private double rampStartSpeed = 0.0;
    private double rampStartTime = 0.0;
    private final double duration = 0.5;

    public double shape(double targ, double time) {
        if(abs(targ - lastTarget) >= 0.1) {
            rampStartTime = time;
            lastTarget = targ;
            rampStartSpeed = curSpeed;
        }
        double progress = (time - rampStartTime) / duration;
        double shape = rampStartSpeed + (targ - rampStartSpeed) * progress * progress;
        double result = max(0.0, min(1.0, shape));
        curSpeed = result;
        lastTarget = targ;
        return result;
    }

    @Override
    public void loop() { 
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.left_stick_y;

        double leftPower = y + x;
        double rightPower = y - x;

        double max = Math.max(1.0, Math.abs(leftPower) + Math.abs(rightPower));

        if(max > 1.0)
        {
            leftPower /= max;
            rightPower /= max;
        }

        leftPower = shape(leftPower, getRuntime());
        rightPower = shape(rightPower, getRuntime());

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}