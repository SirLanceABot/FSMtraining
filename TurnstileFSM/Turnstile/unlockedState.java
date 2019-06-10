package Turnstile;

import org.jeasy.states.api.State;

public class unlockedState extends State
{
    unlockedState(final String name)
    {
        super(name);
    }

@Override
public void doEnter()
{
    System.out.println("unlockedState doEnter");
}

@Override
public void doAction()
{
    //System.out.println("unlockedState doAction");
}

@Override
public void doExit()
{
    System.out.println("unlockedState doExit");
}
}