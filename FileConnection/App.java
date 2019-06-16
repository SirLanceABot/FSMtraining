/*
Use state pattern in Java to define open and closed states that can use
open, read, and close requests to move between states
RK Thomas
5/23/2019
*/
package app;

import java.io.IOException;
import java.util.Scanner;

public class App {

    abstract class State {
        public void openRequest(FileFSM wrapper) {System.out.println("Never gets here; oops if it does");}
        public void readRequest(FileFSM wrapper) {System.out.println("Never gets here; oops if it does");}
        public void closeRequest(FileFSM wrapper) {System.out.println("Never gets here; oops if it does");}
    }

    class FileFSM { // the "wrapper" class that works differently as it moves between the various classes that define the various states
        private State current;
    
        public FileFSM() {
            current = new Closed(); // initial state of the FileFSM
        }
    
        public void setState(State state) { // set (change) the state of the FileFSM
            current = state;
        }
    
        public void openRequest() { // event that changes the state of the FileFSM
            current.openRequest(this);
        }

        public void readRequest() { // event that changes the state of the FileFSM
            current.readRequest(this);
        }

        public void closeRequest() { // event that changes the state of the FileFSM
            current.closeRequest(this);
        }
    }
    
    class Open extends State { // current state
        public void openRequest(FileFSM wrapper) { // handle the transition
            System.out.println("File already open");
        }

        public void readRequest(FileFSM wrapper) { // handle the transition
            System.out.println( "Read something" );
        }

        public void closeRequest(FileFSM wrapper) { // handle the transition
            wrapper.setState(new Closed()); // new state
            System.out.println( "Closing file" );
        }
    }
    
    class Read extends State {
         public void readRequest(FileFSM wrapper) { // handle the transition
            // don't bother with a real transition
            // no need for this example to even consider an exit or entrance code for the state
            // and this simple example doesn't bother with exits and entrances for any state
            System.out.println( "Read something; not changing state" );
        }

    }

    class Closed extends State {
        public void openRequest(FileFSM wrapper) { // handle the transition
            wrapper.setState(new Open()); // new state
            System.out.println("Opening file");
        }

        public void readRequest(FileFSM wrapper) { // handle the transition
            System.out.println( "Can't read closed file" );
        }

        public void closeRequest(FileFSM wrapper) { // handle the transition
            wrapper.setState(new Closed()); // new state
            System.out.println( "File already closed" );
        }
    }
  
         public static void main( String[] args ) throws IOException {

            Scanner scanner = new Scanner(System.in);
            App x = new App();
            FileFSM FileFSM = x.new FileFSM();
            System.out.print( "Type 'o', 'r', or 'c' for file manipulations, file is closed; 'q' to quit\n" );
            while (true) {
                String input = scanner.next(); // space delimited tokens; must hit enter to send line to program; can use nextLine
                input = input.trim();
                if (input.equalsIgnoreCase("o")) { // make the event to trigger the transition
                    FileFSM.openRequest();
                    }
                else
                if (input.equalsIgnoreCase("r")) { // make the event to trigger the transition
                    FileFSM.readRequest();
                    }
                else
                if (input.equalsIgnoreCase("c")) { // make the event to trigger the transition
                    FileFSM.closeRequest();
                    }
                else 
                if (input.equalsIgnoreCase("q")) {
                    break;
                }
            }
            scanner.close();
         }
}
