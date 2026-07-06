package org.firstinspires.ftc.teamcode.robot.subsystems;

import static org.firstinspires.ftc.teamcode.robot.constants.ClimberConstants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.helper.Accelerator;

public class Climber {
    private final DcMotor motor;
    private final Accelerator accelerator;
    private final Telemetry telemetry;

    public Climber(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
        motor.setDirection(MOTOR_DIRECTION);
        accelerator = new Accelerator();
        this.telemetry = telemetry;
    }

    public void setDirection(double direction) {
        motor.setPower(direction);
    }

    public void loop(Gamepad gamepad1, Gamepad gamepad2, double runtime) {
        Gamepad gamepad = SECOND_GAMEPAD ? gamepad2 : gamepad1;
        double curDir = (gamepad.left_bumper ? 1 : 0) - (gamepad.right_bumper ? 1 : 0);
        telemetry.addData("climber direction", curDir);
        double finalDir = accelerator.shape(curDir, runtime, SHAPING_DURATION);
        setDirection(finalDir * MOTOR_POWER);
        telemetry.addData("climber shaped direction", finalDir);
    }
}
