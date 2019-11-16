package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class SixMotorTeleOpNormalDrive extends OpMode{

    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;
    private DcMotor intake = null;

    private boolean lastPowerToggle = false;
    private boolean isIntakePowerOn = false;

    @Override
    public void init() {

        leftDriveMiddle  = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront  = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack  = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        intake = hardwareMap.get (DcMotor.class, "intake");
        leftDriveMiddle.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMiddle.setDirection(DcMotor.Direction.REVERSE);
        leftDriveFront.setDirection(DcMotor.Direction.FORWARD);
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE);
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {

        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        setDriveMotorPower(leftPower,rightPower);

        processIntake();
    }


    private void setDriveMotorPower (double leftPower, double rightPower) {
        leftDriveMiddle.setPower(leftPower);
        rightDriveMiddle.setPower(rightPower);
        leftDriveFront.setPower(leftPower);
        rightDriveFront.setPower(rightPower);
        leftDriveBack.setPower(leftPower);
        rightDriveBack.setPower(rightPower);
    }

    private void processIntake () {
        boolean current = gamepad1.b;
        if (current == lastPowerToggle) {
            return;
        } else if (lastPowerToggle == false && current == true) {
            toggleIntakePower ();
        }
        lastPowerToggle = current;

        telemetry.addData("power toggle",lastPowerToggle);
        telemetry.addData("Intake Motor Power",intake.getPower());
    }

    private void toggleIntakePower() {
        if (isIntakePowerOn == true) {
            isIntakePowerOn = false;
            intake.setPower(0);
        } else {
            isIntakePowerOn = true;
            intake.setPower(1);
        }
    }

}