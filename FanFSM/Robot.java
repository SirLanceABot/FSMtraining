/**
 * Finite State Machine to control a three-speed ceiling fan
 * The speed of the fan changes every 3 seconds
 * The FSM is a bit contrived to illustrate a variety of possibilities such as
 * enter and exit methods and abstract and concrete methods
 * */

 package frc.robot;

import java.util.ArrayList;
import java.util.function.Function;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

  Timer timer1 = new Timer(); // timer is used to generate an event

  FanFSM.Event event;
  FanFSM.State currentFanState;
  FanFSM.State newFanState;

  // for each iteration period add a speed to the end of the list and that's the one that will be used
  ArrayList<FanFSM.Speed> speedRequest = new ArrayList<FanFSM.Speed>(10); // must be initialized with 1 or more adds
  
  // setup motor and supplier to set the (last) speed requested
  int motorCANid = 2;
  TalonFX motor = new TalonFX(motorCANid);
  Function<ArrayList<FanFSM.Speed>, Double> speed = (speedRequest) -> 
    {
      double speed = speedRequest.get(speedRequest.size()-1).value(); // get the last speed saved
      speedRequest.clear(); // reset for next iteration
      speedRequest.add(currentFanState.getSpeed()); // so we don't have to check for empty or null
      return speed;
    };

  @Override
  public void robotInit() {
    motor.setNeutralMode(NeutralMode.Brake);
    System.out.println("CTRE configNeutralDeadband " + motor.configNeutralDeadband(0.001));

    // intitial state of FSM
    currentFanState = FanFSM.initialFanState; // start at the initial state
    speedRequest.add(currentFanState.getSpeed()); // initialize speed for this state so we don't have to check for empty list
    currentFanState.doEnter(speedRequest); // initiate this state with its entry action
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    timer1.reset();
    timer1.start();
  }

  @Override
  public void teleopPeriodic()
   {
    // is there an event?
    event = FanFSM.Event.none; // initially say none

    // timer event to change the state automatically every 3 seconds
    if(timer1.hasElapsed(3.))
    {
      event = FanFSM.Event.TimerElapsed;  
      timer1.reset();
    } 

    // end of events recognition
    
    // process event to see if it triggers a state change
    if(event != FanFSM.Event.none)
    {
      // make the transition to a new currentState if an event triggers it
      newFanState = FanFSM.Transition.findNextState (currentFanState, event);

      // has the state changed by the event?
      if (newFanState != currentFanState)
      {
        // change states
        currentFanState.doExit(speedRequest); // exit current state
        currentFanState = newFanState; // switch states
        currentFanState.doEnter(speedRequest); // initiate new state
      }
    }

    currentFanState.doAction(speedRequest); // maintain current state or the new state determined above

    // done with state changing or not so set motor speed to the last speed decided upon
    motor.set(ControlMode.PercentOutput, speed.apply(speedRequest));
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
