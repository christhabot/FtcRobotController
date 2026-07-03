package org.firstinspires.ftc.teamcode.tankDrive.helper;

import org.firstinspires.ftc.teamcode.tankDrive.constants.driveTrainConstants;

public class inputShaper {
    public static double shape(double x) {
        return (1 - driveTrainConstants.INPUT_SHAPING_E) * x + driveTrainConstants.INPUT_SHAPING_E * x * x * x;
    }
}
