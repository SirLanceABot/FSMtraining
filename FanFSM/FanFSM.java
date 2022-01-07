package frc.robot;

public final class FanFSM {

  /**
   * List of allowed Fan Speeds
   */
  public static enum Speed {
    Off (0.0),
    High(1.),
    Medium(0.5),
    Low(.1);
  
    private double speed;
  
    private Speed (double speed)
    {
      this.speed = speed;
    }
  
    public double value() // maybe not so useful if you approve of accessing the class variable speed
    {
        return speed;
    }
  }
  // END of allowed Fan Speeds

  /**
   * List of the allowed Fan States
   * each State has actions associated with it - doEnter, doExit, doAction
   */
  public static enum State {

  // Having both the argument to the constructor(speed) and implement an abstract method (doAction())
  // is redundant, at least it is in this example, but they are presented as a tutorial of how to
  // pass both a variable and a method to an enum constant.  (The speed could have been coded in the
  // doAction only and the constructor speed would not have been needed.)
  // Note that another concrete method (speed()) is also shown for example)

  // A suggestion for deciding to use parameters or doAction methods or both in the State enum:
  //  If all the states have essentially similar doActions whose only differences can be "parameterized"
  //  using one or more parameters, then use the parameters on the constructor.  Code only one doAction
  //  method or block of code outside of the State enum and use that code after a transition.  Pass the
  //  parameter coresponding to the state to that block of code.
  //  Example may be the motor control shown in this program - all the doActions are essentially
  //  identical and differ only in the speed parameter.  Only one set of code need be made.
  //
  //  If the doActions for the states are significantly different, then code the doAction methods in the
  //  State enum and each state has its own doAction code.  Use parameters, too, if that simplifies
  //  coding similarities between the several states' doAction methods - common code could be put in
  //  another method and receive the parameter appropriate for the state.

    Off(Speed.Off)
    {
      FanFSM.Speed doEnter()
      {
        System.out.println("entering state " + this.name());
        return Speed.Off;
      }

      FanFSM.Speed doAction(){
      System.out.println("testing Off");
      return Speed.Off;
      }
    },

    High(Speed.High)
    {
      FanFSM.Speed doEnter()
      {
        System.out.println("entering state " + this.name());
        return Speed.High;
      }
      
      FanFSM.Speed doAction(){
      System.out.println("testing High");
      return Speed.High;        
      }
    },

    Medium(Speed.Medium)
    {
      FanFSM.Speed doEnter()
      {
        System.out.println("entering state " + this.name());
        return FanFSM.Speed.Medium;
      }

      FanFSM.Speed doAction(){
      System.out.println("testing Medium");
      return Speed.Medium;
      }
    },
    
    Low(Speed.Low)
    {
      FanFSM.Speed doEnter()
      {
        System.out.println("entering state " + this.name()); 
        return FanFSM.Speed.Low;
      }

      FanFSM.Speed doAction(){
      System.out.println("testing Low");
      return Speed.Low;
      }
    };

    // methods each state must have for its own
    abstract FanFSM.Speed doEnter();
    abstract FanFSM.Speed doAction();

    /**
     *  method that each state can use in common
     * 
     *  */
    FanFSM.Speed doExit()
    {
      System.out.println("exiting state " + this.name() + "; switching motor to " + stateExitFanSpeed);
      return stateExitFanSpeed; // all exits the same turn motor off
    }

    // class variable each state has
    private final Speed speed;
    private FanFSM.Speed stateExitFanSpeed = Speed.Off;

    /**
     * Construct a state with its associated speed
     * @param speed
     */
    State(FanFSM.Speed speed)
    {
      this.speed = speed;
    }


    /**
     * getter for the speed of the state
     * note that this is an inherent property of each state and
     * not a memory of the current speed of the FSM which must be
     * held external to the FSM
     * @return speed associated with this state
     */
    public Speed getSpeed()
    {
      return speed;
    }
  }
// ** END OF ALLOWED FAN STATES

  /**
   * Events of the FSM
   * 
   *  */
  static enum Event
  {
    none, TimerElapsed
  }
  // ** END OF EVENTS

  /**
   * Transitions of the FSM
   * 
   * each Transition is composed of 2 independent variables - current state and event - 
   * and a dependent variable - new state
   */
  public static enum Transition
  {
  // transition name   current state        event           new state      
    TRANSITION_01 (State.Off,      Event.TimerElapsed, State.High),
    TRANSITION_02 (State.High,     Event.TimerElapsed, State.Medium),
    TRANSITION_03 (State.Medium,   Event.TimerElapsed, State.Low),
    TRANSITION_04 (State.Low,      Event.TimerElapsed, State.Off);
    
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

      // no transition for this state and event so maintain current state
      // if that would be considered an error then throw one and catch caller or return say undefined state
      // and figure out how to maintain current state or enter an error state by adding error state as a choice
      return currentState;
    }
  }
  // END OF TRANSITIONS

  /**
   * initial state of the FSM
   */
  public static State initialFanState = FanFSM.State.Off;

} // END of FSM
