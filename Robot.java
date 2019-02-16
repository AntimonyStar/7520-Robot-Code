/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GamepadBase;



import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.VictorSP;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

//driver Joysticks
private Joystick driver;
private Joystick operator;

//private gamepad

private Joystick gamepad;

//speed controllers for Drive
private VictorSPX leftDrive1;
private VictorSPX leftDrive2;
private VictorSPX rightDrive1;
private VictorSPX rightDrive2;

private VictorSPX ballIntake;

private long autoStartTime;

private UsbCamera camera;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    System.out.println("robotInit");

 this.operator = new Joystick(0);
 this.driver = new Joystick(1); 

 this.gamepad = new Joystick(2);

 
 /* 01/13/19- code deploys successfully but still
can't control the robot with joysticks- can try
disabling camera stuff to see if it works*/
 try {
   this.camera = new UsbCamera("usb cam", "/note/video0");
   CameraServer.getInstance().addCamera(this.camera);
   this.camera.setResolution(320, 240);
   this.camera.setFPS(30);
 } catch (Exception e) {
   e.printStackTrace();
 }
 

CameraServer.getInstance().startAutomaticCapture();

  this.leftDrive1 = new VictorSPX(0);
  this.leftDrive2 = new VictorSPX(1);

  this.rightDrive1 = new VictorSPX(2);
  this.rightDrive2 = new VictorSPX(3);

  this.ballIntake = new VictorSPX(4);

  this.leftDrive2.set(ControlMode.Follower , 0);
  this.rightDrive2.set(ControlMode.Follower , 2);
//this.ballIntake.set(ControlMode.Follower , 1);

  }

 
  @Override
  public void robotPeriodic() {
  }


  @Override
  public void autonomousInit() {
    this.autoStartTime = System.currentTimeMillis();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
   long timePassed = System.currentTimeMillis() - this.autoStartTime;
   
   SmartDashboard.putNumber("Time Passed" , (double)timePassed);
   if(timePassed < 3000){ 
    this.leftDrive1.set(ControlMode.PercentOutput , -0.3 );
   // this.leftDrive2.set(ControlMode.PercentOutput , -0.3);
    this.rightDrive1.set(ControlMode.PercentOutput , 0.3);
  //  this.rightDrive2.set(ControlMode.SPercentOutput , 0.3);
   
  }

   else if(timePassed < 5000)  {//turn motors same way to turn the robot
   this.leftDrive1.set(ControlMode.PercentOutput ,0.3);
   //this.leftDrive2.set(ControlMode.PercentOutput , 0.3);
   this.rightDrive1.set(ControlMode.PercentOutput , 0.3);
  // this.rightDrive2.set(ControlMode.PercentOutput , 0.3);
   } 
   else {
    this.leftDrive1.set(ControlMode.PercentOutput , 0.0);
   // this.leftDrive2.set(0.0);
    this.rightDrive1.set(ControlMode.PercentOutput , 0.0);
   // this.rightDrive2.set(0.0);
   }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    /* 
    timedRobot - uses a timer (Notifier) to guarantee that 
    the periodic methods are called at a predictable time 
    interval
    */
    System.out.println("Ok");
    System.out.println("teleopPeriodic");
    // tank style drive control


    // getting our inputs
   // double leftStick = this.driver.getRawAxis(0);
   // double rightStick = this.driver.getRawAxis(1);

  /*  double leftStick = this.operator.getRawAxis(0);
    double rightStick = this.operator.getRawAxis(1);

    // calculating our outputs


    //setting speed controllers
    this.leftDrive1.set(-leftStick);
    this.leftDrive2.set(-leftStick);

    this.rightDrive1.set(rightStick);
    this.rightDrive2.set(rightStick);
    */

    // arcade drive 

    // inputs from the driver 
    //double operatorX = this.operator.getRawAxis(0);
    double operatorY = this.operator.getRawAxis(1);

    //double driverX = this.driver.getRawAxis(0);
    double driverY = this.driver.getRawAxis(1);

    double gamepadY = -this.gamepad.getRawAxis(1);

    
//System.out.println(driverX+','+driverY);
    // calculations of output
    double leftOut = operatorY; 
    double rightOut = driverY;

    double intakeOut = gamepadY;

    

    

    // outputs to speed controllers
    
    this.leftDrive1.set(ControlMode.PercentOutput , (0.5 * -leftOut));
    //this.leftDrive2.set(ControlMode.PercentOutput , -leftOut);
    this.rightDrive1.set(ControlMode.PercentOutput , (0.5 * rightOut));
    //this.rightDrive2.set(ControlMode.PercentOutput , rightOut);

    if(Math.abs(intakeOut) > 0.1)
      this.ballIntake.set(ControlMode.PercentOutput , (0.7 * intakeOut)); //to make the intake less sensitive

    else 
      this.ballIntake.set(ControlMode.PercentOutput , 0);

  }

  



}