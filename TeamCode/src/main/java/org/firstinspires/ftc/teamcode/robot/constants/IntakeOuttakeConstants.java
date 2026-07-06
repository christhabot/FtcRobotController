package org.firstinspires.ftc.teamcode.robot.constants;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class IntakeOuttakeConstants {
    public static final String MOTOR_NAME = "intake_motor";
    public static final DcMotor.Direction MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;

    public static final double MOTOR_POWER = 0.8;

    public static final boolean SECOND_GAMEPAD = false; // false: use gamepad 1, true: use gamepad 2

    public static final double SHAPING_DURATION = 0.5; // how many seconds to reach max speed
}
