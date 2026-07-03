package org.firstinspires.ftc.teamcode.tankDrive.constants;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class climberConstants {
    public static final String MOTOR_NAME = "climb_motor";
    public static final DcMotor.Direction MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;

    public static final double MOTOR_POWER = 0.6;

    public static final boolean SECOND_GAMEPAD = true; // false: use gamepad 1, true: use gamepad 2

    public static final double SHAPING_DURATION = 1.0; // how many seconds to reach max speed
}
