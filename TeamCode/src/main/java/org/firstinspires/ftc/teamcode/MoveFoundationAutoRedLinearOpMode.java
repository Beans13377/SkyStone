package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class MoveFoundationAutoRedLinearOpMode extends LinearOpMode {
    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;
    private Servo lfoundationater = null;
    private Servo rfoundationater = null;

    private static final double countsPerMotorRev = 383.6;
    private static final double wheelDiameter = 4;
    private static final double countsPerInch = countsPerMotorRev / (wheelDiameter * 3.1415);
    private ElapsedTime runtime = new ElapsedTime();
    private double driveSpeed = .5;
    private double turnSpeed = .5;

    @Override
    public void runOpMode() {

        leftDriveMiddle = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        lfoundationater = hardwareMap.servo.get("lfoundationater");
        rfoundationater = hardwareMap.servo.get("rfoundationater");
        leftDriveMiddle.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMiddle.setDirection(DcMotor.Direction.REVERSE);
        leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        encoderDrive(driveSpeed, -15, -15, 3);
        encoderDrive(turnSpeed, -4.1875, 4.1875, 3);
        encoderDrive(driveSpeed, -9, -9, 3);
        encoderDrive(turnSpeed, 4.1875, -4.1875, 3);
        encoderDrive(driveSpeed, -15, -15, 3);
        lfoundationater.setPosition(.1);
        rfoundationater.setPosition(.9);
        sleep(1000);
        encoderDrive(driveSpeed, 28, 28, 5);
        lfoundationater.setPosition(.9);
        rfoundationater.setPosition(.1);
        sleep(1000);
        encoderDrive(turnSpeed, -4.1875, 4.1875, 3);
        encoderDrive(driveSpeed, 45, 45, 3);
    }
    private void encoderDrive (double speed, double rightInches, double leftInches, double timeouts) {

        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {
            newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
            newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
            leftDriveMiddle.setTargetPosition(newLeftTarget);
            rightDriveMiddle.setTargetPosition(newRightTarget);
            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftDriveMiddle.setPower(Math.abs(speed));
            rightDriveMiddle.setPower(Math.abs(speed));
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

            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) < 50 &&
            Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) < 50 && opModeIsActive() && (runtime.seconds() < timeouts)) {
                rightDriveFront.setPower(0);
                leftDriveFront.setPower(0);
                rightDriveBack.setPower(0);
                leftDriveBack.setPower(0);
            }
            else if (opModeIsActive() && (runtime.seconds() < timeouts) &&
                    (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) < 10) &&
                    Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) < 10) {
                leftDriveMiddle.setPower(0);
                rightDriveMiddle.setPower(0);
                leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }
}