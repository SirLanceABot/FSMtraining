/**
 * Finite State Machine to control a three-speed ceiling fan.
 * This is the pull-chain model - for every pull change to the next state.
 * The speed of the fan changes every 3 seconds as if the chain was pulled.
 * The FSM is a bit contrived to illustrate a variety of possibilities such as
 * enter and exit methods and abstract and concrete methods
 * */

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

  Timer timer1 = new Timer(); // timer is used to generate an event to change states

  FanFSM.Event event; // current event to process; force to be a state the FSM knows
  // save info about states 
  // some of this could easily be in the FSM but real Moore FSM do not have memory
  // so consider this stuff as outputs of the FSM
  FanFSM.State currentFanState; // current state
  FanFSM.State newFanState; // change to state from current state and event - will be next current state
  FanFSM.Speed speedRequest; // output of a state that returns motor speed
  
  // setup Fan motor - that which is being controlled by the FSM
  int motorCANid = 2;
  TalonFX motor = new TalonFX(motorCANid);
  
  @Override
  public void robotInit() {
    motor.setNeutralMode(NeutralMode.Brake);
    System.out.println("CTRE configNeutralDeadband " + motor.configNeutralDeadband(0.001));

    // intitial state of FSM
    currentFanState = FanFSM.initialFanState; // start at the initial state
    speedRequest = currentFanState.doEnter(); // initiate this state with its entry action
    motor.set(ControlMode.PercentOutput, speedRequest.value()); // set the motor to the initial action
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

    // timer event to change the state automatically every 3 seconds
    if(timer1.hasElapsed(3.))
    {
      event = FanFSM.Event.TimerElapsed;  
      timer1.reset();
    } 

    // end of events recognition
    
    // process event to see if it triggers a state change
    if(event != FanFSM.Event.none) // was there an event to check?
    {
      // make the transition to a new currentState if an event triggered it
      newFanState = FanFSM.Transition.findNextState (currentFanState, event);

      // has the state changed by the event?
      // There is a choice here depending on the system.
      // If the state didn't change by this event, you could still go through the doExit
      // and doEnter because of the event, if that's what makes sense for your FSM.
      // This code doesn't redo the doExit and doEnter but it does do the doAction
      // as it does even if there is no event.
      if (newFanState != currentFanState)
      {
        // change states
        speedRequest = currentFanState.doExit(); // exit current state
        currentFanState = newFanState; // switch states
        speedRequest = currentFanState.doEnter(); // initiate new state
      }
    }

    speedRequest = currentFanState.doAction(); // always maintain current state or the new state as determined above

    // done with state changing or not so set motor speed to the last speed decided upon
    motor.set(ControlMode.PercentOutput, speedRequest.value());
  }

}
