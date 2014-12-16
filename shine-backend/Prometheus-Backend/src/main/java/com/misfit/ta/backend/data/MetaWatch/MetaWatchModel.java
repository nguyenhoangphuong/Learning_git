package com.misfit.ta.backend.data.MetaWatch;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.resting.json.JSONObject;

public class MetaWatchModel {
	private String userId;
	private String deviceModel;
	private String data;
	private String signature;
	private static final String HMAC_ALGORITHM = "HmacMD5";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature){
		this.signature = signature;
	}
	
	public String calSignature(String key){
		String data = this.userId + this.deviceModel + this.data;
		return calHmac(data, key);
	}
	
	public String calSignatureWithoutUserId(String key){
		String data = this.deviceModel + this.data;
		return calHmac(data, key);
	}
	
	public String calSignatureWithoutDeviceModel(String key){
		String data = this.userId + this.data;
		return calHmac(data, key);
	}
	
	public String calSignatureWithoutData(String key){
		String data = this.userId + this.deviceModel;
		return calHmac(data, key);
	}
	
	public JSONObject toJson(){
		try{
			JSONObject object = new JSONObject();
			object.accumulate("user_id", userId);
			object.accumulate("device_model", deviceModel);
			object.accumulate("data", data);
			object.accumulate("signature", signature);
			return object;
		}catch(Exception e){
			return null;
		}
	}
	
	
	private String calHmac(String data, String key){
//		String secretKey = "eqFNHUAshCeQIZgSc48Yh80bTo9OVSYXkMfThspX";
		Mac mac;

		try {
			mac = Mac.getInstance(HMAC_ALGORITHM);
			SecretKeySpec sk = new SecretKeySpec(key.getBytes(),
					mac.getAlgorithm());
			mac.init(sk);

			byte[] result;
			result = mac.doFinal(data.getBytes());

			return this.bytesToString(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String bytesToString(byte[] bytes) {
		return this.bytesToString(bytes, "");
	}
	
	private String bytesToString(byte[] bytes, String seperator) {
		if (bytes == null || bytes.length == 0)
			return null;

		String format = "%02X";
		String formatWithSeperator = format + seperator;

		StringBuilder sb = new StringBuilder();

		int i = 0;
		while (i < bytes.length - 1) {
			sb.append(String.format(Locale.US, formatWithSeperator, bytes[i++]));
		}
		if (i == bytes.length - 1) {
			sb.append(String.format(Locale.US, format, bytes[i]));
		}
		return sb.toString().toLowerCase(Locale.US);
	}
}
