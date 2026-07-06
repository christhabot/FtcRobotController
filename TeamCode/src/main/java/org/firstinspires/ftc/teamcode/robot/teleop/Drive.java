package org.firstinspires.ftc.teamcode.robot.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.TankDriveTrain;

@TeleOp(name = "Tank Drive Full", group = "TeleOp")
public class Drive extends LinearOpMode {

    @Override
    public void runOpMode() {
        TankDriveTrain tankDriveTrain = new TankDriveTrain(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            tankDriveTrain.loop(gamepad1, getRuntime());
            telemetry.update();
        }
    }
}