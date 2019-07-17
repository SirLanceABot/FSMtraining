// Robot program for moving elevator by motor to one of four predetermined positions by pressing and releasing 
// joystick buttons A, B, X, Y.

// Uses a potentiometer to determine current elevator position to effect movement to desired position

// Coding shows thee styles which more or less demonstrate FSM or not.
// Version 1 is pretty much devoid of appearing as a FSM
// Version 2 is FSM with swich and if statements
// Version 3 is FSM with table look up of the transitions

// Demonstration uses one potentiometer to indicate elevator position for all three styles.

// Two motors are used and styles 2 and 3 send the same commands to one of the motors and
// one can see the two styles are likekly producing the same result as their shared motor
// appears to be responding to identical commands (not being jerked around).
// Style 1 has its own motor that behaves identically to the other motor.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

// States used in versions 2 and 3
// each State has an action associated with it
// only version 3 example fully utilizes all the enum constant methods
  public static enum State {

// speed while in the state (subject to P controller adjustment)
//     currentState.speed()
// doAction() while in the state
//     currentState.doAction();
//
// having both the argument to the constructor(speed) and implement an abstract method (doAction()) is likely redundant
// at least it is in this example, they are presented as a tutorial of how to
// pass both a variable and a method to an enum constant
// note that another concrete method (speed()) is also shown for example)
    Off(0.0)
    {
      void doAction(TalonSRX motorController, double speed){
        System.out.println("testing Off");
        motorController.set(ControlMode.PercentOutput, speed);
      }
    },
    Holding(0.05)
    {
      void doAction(TalonSRX motorController, double speed){
        System.out.println("testing Holding");
        speed = 0.05; // just to show it can be changed
        // And yes I realize the P controller as defined makes the holding speed too
        // low that the Talon SRX says it considers it 0 so changing it to 0.05
        // actually gives us the correct holding value.
        // There is a chance that the real elevator would sag a little during
        // holding and the controller would bump it back up.
        // This could be benefit in case we don't know the precise holding speed
        // or if it changes for some reason.
        // For our simple example this is crude and might not be how we'd do it for real
        motorController.set(ControlMode.PercentOutput, speed);
      }
    },
    Moving(.5)
    {
      void doAction(TalonSRX motorController, double speed){
        System.out.println("testing Moving");
        motorController.set(ControlMode.PercentOutput, speed);
      }
    };
    abstract void doAction(TalonSRX motorController, double speed);

    private final double speed;

    State(double speed)
    {
      this.speed = speed;
    }

    public double speed()
    {
      return speed;
    }
  }

  // Events used in version 3
  static enum Event {
    ButtonUp,
    ButtonDownNotAtElevation,
    ButtonDownAtElevation
  }

  // Transitions used in version 3
  public static enum Transition
  {
// transition name   current state     event                      new state      
    TRANSITION_01 (State.Off,     Event.ButtonDownNotAtElevation, State.Moving),
    TRANSITION_02 (State.Moving,  Event.ButtonUp,                 State.Off),
    TRANSITION_03 (State.Holding, Event.ButtonUp,                 State.Off),
    TRANSITION_04 (State.Off,     Event.ButtonDownAtElevation,    State.Holding),
    TRANSITION_05 (State.Moving,  Event.ButtonDownAtElevation,    State.Holding);
    
    private final State currentState;
    private final Event event;
    private final State nextState;
    
    Transition(State currentState, Event event, State nextState)
    {
      this.currentState = currentState;
      this.event = event;
      this.nextState = nextState;
    }

    // table lookup to determine new state given the current state and the event
    public static State findNextState (State currentState, Event event)
    {
      for (Transition transition : Transition.values())
      {
        if (transition.currentState == currentState && transition.event == event) return transition.nextState;
      }
        return currentState; // throw an error if here
    }
  }
  //////////////////////

  boolean firstTime_1; // each version needs its own initialization
  boolean firstTime_2;
  boolean firstTime_3;
  
  // common variables
  private static final int kPotPositionChannel = 0;
  private static final int kMotorChannel_0 = 0;
  private static final int kMotorChannel_1 = 1;
  private static final int kJoystickChannel = 0;

  private AnalogInput m_potentiometerPosition;
  private Joystick m_joystick;

  private  TalonSRX m_elevatorMotor_0; // version 1
  private  TalonSRX m_elevatorMotor_1; // versions 2 and 3

  double target;

  State currentState; // each version needs its own initialization

  @Override
  public void robotInit() {
    m_potentiometerPosition = new AnalogInput(kPotPositionChannel);
    m_elevatorMotor_0 = new TalonSRX(kMotorChannel_0);
    m_elevatorMotor_1 = new TalonSRX(kMotorChannel_1);
    m_joystick = new Joystick(kJoystickChannel);

    m_elevatorMotor_0.set(ControlMode.PercentOutput, 0.0);
    m_elevatorMotor_1.set(ControlMode.PercentOutput, 0.0);
  }

  @Override
  public void teleopInit() {
    firstTime_1 = true;
    firstTime_2 = true;
    firstTime_3 = true;
  }

  @Override
  public void teleopPeriodic() {
  
    // ***
    //   common for all methods
    // ***
    // determine current position and target position
    // compute a "speedAdjust" as the P proportional controller for the motor speed
    // faster the farther away and slowly approach the target position

    double[] targetPosition  = { 1.15  , 2.30,   3.45,   4.60 }; // elevator positions in same order as actuatedButtonIdx
    int[] actuatedButtonIdx  = {  1,     2,     3,     4  }; // stores the button numbers that correspond to targetPosition values
    boolean[] buttonPressed = {false, false, false, false}; // initial values of buttons - same order as actuatedButtonIdx
    boolean buttonDown = false;
    // the first value corresponds to the first button number in actuatedButtonIdx
    // example buttonActuated[0] comes from the button numbered actuatedButtonIdx[0]
    // and coresponds to the value of targetPosition[0]

    // **** get sensor values
    //
    double position = m_potentiometerPosition.getAverageVoltage();
    double error;
    double tolerance = 0.01;
    double kP = 4.;
    boolean atTargetLocation;
   
    for (int idx=0; idx<actuatedButtonIdx.length; idx++)
      buttonPressed[idx] = m_joystick.getRawButton(actuatedButtonIdx[idx]);
    // **** get sensor values completed

    // see if any buttons pressed and released
    for (int idx=0; idx <targetPosition.length; idx++)
      if(buttonPressed[idx])
      {
        target = targetPosition[idx]; // target position associated with this button that was pressed
        buttonDown = true; // there is a button event
        break; // stop looking after the first one is found
      }
 
    error = position - target;
    double speedAdjust = error * kP;
    atTargetLocation = error <= tolerance && error >= -tolerance;

    // end common coding

     {
    // ***
    //          version 1 - not thinking much of FSM
    // This is mostly optimized considering all states go to off if no button pressed.
    // Events translate to actions without thinking much about states except in the comments.
    // ***

      double speed;
      double speedOff = 0.;
      double speedHold = 0.05;
      double speedMove = .5;

       if (buttonDown)
      {
        if (atTargetLocation) speed = speedHold; // hold
        else speed = speedMove; // move
      }
      else speed = speedOff; // all buttons up - stop

      m_elevatorMotor_0.set(ControlMode.PercentOutput,
        Math.copySign(Math.min(speed, Math.abs(speed*speedAdjust)), speedAdjust) );
    // ***
    // end version 1
    // ***
    }

    {
    // ***
    //         version 2
    // Thinking FSM with switch / case for current state and if to determine event
    // to determine next state and perform transition
    // ***

    if (firstTime_2)
    {
      currentState = State.Off;
      firstTime_2 = false;
    }

    // Transition to next state if there is an appropriate event for the current state
    switch (currentState)
    {
      case Off:
        if (buttonDown && atTargetLocation) {currentState = State.Holding;} // event to transition to new state
        if (buttonDown && !atTargetLocation) {currentState = State.Moving;} // event to transition to new state
        break;
      
      case Holding:
        if (buttonDown && !atTargetLocation) {currentState = State.Moving;} // event to transition to new state
        if (!buttonDown) {currentState = State.Off;} // event to transition to new state
        break;
      
      case Moving:
        if (buttonDown && atTargetLocation) {currentState = State.Holding;} // event to transition to new state
        if (!buttonDown) {currentState = State.Off;} // event to transition to new state
        break;
    }

    // Any transition is complete and we might have a new current state
    // Do the action for this current state
    
    m_elevatorMotor_1.set(ControlMode.PercentOutput,
      Math.copySign(Math.min(currentState.speed(), Math.abs(currentState.speed()*speedAdjust)), speedAdjust) );
 
    // ***
    // end version 2
    // ***
    }

    {
    // ***
    //        version 3 Thinking FSM and table lookup to perform transition
    // ***

    if (firstTime_3)
    {
      currentState = State.Off;
      firstTime_3 = false;
    }

    Event event;

// determine the event
    if ( buttonDown )
     if ( atTargetLocation) event = Event.ButtonDownAtElevation;
     else event = Event.ButtonDownNotAtElevation;
    else event = Event.ButtonUp;

// make the transition to a new currentState
    currentState = Transition.findNextState (currentState, event);
    // m_elevatorMotor_1.set(ControlMode.PercentOutput,
    //   Math.copySign(Math.min(currentState.speed(), Math.abs(currentState.speed()*speedAdjust)), speedAdjust) );
    currentState.doAction(m_elevatorMotor_1, Math.copySign(Math.min(currentState.speed(), Math.abs(currentState.speed()*speedAdjust)), speedAdjust) );
    }

  } // end teleop

} // end Robot
//   https://docs.oracle.com/javase/8/docs/technotes/guides/language/enums.html