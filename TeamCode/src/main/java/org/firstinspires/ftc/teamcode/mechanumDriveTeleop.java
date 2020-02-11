package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class mechanumDriveTeleop extends OpMode{
    private DcMotor leftfront = null;
    private DcMotor leftback = null;
    private DcMotor rightfront = null;
    private DcMotor rightback = null;
    private DcMotor intake = null;
    private DcMotor lift = null;
//    private Servo lfoundationater = null;
//    private Servo rfoundationater = null;
    private Servo lgrabber = null;
    private Servo rgrabber = null;
//    private CRServo outake = null;
    private Servo rotate = null;

    private boolean lastPowerToggle = false;
    private boolean isIntakePowerOn = false;
    private boolean lastDirectionToggle = false;
    private int intakeDirection = 1;
    private double currentIntakePower = 0;
    private static final double intakePower = 1.0;

    @Override
    public void init () {
        leftfront = hardwareMap.get(DcMotor.class, "leftfront");
        leftback = hardwareMap.get(DcMotor.class, "leftback");
        rightback = hardwareMap.get(DcMotor.class, "rightback");
        rightfront = hardwareMap.get(DcMotor.class, "rightfront");
        intake = hardwareMap.get(DcMotor.class, "intake");
        lift = hardwareMap.get(DcMotor.class, "lift");
        lgrabber = hardwareMap.servo.get("lgrabber");
        rgrabber = hardwareMap.servo.get("rgrabber");
//        lfoundationater = hardwareMap.servo.get("lfoundationater");
//        rfoundationater = hardwareMap.servo.get("rfoundationater");
//        outake = hardwareMap.crservo.get("outake");
        rotate = hardwareMap.servo.get("rotate");

        leftfront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftback.setDirection(DcMotorSimple.Direction.FORWARD);
        rightfront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightback.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void loop () {
        processIntake();
        intakeDirection();
        intake.setPower(currentIntakePower * intakeDirection);

//        outakeStone();
//        moveFoundation();
        liftStone();

        applyMovement();
        grabStone();
        rotateGrabber();

//        double drive = -gamepad1.left_stick_y;
//        double turn = gamepad1.right_stick_x;
//        double strafe = gamepad1.left_stick_x;
//
//        double leftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
//        double rightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
//
//        double drive2 = -gamepad2.left_stick_y / 2;
//        double turn2 = gamepad2.right_stick_x / 2;
//        double strafe2 = gamepad2.left_stick_x / 2;
//        double leftPower2 = Range.clip(drive2 + turn2 + strafe2, -.5, .5);
//        double rightPower2 = Range.clip(drive2 - turn2 - strafe2, -.5, .5);
//
//        if (Math.abs(gamepad1.left_stick_y) < .15 && Math.abs(gamepad1.right_stick_x) < .15) {
//            leftfront.setPower(leftPower2);
//            rightfront.setPower(rightPower2);
//            leftback.setPower(leftPower2);
//            rightback.setPower(rightPower2);
//        } else {
//            leftfront.setPower(leftPower);
//            rightfront.setPower(rightPower);
//            leftback.setPower(leftPower);
//            rightback.setPower(rightPower);
//        }
    }
    /**
     * Converts movement_y, movement_x, movement_turn into motor powers.
     *
     * author: Original code by FTC Team 7571 Alumineers, modified by Travis Kuiper
     * date: 2020/01/01
     *
     * movement_turn: Positive value to turn clockwise, negative for counterclockwise
     * movement_x: Positive value indicates strafe to the right proportionally
     * movement_y: Positive value indicates drive forward proportionally
     */
    // Code comes from 11115 Peter and 7571 Alumineers
    private void applyMovement() {
        double movement_turn = gamepad1.right_stick_x;
        double movement_x = gamepad1.left_stick_x;
        double movement_y = gamepad1.left_stick_y;

//        long currTime = SystemClock.uptimeMillis();
//        if(currTime - lastUpdateTime < 16){
//            return;
//        }
//        lastUpdateTime = currTime;

        double fl_power_raw = movement_y+movement_turn+movement_x;
        double bl_power_raw = movement_y+movement_turn-movement_x;
        double br_power_raw = -movement_y+movement_turn-movement_x;
        double fr_power_raw = -movement_y+movement_turn+movement_x;

        //find the maximum of the powers
        double maxRawPower = Math.abs(fl_power_raw);
        if(Math.abs(bl_power_raw) > maxRawPower){ maxRawPower = Math.abs(bl_power_raw);}
        if(Math.abs(br_power_raw) > maxRawPower){ maxRawPower = Math.abs(br_power_raw);}
        if(Math.abs(fr_power_raw) > maxRawPower){ maxRawPower = Math.abs(fr_power_raw);}

        //if the maximum is greater than 1, scale all the powers down to preserve the shape
        double scaleDownAmount = 1.0;
        if(maxRawPower > 1.0){
            //when max power is multiplied by this ratio, it will be 1.0, and others less
            scaleDownAmount = 1.0/maxRawPower;
        }
        fl_power_raw *= scaleDownAmount;
        bl_power_raw *= scaleDownAmount;
        br_power_raw *= scaleDownAmount;
        fr_power_raw *= scaleDownAmount;

        //now we can set the powers ONLY IF THEY HAVE CHANGED TO AVOID SPAMMING USB COMMUNICATIONS
        leftfront.setPower(-fl_power_raw);
        leftback.setPower(-bl_power_raw);
        rightback.setPower(-br_power_raw);
        rightfront.setPower(-fr_power_raw);
    }
    private void processIntake() {
        boolean current = gamepad1.b || gamepad2.b;
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
        boolean current = gamepad1.a || gamepad2.a;
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

//    private void outakeStone() {
//        if (gamepad1.x || gamepad2.x) {
//            outake.setPower(1);
//        }
//        else if (gamepad1.y || gamepad2.y) {
//            outake.setPower(0);
//        }
//    }
//    private void moveFoundation () {
//        if (gamepad1.dpad_right || gamepad2.dpad_right) {
//            lfoundationater.setPosition(.1);
//            rfoundationater.setPosition(.9);
//        }
//        else if (gamepad1.dpad_left || gamepad2.dpad_left) {
//            lfoundationater.setPosition(.9);
//            rfoundationater.setPosition(.1);
//        }
//    }
    private void liftStone () {
        lift.setPower(gamepad1.left_trigger - gamepad1.left_trigger);
    }
    private void grabStone () {
        if (gamepad1.dpad_down) {
            lgrabber.setPosition(1);
            rgrabber.setPosition(.4);
        }
        else if (gamepad1.dpad_up) {
            lgrabber.setPosition(.7);
            rgrabber.setPosition(0);
        }
    }
    private void rotateGrabber () {
        if (gamepad1.dpad_left) {
            rotate.setPosition(1);
        }
        else if (gamepad1.dpad_right) {
            rotate.setPosition(0);
        }
    }
}
