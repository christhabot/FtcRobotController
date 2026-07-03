package org.firstinspires.ftc.teamcode.tankDrive.constants;

import com.qualcomm.robotcore.hardware.DcMotor;

public class driveTrainConstants {
    public static final double MAX_MOTOR_POWER = 1.0;

    public static final String LEFT_MOTOR_NAME = "left_motor";
    public static final String RIGHT_MOTOR_NAME = "right_motor";
    public static final DcMotor.Direction LEFT_MOTOR_DIRECTION = DcMotor.Direction.FORWARD;
    public static final DcMotor.Direction RIGHT_MOTOR_DIRECTION = DcMotor.Direction.REVERSE;

    public static final boolean DO_DEADZONE = true; // enable deadzone
    public static final double DEADZONE_SIZE = 0.1;
    public static final boolean INPUT_SHAPING = true;
    public static final double INPUT_SHAPING_E = 0.6; // formula is (1-e)*x + ex^3, e=0 is linear, e=1 is cubic

    public static final boolean ACCELERATOR = true;
    public static final double ACCELERATOR_TIME = 0.1;


    public static final boolean DRIVE_TYPE = false;
    // false: pov drive - left stick controls forwards/backwards, right stick controls left/right
    // true: arcade drive - left stick controls forwards backwards left and right

    public static final boolean TRIGGER_BOOST = true;
    public static final double BOOST_TRIGGER_LENGTH = 0.4; // last x of drive is controlled by joystick
    // example: if boost trigger length is 0.4, no trigger sets max power to 0.6 (1 - 0.4),
    // full trigger sets max power to MAX_MOTOR_POWER, between values scale linearly

    public static final boolean TRIGGER_SLOWDOWN  = true;
    public static final double SLOWDOWN_TRIGGER_DISTANCE = 0.3; // minimum power when trigger is fully pressed
    // example: if slowdown trigger distance is 0.3, no trigger doesn't control max power
    // (it's controlled by boost trigger, or if disabled, max motor power
    // full trigger sets power to 0.3, between values scale linearly

    public static final boolean FIELD_ORIENTED = false;
}