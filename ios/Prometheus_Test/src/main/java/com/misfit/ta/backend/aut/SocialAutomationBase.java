package com.misfit.ta.backend.aut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.BeforeClass;

import com.misfit.ta.backend.api.social.SocialAPI;

public class SocialAutomationBase extends BackendAutomation {

	protected static Logger logger = Util.setupLogger(SocialAutomationBase.class);
	
	protected String misfitToken;
	protected String tungToken;
	protected String thyToken;
	
	protected String misfitUid;
	protected String tungUid;
	protected String thyUid;
	
	protected Map<String, HashMap<String, Object>> mapNameData;
	protected List<String> mapNames;
	
	
	// set up and clean up
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
			
		// get social test data
		mapNameData = SocialTestHelpers.getSocialInitialTestData();
		
		misfitToken = (String) mapNameData.get("misfit").get("token");
		tungToken = (String) mapNameData.get("tung").get("token");
		thyToken = (String) mapNameData.get("thy").get("token");
		
		misfitUid = (String) mapNameData.get("misfit").get("fuid");
		tungUid = (String) mapNameData.get("tung").get("fuid");
		thyUid = (String) mapNameData.get("thy").get("fuid");
		
		
		// map key set
		Iterator<String> iterator = mapNameData.keySet().iterator();
		mapNames = new ArrayList<String>();
		while(iterator.hasNext())
			mapNames.add(iterator.next());
		
		
		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
		SocialAPI.deleteFriend(misfitToken, thyUid);
		SocialAPI.deleteFriend(tungToken, thyUid);
	}
}
