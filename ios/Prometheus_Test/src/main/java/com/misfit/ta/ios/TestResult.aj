package com.misfit.ta.ios;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.modelAPI.ModelAPI;

public aspect TestResult {
	
	private static Logger logger = Util.setupLogger(TestResult.class);
	
	// Report that the test failed
	after(ModelAPI modelAPI) throwing: addVertexResult() && this(modelAPI) {
		try {
			logger.debug("Method: " + thisJoinPoint.getSignature().toString() + ": FAILED");
			modelAPI.getMbt().passRequirement(false);
			String reqs[] = modelAPI.getMbt().getCurrentRequirement();
		} catch (Exception ignore ) {      
		} finally {
		}
	}
	
	// Report that the test passed
	after(ModelAPI modelAPI) returning: addVertexResult() && this(modelAPI) {
		logger.debug("Method: " + thisJoinPoint.getSignature().toString() + ": PASSED");
		modelAPI.getMbt().passRequirement(true);
		String reqs[] = modelAPI.getMbt().getCurrentRequirement();
	}
	
	
	// Report that the test passed
	after(ModelAPI modelAPI) returning: addEdgeResult() && this(modelAPI) {
		logger.debug("Method: " + thisJoinPoint.getSignature().toString() + ": PASSED");
		modelAPI.getMbt().passRequirement(true);
		String reqs[] = modelAPI.getMbt().getCurrentRequirement();
	}
	
	pointcut addVertexResult():
		execution(* com.spotify.qa.modelAPI.*..*.v_*(..));
	
	pointcut addEdgeResult():
		execution(* com.spotify.qa.modelAPI.*..*.e_*(..));
}
