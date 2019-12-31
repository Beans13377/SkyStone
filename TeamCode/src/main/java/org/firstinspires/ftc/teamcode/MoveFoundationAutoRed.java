package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class MoveFoundationAutoRed extends OpMode{
    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;
    private Servo lfoundationater = null;
    private Servo rfoundationater = null;
    private Servo capstoneRelease = null;

    private static final double countsPerMotorRev = 383.6;
    private static final double wheelDiameter = 4;
    private static final double countsPerInch = countsPerMotorRev / (wheelDiameter * 3.1415);
    private static double driveSpeed = .6;
    private static double turnSpeed = .5;
    private boolean encodersAreBusy = false;
    private ElapsedTime runtime = new ElapsedTime();

    private int autoStage = 0;

    @Override
    public void init () {

        leftDriveMiddle = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        lfoundationater = hardwareMap.servo.get("lfoundationater");
        rfoundationater = hardwareMap.servo.get("rfoundationater");
        capstoneRelease = hardwareMap.servo.get ("capstoneRelease");
        leftDriveMiddle.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMiddle.setDirection(DcMotor.Direction.REVERSE);
        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void start () {
        capstoneRelease.setPosition(.6);
    }
    public void loop () {
        autonomous();
    }
    private void autonomous () {
        if(autoStage == 0) {
            encoderDrive(driveSpeed, -41, -41);
        }
        else if (autoStage == 1) {
            runtime.reset();
            lfoundationater.setPosition(.1);
            rfoundationater.setPosition(.9);
            autoStage ++;
        }
        else if (autoStage == 2 && runtime.seconds() > 1.5) {
            encoderDrive(driveSpeed, 32, 32);
        }
    }
    private void encoderDrive (double speed, double rightInches, double leftInches) {
        int newLeftTarget;
        int newRightTarget;

        if (!encodersAreBusy) {
            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
            newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
            leftDriveMiddle.setTargetPosition(newLeftTarget);
            rightDriveMiddle.setTargetPosition(newRightTarget);
            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDriveMiddle.setPower(Math.abs(speed));
            rightDriveMiddle.setPower(Math.abs(speed));

            encodersAreBusy = true;
        }

        if (autoStage == 0 && Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 360
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 360) {
            driveSpeed = .2;
        }
        if (encodersAreBusy) {
            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 10
                    && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 10) {

                leftDriveMiddle.setPower(0);
                rightDriveMiddle.setPower(0);
                leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                encodersAreBusy = false;
                autoStage ++;
            }
        }
    }
}
