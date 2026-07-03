package org.firstinspires.ftc.teamcode.tankDrive.helper;

import org.firstinspires.ftc.teamcode.tankDrive.constants.driveTrainConstants;

public class accelerator {

    private double prevTarget = 0.0;
    private double startRuntime = 0.0;
    private double startSpeed = 0.0;
    public double prevSpeed = 0.0;
    public accelerator() {
        reset();
    }

    public void reset() {
        prevTarget = 0.0;
        startRuntime = 0.0;
        startSpeed = 0.0;
        prevSpeed = 0.0;
    }

    public double shape(double target, double runtime) {
        return shape(target, runtime, driveTrainConstants.ACCELERATOR_TIME);
    }

    public double shape(double target, double runtime, double time) {

        if (Math.abs(target - prevTarget) > 0.1) {
            startRuntime = runtime;
            startSpeed = prevSpeed;
        }

        prevTarget = target;

        double t = (runtime - startRuntime) / time;
        t = Math.min(1.0, Math.max(0.0, t));

        double ease = t * t;

        prevSpeed = startSpeed + ease * (target - startSpeed);

        return prevSpeed;
    }
}