package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class SixMotorTeleOp extends OpMode {
    private static final double MAX_POWER = 1.0;
    private static final double MAX_TURNING_POWER = 0.5;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        leftDriveMiddle  = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront  = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack  = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        leftDriveMiddle.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMiddle.setDirection(DcMotor.Direction.REVERSE);
        leftDriveFront.setDirection(DcMotor.Direction.FORWARD);
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE);
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {

        double leftPower;
        double rightPower;

        leftPower = -gamepad1.left_stick_y;
        rightPower  =  -gamepad1.right_stick_y;

        if (leftPower == 0 && rightPower == 0) {
            setDriveMotorPower(0,0);
            telemetry.addData("Is Stopped", "True");
        } else if ((leftPower > 0 && rightPower > 0) || (leftPower < 0 && rightPower < 0)) {
            leftPower = calculateMotorPowerForDrivingStraight(leftPower);
            rightPower = calculateMotorPowerForDrivingStraight(rightPower);
            telemetry.addData("Is Turning", "False");
        } else {
            leftPower = calculateMotorPowerForTurning(leftPower);
            rightPower = calculateMotorPowerForTurning(rightPower);
            telemetry.addData("Is Turning", "True");
        }

        setDriveMotorPower(leftPower,rightPower);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    private double calculateMotorPowerForDrivingStraight (double Power) {
        return Range.clip(Power  * MAX_POWER,-1.0,1.0);
    }

    private double calculateMotorPowerForTurning (double Power) {
        return Range.clip(Power  * MAX_TURNING_POWER,-1.0,1.0);
    }

    private void setDriveMotorPower (double leftPower, double rightPower) {
        leftDriveMiddle.setPower(leftPower);
        rightDriveMiddle.setPower(rightPower);
        leftDriveFront.setPower(leftPower);
        rightDriveFront.setPower(rightPower);
        leftDriveBack.setPower(leftPower);
        rightDriveBack.setPower(rightPower);
    }
    @Override
    public void stop() {
    }
}
