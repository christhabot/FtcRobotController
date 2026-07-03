package org.firstinspires.ftc.teamcode.tankDrive.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tankDrive.helper.accelerator;
import static org.firstinspires.ftc.teamcode.tankDrive.constants.intakeOuttakeConstants.*;

public class intakeOuttake {
    private final DcMotor motor;
    private double direction = 0.0;
    private final accelerator shaper;
    private final Telemetry telemetry;

    public intakeOuttake(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
        motor.setDirection(MOTOR_DIRECTION);
        shaper = new accelerator();
        this.telemetry = telemetry;
    }

    public void setDirection(double direction) {
        motor.setPower(direction);
    }

    public void loop(Gamepad gamepad1, Gamepad gamepad2, double runtime) {
        Gamepad gamepad = SECOND_GAMEPAD ? gamepad2 : gamepad1;
        if(gamepad.a) {
            direction = direction == 1.0 ? 0.0 : 1.0;
        }
        double curDir = direction;
        if(gamepad.x) {
            curDir = -1.0;
        }
        telemetry.addData("intake outtake direction", curDir);
        double finalDir = shaper.shape(curDir, runtime, 0.5);
        setDirection(finalDir * MOTOR_POWER);
        telemetry.addData("intake outtake shaped direction", finalDir);
    }
}
