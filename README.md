Repository of a variety of ways to implement an FSM in Java.
Use FanFSM on the roboRIO!

Note that FanFSMroboRIO has the latest greatest version of the modified jeasy easy states.
But jeasy easy states is NOT recommended for use by the team.

Arguably the best example is the FanFSM that uses Java "enum" to define the states, events, and state transition table.
Enter, Action, and Exit methods are defined for each state based on the abstract method for the state enum.
It runs on the roboRIO and does not use jeasy easy states.

Another good example but slightly less complete of an example is the FSMtemplate that uses Java "enum" to define the states, events, and state transition table.
Action methods are defined for each state based on the abstract method for the state enum.

Other examples include the use of "if" statements, "switch" statements, "enum" statements, and a"map" statement to define a state
transition table.  These examples may be approproiate for trivial FSM but beware that often a FSM grows after its initial definition
so taking a little extra time upfront to use say the full example of the FSMtemplate can reap dividends later.

The rosettacode half-heartedly uses the "enum" statement for the states but then mixes the events and transitions into
the same "enum".  Not very good modular coding.  It is interesting how the "map" statement is used for the state
transition table.  But it doesn't seem any better than the "enum" in FSMtemplate.  The rosettacode does show "explicit" and
"implicit" events - internal and external triggers.  It's another way of handling such cases.  Again not sure that
FanFSM or FSMtemplate aren't the "best" examples for the robot team needs.

# FSMtraining

Finite State Machines lessons - examples and "answers" to exercises.

#1 FanFSMIfSwitch – local; Scanner input; “if” FSM; “switch” FSM
Student Exercise Answer Key

#2 NotificationStates – local; state pattern; Scanner input
Demonstration shown as completed

#3 FileFSM – local; state pattern; Scanner input
Student Exercise Answer Key

#4 TurnstileFSM – roboRIO; j-easy easy states (SLAB older version); 5 turnstiles; SmartDashboard input
Demonstration shown as completed

#5 FanFSMroboRIO – roboRIO; j-easy easy states (SLAB newer version); 2 fans; SmartDashboard input
Student Exercise Answer Key

#6 ElevatorFSM Example – roboRIO; 3 different coding styles - linear path and 2 FSM styles; Joystick input
See Elevator2019Robot.java for FSM version
Demonstration shown as completed

#7 FSMtemplate - roboRIO; similar to the FSM example in #6.  One complete worked nice FSM style using enums and table look up
of a Moore FSM - a good FSM model and it even throws in a decision table usage for managing events as a bonus example!

#8 RosettaCode - copied a FSM example (Java version) from RosettaCode web site

#9 FanFSM - roboRIO; most complete example using Java enum and table lookup of a Moore FSM - best example here

#10 TableLamp - another good, simple example with required doEnter and optional, common, overridable doAction
