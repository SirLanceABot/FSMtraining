/*
Use state pattern in Java to define the four speeds of a ceiling fan
that uses the pull of a chain to "advance" to the next state.

Modified code from an Internet site article:
https://sourcemaking.com/design_patterns/state/java/3

RK Thomas
5/23/2019
*/

// the launch.json needs  "console":"integratedTerminal",  to use the terminal for I/O
// (otherwise DEBUG CONSOLE may be used for O but not I although it will look like it should work it doesn't)
// use ctrl ` to open a terminal or the Terminal menu item or it might open automatically
package app;

import java.io.IOException;
import java.util.Scanner;

public class App {

    abstract class State {
        /*
        Abstract class in Java is similar to interface except that it can contain default method implementation.
        And it cann be entended by other classes.  Interfaces cannot be extends by other classes.
        An abstract class can have an abstract method without body and it can have methods with implementation also.

        Also, this default method may be required becasue of the way the class are internal to the main static class
        and had to be instantiated to access all the other classes
        */
        abstract public void pullChain(Fan wrapper);
    }

    class Fan { // the "wrapper" class that works differently as it moves between the various classes that define the various states
        private State current;
    
        public Fan() {
            current = new Off(); // initial state of the fan
            //current = off;
        }
    
        public void setState(State state) { // set (change) the state of the fan
            current = state;
        }
    
        public void pullChain() { // event that changes the state of the fan
            current.pullChain(this);
        }
    }
    
    class Off extends State { // current state
        public void pullChain(Fan wrapper) { // handle the transition
            wrapper.setState(new Low()); // new state
            System.out.println( "   low speed" );
        }
    }
    
    class Low extends State {
        public void pullChain(Fan wrapper) {
            wrapper.setState(new Medium());
            System.out.println("   medium speed");
        }
    }

    class Medium extends State {
        public void pullChain(Fan wrapper) {
            wrapper.setState(new High());
            System.out.println("   high speed");
        }
    }
    
    class High extends State {
        public void pullChain(Fan wrapper) {
           wrapper.setState(new Off());
           // wrapper.setState(off);
            System.out.println("   turning off");
        }
    }
 
    //final Off off = new Off();

         public static void main( String[] args ) throws IOException {

            Scanner scanner = new Scanner(System.in);
            App x = new App();
            Fan fan = x.new Fan();
            System.out.print( "Type 'p' and enter to simulate pulling a ceiling fan chain; the fan is currently off; 'q' to quit\n" );

            while (true) {
                String input = scanner.next(); // space delimited tokens; must hit enter to send line to program; can use nextLine
                input = input.trim();
                if (input.equalsIgnoreCase("p")) { // make the event to trigger the transition
                    fan.pullChain();
                }
                else if (input.equalsIgnoreCase("q")) {
                    break;
                }
            }
            scanner.close();
         }
}