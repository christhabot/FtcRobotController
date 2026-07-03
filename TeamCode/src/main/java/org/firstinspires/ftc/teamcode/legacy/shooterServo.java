package org.firstinspires.ftc.teamcode.legacy;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Disabled
@TeleOp(name = "shooter servo")
public class shooterServo extends OpMode {
    private double openPos = 1.0;
    private double closedPos = 1.0;
    private Servo servo;

    public void setServo(boolean pos) {
        servo.setPosition(pos ? openPos : closedPos);
    }

    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "shooterServo");
    }

    @Override
    public void loop() {

    }
}