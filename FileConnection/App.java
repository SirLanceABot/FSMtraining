/*
Use state pattern in Java to define open and closed states that can use
open and close requests to move between states and handle a read request as appropriate
RK Thomas
5/23/2019
*/

// the launch.json needs  "console":"integratedTerminal",  to use the terminal for "I/O"
// (otherwise DEBUG CONSOLE may be used for "O" but not "I" although it will look like it should work it doesn't)
// use ctrl ` to open a terminal or the Terminal menu item or it might open automatically

package app;

import java.io.IOException;
import java.util.Scanner;

public class App {

    abstract class State {
        abstract public void openRequest(FileConnection wrapper);
        abstract public void readRequest(FileConnection wrapper);
        abstract public void closeRequest(FileConnection wrapper);
    }

    class FileConnection { // the "wrapper" class that works differently as it moves between the various classes that define the various states
        private State current;
    
        public FileConnection() {
            current = new Closed(); // initial state of the FileConnection
        }
    
        public void setState(State state) { // set (change) the state of the FileConnection
            current = state;
        }
    
        public void openRequest() { // request that may change the state of the FileConnection
            current.openRequest(this);
        }

        public void readRequest() { // read the file if possible for the current state otherwise error
            current.readRequest(this);
        }

        public void closeRequest() { // request that may change the state of the FileConnection
            current.closeRequest(this);
        }
    }
    
    class Open extends State { // current state
        public void openRequest(FileConnection wrapper) { // already in requested state
            System.out.println("File already open");
        }

        public void readRequest(FileConnection wrapper) { // can read an open file
            System.out.println( "Read something" );
        }

        public void closeRequest(FileConnection wrapper) { // handle the transition
            wrapper.setState(new Closed()); // new state
            System.out.println( "Closing file" );
        }
    }
    
    class Closed extends State {
        public void openRequest(FileConnection wrapper) { // handle the transition
            wrapper.setState(new Open()); // new state
            System.out.println("Opening file");
        }

        public void readRequest(FileConnection wrapper) { // impossible to read a closed file
            System.out.println( "Can't read closed file" );
        }

        public void closeRequest(FileConnection wrapper) { // handle the transition
            wrapper.setState(new Closed()); // already in requested state
            System.out.println( "File already closed" );
        }
    }
  
         public static void main( String[] args ) throws IOException {

            Scanner scanner = new Scanner(System.in);
            App x = new App();
            FileConnection FileConnection = x.new FileConnection();
            System.out.print( "Type 'o', 'r', or 'c' for file manipulations, file is closed; 'q' to quit\n" );
            while (true) {
                String input = scanner.next(); // space delimited tokens; must hit enter to send line to program; can use nextLine
                input = input.trim();
                if (input.equalsIgnoreCase("o")) { // make the request to trigger the transition
                    FileConnection.openRequest();
                    }
                else
                if (input.equalsIgnoreCase("r")) { // make the request to read the file
                    FileConnection.readRequest();
                    }
                else
                if (input.equalsIgnoreCase("c")) { // make the request to trigger the transition
                    FileConnection.closeRequest();
                    }
                else 
                if (input.equalsIgnoreCase("q")) {
                    break;
                }
            }
            scanner.close();
         }
}
