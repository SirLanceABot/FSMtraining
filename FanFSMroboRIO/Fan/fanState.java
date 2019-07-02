package Fan;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.jeasy.states.api.State;

public abstract class fanState extends State
{
    Fan fan;
    double fanSpeed;
    
    fanState(final String name, Fan fanController)
    {
        super(name);
        fan = fanController;
    }

    @Override
    public void doEnter()
    {
        System.out.println("fanState doEnter");
    }

    @Override
    public void doAction()
    {
        fan.fanMotor.set(ControlMode.PercentOutput, fanSpeed);
        //System.out.println("fanState doAction");
    }

    @Override
    public void doExit()
    {
        System.out.println("fanState doExit");
    }

    public void setFanSpeed(double fanSpeed)
    {
        this.fanSpeed = fanSpeed;
    }
}