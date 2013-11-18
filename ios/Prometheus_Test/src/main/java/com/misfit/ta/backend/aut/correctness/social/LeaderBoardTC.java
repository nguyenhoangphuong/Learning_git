package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.social.Leaderboard;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserLeaderBoardEvent;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;

public class LeaderBoardTC extends BackendAutomation {

    protected static Logger logger = Util.setupLogger(LeaderBoardTC.class);
    protected String socialEmail = Settings.getParameter("QAFacebookEmail");
    protected String socialAccessToken = Settings.getParameter("QAFacebookAccessToken");
    protected String socialToken = "";

    protected Map<String, HashMap<String, Object>> mapNameData;
    protected List<String> mapNames;

    // helpers
    public static void printUsers(SocialUserBase[] users) {

        logger.info("-----------------------------------------------------------------------");
        for (SocialUserBase user : users) {
            logger.info(user.toJson().toString());
        }
        logger.info("-----------------------------------------------------------------------\n\n");
    }

   
    // test methods
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
    public void checkLeaderBoard() {

        AccountResult accResult = MVPApi.signIn("thinh@misfitwearables.com", "misfit1");

        TRS.instance().addStep("==================== QUERY LEADERBOARD ====================", null);
        BaseResult result = SocialAPI.getLeaderboardInfo(accResult.token);

        Leaderboard board = Leaderboard.fromResponse(result.response);

        List<SocialUserLeaderBoardEvent> today = board.getToday();

        for (int i = 0; i < today.size(); i++) {
            SocialUserLeaderBoardEvent event = today.get(i);
            System.out.println("LOG [LeaderBoardTC.checkLeaderBoard]: user: " + event.getHandle() + ", point= "
                    + event.getPoints());
        }
        
        long start = System.currentTimeMillis()/1000 - (3600 * 24); 
        
        MVPApi.searchGoal(accResult.token, start, Integer.MAX_VALUE, 0);
        
        

    }



    public static void main(String[] args) throws JSONException {
        LeaderBoardTC test = new LeaderBoardTC();
        test.checkLeaderBoard();
    }

}
