package com.misfit.ta.backend.data.MetawatchModel;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class MetawatchProfileModel {
	private String dateOfBirth;
    private Integer distanceUnit;
    private Integer weightUnit;
    private Integer userHeightInches;
    private Double weight;
    private Double height;
    private Integer gender;
    private String email;
    private String password;
	
    public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(Integer distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public Integer getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(Integer weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Integer getUserHeightInches() {
		return userHeightInches;
	}

	public void setUserHeightInches(Integer userHeightInches) {
		this.userHeightInches = userHeightInches;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Integer getGender() {
		return gender;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String pwd) {
		this.password = pwd;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public MetawatchProfileModel(){
    	
    }
	
	public JSONObject toJson(){
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("weight", weight);
			obj.accumulate("userHeight", height);
			obj.accumulate("gender", gender);
			obj.accumulate("userHeightInches", userHeightInches);
			obj.accumulate("weightUnit", weightUnit);
			obj.accumulate("dateOfBirth", dateOfBirth);
			obj.accumulate("distanceUnit", distanceUnit);
			obj.accumulate("email", email);
			obj.accumulate("password", password);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
