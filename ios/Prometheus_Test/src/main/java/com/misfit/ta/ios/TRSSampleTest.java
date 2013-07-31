package com.misfit.ta.ios;

import org.testng.annotations.Test;
import com.misfit.ta.report.TRS;

public class TRSSampleTest extends TRSAutomation
{
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TRSTest" })
    public void TRSTestOne() 
    {
		TRS.instance().addStep("Step 10", null);
		TRS.instance().addCode("Code 10.1", null);
		TRS.instance().addCode("Code 10.2", null);
		TRS.instance().addStep("Step 20", null);
		TRS.instance().addCode("Code 20.1", null);
		TRS.instance().addCode("Code 20.2", null);
		
    }

}
