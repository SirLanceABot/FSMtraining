/**
 * Finite State Machine to control a three-speed ceiling fan.
 * 
 * This FSM has an output that is the requested speed that should then be given to the
 * motor controller to effect the motor speed.
 * 
 * This is the pull-chain model - for every pull change to the next state.
 * The FSM is a contrived demo to illustrate a variety of possibilities such
 * as enter and exit methods and abstract and concrete methods.
 * 
 * Additional work could include a STOP FSM state and event to enter that state.
 * This is not the stop fan state but the state that ends the execution of the FSM.
 * There would be no output - say NaN or null to indicate nothing produced by the FSM.
 * 
 * An embellishment for the fan could include the reverse switch that changes the
 * direction of the fan on an event of the seasons changing.
 * 
 * Another embellishment would be to determine the initial state from sensors on
 * the motor or motor controller instead of assuming OFF. [For safety, motors should be told
 * to start at off but that doesn't always happen - need to coordinate the FSM starting
 * with the actual hardware state somehow.  Be AWARE!]
 * */

package frc.robot;

public class FanFSM {

  // save info about current state output - not really memory since Moore FSM has no memory
  // but it has to know where it currently stands and its output
  private State currentFanState; // current state
  private Speed speedRequest; // output of a state that returns output speed

  // instantiate a fanFSM object
  FanFSM ()
  {
       // initial state of FSM
       currentFanState = FanFSM.initialFanState; // start at the initial state
       speedRequest = currentFanState.doEnter(); // initiate this state with its entry action
       speedRequest = currentFanState.doAction(); // settle into the initial state
  }
    
  /**
   * initial state of the FSM
   */
  private static State initialFanState = FanFSM.State.Off;

  /**
   * List of allowed Fan Speeds by name and the FanFSM output value associated with that name
   * 
   * This could be publicly changeable in some manner say through the FSM constructor
   * For now it's hardwired here
   */
  private static enum Speed {
    Off (0.0),
    High(1.),
    Medium(0.5),
    Low(.1);
  
    private double speed;
  
    private Speed (double speed)
    {
      this.speed = speed;
    }
  
    /**
     * getter for the relative numeric speed associated with each possible named Speed
     * @return numeric speed associated with each named speed
     */
    public double value() // not useful if you approve of public accessing directly the class variable
    {
        return speed;
    }
  }
   // END of allowed Fan Speeds

  /**
   * List of the allowed Fan States
   * each State has actions associated with it - doEnter, doExit, doAction
   */
  private static enum State {

    /**
    Having both the argument to the constructor(speed) and implement an abstract method (doAction())
    is redundant, at least it is in this example, but they are presented as a tutorial of how to
    pass both a variable and a method to an enum constant.  (The speed could have been coded in the
    doAction only and the constructor speed would not have been needed.)
    Note that another concrete method (speed()) is also shown for example)

    A suggestion for deciding to use parameters or doAction methods or both in the State enum:
    If all the states have essentially similar doActions whose only differences can be "parameterized"
    using one or more parameters, then use the parameters on the constructor.  Code only one doAction
    method or block of code outside of the State enum and use that code after a transition.  Pass the
    parameter corresponding to the state to that block of code.
    Example may be the output control shown in this program - all the doActions are essentially
    identical and differ only in the speed parameter.  Only one set of code need be made.
  
    If the doActions for the states are significantly different, then code the doAction methods in the
    State enum and each state has its own doAction code.  Use parameters, too, if that simplifies
    coding similarities between the several states' doAction methods - common code could be put in
    another method and receive the parameter appropriate for the state.
     */

    Off(Speed.Off)
    {
      FanFSM.Speed doAction(){
      System.out.println("this is something unique to Off");
      return this.speed;
      }
    },

    High(Speed.High)
    {
      FanFSM.Speed doAction(){
      System.out.println("this is something unique to High");
      return this.speed;        
      }
    },

    Medium(Speed.Medium)
    {
      FanFSM.Speed doAction(){
      System.out.println("this is something unique to Medium");
      return this.speed;
      }
    },
    
    Low(Speed.Low)
    {
      FanFSM.Speed doAction(){
      System.out.println("this is something unique to Low");
      return this.speed;
      }
    };

    /**
     * method each state must have for its own because the code is different in this example
     */

    /**
     * 
     * @return
     */
    abstract FanFSM.Speed doAction();

    /**
     *  methods each state can use in common because the code is the same in this example
     */

    /**
     * 
     * @return
     */
    FanFSM.Speed doEnter()
    {
      System.out.println("entering state " + this.name()); 
      return this.speed;
    }

    /**
     * 
     * @return
     */
    FanFSM.Speed doExit()
    {
      System.out.println("exiting state " + this.name() + "; switching FanFSM output to " + stateExitFanSpeed);
      return stateExitFanSpeed; // all exits the same turn output off
    }

    // class variables each state has
    protected final Speed speed;
    protected FanFSM.Speed stateExitFanSpeed = Speed.Off;

    /**
     * Construct a state with its associated speed
     * @param speed
     */
    State(FanFSM.Speed speed)
    {
      this.speed = speed;
    }

    /**
     * getter for the named speed associated with each possible state
     * note that this is an inherent property of each state and
     * not a memory of the current speed of the FSM
     * @return named speed associated with this state
     */
    public Speed value()
    {
      return speed;
    }
  }
  // ** END OF ALLOWED FAN STATES

  /**
   * Events of the FSM
   * 
   */
  public static enum Event
  {
    none, chainPulled
  }
  // ** END OF EVENTS

  /**
   * Transitions of the FSM
   * 
   * each Transition is composed of 2 independent variables - current state and event - 
   * and a dependent variable - new state
   */
  private static enum Transition
  {
  // transition name   current state        event           new state
    TRANSITION_01 (State.Off,      Event.chainPulled, State.High),
    TRANSITION_02 (State.High,     Event.chainPulled, State.Medium),
    TRANSITION_03 (State.Medium,   Event.chainPulled, State.Low),
    TRANSITION_04 (State.Low,      Event.chainPulled, State.Off);
    
    private final State currentState;
    private final Event event;
    private final State nextState;
    
    Transition(State currentState, Event event, State nextState)
    {
      this.currentState = currentState;
      this.event = event;
      this.nextState = nextState;
    }

    /**
     * table lookup to determine new state given the current state and the event
     * 
     * @param currentState
     * @param event
     * @return
     */
    private static State findNextState (State currentState, Event event)
    {
      // for efficiency could check for no event but that's another "if" statement to understand and it's not
      // needed for the logic unless not finding a corresponding transition is considered an error
      for (Transition transition : Transition.values())
      {
        if (transition.currentState == currentState && transition.event == event) return transition.nextState;
      }

      // no transition for this state and event so maintain current state
      // if that would be considered an error then throw one and catch caller or return say undefined state
      // and figure out how to maintain current state or enter an error state by adding error state as a choice
      return currentState;
    }
  }
  // END OF TRANSITIONS

  public void checkStateChange(Event event)
  {
    // make the transition to a new currentState if an event triggered it

    State newFanState = FanFSM.Transition.findNextState (currentFanState, event); // the next state

    // has the state changed by the event?
    // There is a choice here depending on the system.
    // If the state didn't change by this event, you could still go through the doExit
    // and doEnter because of the event, if that's what makes sense for your FSM.
    // This code doesn't redo the doExit, doEnter, and doAction but there is a comment below
    // to move the doAction if you always want to execute it even if no event or state change.
    if (newFanState != currentFanState)
    {
      // change states
      speedRequest = currentFanState.doExit(); // exit current state
      currentFanState = newFanState; // switch states
      speedRequest = currentFanState.doEnter(); // initiate new state
      speedRequest = currentFanState.doAction();
    }
    // move above doAction to below to always run it
    // speedRequest = currentFanState.doAction(); // always maintain current state or the new state as determined above
  }

  /**
   * getter for the FanFSM output
   * @return relative speed output of the current state
   */
  public double speed()
  {
    return speedRequest.value();
  }

  /**
  * getter for the FanFSM current state
  * @return the current fan state
  */
  public State state()
  {
    return currentFanState;
  }
  
  /**
   * print information about the FSM
   * @return String of information about the FSM
   */
  public String dump()
  {
    StringBuilder sb = new StringBuilder(500);
    sb.append("Transitions\nFrom State           Event                Next State\n");
    for (Transition transition : Transition.values())
    {
    sb.append(String.format("%-20s %-20s %-20s\n",transition.currentState, transition.event, transition.nextState));
    }
    sb.append("initial state " + initialFanState + ", current state " + currentFanState + ", current speed " + speedRequest);
    return sb.toString();
  }

} // END of FSM
