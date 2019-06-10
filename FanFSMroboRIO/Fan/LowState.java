package Fan;

public class LowState extends fanState
{
    LowState(final String name, Fan fanController)
    {
        super(name, fanController);
        super.setFanSpeed(0.2);
    }
}