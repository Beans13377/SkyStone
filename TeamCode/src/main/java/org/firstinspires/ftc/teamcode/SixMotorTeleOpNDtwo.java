package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class SixMotorTeleOpNDtwo extends OpMode {

    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;
    private DcMotor intake = null;
    private Servo outake = null;
    private Servo capstoneRelease = null;

    private boolean lastPowerToggle = false;
    private boolean isIntakePowerOn = false;
    private boolean lastDirectionToggle = false;
    private int intakeDirection = 1;
    private double currentIntakePower = 0;
    private static final double intakePower = 1.0;

//    private static final double countsPerMotorRev = 383.6;
//    private static final double wheelDiameter = 4;
//    private static final double countsPerInch = countsPerMotorRev / (wheelDiameter * 3.1415);
//
//    private boolean driveToDepotToggle = false;
//    private boolean encodersAreBusy = false;
//
//    private boolean firstPressx = true;
//    private int depotDrivingStage = 0;
//
//    private boolean driveToSkyBridgeToggle = false;
//    private int skyBridgeDrivingStage = 0;
//    private boolean firstPressY = true;

    @Override
    public void init () {

        leftDriveMiddle = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        intake = hardwareMap.get(DcMotor.class, "intake");
        outake = hardwareMap.servo.get ("outake");
        capstoneRelease = hardwareMap.servo.get ("capstoneRelease");
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
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
    @Override
    public void start () {
        capstoneRelease.setPosition(.6);
    }
    public void loop () {
        processIntake();
        intakeDirection();
        intake.setPower(currentIntakePower * intakeDirection);
        telemetry.addData("Intake Motor Direction", intakeDirection);

//        driveToDepot();
//        telemetry.addData("encodersAreBusy", encodersAreBusy);
//        telemetry.addData("driveToDepotToggle", driveToDepotToggle);
//        telemetry.addData("depotDrivingStage", depotDrivingStage);
//        telemetry.addData("rightEncoder", rightDriveMiddle.getCurrentPosition());
//        telemetry.addData("leftEncoder", leftDriveMiddle.getCurrentPosition());
//        telemetry.update();
//        driveToSkyBridge();
        outakeStone();
        double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);

            leftDriveMiddle.setPower(leftPower);
            rightDriveMiddle.setPower(rightPower);
            leftDriveFront.setPower(leftPower);
            rightDriveFront.setPower(rightPower);
            leftDriveBack.setPower(leftPower);
            rightDriveBack.setPower(rightPower);
        releaseCapstone();
    }

    private void processIntake() {
        boolean current = gamepad1.b;
        if (current == lastPowerToggle) {
            return;
        } else if (!lastPowerToggle && current) {
            toggleIntakePower();
        }
        lastPowerToggle = current;

    }

    private void toggleIntakePower() {
        if (isIntakePowerOn) {
            isIntakePowerOn = false;
            currentIntakePower = 0;
        } else {
            isIntakePowerOn = true;
            currentIntakePower = (intakeDirection * intakePower);
        }
    }

    private void intakeDirection() {
        boolean current = gamepad1.a;
        if (current == lastDirectionToggle) {
            return;
        } else if (!lastDirectionToggle && current) {
            toggleIntakeDirection();
        }
        lastDirectionToggle = current;

    }

    private void toggleIntakeDirection() {
        if (intakeDirection == 1) {
            intakeDirection = -1;
        } else {
            intakeDirection = 1;
        }
    }


//    private void driveToDepot () {
//        final double driveSpeed = 1;
//        final double turnSpeed = 1;
//        if (gamepad1.x) {
//            if (firstPressx) {
//                if (depotDrivingStage == 0) {
//                    driveToDepotToggle = true;
//                }
//                firstPressx = false;
//            }
//        }
//        else {
//            firstPressx = true;
//        }
//
//        if (driveToDepotToggle && !encodersAreBusy) {
//            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            depotDrivingStage = 1;
//            driveToDepotToggle = false;
//        }
//
//        if (Math.abs(gamepad1.left_stick_x) > .1 || Math.abs(gamepad1.left_stick_y) > .1 || Math.abs(gamepad1.right_stick_x) > .1 || Math.abs(gamepad1.right_stick_y) >.1) {
//            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            double drive = -gamepad1.left_stick_y;
//            double turn = gamepad1.right_stick_x;
//
//            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
//            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
//
//            leftDriveMiddle.setPower(leftPower);
//            rightDriveMiddle.setPower(rightPower);
//            leftDriveFront.setPower(leftPower);
//            rightDriveFront.setPower(rightPower);
//            leftDriveBack.setPower(leftPower);
//            rightDriveBack.setPower(rightPower);
//
//            depotDrivingStage = 0;
//
//            encodersAreBusy = false;
//            }
//
//        if (depotDrivingStage == 1) {
//            encoderDrive(driveSpeed, 21, 21); // maybe change slip #
//        }
//        else if (depotDrivingStage == 2) {
//            encoderDrive(turnSpeed, -4.8 *3.1415 +2, 4.8 *3.1415 -2);
//        }
//        else if (depotDrivingStage == 3) {
//            encoderDrive(driveSpeed, 110 -(driveSpeed * 12), 110 -(driveSpeed * 12)); // maybe change slip#
//        }
//        else if (!encodersAreBusy) {
//            depotDrivingStage = 0;
//
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            double drive = -gamepad1.left_stick_y;
//            double turn = gamepad1.right_stick_x;
//
//            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
//            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
//
//            leftDriveMiddle.setPower(leftPower);
//            rightDriveMiddle.setPower(rightPower);
//            leftDriveFront.setPower(leftPower);
//            rightDriveFront.setPower(rightPower);
//            leftDriveBack.setPower(leftPower);
//            rightDriveBack.setPower(rightPower);
//        }
//
//    }
//
//    private void driveToSkyBridge () {
//        final double driveSpeed = 1;
//        final double turnSpeed = 1;
//        if (gamepad1.y) {
//            if (firstPressY) {
//                if (skyBridgeDrivingStage == 0) {
//                    driveToSkyBridgeToggle = true;
//                }
//                firstPressY = false;
//            }
//        }
//        else {
//            firstPressY = true;
//        }
//
//        if (driveToSkyBridgeToggle && !encodersAreBusy) {
//            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            skyBridgeDrivingStage = 1;
//            driveToSkyBridgeToggle = false;
//        }
//
//        if (Math.abs(gamepad1.left_stick_x) > .1 || Math.abs(gamepad1.left_stick_y) > .1 || Math.abs(gamepad1.right_stick_x) > .1 || Math.abs(gamepad1.right_stick_y) >.1) {
//            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            double drive = -gamepad1.left_stick_y;
//            double turn = gamepad1.right_stick_x;
//
//            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
//            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
//
//            leftDriveMiddle.setPower(leftPower);
//            rightDriveMiddle.setPower(rightPower);
//            leftDriveFront.setPower(leftPower);
//            rightDriveFront.setPower(rightPower);
//            leftDriveBack.setPower(leftPower);
//            rightDriveBack.setPower(rightPower);
//
//            skyBridgeDrivingStage = 0;
//
//            encodersAreBusy = false;
//        }
//
//        if (skyBridgeDrivingStage == 1) {
//            encoderDrive(driveSpeed, -85 -(driveSpeed * -14), -85 -(driveSpeed * -14)); // maybe change slip #
//        }
//        else if (skyBridgeDrivingStage == 2) {
//            encoderDrive(turnSpeed, 4.4 *3.1415 -2, -4.4 *3.1415 +2);
//        }
//        else if (skyBridgeDrivingStage == 3) {
//            encoderDrive(driveSpeed, -35, -35); // maybe change slip#
//        }
//        else if (!encodersAreBusy){
//            skyBridgeDrivingStage = 0;
//
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            double drive = -gamepad1.left_stick_y;
//            double turn = gamepad1.right_stick_x;
//
//            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
//            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
//
//            leftDriveMiddle.setPower(leftPower);
//            rightDriveMiddle.setPower(rightPower);
//            leftDriveFront.setPower(leftPower);
//            rightDriveFront.setPower(rightPower);
//            leftDriveBack.setPower(leftPower);
//            rightDriveBack.setPower(rightPower);
//        }
//
//    }
//
//    private void encoderDrive (double speed, double leftInches, double rightInches) {
//        int newLeftTarget;
//        int newRightTarget;
//
//        if (!encodersAreBusy) {
//            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
//            newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
//            leftDriveMiddle.setTargetPosition(newLeftTarget);
//            rightDriveMiddle.setTargetPosition(newRightTarget);
//            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            leftDriveMiddle.setPower(Math.abs(speed));
//            rightDriveMiddle.setPower(Math.abs(speed));
//
//            if(leftInches < 0) {
//                leftDriveFront.setPower(-Math.abs(speed));
//                leftDriveBack.setPower(-Math.abs(speed));
//            }
//            else {
//                leftDriveFront.setPower(Math.abs(speed));
//                leftDriveBack.setPower(Math.abs(speed));
//            }
//            if (rightInches < 0) {
//                rightDriveFront.setPower(-Math.abs(speed));
//                rightDriveBack.setPower(-Math.abs(speed));
//            }
//            else {
//                rightDriveFront.setPower(Math.abs(speed));
//                rightDriveBack.setPower(Math.abs(speed));
//            }
//
//            encodersAreBusy = true;
//        }
//
//        if(encodersAreBusy) {
//            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 230
//                    && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 230) { // maybe make 200 bigger
//                leftDriveFront.setPower(0);
//                rightDriveFront.setPower(0);
//                leftDriveBack.setPower(0);
//                rightDriveBack.setPower(0);
//            }
//
//            if(leftInches==rightInches) {
//
//                if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 20
//                        && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 20) {
//
//                    leftDriveMiddle.setPower(0);
//                    rightDriveMiddle.setPower(0);
//                    leftDriveFront.setPower(0);
//                    rightDriveFront.setPower(0);
//                    leftDriveBack.setPower(0);
//                    rightDriveBack.setPower(0);
//
//                    leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//                    encodersAreBusy = false;
//                    if (depotDrivingStage >= 1){
//                        depotDrivingStage++;
//                    }
//                    else {
//                        skyBridgeDrivingStage++;
//                    }
//                }
//            }
//            else {
//                if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 10
//                        && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 10) {
//
//                    leftDriveMiddle.setPower(0);
//                    rightDriveMiddle.setPower(0);
//                    leftDriveFront.setPower(0);
//                    rightDriveFront.setPower(0);
//                    leftDriveBack.setPower(0);
//                    rightDriveBack.setPower(0);
//
//                    leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                    intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//                    encodersAreBusy = false;
//                    if (depotDrivingStage >= 1){
//                        depotDrivingStage++;
//                    }
//                    else {
//                        skyBridgeDrivingStage++;
//                    }
//                }
//            }
//        }
//    }
    private void outakeStone () {
        if (gamepad1.right_bumper) {
            outake.setPosition(1);
            telemetry.addData("outake", "closed");
        }
        else if (gamepad1.left_bumper){
            outake.setPosition(.6);
            telemetry.addData("outake", "open");
        }
    }
    private void releaseCapstone () {
        if (gamepad1.x) {
            capstoneRelease.setPosition(.6);
        }
        else if (gamepad1.y) {
            capstoneRelease.setPosition(.1);
        }
    }
}