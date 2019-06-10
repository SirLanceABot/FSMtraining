package Fan;

public class HighState extends fanState
{
    HighState(final String name, Fan fanController)
    {
        super(name, fanController);
        super.setFanSpeed(0.4);
    }
}