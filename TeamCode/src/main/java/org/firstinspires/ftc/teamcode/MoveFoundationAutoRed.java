package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class MoveFoundationAutoRed extends OpMode {
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
    private static final double driveSpeed = .5;
    private static final double turnSpeed = .4;
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
        rightDriveFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDriveBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDriveFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDriveBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void start () {
        capstoneRelease.setPosition(.6);
    }
    public void loop () {
        autonomous();
        telemetry.addData("auto stage", autoStage);
    }
    private void autonomous () {
        if (autoStage == 0) {
            encoderDrive(driveSpeed, -15, -15);
        }
        else if (autoStage == 1) {
            encoderDrive(turnSpeed, -13.1554, 13.1554);
        }
        else if (autoStage == 2) {
            encoderDrive(driveSpeed, -9, -9);
        }
        else if (autoStage == 3) {
            encoderDrive(turnSpeed, 13.1554, -13.1554);
        }
        else if (autoStage == 4) {
            encoderDrive(driveSpeed, -15, -15);
        }
        else if (autoStage == 5) {
            runtime.reset();
            lfoundationater.setPosition(.1);
            rfoundationater.setPosition(.9);
            autoStage++;
        }
        else if (autoStage == 6 && runtime.seconds() > 1) {
            encoderDrive(driveSpeed, 26, 26);
        }
        else if (autoStage == 7) {
            runtime.reset();
            lfoundationater.setPosition(.9);
            rfoundationater.setPosition(.1);
            autoStage++;
        }
        else if (autoStage == 8 && runtime.seconds() > 1) {
            encoderDrive(turnSpeed, -13.1554, 13.1554);
        }
        else if (autoStage == 9) {
            encoderDrive(driveSpeed, 30, 30);
        }
        else if (autoStage == 10) {
            encoderDrive(turnSpeed, -13, 13);
        }
    }
    private void encoderDrive (double speed, double rightInches, double leftInches) {
        int newLeftTarget;
        int newRightTarget;

        if (!encodersAreBusy) {

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


        if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 100 &&
                Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) >= 10
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 100 &&
                Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) >= 10) {
            rightDriveFront.setPower(0);
            rightDriveBack.setPower(0);
            leftDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 60
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 60) {

                if(leftInches < 0) {
                    leftDriveMiddle.setPower((-Math.abs(speed)) / 2);
                }
                else {
                    leftDriveMiddle.setPower((Math.abs(speed)) / 2);
                }
                if (rightInches < 0) {
                    rightDriveMiddle.setPower((-Math.abs(speed)) / 2);
                }
                else {
                    rightDriveMiddle.setPower((Math.abs(speed)) / 2);
                }
            }
        }
        else {
            if(leftInches < 0) {
                leftDriveFront.setPower(-Math.abs(speed));
                leftDriveBack.setPower(-Math.abs(speed));
            }
            else {
                leftDriveFront.setPower(Math.abs(speed));
                leftDriveBack.setPower(Math.abs(speed));
            }
            if (rightInches < 0) {
                rightDriveFront.setPower(-Math.abs(speed));
                rightDriveBack.setPower(-Math.abs(speed));
            }
            else {
                rightDriveFront.setPower(Math.abs(speed));
                rightDriveBack.setPower(Math.abs(speed));
            }
        }
        if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 10
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 10 && encodersAreBusy) {

            leftDriveMiddle.setPower(0);
            rightDriveMiddle.setPower(0);
            rightDriveFront.setPower(0);
            rightDriveBack.setPower(0);
            leftDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            encodersAreBusy = false;
            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            autoStage++;
        }
    }
}