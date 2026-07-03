package org.firstinspires.ftc.teamcode.tankDrive.helper;

import org.firstinspires.ftc.teamcode.tankDrive.constants;

public class slewRateLimiter {

    private double prevOutput = 0.0;
    private double prevVel = 0.0;
    private double prevAccel = 0.0;
    private double prevRuntime = 0.0;

    public slewRateLimiter() {
        reset();
    }

    public void reset() {
        prevOutput = 0.0;
        prevVel = 0.0;
        prevAccel = 0.0;
        prevRuntime = 0.0;
    }

    public double shape(double target, double runtime) {
        double deltaTime = runtime - prevRuntime;
        if (deltaTime < 0) deltaTime = 0;
        if (deltaTime == 0) {
            return prevOutput;
        }

        final double speed = constants.SLEW_RATE_LIMITER_SPEED;
        final double maxVel   = constants.SLEW_RATE_LIMITER_MAX_VEL * deltaTime;
        final double maxAccel = constants.SLEW_RATE_LIMITER_MAX_ACCEL * speed * deltaTime;
        final double maxJerk  = constants.SLEW_RATE_LIMITER_MAX_JERK  * speed * deltaTime;

        double error = target - prevOutput;
        double desiredVel = clamp(error, -maxVel, maxVel);

        double accel = desiredVel - prevVel;
        accel = clamp(accel, -maxAccel, maxAccel);

        double jerk = accel - prevAccel;
        jerk = clamp(jerk, -maxJerk, maxJerk);

        double newAccel = prevAccel + jerk;
        double newVel = prevVel + newAccel;
        newVel = clamp(newVel, -maxVel, maxVel);

        double newOutput = prevOutput + newVel;
        newOutput = clamp(newOutput, -1.0, 1.0);

        if (prevOutput == target) {
            prevOutput = target;
            prevVel = 0;
            prevAccel = 0;
            prevRuntime = runtime;
            return target;
        }

        if ((prevOutput < target && newOutput > target) ||
                (prevOutput > target && newOutput < target)) {
            newOutput = target;
            prevVel = 0;
            prevAccel = 0;
        } else {
            prevVel = newVel;
            prevAccel = newAccel;
        }

        prevOutput = newOutput;
        prevRuntime = runtime;

        return newOutput;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}