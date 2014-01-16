package com.misfit.ta.backend.api.social;


import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;

public class SocialAPI extends MVPApi {
	
	protected static Logger logger = Util.setupLogger(SocialAPI.class);
	
	public static final Integer STATUS_NOT_REQUESTED = 0;
	public static final Integer STATUS_REQUESTED_FROM_ME = 1;
	public static final Integer STATUS_REQUESTED_TO_ME = 2;
	public static final Integer STATUS_APPROVED = 3;
	public static final Integer STATUS_IGNORED = 4;
	public static final Integer STATUS_REJECTED = 5;
	
	
	public static BaseResult getFriends(String token) {
		
		String url = baseAddress + "friends";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getFacebookFriends(String token) {
		
		String url = baseAddress + "fb_friends";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult searchSocialUsers(String token, String keyword) {
		
		String url = baseAddress + "users/search";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		if(keyword != null)
			requestInf.addParam("keyword", keyword);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult getFriendRequestsToMe(String token) {
		
		String url = baseAddress + "friend_requests/to_me";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getFriendRequestsFromMe(String token) {
		
		String url = baseAddress + "friend_requests/from_me";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	public static BaseResult sendFriendRequest(String token, String friendUserId) {
		
		String url = baseAddress + "friend_requests/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult acceptFriendRequest(String token, String friendUserId) {
		
		String url = baseAddress + "friend_requests/accept/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult ignoreFriendRequest(String token, String friendUserId) {
		
		String url = baseAddress + "friend_requests/ignore/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult cancelFriendRequest(String token, String friendUserId) {
		
		String url = baseAddress + "friend_requests/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = delete(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult deleteFriend(String token, String friendUserId) {
		
		String url = baseAddress + "friends/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = delete(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
    public static AccountResult connectFacebook(String email, String accessToken, String accessTokenSecret) {

        String url = baseAddress + "connect_facebook";
        BaseParams request = new BaseParams();
        ServiceResponse response;

        request.addParam("access_token", accessToken);
        request.addParam("access_token_secret", accessTokenSecret);
        request.addParam("email", email);

        response = MVPApi.post(url, port, request);

        AccountResult result = new AccountResult(response);

        return result;
    }

    public static BaseResult linkFacebook(String token, String email, String accessToken) {

        String url = baseAddress + "link_facebook";
        BaseParams request = new BaseParams();
        ServiceResponse response;

        request.addHeader("auth_token", token);
        request.addParam("access_token", accessToken);
        request.addParam("email", email);

        response = MVPApi.post(url, port, request);
        return new BaseResult(response);
    }

    
	public static BaseResult getLeaderboardInfo(String token) {
		
		String url = baseAddress + "leaderboard";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getWorldInfo(String token) {
		
		String url = baseAddress + "world";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
}

