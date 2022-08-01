package app;

// FSM for a table lamp with the turn-knob Twist Click
// light fixture.  Twist - it's ON; Twist - it's OFF

class TableLampFSM 
{
  
  private State FSMstate; // this class instantiation remembers its current state; use the getter to see it

// **
// *
// States of the FSM
// Note that these states have a doAction entrance method but no exit method
  public enum State
  {
    ON()
    {
      void doEnter()
      {
        System.out.println("Let there be light");
      }

      void doAction()
      {
        super.doAction();
        System.out.println("Turn Light On");
      }
    },

    OFF()
    {
      void doEnter()
      {
        System.out.println("Entering a period of darkness");
      }

      void doAction()
      {
        super.doAction();
        System.out.println("Turn Light Off");
      }
    };

    public boolean isOn()
    {
      return this == State.ON;
    }

    public boolean isOff()
    {
      return this == State.OFF;
    }

    // default or common optional method
    void doAction(){
      System.out.print("Entering state " + name() + " action is ");
    };

    // required method
    abstract void doEnter();
  
  }
// END OF STATES
// *
// **

// **
// *
// Events of the FSM
public enum Event
   {
       twistClick;

       public static boolean isEvent(String event) // verify a string is a named constant in the enum
        {
          for (TableLampFSM.Event ev : TableLampFSM.Event.values()) { // values() is array of all the named 
            if (event.equals(ev.toString()) ) return true;
          }
          return false;
        }
    }
// END OF EVENTS
// *
// **

// **
// *
// Transitions of the FSM

// each Transition is composed of 2 independent variables - current state and event - 
// and a dependent variable - new state

// _________________________________________________________________
//|           |                               |      Rules          |
//|___________|_______________________________|_____________________|
//| Conditions| Twist Click Entered           | Yes          No     |
//|___________|_______________________________|_____________________|
//| Actions   | Next State Event              |  X                  |
//|___________|_______________________________|_____________________|
public enum Transition
    {
    // transition name, current state, event,    new state      
    TRANSITION_01 ( State.OFF, Event.twistClick, State.ON ),
    TRANSITION_02 ( State.ON,  Event.twistClick, State.OFF );

    private final State currentState;
    private final Event event;
    private final State nextState;

    Transition(final State currentState, final Event event, final State nextState) {
                this.currentState = currentState;
                this.event = event;
                this.nextState = nextState;
            }

    // table lookup to determine new state given the current state and the event
    static State findNextState(final State state, final Event event)
      {
        for (final Transition transition : Transition.values())
        {
            if (transition.currentState == state && transition.event == event)
                return transition.nextState;
        }
        return null; // no transition found so return null to indicate that
    }
    }
// END OF TRANSITIONS
// *
// **

TableLampFSM (State state) // initialize state
{
    this.FSMstate = state;
    this.FSMstate.doEnter(); // assume FSM must be put into this initial state
    this.FSMstate.doAction(); // assume FSM must be put into this initial state
}

boolean processEvent(Event event)
{
    // make the transition to a new currentState
    State tempState = Transition.findNextState (this.FSMstate, event);
    // set new state and do the actions required of the new state
    if (tempState != null) 
    {
      this.FSMstate = tempState;
      this.FSMstate.doEnter();
      this.FSMstate.doAction();
      return true;
    }
    else return false; // if no transition found, state returned is null then do nothing and return false.

}

public State getState()
{
  return this.FSMstate;
}

public String toString()
{
  // print States, Events, Transitions, current State
  String info = null;

  info = "Current state is " + this.getState().toString() + "\n\nStates of the FSM\n";
      for (TableLampFSM.State state : TableLampFSM.State.values()) { // values() is array of all the named constants of the enum
      info = info + state + " ";
    }
    info = info + "\n\n";

    info = info + "FSM responds to Events\n";
      for (TableLampFSM.Event event : TableLampFSM.Event.values()) { // values() is array of all the named constants of the enum
    info = info + event + "\n";
    }
    info = info + "\n";

    info = info + "Transitions: old state, triggering event, new state\n";
    for (TableLampFSM.Transition transition : TableLampFSM.Transition.values()) { // values() is array of all the named constants of the enum
        info = info + transition.currentState + ", " + transition.event + ", " + transition.nextState + "\n";
        }
    
  return info;
}
}
