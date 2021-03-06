package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.ftcrobotcontroller.opmodes.components.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.ArrayList;
import java.util.List;

public class TankOpMode extends OpMode
{

    public GyroSensor gyro;
    public DcMotor[] rightMotors = new DcMotor[2];
    public DcMotor[] leftMotors = new DcMotor[2];

    public ServoController servoController;

    public NormalServo winchServo;
    public DcMotor winchMotor;

    public NormalServo leftEar;
    public NormalServo rightEar;

    public ColorSensor colorSensor;
    public TankDrive tank;

    public List<Component> components = new ArrayList<Component>();

    public void init()
    {
        //DRIVING
        leftMotors[0] = hardwareMap.dcMotor.get("motor_1");
        leftMotors[1] = hardwareMap.dcMotor.get("motor_2");
        rightMotors[0] = hardwareMap.dcMotor.get("motor_3");
        rightMotors[1] = hardwareMap.dcMotor.get("motor_4");


        leftMotors[0].setDirection(DcMotor.Direction.REVERSE);
        leftMotors[1].setDirection(DcMotor.Direction.REVERSE);


        //tank = new TankDrive(leftMotors, rightMotors);


        //SERVO CONTROLLER
        servoController = hardwareMap.servoController.get("servo_cnrtl");

        //WINCH
        winchServo = new NormalServo(servoController, 1);
        components.add(winchServo);
        winchMotor = hardwareMap.dcMotor.get("winch_motor");

        //DUMBO EARS
        leftEar = new NormalServo(servoController, 2);
        components.add(leftEar);
        leftEar.setLocation(1);
        rightEar = new NormalServo(servoController, 6);
        components.add(rightEar);
        rightEar.setLocation(-1);

        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        tank = new TankDrive(leftMotors, rightMotors, gyro);
        components.add(tank);

        colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(true);
        while(gyro.isCalibrating())
        {
            try {
                wait(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void loop()
    {
        //REVERSE
        if (gamepad1.a)
        {
            tank.reverse();
        }

        //DRIVE
        tank.move(-1*gamepad1.left_stick_y, -1*gamepad1.right_stick_y);

        //WINCH
        if (this.gamepad1.dpad_down && !this.gamepad1.dpad_up){
            winchServo.decrease();
        }
        else if (this.gamepad1.dpad_up && !this.gamepad1.dpad_down) {
            winchServo.increase();
        }

        if (!this.gamepad1.dpad_left && this.gamepad1.dpad_right)
        {
            if (gamepad1.y)
            {
                tank.move(-0.1f, -0.1f);
            }
            if (gamepad1.x)
            {
                tank.move(-0.4f, -0.4f);
            }
            winchMotor.setPower(1);
        }
        else if (!this.gamepad1.dpad_right && this.gamepad1.dpad_left)
        {
            if (gamepad1.y)
            {
                tank.move(0.1f, 0.1f);
            }
            if (gamepad1.x)
            {
                tank.move(0.4f, 0.4f);
            }
            winchMotor.setPower(-1);
        }
        else
        {
            winchMotor.setPower(0);
        }

        if(gamepad1.left_bumper || gamepad2.left_bumper) {
            leftEar.decrease();
        }else{
            leftEar.increase();
        }

        if(gamepad1.right_bumper || gamepad2.right_bumper) {
            rightEar.increase();
        }else{
            rightEar.decrease();
        }


        if (gamepad1.b)
        {
            tank.angleRotation(90);
        }

        for (Component component : components)
        {
            component.doit();
        }





        telemetry.addData("Winch Position", winchServo.location);
        telemetry.addData("Reverse", tank.isReverse());
        telemetry.addData("Gyro Heading", gyro.getHeading());
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.addData("Green", colorSensor.green());
    }
}
