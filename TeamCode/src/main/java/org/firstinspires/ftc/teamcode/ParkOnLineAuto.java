package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class ParkOnLineAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 374.325;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.2;
    static final double TURN_SPEED = 0.2;

    DcMotor leftDriveFront = null;
    DcMotor rightDriveFront = null;
    DcMotor leftDriveBack = null;
    DcMotor rightDriveBack = null;
    DcMotor rightDriveMiddle = null;
    DcMotor leftDriveMiddle = null;
    DcMotor intake = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftDriveFront = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        leftDriveMiddle = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        intake = hardwareMap.get(DcMotor.class, "intake");
        leftDriveFront.setDirection(DcMotor.Direction.REVERSE);
        rightDriveBack.setDirection(DcMotor.Direction.FORWARD);
        leftDriveBack.setDirection(DcMotor.Direction.REVERSE);
        rightDriveFront.setDirection(DcMotor.Direction.FORWARD);
        leftDriveMiddle.setDirection(DcMotor.Direction.REVERSE);
        rightDriveMiddle.setDirection(DcMotor.Direction.FORWARD);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        leftDriveMiddle.setPower(0);
        rightDriveMiddle.setPower(0);
        intake.setPower(0);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0", "Starting at %7d :%7d",
                leftDriveFront.getCurrentPosition(),
                rightDriveFront.getCurrentPosition(),
                leftDriveBack.getCurrentPosition(),
                rightDriveBack.getCurrentPosition(),
                leftDriveMiddle.getCurrentPosition(),
                telemetry.update());
        waitForStart();
        encoderDrive(DRIVE_SPEED, -9, -9, 2.0);
    }

    private void encoderDrive(double speed,
                              double leftInches, double rightInches,
                              double timeoutS) {
        int newLeftTargetFront;
        int newRightTargetFront;
        int newLeftTargetMiddle;
        int newRightTargetMiddle;
        int newLeftTargetBack;
        int newRightTargetBack;
        if (opModeIsActive()) {

            newLeftTargetFront = leftDriveFront.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTargetFront = rightDriveFront.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newLeftTargetBack = leftDriveBack.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTargetBack = rightDriveBack.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newLeftTargetMiddle = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTargetMiddle = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftDriveFront.setTargetPosition(newLeftTargetFront);
            rightDriveFront.setTargetPosition(newRightTargetFront);
            leftDriveBack.setTargetPosition(newLeftTargetBack);
            rightDriveBack.setTargetPosition(newRightTargetBack);
            leftDriveMiddle.setTargetPosition(newLeftTargetMiddle);
            rightDriveMiddle.setTargetPosition(newRightTargetMiddle);
            leftDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();
            leftDriveFront.setPower(Math.abs(speed));
            rightDriveFront.setPower(Math.abs(speed));
            leftDriveBack.setPower(Math.abs(speed));
            rightDriveBack.setPower(Math.abs(speed));
            leftDriveMiddle.setPower(Math.abs(speed));
            rightDriveMiddle.setPower(Math.abs(speed));
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftDriveFront.isBusy() && rightDriveFront.isBusy()&&  leftDriveBack.isBusy() && rightDriveBack.isBusy()&& rightDriveMiddle.isBusy() && leftDriveMiddle.isBusy())){
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTargetFront, newRightTargetFront, newLeftTargetMiddle, newRightTargetMiddle, newLeftTargetBack, newRightTargetBack);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftDriveFront.getCurrentPosition(),
                        rightDriveFront.getCurrentPosition(),
                        leftDriveBack.getCurrentPosition(),
                        rightDriveBack.getCurrentPosition(),
                        leftDriveMiddle.getCurrentPosition(),
                        rightDriveMiddle.getCurrentPosition());
                telemetry.update();
            }
            leftDriveFront.setPower(0);
            rightDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveBack.setPower(0);
            leftDriveMiddle.setPower(0);
            rightDriveMiddle.setPower(0);

            leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}