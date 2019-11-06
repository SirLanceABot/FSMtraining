/*
Use "if" and "switch" in Java to define the four speeds of a ceiling fan
that uses the pull of a chain to "advance" to the next state.

RK Thomas
6/11/2019
*/

// the launch.json needs  "console":"integratedTerminal",  to use the terminal for "I/O"
// (otherwise DEBUG CONSOLE may be used for "O" but not "I" although it will look like it should work it doesn't)
// use ctrl ` to open a terminal or the Terminal menu item or it might open automatically
package app;

import java.io.IOException;
import java.util.Scanner;

public class App {
    enum State {OFF, LOW, MEDIUM, HIGH} // possible states
    static State state = State.OFF; // initial state
  
    public static void main( String[] args ) throws IOException {

    Scanner scanner = new Scanner(System.in);

    System.out.print( "Type 'p' and enter to simulate pulling a ceiling fan chain; the fan is currently off; 'q' to quit\n" );

// if if if if if if if if if if if if if if if if if if if if if if if if if           
    while (true) {
        String input = scanner.next(); // space delimited tokens; must hit enter to send line to program; can use nextLine
        input = input.trim();
        if (input.equalsIgnoreCase("p")) { // make the event to trigger the transition

            if (state == State.OFF)
            {
                state = State.LOW;  // transition to next state
                System.out.println( "   low speed" ); // now in the new state
            }
            else
            if (state == State.LOW)
            {
                state = State.MEDIUM;
                System.out.println("   medium speed");
            }
            else
            if (state == State.MEDIUM)
            {
                state = State.HIGH;
                System.out.println("   high speed");      
            }
            else
            if (state == State.HIGH)
            {
                state = State.OFF;
                System.out.println("   turning off");
            }

        }

        else if (input.equalsIgnoreCase("q")) {
            break;
        }
    }
/////////////////////////////////////////////////////////////////////////////

// switch switch switch switch switch switch switch switch switch switch switch 
    state = State.OFF; // initial state
    inputLoop:
    while (true) {
        String input = scanner.next(); // space delimited tokens; must hit enter to send line to program; can use nextLine
        input = input.trim().toLowerCase();
        switch (input)
        {
            case "p": // make the event to trigger the transition

                switch (state)
                {
                    case OFF:
                        state = State.LOW;
                        System.out.println( "   low speed" );
                        break;
                    case LOW:
                            state = State.MEDIUM;
                            System.out.println("   medium speed");
                            break;
                    case MEDIUM:
                            state = State.HIGH;
                            System.out.println("   high speed");
                            break;    
                    case HIGH:
                            state = State.OFF;
                            System.out.println("   turning off");
                            break;
                }

                break;

            case "q":
                break inputLoop;
        }
    }
/////////////////////////////////////////////////////////////////////////////
    scanner.close();
    }
}
