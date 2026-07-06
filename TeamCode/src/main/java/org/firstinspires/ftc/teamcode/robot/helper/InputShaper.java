package org.firstinspires.ftc.teamcode.robot.helper;

import org.firstinspires.ftc.teamcode.robot.constants.DriveTrainConstants;

public class InputShaper {
    public static double shape(double x) {
        return (1 - DriveTrainConstants.INPUT_SHAPING_E) * x + DriveTrainConstants.INPUT_SHAPING_E * x * x * x;
    }
}
