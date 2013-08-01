package com.misfit.ta.ios;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.report.TestReportConnection;
import com.misfit.ta.aut.AutomationTest;

public class TRSTestAPI extends ModelAPI
{
	
	public TRSTestAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight)
	{
		super(automation, model, efsm, generator, weight);
	}
	
	private static Logger logger = Util.setupLogger(TestReportConnection.class);
	
	/**
	 * This method implements the Edge 'e_Edge1'
	 * 
	 */
	public void e_Edge1()
	{
		logger.debug("Edge 1");
	}
	
	/**
	 * This method implements the Edge 'e_Edge2'
	 * 
	 */
	public void e_Edge2()
	{
		logger.debug("Edge 2");
	}
	
	/**
	 * This method implements the Edge 'e_Edge3'
	 * 
	 */
	public void e_Edge3()
	{
		logger.debug("Edge 3");
		Assert.assertTrue(false, "Test throwable");
	}
	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init()
	{
		logger.debug("Init");
	}
	
	/**
	 * This method implements the Vertex 'v_Vertex1'
	 * 
	 */
	public void v_Vertex1()
	{
		logger.debug("Vertex 1");
	}
	
	/**
	 * This method implements the Vertex 'v_Vertext2'
	 * 
	 */
	public void v_Vertext2()
	{
		logger.debug("Vertex 2");
	}
	
	/**
	 * This method implements the Vertex 'v_Vertext3'
	 * 
	 */
	public void v_Vertext3()
	{
		logger.debug("Vertex 3");
	}
	
}
