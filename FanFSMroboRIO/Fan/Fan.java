/**
 * The MIT License
 *
 *  Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package Fan;

import java.util.Set;
import java.lang.String;
import java.util.HashSet;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.TransitionBuilder;
import org.jeasy.states.core.FiniteStateMachineBuilder;

/**
 * This tutorial is an implementation of the fan FSM:
 * <p>
 * The fan has four states: Off, Low, Medium, High. There is one input that affects its state: pulling the chain.
 * Each pull of the change advancs the speed.  High speed advances to Off.
 *
 * @author RK Thomas
 */
public class Fan {

    /*
    need states - specific states defined as named objects (variables).  Used as source and target states
    each user-defined state has a constructor and may have doEnter, doAction, and doExit if specialized actions are needed
    (other than the default actions which are no actions taken).

    need events - events to which a FSM should react and make transitions.  An event is defined and used as a class so no
    specific permanent instantiation (object/variable) is needed - just say new myEvent()
    each user-defined event has a constructor

    need event handlers -an event handler is actions to perform when an event is triggered.  At a minimum no actions are required
    in the event handler other than to construct the super to transistion to the next state.
    The FSM framework handles the basic transition automatically if an event is fired.

    need transitions - each user-defined transition is built up in JAva code from a "tabular" type statement of source state,
    event, event handler, and target state.
    A final state may be defined.  In this implementation of an FSM the final state does not accept any more events. 
    The general FSM (which this is not) the acceptatance state might be a final state but generally is just a 
    state which might be considered "idle" waiting for more work to be performed and processed through the FSM.

    */

    public String fanName;
    public TalonSRX fanMotor;
 
    /*
    * Define FSM states
    */
    State off    = new OffState("off", this);
    State low    = new LowState("low", this);
    State medium = new MediumState("medium", this);
    State high   = new HighState("high", this);
    Set<State> states = new HashSet<State>();
    {
       states.add(off);
       states.add(low);
       states.add(medium);
       states.add(high);
    }
 
    FiniteStateMachine fanStateMachine;

    public Fan(String fanName, int fanMotorID)
    {
        this.fanName = fanName;
        fanMotor = new TalonSRX(fanMotorID);
    }

    public void fanInit() throws FiniteStateMachineException {

         /*
        * Define FSM transitions
        */
        Transition pull_1 = new TransitionBuilder().name("low") .sourceState(off)     .eventType(PullChainEvent.class) .transitionHandler(new ChangeSpeed()) .targetState(low)    .build();
        Transition pull_2 = new TransitionBuilder().name("medium") .sourceState(low)  .eventType(PullChainEvent.class) .transitionHandler(new ChangeSpeed()) .targetState(medium) .build();
        Transition pull_3 = new TransitionBuilder().name("high") .sourceState(medium) .eventType(PullChainEvent.class) .transitionHandler(new ChangeSpeed()) .targetState(high)   .build();
        Transition pull_4 = new TransitionBuilder().name("off") .sourceState(high)    .eventType(PullChainEvent.class) .transitionHandler(new ChangeSpeed()) .targetState(off)    .build();
        
        /*
        * Build FSM instance
        */
        System.out.println("Fan " + fanName + "\n" + "Fan TalonSRX ID " + fanMotor.getDeviceID());

        System.out.println("Constructing the FSM and the initial state will be defined and entered now: " + off.getName());
        
        fanStateMachine = new FiniteStateMachineBuilder(states, off) // note that this also defines the initial state
                .registerTransition(pull_1)
                .registerTransition(pull_2)
                .registerTransition(pull_3)
                .registerTransition(pull_4)
                .build();  
 
        System.out.println(fanStateMachine);
}

    public void fanRun() throws FiniteStateMachineException {
   
        // logic to fire states based on input events

        // figure out which event should be fired on this iteration
        String input = SmartDashboard.getString("Fan User Action " + fanName, ""); // get user input
        SmartDashboard.putString("Fan User Action " + fanName, ""); // one shot event - mark it as processed (nulled)

        //String input = "p"; // trivial input - it never changes
        input = input.trim();
        //System.out.println("input = " + input);

        if (input.equalsIgnoreCase("p")) {
            fanStateMachine.fire(new PullChainEvent());
            System.out.println("Fan " + fanName);
        }
        else if (input.equalsIgnoreCase("")) {
        }
        else System.out.println("Unknown input = " + input);
        // end of processing events

        // current state's doAction
        fanStateMachine.getCurrentState().doAction();

        // update user presentation of status
        SmartDashboard.putString("Fan State " + fanName, fanStateMachine.getCurrentState().getName());
   }
}
