// FSM Template
// maybe not a template so much as a fairly rigorous example that can be used as a good model

// Robot program for moving elevator by motor to one of four predetermined positions by pressing
// joystick buttons A, B, X, or Y.

// Uses a potentiometer to determine current elevator position to effect movement to desired position

// Coding shows a style to demonstrate FSM using "enum" and table look up of the transitions

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

// An enum Speed puts the all speeds in the same place which may
// make it easier to find and change them.
// But that means the States (or state names) essentially have to be coded twice -
// once for the actual State and again here for the Speeds.  I hate duplication.
// I like just coding the actual speed number in the State but I leave them coded
// here as an example of what can be done if the speeds must be consolidated.
public static enum Speed {
  speedOff (0.00),
  speedHolding(0.05),
  speedMoving(0.50);

  double speed;

  Speed (double speed)
  {
    this.speed = speed;
  }

   double value()
  {
     return speed;
  }
}
// **
// *
// States of the FSM
// each State has an action associated with it - doAction

  public static enum State {

// Having both the argument to the constructor(speed) and implement an abstract method (doAction())
// is redundant, at least it is in this example, but they are presented as a tutorial of how to
// pass both a variable and a method to an enum constant.  (The speed could have been coded in the
// doAction only and the constructor speed would not have been needed.)
// Note that another concrete method (speed()) is also shown for example)
    Off(Speed.speedOff)
    {
      void doAction(TalonSRX motorController, Speed speed, double speedAdjust){
        System.out.println("testing Off");
        
        motorController.set(ControlMode.PercentOutput, currentState.speed().value()*speedAdjust);
      }
    },
    Holding(Speed.speedHolding)
    {
      void doAction(TalonSRX motorController, Speed speed, double speedAdjust){
        System.out.println("testing Holding");
        // The P controller as defined makes the holding speed too
        // low that the Talon SRX says it considers it 0 so changing it to 0.05
        // actually gives us the correct holding value so make sure min speed is okay.
        // There is a chance that the real elevator would sag a little during
        // holding and the controller would bump it back up.
        // This could be benefit in case we don't know the precise holding speed
        // or if it changes for some reason.
        // For our simple example this is crude and might not be how we'd do it for real
        
        motorController.set(ControlMode.PercentOutput, currentState.speed().value()*speedAdjust);
      }
    },
    Moving(Speed.speedMoving)
    {
      void doAction(TalonSRX motorController, Speed speed, double speedAdjust){
        System.out.println("testing Moving");
        
        motorController.set(ControlMode.PercentOutput, currentState.speed().value()*speedAdjust);
      }
    };
    abstract void doAction(TalonSRX motorController, Speed speed, double speedAdjust);

    private final Speed speed;

    State(Speed speed)
    {
      this.speed = speed;
    }

    public Speed speed()
    {
      return speed;
    }
  }
// ** END OF STATES

// **
// *
// Events of the FSM
  static enum Event {
    ButtonNotDown,
    ButtonDownNotAtElevation,
    ButtonDownAtElevation
  }
// ** END OF EVENTS

// **
// *
// Transitions of the FSM
// each Transition is composed of 2 independent variables - current state and event - 
// and a dependent variable - new state

  public static enum Transition
  {
// transition name   current state     event                      new state      
    TRANSITION_01 (State.Off,     Event.ButtonDownNotAtElevation, State.Moving ),
    TRANSITION_02 (State.Moving,  Event.ButtonNotDown,            State.Off    ),
    TRANSITION_03 (State.Holding, Event.ButtonNotDown,            State.Off    ),
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
  // END OF TRANSITIONS

  private static final int kPotPositionChannel = 0;
  private static final int kMotorChannel = 0;
  private static final int kJoystickChannel = 0;

  private AnalogInput m_potentiometerPosition;
  private Joystick m_joystick;
  private  TalonSRX m_elevatorMotor;

  static State currentState;
  double speed;
  double speedAdjust;

  @Override
  public void robotInit() {
    m_potentiometerPosition = new AnalogInput(kPotPositionChannel);
    m_elevatorMotor = new TalonSRX(kMotorChannel);
    m_joystick = new Joystick(kJoystickChannel);
  }

  @Override
  public void teleopInit() {
    currentState = State.Off;
    speedAdjust = 0.;
    currentState.doAction(m_elevatorMotor, currentState.speed(), speedAdjust);
  }

  @Override
  public void teleopPeriodic() {
  
    // ***
    //   common for all methods
    // ***
    // determine current position and target position
    // compute a "speedAdjust" as the P proportional controller for the motor speed
    // faster the farther away and slowly approach the target position

    double target = -999.; // arbitrary initialization since target isn't used if a button isn't pressed
                           // Probably bad style but changing the structure would require deep knowledge
                           // and a major re-org, I think, that might not be appropriate.
                           // (speed Adjust is applied even if in Off)
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
    speedAdjust = error * kP;
    if (speedAdjust < -1.) speedAdjust = -1.;
    else if (speedAdjust > 1.) speedAdjust = 1.;
    atTargetLocation = error <= tolerance && error >= -tolerance;

    // ***
    //        Thinking FSM and table lookup to perform transition
    // ***

// Decision Table to determine the event based on joystick button press
// and if near or far from target elevation
//
//                                               Rules
// Conditions Button Pressed Down           Yes Yes  No  No
//            AtTargetLocation              Yes  No Yes  No
//---------------------------------------------------------------
// Actions    ButtonNotDown Event                    X   X
//            ButtonDownAtElevation Event    X
//            ButtonDownNotAtElevation Event     X
//
    Event event;
    if ( buttonDown )
     {
      if ( atTargetLocation) event = Event.ButtonDownAtElevation;
      else event = Event.ButtonDownNotAtElevation;
     }
    else event = Event.ButtonNotDown;

// make the transition to a new currentState
   currentState = Transition.findNextState (currentState, event);
   currentState.doAction(m_elevatorMotor, currentState.speed(), speedAdjust);

  } // end teleop

} // end Robot
