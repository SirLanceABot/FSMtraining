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
    
        public void openRequest() { // event that changes the state of the FileConnection
            current.openRequest(this);
        }

        public void readRequest() { // event that changes the state of the FileConnection
            current.readRequest(this);
        }

        public void closeRequest() { // event that changes the state of the FileConnection
            current.closeRequest(this);
        }
    }
    
    class Open extends State { // current state
        public void openRequest(FileConnection wrapper) { // handle the transition
            System.out.println("File already open");
        }

        public void readRequest(FileConnection wrapper) { // handle the transition
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

        public void readRequest(FileConnection wrapper) { // handle the transition
            System.out.println( "Can't read closed file" );
        }

        public void closeRequest(FileConnection wrapper) { // handle the transition
            wrapper.setState(new Closed()); // new state
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
                if (input.equalsIgnoreCase("o")) { // make the event to trigger the transition
                    FileConnection.openRequest();
                    }
                else
                if (input.equalsIgnoreCase("r")) { // make the event to trigger the transition
                    FileConnection.readRequest();
                    }
                else
                if (input.equalsIgnoreCase("c")) { // make the event to trigger the transition
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