package com.misfit.ta.backend.api.social;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class SocialAPI extends MVPApi {

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
	
	public static BaseResult searchSocialUsers(String token, String keyword) {
		
		String url = baseAddress + "users/search";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("keyword", keyword);

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
	
	public static BaseResult deleteFriend(String token, String friendUserId) {
		
		String url = baseAddress + "friends/" + friendUserId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = delete(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	
}