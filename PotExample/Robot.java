// Warning - elevator moves to 0.5 upon program starting

// robot program for moving elevator by motor to one of four predetermined positions by pressing and releasing 
// joystick buttons A, B, X, Y
// uses potentiometer to determine current elevator position to effect movement to desired position

package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Robot extends TimedRobot {

  private static final int kPotPositionChannel = 0;
  private static final int kMotorChannel = 0;
  private static final int kJoystickChannel = 0;

  private AnalogInput m_potentiometerPosition;
  private Joystick m_joystick;

  private  TalonSRX m_elevatorMotor;

  double target = 0.5;

  @Override
  public void robotInit() {
    m_potentiometerPosition = new AnalogInput(kPotPositionChannel);
    m_elevatorMotor = new TalonSRX(kMotorChannel);
    m_joystick = new Joystick(kJoystickChannel);

    m_elevatorMotor.set(ControlMode.PercentOutput, 0.0);
  }

  @Override
  public void teleopInit() {
    target = 0.5; // will go near the bottom upon starting the robot
  }

  @Override
  public void teleopPeriodic() {
    double[] targetPosition  = { 1.15  , 2.30,   3.45,   4.60 }; // elevator positions in same order as actuatedButtonIdx
    int[] actuatedButtonIdx  = {  1,     2,     3,     4  }; // stores the button numbers that correspond to targetPosition values
    boolean[] buttonActuated = {false, false, false, false}; // initial values of buttons - same order as actuatedButtonIdx
    // the first value corresponds to the first button number in actuatedButtonIdx
    // example buttonActuated[0] comes from the button numbered actuatedButtonIdx[0]
    // and coresponds to the value of targetPosition[0]

    // **** get sensor values
    //
    double position = m_potentiometerPosition.getAverageVoltage();
    double error = 0.;
    double kP = 4.;

    for (int idx=0; idx<actuatedButtonIdx.length; idx++)
      buttonActuated[idx] = m_joystick.getRawButtonReleased(actuatedButtonIdx[idx]);
    // **** get sensor values completed

    // see if any buttons pressed and released
    for (int idx=0; idx <targetPosition.length; idx++)
      if(buttonActuated[idx])
      {
        target = targetPosition[idx];
        break; // stop looking after the first one is found
      }
    
      error = position - target;
      double speedAdjust = Math.abs(error * kP);

    if (position <= target - .01) // check for elevator below deadband of targetPosition
    {
      System.out.println("up " + position + " " + speedAdjust);
      m_elevatorMotor.set(ControlMode.PercentOutput, -1.*speedAdjust); // motor up
    }
    else
    if (position >= target + .01) // check for elevator above deadband of targetPosition
    { 
      System.out.println("down " + position + " " + speedAdjust);
      m_elevatorMotor.set(ControlMode.PercentOutput, 1.*speedAdjust); // motor down
    }
    else m_elevatorMotor.set(ControlMode.PercentOutput, 0.0); // hold
  }
}
