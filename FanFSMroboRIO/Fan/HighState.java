package Fan;

public class HighState extends fanState
{
    HighState(final String name, Fan fanController)
    {
        super(name, fanController);
    }

    @Override
    public void doEnter()
    {
        super.setFanSpeed(0.4);
    }
}