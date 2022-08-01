package app;

// FSM for a table lamp with the turn-knob Twist Click
// light fixture.  Twist - it's ON; Twist - it's OFF

import java.util.Scanner;

class App {

  public static void main(final String[] args) {

    Scanner scEvents = null;

    try {
      TableLampFSM fsm = new TableLampFSM(TableLampFSM.State.OFF); // instantiate FSM with initial state
      System.out.println(fsm); // current information about the FSM

      TableLampFSM.Event event = null;

      scEvents = new Scanner(System.in);
      String input;

      while (true) // loop accepting events until "q" quits
      {
        System.out.println("Enter an event of");
        for (TableLampFSM.Event evt : TableLampFSM.Event.values()) { // values() is array of all the named constants of the enum
          System.out.print(" " + evt);
        }
        System.out.println(" or q to quit");

        input = scEvents.next();

        if (input.equalsIgnoreCase("q"))
          break;
        else if (TableLampFSM.Event.isEvent(input)) // make sure it's a valid event for this FSM
        {
          event = TableLampFSM.Event.valueOf(input); // create the event from the input
        } else {
          System.out.println("input is not an event");
          continue;
        }

        // handle the event
        // Choose to fail if there is no transition for the state/event,
        // but no transition is legitimate for most FSMs just not this trivial one.
        if (!fsm.processEvent(event))
          throw new Exception("missing a transition");

        System.out.println(fsm.getState()); // current information about the FSM
        System.out.println("The light is " + (fsm.getState().isOn() ? "ON" : "not ON"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      scEvents.close();
    }
  }
}
