package com.misfit.ta.ios;

import org.testng.annotations.Test;
import com.misfit.ta.report.TRS;

public class TRSSampleTest extends TRSAutomation
{
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TRSTest" })
    public void TRSTestOne() 
    {
		TRS.instance().addStep("Step 1", null);
		TRS.instance().addCode("Code 1.1", null);
		TRS.instance().addCode("Code 1.2", null);
		TRS.instance().addStep("Step 2", null);
		TRS.instance().addCode("Code 2.1", null);
		TRS.instance().addCode("Code 2.2", null);
		
    }

}
