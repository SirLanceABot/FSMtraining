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
package Turnstile;

import java.util.Set;
import java.lang.String;
import java.util.HashSet;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.TransitionBuilder;
import org.jeasy.states.core.FiniteStateMachineBuilder;

/**
 * This tutorial is an implementation of the turnstile FSM described in <a href="http://en.wikipedia.org/wiki/Finite-state_machine">wikipedia</a>:
 * <p>
 * The turnstile has two states: Locked and Unlocked. There are two inputs that affect its state: putting a coin in the slot (coin) and pushing the arm (push).
 * In the locked state, pushing on the arm has no effect; no matter how many times the input push is given it stays in the locked state.
 * Putting a coin in, that is giving the machine a coin input, shifts the state from Locked to Unlocked.
 * In the unlocked state, putting additional coins in has no effect; that is, giving additional coin inputs does not change the state.
 * However, a customer pushing through the arms, giving a push input, shifts the state back to Locked.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Turnstile {

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
        /*
         * Define FSM states
         */
        State locked = new State("locked");
        State unlocked = new unlockedState("unlocked");
        //State broken = new State("broken");
        Set<State> states = new HashSet<State>();
        {
            states.add(locked);
            states.add(unlocked);
            //states.add(broken);
        }

        FiniteStateMachine turnstileStateMachine;
    
        public void turnstileInit() throws FiniteStateMachineException {

            /*
            * Define FSM transitions
            */
            Transition unlock = new TransitionBuilder().name("unlock")      .sourceState(locked)   .eventType(CoinEvent.class) .transitionHandler(new Unlock())     .targetState(unlocked).build();
            //pushLocked  = new TransitionBuilder().name("pushLocked") .sourceState(locked)   .eventType(PushEvent.class)    /* no transitionHandler */        .targetState(broken).build();
            Transition lock  = new TransitionBuilder().name("lock")       .sourceState(unlocked) .eventType(PushEvent.class) .transitionHandler(new Lock())       .targetState(locked).build();
            Transition coinUnlocked = new TransitionBuilder().name("coinUnlocked").sourceState(unlocked) .eventType(CoinEvent.class) .transitionHandler(new CoinReturn()) .targetState(unlocked).build();
            
            /*
            * Build FSM instance
            */
            turnstileStateMachine = new FiniteStateMachineBuilder(states, locked) // note that this also defines the initial state
                    .registerTransition(lock)
                    //.registerTransition(pushLocked)
                    .registerTransition(unlock)
                    .registerTransition(coinUnlocked)
                    //.registerFinalState(broken) // optional final state - in this code it's really final not just accepted
                    .build();

            System.out.println(turnstileStateMachine);
            
            /*
            * Fire some events and print FSM state
            */
    
            // System.out.println("1. Push [p]");
            // System.out.println("2. Coin [c]");
      }
    public void turnstileRun(int idx) throws FiniteStateMachineException {
   
            // logic to fire states based on input events

            // figure out which event should be fired on this iteration
            String input = SmartDashboard.getString("Turnstile User Action "+idx, ""); // get user input
            SmartDashboard.putString("Turnstile User Action "+idx, ""); // one shot event - mark it as processed (nulled)

            //String input = "p"; // trivial input - it never changes
            input = input.trim();
            //System.out.println("input = " + input);

            if (input.equalsIgnoreCase("p")) {
                turnstileStateMachine.fire(new PushEvent());
                //System.out.println("Turnstile state : " + turnstileStateMachine.getCurrentState().getName());
            }
            else
            if (input.equalsIgnoreCase("c")) {
                turnstileStateMachine.fire(new CoinEvent());
                //System.out.println("Turnstile state : " + turnstileStateMachine.getCurrentState().getName());
            }
            else
            if (input.equalsIgnoreCase("")) {
            }
            else System.out.println("Unknown input = " + input);
            // end of processing events

            // current state's doAction
            turnstileStateMachine.getCurrentState().doAction();

            // update user presentation of status
            SmartDashboard.putString("Turnstile State "+idx, turnstileStateMachine.getCurrentState().getName());
   }
}
