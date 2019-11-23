package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class SixMotorTeleOpNormalDrive extends OpMode{

    private static final double intakePower = 1.0;

    private DcMotor leftDriveMiddle = null;
    private DcMotor rightDriveMiddle = null;
    private DcMotor leftDriveFront = null;
    private DcMotor leftDriveBack = null;
    private DcMotor rightDriveFront = null;
    private DcMotor rightDriveBack = null;
    private DcMotor intake = null;

    private boolean lastPowerToggle = false;
    private boolean isIntakePowerOn = false;
    private boolean lastDirectionToggle = false;
    private int intakeDirection = 1;
    private double currentIntakePower = 0;

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
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
        intakeDirection();
        intake.setPower(currentIntakePower * intakeDirection);

        telemetry.addData("Is intake on", lastPowerToggle);
        telemetry.addData("Intake Motor Power", "%.2f", intake.getPower());
        telemetry.addData("Is intake forward",lastDirectionToggle);
        telemetry.addData("Intake Motor Direction", intakeDirection);
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

    }

    private void toggleIntakePower() {
        if (isIntakePowerOn == true) {
            isIntakePowerOn = false;
            currentIntakePower = 0;
        } else {
            isIntakePowerOn = true;
            currentIntakePower = (intakeDirection * intakePower);
        }
    }

    private void intakeDirection () {
        boolean current = gamepad1.y;
        if (current == lastDirectionToggle) {
            return;
        } else if (lastDirectionToggle == false && current == true) {
            toggleIntakeDirection ();
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
}