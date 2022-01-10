/**
 * Three-speed ceiling fan controlled by a Finite State Machine (FSM).
 * This is the pull-chain model -- for every pull of the chain change to the next state.
 * The speed of the fan changes automatically every 3 seconds as if the chain was pulled.
 * */

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

  FanFSM fanFSM; // the fan FSM

  Timer timer1 = new Timer(); // timer is used to generate an event to change states

  FanFSM.Event event; // current event to process; force to be a state the FSM knows
  
  // setup Fan motor - that which is being controlled by the FSM
  int motorCANid = 2;
  TalonFX motor = new TalonFX(motorCANid);
  
  @Override
  public void robotInit() {
    motor.setNeutralMode(NeutralMode.Brake);
    System.out.println("CTRE configNeutralDeadband " + motor.configNeutralDeadband(0.001));

    // instantiate a new FanFSM
    fanFSM = new FanFSM();
    System.out.println(fanFSM.dump()); // dump FSM information
    motor.set(ControlMode.PercentOutput, fanFSM.speed()); // set the motor to the initial action but it's disabled!
  }

  @Override
  public void teleopInit() {
    // reset timer that is used to generate an event
    timer1.reset();
    timer1.start();
  }

  @Override
  public void teleopPeriodic()
   {
    // is there an event?
    event = FanFSM.Event.none; // initially say none then continue on to look for an event

    // timer event to change the state automatically every 3 seconds with a pretend chain pull
    if(timer1.advanceIfElapsed(3.))
    {
      event = FanFSM.Event.chainPulled;  
      timer1.reset();
    } 
    // end of events recognition

    fanFSM.checkStateChange(event); // send the event to the FSM

    // System.out.println(fanFSM.speed());
    // done with any state changing or maintaining; set motor speed to the last speed decided upon by the FSM
    motor.set(ControlMode.PercentOutput, fanFSM.speed());
  }

}
