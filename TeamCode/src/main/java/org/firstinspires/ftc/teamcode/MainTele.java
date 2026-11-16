package org.firstinspires.ftc.teamcode;
//<3
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class MainTele extends OpMode {
    private DcMotor rightFront;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor intake;
    private DcMotorEx shooter;
    private double ticksPerRev;

    double targetV;
    private double pusher;


    @Override
    public void init() {
        //drivetrain init
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        //shooter init
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotor.Direction.FORWARD);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        double targetV = 0.5;


        ticksPerRev = shooter.getMotorType().getTicksPerRev();


        intake = hardwareMap.get(DcMotor.class, "intake");
     // middle = hardwareMap.get(DcMotor.class, "middle");
      //middle.setDirection(DcMotor.Direction.FORWARD);


        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       // pusher = hardwareMap.get(Servo.class, "pusher");
       // pusher.setPosition(0.6);
    }

    @Override
    public void loop() {
        drivetrain();
        allintake();
      //  allmiddle();

       //<3
        try {
            allshoot();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //shooter data
        double rotations = shooter.getCurrentPosition() / ticksPerRev;
        double targetV = 0.5; //need to update
        telemetry.addData("Shooter Power:", shooter.getPower());
        telemetry.addData("Encoder:", shooter.getCurrentPosition());
        telemetry.addData("Velocity:", shooter.getVelocity());
        telemetry.addData("max ticks:", ticksPerRev);
        telemetry.update();
    }

    //<3  throws InterruptedException
    public void allshoot() throws InterruptedException {
        if (gamepad2.aWasPressed()){
            shooter.setDirection(DcMotor.Direction.FORWARD);
            shooter.setPower(targetV); // need to find target v
            intake.setPower(1);
        } else if (gamepad2.yWasPressed()) {
            shooter.setPower(0);
            intake.setPower(0);
        }

        if (gamepad2.dpadUpWasPressed()){
            shooter.setVelocity(shooter.getVelocity() + 50.0);
        } else if (gamepad2.dpadDownWasPressed()) {
            shooter.setVelocity(shooter.getVelocity() - 50.0);

            telemetry.update();

        }

    }

    public void allintake() {
        if (gamepad1.right_bumper || gamepad2.right_bumper){
            intake.setPower(1);
        } else if (gamepad1.left_bumper || gamepad2.left_bumper){
            intake.setPower(-1);
        } else {
            intake.setPower(0);
}
    }

    //middle
 /*public void allmiddle(){
        if (gamepad2.x || gamepad1.x) {
        middle.setDirection(DcMotor.Direction.FORWARD);
        middle.setPower(1);
    } else if (gamepad2.b){
        middle.setDirection(DcMotor.Direction.REVERSE);
        middle.setPower(1);
    } else {
        middle.setPower(0);
    }}
*/
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
