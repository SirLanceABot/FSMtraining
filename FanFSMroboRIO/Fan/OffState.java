package Fan;

public class OffState extends fanState
{
    OffState(final String name, Fan fanController)
    {
        super(name, fanController);
        super.setFanSpeed(0.0);
    }
}