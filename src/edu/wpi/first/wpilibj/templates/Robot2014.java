/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/* Written by 2T's and 1J                                                     */
/* PizzaLovers007, T0xicFail and Mariocrazy1000                               */
/* (c) 2014                                                                   */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.camera.AxisCameraException;
//import edu.wpi.first.wpilibj.image.BinaryImage;
//import edu.wpi.first.wpilibj.image.ColorImage;
//import edu.wpi.first.wpilibj.image.CriteriaCollection;
//import edu.wpi.first.wpilibj.image.NIVision;
//import edu.wpi.first.wpilibj.image.NIVisionException;
//import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class Robot2014 extends IterativeRobot {

    private static final double SLOW_SPEED = 0.5;
    private static final double FAST_SPEED = 0.95;

    private Joystick mXboxController;
    private RobotDrive drive;
  //  private AxisCamera camera;
    private final DriverStationLCD lcd = DriverStationLCD.getInstance();
  //  private Encoder E_FrontLeft, E_FrontRight, E_BackLeft, E_BackRight;
  //  private Ultrasonic sonic;
    private SpeedController M_FrontLeft, M_FrontRight, M_BackLeft, M_BackRight, ShoulderLeft, ShoulderRight, PickupLeft, PickupRight;
  //  private CriteriaCollection cc;
    private double autoSeconds, sens;
    private boolean hotGoalLeft, hasChangedSpeed;

    public void robotInit() {
        joystickInit();
        SpeedControllerInit();
        robotDriveInit();
        SensorInit();
    //    E_FrontLeft.start();
    //    E_FrontRight.start();
    //    E_BackLeft.start();
    //    E_BackRight.start();
    //    sonic.setAutomaticMode(true);
      //  camera = AxisCamera.getInstance();
      //  camera.writeCompression(0);
      //  camera.writeResolution(AxisCamera.ResolutionT.k320x240);
      //  camera.writeBrightness(10);
        getWatchdog().setExpiration(3);
        getWatchdog().feed();
      //  cc = new CriteriaCollection();
      //  cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 500, 65535, false);
        hasChangedSpeed = false;
        sens = SLOW_SPEED;

    }

    public void SensorInit() {
        //E_FrontLeft = new Encoder(1, 2);
        //E_FrontRight = new Encoder(3, 4);
        //E_BackLeft = new Encoder(5, 6);
        //E_BackRight = new Encoder(7, 8);
        //sonic = new Ultrasonic(9, 10);
    }

    public void joystickInit() {
        mXboxController = new Joystick(1);
    }

    public void robotDriveInit() {
        drive = new RobotDrive(M_FrontLeft, M_BackLeft, M_FrontRight, M_BackRight);
    }

    public void SpeedControllerInit() {
        M_FrontLeft = new Victor(1);
        M_FrontRight = new Victor(2);
        M_BackLeft = new Victor(3);
        M_BackRight = new Victor(4);
        ShoulderLeft = new Jaguar(5);
        ShoulderRight = new Jaguar(6);
        PickupLeft = new Talon(7);
        PickupRight = new Talon(8);
    }

    public void disabledContinuous() {
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
        autoSeconds = 0;
        hotGoalLeft = false;
    }

    public void autonomousPeriodic() {
        //drive.mecanumDrive_Cartesian()
        //x = left/right (+ is left)
        //y = rotation (- is clockwise)
        //rotation = forward/backward (+ is forward)
        getWatchdog().feed();

        //autoSeconds increases 1/100 sec every time autonomousPeriodic() is called 
        if (autoSeconds == 0) {
            lcd.println(DriverStationLCD.Line.kUser1, 1, "Calculating hot goal");
            calculateHotGoal();
        } else if (autoSeconds <= 0.3) {
            //Turn towards hot goal
            lcd.println(DriverStationLCD.Line.kUser2, 1, "Turning toward hot goal");
            if (hotGoalLeft) {
                drive.mecanumDrive_Cartesian(0, 1, 0, 0);
            } else {
                drive.mecanumDrive_Cartesian(0, -1, 0, 0);
            }
        } else if (autoSeconds <= 0.6) {
            lcd.println(DriverStationLCD.Line.kUser3, 1, "Shooting ball");
            //TODO shoot or something...
        } else if (autoSeconds <= 1.6) {
            //Turn back forward
            lcd.println(DriverStationLCD.Line.kUser4, 1, "Turning forward");
            if (hotGoalLeft) {
                drive.mecanumDrive_Cartesian(0, -1, 0, 0);
            } else {
                drive.mecanumDrive_Cartesian(0, 1, 0, 0);
            }
        } else if (autoSeconds <= 1.9) {
            //Move forward
            lcd.println(DriverStationLCD.Line.kUser5, 1, "Moving forward");
            drive.mecanumDrive_Cartesian(0, 0, 0.5, 0);
        } else {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "Autonomous Done!");
            drive.mecanumDrive_Cartesian(0, 0, 0, 0);
        }
        autoSeconds += 0.01;
    }

    public void calculateHotGoal() {
        //try {
          //  ColorImage image = camera.getImage();
          //  BinaryImage thresholdImage = image.thresholdRGB(0, 45, 25, 255, 0, 47);
          //  BinaryImage convexHullImage = thresholdImage.convexHull(false);
          //  BinaryImage filteredImage = convexHullImage.particleFilter(cc);
          //  ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();
          //  for (int i = 0; i < reports.length; i++) {
          //      ParticleAnalysisReport rep = reports[i];
          //      lcd.println(DriverStationLCD.Line.kUser2, 1, rep.toString());
          //      if (rep.boundingRectWidth > rep.boundingRectHeight && rep.boundingRectHeight > 5) {
          //          hotGoalLeft = rep.center_mass_x_normalized < 0;
            //    }
            }

          //  image.free();
          //  thresholdImage.free();
          //  convexHullImage.free();
          //  filteredImage.free();
       // } catch (NIVisionException ex) {
       // } catch (AxisCameraException ex) {
        

    public void teleopInit() {
        getWatchdog().setEnabled(false);
    }

    public void teleopPeriodic() {
        //kThrottle = X rotation
        //kTwist = Y rotation
        //sens = [0,1.0], the speed of the robot

        //They see me *rollin'*
        drive.mecanumDrive_Cartesian(getDeadZone(mXboxController.getAxis(Joystick.AxisType.kY), 0.25) * sens,
                getDeadZone(-mXboxController.getAxis(Joystick.AxisType.kThrottle), 0.25) * sens,
                getDeadZone(mXboxController.getAxis(Joystick.AxisType.kX), 0.25) * sens, 0);

        //Do you *lift* bruh?
        if (mXboxController.getAxis(Joystick.AxisType.kZ) < 0) {
            ShoulderLeft.set(-mXboxController.getAxis(Joystick.AxisType.kZ));
            ShoulderRight.set(-mXboxController.getAxis(Joystick.AxisType.kZ));
        } else {
            ShoulderLeft.set(-mXboxController.getAxis(Joystick.AxisType.kZ) * 0.6);
            ShoulderRight.set(-mXboxController.getAxis(Joystick.AxisType.kZ) * 0.6);
        }

        //Cheesy *Pick up* lines
        if (mXboxController.getRawButton(5)) {
            PickupLeft.set(1);
            PickupRight.set(-1);
        } else if (mXboxController.getRawButton(6)) {
            PickupLeft.set(-1);
            PickupRight.set(1);
        } else {
            PickupLeft.set(0);
            PickupRight.set(0);
        }

        //Sprint button!
        if (mXboxController.getRawButton(9) && !hasChangedSpeed) {
            sens = (sens == FAST_SPEED) ? SLOW_SPEED : FAST_SPEED;
            hasChangedSpeed = true;
        } else if (!mXboxController.getRawButton(9) && hasChangedSpeed) {
            hasChangedSpeed = false;
        }
        //Self Destruct
        if (mXboxController.getRawButton(7) && (mXboxController.getRawButton(8))) {
            lcd.println(DriverStationLCD.Line.kUser1, 1, "Self Destruct :D");
        } else {
            lcd.println(DriverStationLCD.Line.kUser2, 1, "X: " + mXboxController.getAxis(Joystick.AxisType.kX));
            lcd.println(DriverStationLCD.Line.kUser3, 1, "Y: " + mXboxController.getAxis(Joystick.AxisType.kY));
            lcd.println(DriverStationLCD.Line.kUser4, 1, "Turn: " + mXboxController.getAxis(Joystick.AxisType.kThrottle));
            lcd.println(DriverStationLCD.Line.kUser5, 1, "Max Speed: " + sens);
            lcd.println(DriverStationLCD.Line.kUser6, 1, "Z: " + mXboxController.getAxis(Joystick.AxisType.kZ));
        }
        lcd.updateLCD();
    }

    private double getDeadZone(double num, double dead) {
        if (num < 0) {
            if (num < dead) {
                return num;
            } else {
                return 0;
            }
        } else {
            if (num > dead) {
                return num;
            } else {
                return 0;
            }
        }
    }
}
//    :/                     /\
//                O:        /  \                  o.o
//      D:                 /    \
//                        /      \          :D
//         :3            /________\    'o'                  B|
//                      /\        /\                :(
//  :)                 /  \      /  \      c:
//                    /    \    /    \          XD
//         e_e       /      \  /      \               :P
//     ._.          /________\/________\   T^T     
//
//              (/'o')/ ^ _|___|_   OVAH 9000!!!!!!!
//                       PUT THE TABLE BACK!!!
//                      __________
//              (\'_')\   |    |
//                     if(sad == true){
//                     sad.stop();
//                     beAwesome();
//                     }
//
//          while (true && false) {
//              doQuantumPhysics();
//          }
