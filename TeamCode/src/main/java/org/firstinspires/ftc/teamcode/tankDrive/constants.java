package org.firstinspires.ftc.teamcode.tankDrive;

import com.qualcomm.robotcore.hardware.DcMotor;

public class constants {
    public static final double MAX_MOTOR_POWER = 1.0;

    public static final String LEFT_MOTOR_NAME = "left_motor";
    public static final String RIGHT_MOTOR_NAME = "right_motor";
    public static final DcMotor.Direction LEFT_MOTOR_DIRECTION = DcMotor.Direction.FORWARD;
    public static final DcMotor.Direction RIGHT_MOTOR_DIRECTION = DcMotor.Direction.REVERSE;

    public static final boolean DO_DEADZONE = true;
    public static final double DEADZONE_SIZE = 0.1;
    public static final boolean INPUT_SHAPING = false;
    public static final double INPUT_SHAPING_E = 0.6; // (1-e)*x + ex^3    e=1 == x^3

    public static final boolean SLEW_RATE_LIMITING = true;
    public static final double SLEW_RATE_LIMITER_SPEED   = 1.0;   // overall responsiveness (1.0 = normal)
    public static final double SLEW_RATE_LIMITER_MAX_VEL = 10.0;   // max speed change per cycle
    public static final double SLEW_RATE_LIMITER_MAX_ACCEL = 10.0; // max velocity change per cycle
    public static final double SLEW_RATE_LIMITER_MAX_JERK  = 200.0; // max acceleration change per cycle

    public static final boolean DRIVE_TYPE = true; // false: pov drive, true: arcade drive

    public static final boolean TRIGGER_BOOST = false;
    public static final double BOOST_TRIGGER_LENGTH = 0.4; // no trigger = 1 - trigger length

    public static final boolean TRIGGER_SLOWDOWN  = false;
    public static final double SLOWDOWN_TRIGGER_DISTANCE = 0.3; // trigger = trigger distance

    public static final boolean FIELD_ORIENTED = false;
}