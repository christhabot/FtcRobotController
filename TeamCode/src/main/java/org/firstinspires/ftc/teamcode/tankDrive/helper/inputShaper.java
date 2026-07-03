package org.firstinspires.ftc.teamcode.tankDrive.helper;

import org.firstinspires.ftc.teamcode.tankDrive.constants;

public class inputShaper {
    public static double shape(double x) {
        return (1 - constants.INPUT_SHAPING_E) * x + constants.INPUT_SHAPING_E * x * x * x;
    }
}
