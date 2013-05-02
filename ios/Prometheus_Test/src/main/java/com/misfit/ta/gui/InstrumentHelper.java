package com.misfit.ta.gui;

import com.misfit.ios.AppHelper;

public class InstrumentHelper implements Runnable
{
    private volatile Thread instrument;
    
    public void start()
    { 
    	instrument = new Thread(this);
    	instrument.start();
    }
    
    public void stop()
    {
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

}
