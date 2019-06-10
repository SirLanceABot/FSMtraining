package frc.robot;

import org.jeasy.states.api.FiniteStateMachineException;

import Fan.Fan;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

  int numFans = 2;

  private static final String kReset = "";
  private static final String kOff = "off";
  private static final String kPullChain = "p";
  public Fan[] fan = new Fan[numFans];
  private String[] fanName = {"Bathroom", "Laundry"};
  private int[] fanMotorID = {0, 1};
  {
     for(int idx=0; idx < fan.length; idx++)
       fan[idx] = new Fan(fanName[idx], fanMotorID[idx]);
  }

  @Override
  public void robotInit() {
    
    try {
      for(int idx=0; idx<fan.length; idx++)
      {
         fan[idx].fanInit();
      }
    } catch (FiniteStateMachineException e) {
		e.printStackTrace();
  }

    for(int idx = 0; idx < fan.length; idx++)
  {
    SmartDashboard.putString("Fan User Action "+idx, "");
    SmartDashboard.putString("Fan State "+idx, kOff);
  }
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {}
 
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopPeriodic() {
    long t = System.currentTimeMillis();

    // run the FSMs at this iteration
    try {
      for(int idx=0; idx<fan.length; idx++)
       fan[idx].fanRun();
    } catch (FiniteStateMachineException e) {
		e.printStackTrace();
    }
    // completed running the FSMs

    //System.out.println( "end of FSMs " + (-t + (t = System.currentTimeMillis())) );
  }
}
