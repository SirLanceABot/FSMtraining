/*
Use state pattern in Java to define 3 levels of alarms for notifications:
 silent, vibrate, tone
Notify a message using the appropriate alarm level

RK Thomas
6/27/2019
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
        abstract public void setState(Alarm wrapper);
        abstract public void notify(String message);
    }

    class Alarm { // the "wrapper" class that works differently as it moves between the various classes that define the various states
        private State currentState;
    
        public Alarm() {
            currentState = new Tone(); // initial state of the alarm
        }
    
        public void setState(State state) { // set (change) the state of the alarm
            currentState = state;
        }
    
        public void notify(String message) { // event that changes the state of the alarm
            currentState.notify(message);
        }
    }
    
    class Silent extends State {
        public void setState(Alarm wrapper) // handle the transition
        {
            wrapper.setState(new Silent()); // new state
        }
        public void notify(String message) {
            System.out.println("Silently displaying " + message);
        }
    }
    
    class Vibrate extends State {
        public void setState(Alarm wrapper)
        {
            wrapper.setState(new Vibrate());
        }
        public void notify(String message) {
            System.out.println("Vibrating " + message);
        }
    }

    class Tone extends State {
        public void setState(Alarm wrapper)
        {
            wrapper.setState(new Tone());
        }
        public void notify(String message) {
            System.out.println("Tone " + message);
        }
    }
  
    //final Silent silent = new Silent();

         public static void main( String[] args ) throws IOException {

            Scanner scanner = new Scanner(System.in);
            App x = new App();
            Alarm alarm = x.new Alarm();
            System.out.print( "Type 's', 'v', or 't' to set the alarm; type other stuff to make a notification; type 'q' to quit\n" );

            while (true) {
                String input = scanner.nextLine(); // must hit enter to send line to program
                input = input.trim();
                if (input.equalsIgnoreCase("s")) { // make the event to trigger the transition
                    alarm.setState(x.new Silent());
                }
                else
                if (input.equalsIgnoreCase("v")) {
                    alarm.setState(x.new Vibrate());
                }
                else
                if (input.equalsIgnoreCase("t")) {
                    alarm.setState(x.new Tone());
                }
                else if (input.equalsIgnoreCase("q")) {
                    break;
                }
                else // assume it's the message if not a command
                {
                    alarm.notify(input);
                }
            }
            scanner.close();
         }
}