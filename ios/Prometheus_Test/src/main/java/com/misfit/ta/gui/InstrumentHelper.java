package com.misfit.ta.gui;

import com.misfit.ios.AppHelper;

public class InstrumentHelper implements Runnable
{
    private volatile Thread instrument;
    private volatile Thread checker;
    
    public void start()
    { 
    	instrument = new Thread(this);
    	instrument.start();
    }
    
    public void stop()
    {
    	checker = instrument;
    	
    	Thread interrupter = instrument;
    	instrument = null;
    	interrupter.interrupt();
    }
    
	public void run() 
	{
		while(instrument == Thread.currentThread())
		{
			AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/automation/alertsupport.js");
		}
	}
	
	public void waitTillFinished()
	{
		while(true)
		{
            if (checker.getState() != Thread.State.TERMINATED) 
            {
                checker.interrupt();
            }
            else
            	break;
		}
	}

}
