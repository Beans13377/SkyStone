package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class SixMotorTeleOpNormalDrive extends OpMode {

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

    private static final double countsPerMotorRev = 383.6;
    private static final double wheelDiameter = 4;
    private static final double countsPerInch = countsPerMotorRev / (wheelDiameter * 3.1415);

    private boolean lastDriveToDepotToggle = false;
    private boolean lastDriveToDepotToggleTwo = false;
    private boolean driveToDepotToggle = false;
    private boolean driveToDepotLastValue = false;
    private boolean driveToDepotLastValueTwo = false;

    private boolean encodersAreBusy = false;
    private boolean encodersAreBusyTwo = false;
    private int stagesForDrivingToDepot = 0;
    private boolean driveToDepotCurrent = false;
    private boolean driveToDepotCurrentTwo = false;

    @Override
    public void init() {

        leftDriveMiddle = hardwareMap.get(DcMotor.class, "left_drive_middle");
        rightDriveMiddle = hardwareMap.get(DcMotor.class, "right_drive_middle");
        leftDriveFront = hardwareMap.get(DcMotor.class, "left_drive_front");
        rightDriveFront = hardwareMap.get(DcMotor.class, "right_drive_front");
        leftDriveBack = hardwareMap.get(DcMotor.class, "left_drive_back");
        rightDriveBack = hardwareMap.get(DcMotor.class, "right_drive_back");
        intake = hardwareMap.get(DcMotor.class, "intake");
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

        double leftPower = Range.clip(drive + turn, -1.0, 1.0);
        double rightPower = Range.clip(drive - turn, -1.0, 1.0);

//        setDriveMotorPower(leftPower, rightPower);

        processIntake();
        intakeDirection();
        intake.setPower(currentIntakePower * intakeDirection);

        driveToDepot();

        telemetry.addData("Is intake on", lastPowerToggle);
        telemetry.addData("Intake Motor Power", "%.2f", intake.getPower());
        telemetry.addData("Is intake forward", lastDirectionToggle);
        telemetry.addData("Intake Motor Direction", intakeDirection);
    }


    private void setDriveMotorPower(double leftPower, double rightPower) {
        leftDriveMiddle.setPower(leftPower);
        rightDriveMiddle.setPower(rightPower);
        leftDriveFront.setPower(leftPower);
        rightDriveFront.setPower(rightPower);
        leftDriveBack.setPower(leftPower);
        rightDriveBack.setPower(rightPower);
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
        boolean current = gamepad1.y;
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

    private boolean driveToDepot() {
        final double driveSpeed = 0.2;
        final double turnSpeed = 0.2;

        boolean current = gamepad1.x;
        if (current == driveToDepotToggle) {

        } else if (!driveToDepotToggle && current && !encodersAreBusy) {

            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        driveToDepotToggle = current;

        stagesForDrivingToDepot = 1;

        if(stagesForDrivingToDepot == 1) {
            encoderDrive(driveSpeed, 21-(driveSpeed * 12), 21-(driveSpeed * 12), 4.0); //44.091603053   take off 12 for skidding
            telemetry.addData("1st Stage", "Running to %7d :%7d", leftDriveMiddle.getTargetPosition(), rightDriveMiddle.getTargetPosition());
            stagesForDrivingToDepot++;
        }
        if (stagesForDrivingToDepot == 2) {
            leftDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            stagesForDrivingToDepot++;
        }
        if (stagesForDrivingToDepot == 3) {
            encoderDriveTwo(turnSpeed, -4.5 * 3.1415, 4.5 * 3.1415, 2.0);
            telemetry.addData("3rd Stage", "Running to %7d :%7d", leftDriveMiddle.getTargetPosition(), rightDriveMiddle.getTargetPosition());
        }
        return current;
    }
//        encoderDrive(driveSpeed, 21-(driveSpeed * 12), 21-(driveSpeed * 12), 4.0);


//    int newLeftTarget;
//    int newRightTarget;
//    boolean followingEncoders = false;
//    boolean inMotion = false;
//
//    public boolean encoderDrive(double speed,
//                                double leftInches, double rightInches,
//                                double timeoutS) {
//        if (gamepad1.x) {
//            if (!inMotion) {
//                newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
//                newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
//                leftDriveMiddle.setTargetPosition(newLeftTarget);
//                rightDriveMiddle.setTargetPosition(newRightTarget);
//                leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                leftDriveMiddle.setPower(Math.abs(speed));
//                rightDriveMiddle.setPower(Math.abs(speed));
//                leftDriveFront.setPower(Math.abs(speed));
//                rightDriveFront.setPower(Math.abs(speed));
//                leftDriveBack.setPower(Math.abs(speed));
//                rightDriveBack.setPower(Math.abs(speed));
//
//                double startTime = getRuntime();
//            } else if (inMotion) {
//                telemetry.addData("Path1", "Running to %7d :%7d", leftDriveMiddle.getTargetPosition(), rightDriveMiddle.getTargetPosition());
//                telemetry.addData("Path2", "Running at %7d :%7d",
//                        leftDriveMiddle.getCurrentPosition(),
//                        rightDriveMiddle.getCurrentPosition());
//                telemetry.update();
//            }
//            if (driveToDepotToggle && !inMotion) {
//                leftDriveMiddle.setPower(0);
//                rightDriveMiddle.setPower(0);
//                leftDriveFront.setPower(0);
//                rightDriveFront.setPower(0);
//                leftDriveBack.setPower(0);
//                rightDriveBack.setPower(0);
//
//                leftDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//                rightDriveMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//                leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            }
//        }
//    }


        private boolean encoderDrive( double speed, double leftInches, double rightInches,
        double timeoutS){
            int newLeftTarget;
            int newRightTarget;
            boolean followingEncoders = gamepad1.x;

            boolean currentXvalue = gamepad1.x;
            if (encodersAreBusy) {
                telemetry.addData("setting x value to false", "");
                currentXvalue = false;
            }

            if (currentXvalue == lastDriveToDepotToggle) {

            } else if (!lastDriveToDepotToggle && currentXvalue) {
                newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
                newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
                leftDriveMiddle.setTargetPosition(newLeftTarget);
                rightDriveMiddle.setTargetPosition(newRightTarget);
                leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftDriveMiddle.setPower(Math.abs(speed));
                rightDriveMiddle.setPower(Math.abs(speed));
                leftDriveFront.setPower(Math.abs(speed));
                rightDriveFront.setPower(Math.abs(speed));
                leftDriveBack.setPower(Math.abs(speed));
                rightDriveBack.setPower(Math.abs(speed));

                this.encodersAreBusy = true;

                //double startTime = getRuntime();
            }
            lastDriveToDepotToggle = currentXvalue;

//        while (opModeIsActive() &&
//                ((getRuntime() - startTime) < timeoutS) &&
//                (leftDriveMiddle.isBusy() && rightDriveMiddle.isBusy())){

            // driveToDepotCurrent == false if encoder drive is done

            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) >= 5
                    && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) >= 5) {
                driveToDepotCurrent = true;
            } else {
                driveToDepotCurrent = false;
            }
            if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 200
                    || Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 200) {
                leftDriveFront.setPower(0);
                rightDriveFront.setPower(0);
                leftDriveBack.setPower(0);
                rightDriveBack.setPower(0);
            }

//        boolean driveToDepotCurrent = (leftDriveMiddle.isBusy() && rightDriveMiddle.isBusy());
//        boolean driveToDepotCurrent = (leftDriveMiddle.isBusy() || rightDriveMiddle.isBusy());
//        if (gamepad1.left_stick_y ||gamepad1.right_stick_x) {
//            driveToDepotCurrent = true;
//        }
            if (driveToDepotCurrent == this.driveToDepotLastValue) {
                if (driveToDepotCurrent) {
                    telemetry.addData("Path1", "Running to %7d :%7d", leftDriveMiddle.getTargetPosition(), rightDriveMiddle.getTargetPosition());
                    telemetry.addData("Path2", "Running at %7d :%7d",
                            leftDriveMiddle.getCurrentPosition(),
                            rightDriveMiddle.getCurrentPosition());
                    telemetry.update();
                }
            } else if (this.driveToDepotLastValue && !driveToDepotCurrent) {
                leftDriveMiddle.setPower(0);
                rightDriveMiddle.setPower(0);
                leftDriveFront.setPower(0);
                rightDriveFront.setPower(0);
                leftDriveBack.setPower(0);
                rightDriveBack.setPower(0);

                this.encodersAreBusy = false;

                leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            }

            this.driveToDepotLastValue = driveToDepotCurrent;
            return followingEncoders;
        }
    private boolean encoderDriveTwo( double speed, double leftInches, double rightInches,
                                       double timeoutS){
        int newLeftTarget;
        int newRightTarget;

        boolean currentXvalue = gamepad1.x;
        if (encodersAreBusyTwo) {
            telemetry.addData("setting x value to false", "");
            currentXvalue = false;
        }

        if (currentXvalue == lastDriveToDepotToggleTwo) {

        } else if (!lastDriveToDepotToggleTwo && currentXvalue) {
            newLeftTarget = leftDriveMiddle.getCurrentPosition() + (int) (leftInches * countsPerInch);
            newRightTarget = rightDriveMiddle.getCurrentPosition() + (int) (rightInches * countsPerInch);
            leftDriveMiddle.setTargetPosition(newLeftTarget);
            rightDriveMiddle.setTargetPosition(newRightTarget);
            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDriveMiddle.setPower(Math.abs(speed));
            rightDriveMiddle.setPower(Math.abs(speed));
            leftDriveFront.setPower(-Math.abs(speed));
            rightDriveFront.setPower(Math.abs(speed));
            leftDriveBack.setPower(-Math.abs(speed));
            rightDriveBack.setPower(Math.abs(speed));

            this.encodersAreBusyTwo = true;

            //double startTime = getRuntime();
        }
        lastDriveToDepotToggleTwo = currentXvalue;

//        while (opModeIsActive() &&
//                ((getRuntime() - startTime) < timeoutS) &&
//                (leftDriveMiddle.isBusy() && rightDriveMiddle.isBusy())){

        // driveToDepotCurrent == false if encoder drive is done

        if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) >= 5
                && Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) >= 5) {
            driveToDepotCurrentTwo = true;
        } else {
            driveToDepotCurrentTwo = false;
        }
        if (Math.abs(leftDriveMiddle.getCurrentPosition() - leftDriveMiddle.getTargetPosition()) <= 200
                || Math.abs(rightDriveMiddle.getCurrentPosition() - rightDriveMiddle.getTargetPosition()) <= 200) {
            leftDriveFront.setPower(0);
            rightDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveBack.setPower(0);
        }

//        boolean driveToDepotCurrent = (leftDriveMiddle.isBusy() && rightDriveMiddle.isBusy());
//        boolean driveToDepotCurrent = (leftDriveMiddle.isBusy() || rightDriveMiddle.isBusy());
//        if (gamepad1.left_stick_y ||gamepad1.right_stick_x) {
//            driveToDepotCurrent = true;
//        }
        if (driveToDepotCurrentTwo == this.driveToDepotLastValueTwo) {
            if (driveToDepotCurrentTwo) {
                telemetry.addData("Path1", "Running to %7d :%7d", leftDriveMiddle.getTargetPosition(), rightDriveMiddle.getTargetPosition());
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftDriveMiddle.getCurrentPosition(),
                        rightDriveMiddle.getCurrentPosition());
                telemetry.update();
            }
        } else if (this.driveToDepotLastValueTwo && !driveToDepotCurrentTwo) {
            leftDriveMiddle.setPower(0);
            rightDriveMiddle.setPower(0);
            leftDriveFront.setPower(0);
            rightDriveFront.setPower(0);
            leftDriveBack.setPower(0);
            rightDriveBack.setPower(0);

            this.encodersAreBusyTwo = false;

            leftDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDriveMiddle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveMiddle.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }

        this.driveToDepotLastValueTwo = driveToDepotCurrentTwo;
        return currentXvalue;
    }

    }