package org.firstinspires.ftc.teamcode.tankDrive.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tankDrive.helper.accelerator;
import static org.firstinspires.ftc.teamcode.tankDrive.constants.intakeOuttakeConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class intakeOuttake {
    private final DcMotor motor;
    private double direction = 0.0;
    private final accelerator shaper;
    private final Telemetry telemetry;

    private Method aWasPressedMethod;

    public intakeOuttake(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
        motor.setDirection(MOTOR_DIRECTION);
        shaper = new accelerator();
        this.telemetry = telemetry;
        try {
            aWasPressedMethod = Gamepad.class.getMethod("aWasPressed");
        }
        catch (NoSuchMethodException e) {
            aWasPressedMethod = null;
        }
    }

    public void setDirection(double direction) {
        motor.setPower(direction);
    }

    private boolean aWasPressed(Gamepad gamepad) {
        if(aWasPressedMethod != null && gamepad != null) {
            try {
                return (boolean) aWasPressedMethod.invoke(gamepad);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        assert gamepad != null;
        return gamepad.a;
    }

    public void loop(Gamepad gamepad1, Gamepad gamepad2, double runtime) {
        Gamepad gamepad = SECOND_GAMEPAD ? gamepad2 : gamepad1;
        if(aWasPressed(gamepad)) {
            direction = direction == 1.0 ? 0.0 : 1.0;
        }
        double curDir = direction;
        if(gamepad.x) {
            curDir = -1.0;
        }
        telemetry.addData("intake outtake direction", curDir);
        double finalDir = shaper.shape(curDir, runtime, SHAPING_DURATION);
        setDirection(finalDir * MOTOR_POWER);
        telemetry.addData("intake outtake shaped direction", finalDir);
    }
}
