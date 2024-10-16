package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.FieldCentricDriveTrain;
import org.firstinspires.ftc.teamcode.modules.*;

@TeleOp
public class TeleOpMain extends OpMode {

    private FieldCentricDriveTrain driveTrain;

    private LinearSlide slide;

    private Arm arm;

    private Intake intake;

    private final int armSpeed = 20;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driveTrain = new FieldCentricDriveTrain(this);

        slide = new LinearSlide(this);

        arm = new Arm(this);

        intake = new Intake(this);
    }

    @Override
    public void init_loop() {
        driveTrain.log();
        slide.log();
        arm.log();
        intake.log();
    }

    @Override
    public void start() {
        slide.setTargetHeight(LinearSlide.SLIDE_HEIGHT_MOVING);
        arm.setTargetRotation(Arm.ARM_ROTATION_MOVING);
        intake.holdWristRotation();

        driveTrain.resetRotation();
    }

    @Override
    public void loop() {

        if (gamepad1.back) {
            driveTrain.resetRotation();
        }

        driveTrain.setVelocity(gamepad1.left_stick_x * 0.5, -gamepad1.left_stick_y * 0.5, -gamepad1.right_stick_x * 0.5);

        slide.updateMotorPower();
        arm.updateMotorPowers();

        intake.holdWristRotation();
        if (gamepad1.a) {
            intake.grab();
        } else if (gamepad1.b) {
            intake.eject();
        } else if (gamepad1.x) {
            intake.settle();
        }

        if (gamepad2.a) {
            slide.setTargetHeight(LinearSlide.SLIDE_HEIGHT_MOVING);
            arm.setTargetRotation(Arm.ARM_ROTATION_MOVING);
        }
        else if (gamepad2.x) {
            slide.setTargetHeight(LinearSlide.SLIDE_HEIGHT_SCORING);
            arm.setTargetRotation(Arm.ARM_ROTATION_SCORING);
        }
        else if (gamepad2.b) {
            slide.setTargetHeight(LinearSlide.SLIDE_HEIGHT_INTAKE);
            arm.setTargetRotation(Arm.ARM_ROTATION_INTAKE);
        }

        if (gamepad2.y) {
            intake.turn();
        }

        arm.setTargetRotation(Math.min(Math.max(-120, arm.getTargetRotation() + armSpeed*gamepad2.left_stick_y), -20));

        driveTrain.log();
        slide.log();
        arm.log();
        intake.log();
        telemetry.addData("Gamepad1 Right Trigger: ", gamepad1.right_trigger);
    }

}
