package Fan;

public class MediumState extends fanState
{
    MediumState(final String name, Fan fanController)
    {
        super(name, fanController);
    }
    
    @Override
    public void doEnter()
    {
        super.setFanSpeed(0.3);
    }
}