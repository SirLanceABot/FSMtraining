package Fan;

public class MediumState extends fanState
{
    MediumState(final String name, Fan fanController)
    {
        super(name, fanController);
        super.setFanSpeed(0.3);
    }
}