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
package org.jeasy.states.core;

import org.jeasy.states.api.*;
import org.jeasy.states.util.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

final class FiniteStateMachineImpl implements FiniteStateMachine {

    private static final Logger LOGGER = Logger.getLogger(FiniteStateMachineImpl.class.getSimpleName());

    private State currentState;
    private State initialState;
    private Set<State> finalStates;
    private Set<State> states;
    private Set<Transition> transitions;
    private Event lastEvent;
    private Transition lastTransition;

    FiniteStateMachineImpl(final Set<State> states, final State initialState) {
        this.states = states;
        this.initialState = initialState;
        currentState = initialState;
        transitions = new HashSet<>();
        finalStates = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized State fire(final Event event) throws FiniteStateMachineException {

        if (!finalStates.isEmpty() && finalStates.contains(currentState)) {
            LOGGER.log(Level.WARNING, "FSM is in final state '" + currentState.getName() + "', event " + event + " is ignored.");
            return currentState;
        }

        if (event == null) {
            LOGGER.log(Level.WARNING, "Null event fired, FSM state unchanged");
            return currentState;
        }

        for (Transition transition : transitions) {
            if (
                    currentState.equals(transition.getSourceState()) && //fsm is in the right state as expected by transition definition
                    transition.getEventType().equals(event.getClass()) && //fired event type is as expected by transition definition
                    states.contains(transition.getTargetState()) //target state is defined
                    ) {
                try {
                    //System.out.println("about to exit " + currentState.getName());
                    currentState.doExit(); // soon to be the previous state needs to exit
                    
                    //perform action, if any
                    if (transition.getTransitionHandler() != null) {
                        transition.getTransitionHandler().handleTransition();
                    }
    
                    //transit to target state
                    currentState = transition.getTargetState();
                    //System.out.println("about to enter " + currentState.getName());
                    currentState.doEnter(); // new state needs to enter
                    // current state's doAction
                    currentState.doAction();

                    //save last triggered event and transition
                    lastEvent = event;
                    lastTransition = transition;

                    break;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred during handling event " + event + " of transition " + transition, e);
                    throw new FiniteStateMachineException(transition, event, e);
                }
            }
        }
   
        return currentState;
    }

    void registerTransition(final Transition transition) {
        transitions.add(transition);
    }

    void registerFinalState(final State finalState) {
        finalStates.add(finalState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getCurrentState() {
        return currentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getInitialState() {
        return initialState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<State> getFinalStates() {
        return finalStates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<State> getStates() {
        return states;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Transition> getTransitions() {
        return transitions;
    }

    @Override
    public Event getLastEvent() {
        return lastEvent;
    }

    @Override
    public Transition getLastTransition() {
        return lastTransition;
    }

    @Override
    public String toString() {
         
    StringBuilder transitionsResult = new StringBuilder();
        for (Transition transition : transitions) transitionsResult.append(transition.toString() + "\n");

        return
            "\ncurrentState " + currentState.getName() + "\n" +
            "initialState " + initialState.getName() + "\n" +
            "Final States " + Utils.dumpFSMStates(finalStates) + "\n" +
            "States " + Utils.dumpFSMStates(states) + "\n" +
            transitionsResult +
            "lastEvent " + (lastEvent == null ? "none" : lastEvent.toString()) + "\n" +
            "lastTransition " + (lastTransition ==  null ? "none" : lastTransition.toString() + "\n");
            
    }

}
