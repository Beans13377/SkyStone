package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class MoveFoundationAutoRedOldVersion extends OpMode {
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
    private static double driveSpeed = .15;
    private static double turnSpeed = .075;
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
        telemetry.addData("auto stage", autoStage);
        telemetry.update();
    }
    private void autonomous () {
        if(autoStage == 0) {
            encoderDrive(driveSpeed, -30, -30);
        }
        else if (autoStage == 1) {
            runtime.reset();
            lfoundationater.setPosition(.1);
            rfoundationater.setPosition(.9);
            autoStage ++;
        }
        else if (autoStage == 2 && runtime.seconds() > 1) {
            encoderDrive(driveSpeed, 28, 28);
        }
        else if (autoStage == 3) {
            encoderDrive(turnSpeed, 2,0);
        }
        else if (autoStage == 4) {
            runtime.reset();
            lfoundationater.setPosition(.9);
            rfoundationater.setPosition(.1);
            autoStage ++;
        }
        else if (autoStage == 5 && runtime.seconds() > 1) {
            encoderDrive(turnSpeed, -4.1875, 4.1875);
        }
        else if (autoStage == 6) {
            encoderDrive (driveSpeed, 12, 12);
        }
        else if (autoStage == 7) {
            encoderDrive(turnSpeed, -4.1875, 4.1875);
        }
        else if (autoStage == 8) {
            encoderDrive(driveSpeed, 24, 24);
        }
        else if (autoStage == 9) {
            encoderDrive(turnSpeed, -4.1875, 4.1875);
        }
        else if (autoStage == 10) {
            encoderDrive(driveSpeed, 12, 12);
        }
        else if (autoStage == 11) {
            encoderDrive(turnSpeed, 4.1875, -4.1875);
        }
        else if (autoStage == 12) {
            runtime.reset();
            lfoundationater.setPosition(.1);
            rfoundationater.setPosition(.9);
            autoStage ++;
        }
        else if (autoStage == 13 && runtime.seconds() > 1) {
            encoderDrive(driveSpeed, -24, -24);
        }
        else if (autoStage == 14) {
            runtime.reset();
            lfoundationater.setPosition(.9);
            rfoundationater.setPosition(.1);
            autoStage ++;
        }
        else if (autoStage == 15 && runtime.seconds() > 1) {
            encoderDrive(turnSpeed, 4.1875, -4.1875);
        }
        else if (autoStage == 16) {
            encoderDrive(driveSpeed, 24, 24);
        }
        else if (autoStage == 17) {
            encoderDrive(turnSpeed, 4.1875, -4.1875);
        }
        else if (autoStage == 18) {
            encoderDrive(driveSpeed, 19, 19);
        }
        else if (autoStage == 19) {
            encoderDrive(turnSpeed, -4.1875, 4.1875);
        }
        else if (autoStage == 20) {
            encoderDrive(driveSpeed, 24,24);
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


        if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 500
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 500) {
            driveSpeed = .15;
            rightDriveFront.setPower(0);
            rightDriveBack.setPower(0);
            leftDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 90
                    && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 90) {
                driveSpeed = .1;
            }
    }
        else {
            driveSpeed = .15;
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
        if (autoStage == 0 && leftDriveMiddle.getTargetPosition() > leftDriveMiddle.getCurrentPosition()
                && rightDriveMiddle.getTargetPosition() > rightDriveMiddle.getCurrentPosition()) {
            leftDriveMiddle.setPower(0);
            rightDriveMiddle.setPower(0);
            leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            encodersAreBusy = false;
            autoStage ++;
        }
        else if (encodersAreBusy) {
            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 10
                    && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 10) {

                leftDriveMiddle.setPower(0);
                rightDriveMiddle.setPower(0);
                leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                encodersAreBusy = false;
                autoStage ++;
            }
        }
    }
}