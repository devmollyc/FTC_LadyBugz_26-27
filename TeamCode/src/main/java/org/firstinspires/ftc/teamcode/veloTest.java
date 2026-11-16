package org.firstinspires.ftc.teamcode;
//<3 - test intake, shooter, moving
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class veloTest extends OpMode {
    private DcMotor rightFront;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor intake;
    private DcMotorEx shooter;
   // private Servo flower;

    double targetV = -1700; //test



    @Override
    public void init() {
        //drivetrain init
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        //shooter init
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setVelocity(0);
        PIDFCoefficients pidf = new PIDFCoefficients(100, 0, 0, 13);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);


        intake = hardwareMap.get(DcMotor.class, "intake");



        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       // flower = hardwareMap.get(Servo.class, "flower");
       // flower.setPosition(0.0);

    }

    @Override
    public void loop() {
        drivetrain();
        allintake();
        velocityControl();

        /*  if (gamepad1.a){
            flower.setPosition(0.5);
        } else if (gamepad1.dpad_up){
            flower.setPosition();
        }
        */


        boolean ready = Math.abs(shooter.getVelocity() - targetV) < 10;
        telemetry.addData("READY: ", ready);
        telemetry.addLine();
        telemetry.addData("Velocity:", shooter.getVelocity());
        telemetry.addData("targetvelo", targetV);
        telemetry.addData("f", f);
        telemetry.addData("p", p);
        telemetry.update();
    }

    double p = 0;
    double f = 20;
    public void velocityControl(){
        shooter.setVelocity(0);
        if(gamepad2.leftTriggerWasPressed())
        shooter.setVelocity(0);
        else if(gamepad2.rightTriggerWasPressed())
            shooter.setVelocity(targetV);
        else if(gamepad2.a){
            shooter.setVelocity(-targetV);
        }

        if(gamepad2.rightBumperWasPressed()) targetV += 10;
        else if(gamepad2.leftBumperWasPressed()) targetV -= 10;
    }

    public void allintake() {
        if (gamepad1.rightTriggerWasPressed())
            intake.setPower(1);
         else if (gamepad1.leftTriggerWasPressed())
            intake.setPower(0);
         else if(gamepad1.aWasPressed()) {
            intake.setDirection(DcMotorSimple.Direction.REVERSE);
            intake.setPower(1);
        }

    }

    //switch movement
    public void drivetrain() {
        //Drivetrain
        double moveX = gamepad1.left_stick_x;
        double moveY = gamepad1.left_stick_y;
        double turnX = gamepad1.right_stick_x;

        double frontLeftPower = moveY + moveX + turnX;
        double frontRightPower = moveY - moveX - turnX;
        double backLeftPower = moveY - moveX + turnX;
        double backRightPower = moveY + moveX - turnX;

        frontLeftPower *= 0.98;
        backLeftPower *= 0.98;

        //Drivetrain Driver Controls
        if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {

            if (gamepad1.right_bumper) {
                leftFront.setPower(frontLeftPower);
                rightFront.setPower(frontRightPower);
                leftBack.setPower(backLeftPower);
                rightBack.setPower(backRightPower);
                telemetry.addLine("drive multiplier: 1");
            } else if (gamepad1.left_bumper) {
                leftFront.setPower(frontLeftPower * 0.35);
                rightFront.setPower(frontRightPower * 0.35);
                leftBack.setPower(backLeftPower * 0.35);
                rightBack.setPower(backRightPower * 0.35);
                telemetry.addLine("drive multiplier: 0.35");
            } else {
                leftFront.setPower(frontLeftPower * 0.8);
                rightFront.setPower(frontRightPower * 0.8);

                leftBack.setPower(backLeftPower * 0.8);
                rightBack.setPower(backRightPower * 0.8);
                telemetry.addLine("drive multiplier: 0.8");

            }
        } else {
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
        }


    }


}
