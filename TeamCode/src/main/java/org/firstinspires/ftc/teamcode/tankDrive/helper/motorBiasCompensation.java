package org.firstinspires.ftc.teamcode.tankDrive.helper;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class motorBiasCompensation {

    private final IMU imu;
    private double targetHeading = 0.0;
    private boolean active = false;
    private final Telemetry telemetry;

    public motorBiasCompensation(IMU imu, Telemetry telemetry) {
        this.imu = imu;
        this.telemetry = telemetry;
    }

    public void reset() {
        targetHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        active = true;
    }

    public double[] compensate(double turn, double forward) {
        // TODO: make better
        if (Math.abs(turn) > 1e-6) {
            active = false;
            return new double[]{turn, forward};
        }

        if (!active) {
            reset();
            return new double[]{turn, forward};
        }

        double currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        telemetry.addData("imu theta", currentHeading);
        double error = currentHeading - targetHeading;
        error = Math.atan2(Math.sin(error), Math.cos(error));

        double correctedTurn = -Math.abs(forward) * Math.sin(error);

        return new double[]{correctedTurn, forward};
    }
}