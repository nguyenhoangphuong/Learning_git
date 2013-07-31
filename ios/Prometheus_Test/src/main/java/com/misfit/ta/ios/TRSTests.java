package com.misfit.ta.ios;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.TestNG;

import com.misfit.ta.report.TRS;

public class TRSTests extends TestNG 
{

	private static String udid = null;
	private static Logger logger = Util.setupLogger(TRSTests.class);

	public static void main(String[] args)
	{
		if (args != null && args.length >= 2)
		{
			udid = args[1];

			logger.info("UDID: " + udid);
			String[] argv = new String[args.length - 1];

			for (int i = 0; i < argv.length; i++)
			{
				argv[i] = args[i];
			}
			System.setProperty("udid", udid);

			runTest(argv);

		}
		else
		{
			String[] params = {"testng.xml"}; 
			runTest(params);
		}
	}

	public static void runTest(String[] argv)
	{
		try 
		{
			// start session
			TRS.instance().startSession("com.misfit.trs.test", "TRS Test", "hainguyen");
			
			logger.info("Running the test with udid = " + udid);
			TestNG.main(argv);
			logger.info("Session end");
			
			// end session
			TRS.instance().endSession();
		}
		catch (Exception e)
		{
			logger.info("Exception e: " + e);
		}	
	}
}
