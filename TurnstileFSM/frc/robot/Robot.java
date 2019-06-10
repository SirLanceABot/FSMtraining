package frc.robot;

import org.jeasy.states.api.FiniteStateMachineException;

import Turnstile.Turnstile;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

  private static final String kReset = "";
  private static final String kLock = "locked";
  private static final String kCoin = "c";
  private static final String kPush = "p";
 
   
  // private Turnstile[] turnstile = {
  //   new Turnstile(),
  //   new Turnstile(),
  //   new Turnstile(),
  //   new Turnstile(),
  //   new Turnstile(),
  // };
  
  private Turnstile[] turnstile = new Turnstile[5];
  {
          
    for(int idx=0; idx < turnstile.length; idx++)
      turnstile[idx] = new Turnstile();
  }

  @Override
  public void robotInit() {
    
    try {
      for(int idx=0; idx<turnstile.length; idx++)
      {
        System.out.println("\nTurnstile " + idx);
        turnstile[idx].turnstileInit();
      }
    } catch (FiniteStateMachineException e) {
		e.printStackTrace();
  }

    for(int idx = 0; idx < turnstile.length; idx++)
	{
    SmartDashboard.putString("Turnstile User Action "+idx, kReset);
    SmartDashboard.putString("Turnstile State "+idx, kLock);
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
      for(int idx=0; idx<turnstile.length; idx++)
       turnstile[idx].turnstileRun(idx);
    } catch (FiniteStateMachineException e) {
		e.printStackTrace();
    }
    // completed running the FSMs

    //System.out.println( "end of FSMs " + (-t + (t = System.currentTimeMillis())) );
  }
}
