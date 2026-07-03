package org.firstinspires.ftc.teamcode.tankDrive.robot;

import static org.firstinspires.ftc.teamcode.tankDrive.constants.climberConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tankDrive.helper.accelerator;

public class climber {
    private final DcMotor motor;
    private final accelerator shaper;
    private final Telemetry telemetry;

    public climber(HardwareMap hardwareMap, Telemetry telemetry) {
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
        double curDir = (gamepad.left_bumper ? 1 : 0) - (gamepad.right_bumper ? 1 : 0);
        telemetry.addData("climber direction", curDir);
        double finalDir = shaper.shape(curDir, runtime, SHAPING_DURATION);
        setDirection(finalDir * MOTOR_POWER);
        telemetry.addData("climber shaped direction", finalDir);
    }
}
