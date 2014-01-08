package com.misfit.ta.gui;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.AppHelper;
import com.misfit.ta.utils.ProcessFinder;

public class InstrumentHelper implements Runnable
{
    private volatile Thread instrument;
    private static Logger logger = Util.setupLogger(InstrumentHelper.class);
    public String script = "script/automation/loop.js";
    
    public void start()
    { 
    	if(instrument != null) {
    		kill();
    	}
    	
    	instrument = new Thread(this);
    	logger.info("Launching Instrument...");
    	instrument.start();
    }
    
    public void stop()
    {
    	Thread interrupter = instrument;
    	instrument = null;
    	if(interrupter != null)
    		interrupter.interrupt();
    }
    
	public void run() 
	{
		while(instrument == Thread.currentThread())
		{
			AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), script);
		}
	}
	
	public String getState() {
		
		return instrument.getState().name();
	}
	
	public boolean isFinished() {
		
		return instrument.getState() == Thread.State.TERMINATED;
	}
	
	public void kill()
	{
		ProcessFinder.kill("Instruments.app");
		ProcessFinder.kill("instruments");
		instrument = null;
	}

}
