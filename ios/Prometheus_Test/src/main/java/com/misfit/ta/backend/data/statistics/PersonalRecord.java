package com.misfit.ta.backend.data.statistics;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class PersonalRecord {

	// fields
	protected Record personalBestRecordsInPoint;
	protected Record Swimming;
	protected Record Cycling;
	protected Record Tennis;
	protected Record Basketball;
	protected Record Soccer;

	
	// constructors
	public PersonalRecord() {
	}


	// methods
	public JSONObject toJson() {
		
		try {
			
			JSONObject obj = new JSONObject();
			if(personalBestRecordsInPoint != null)
				obj.accumulate("personalBestRecordsInPoint", personalBestRecordsInPoint.toJson());

			if(Swimming != null)
				obj.accumulate("Swimming", Swimming.toJson());
			
			if(Cycling != null)
				obj.accumulate("Cycling", Cycling.toJson());
			
			if(Tennis != null)
				obj.accumulate("Tennis", Tennis.toJson());
			
			if(Basketball != null)
				obj.accumulate("Basketball", Basketball.toJson());
			
			if(Soccer != null)
				obj.accumulate("Soccer", Soccer.toJson());

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PersonalRecord fromJson(JSONObject json) {
		PersonalRecord obj = new PersonalRecord();
		try {
			
			if (!json.isNull("personalBestRecordsInPoint"))
				obj.setPersonalBestRecordsInPoint(Record.fromJson(json.getJSONObject("personalBestRecordsInPoint")));

			if (!json.isNull("Swimming"))
				obj.setSwimming(Record.fromJson(json.getJSONObject("Swimming")));
			
			if (!json.isNull("Cycling"))
				obj.setCycling(Record.fromJson(json.getJSONObject("Cycling")));
			
			if (!json.isNull("Tennis"))
				obj.setTennis(Record.fromJson(json.getJSONObject("Tennis")));
			
			if (!json.isNull("Basketball"))
				obj.setBasketball(Record.fromJson(json.getJSONObject("Basketball")));
			
			if (!json.isNull("Soccer"))
				obj.setSoccer(Record.fromJson(json.getJSONObject("Soccer")));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}

	
	// getters setters
	public Record getSwimming() {
		return Swimming;
	}

	public void setSwimming(Record swimming) {
		Swimming = swimming;
	}

	public Record getCycling() {
		return Cycling;
	}

	public void setCycling(Record cycling) {
		Cycling = cycling;
	}

	public Record getTennis() {
		return Tennis;
	}

	public void setTennis(Record tennis) {
		Tennis = tennis;
	}

	public Record getBasketball() {
		return Basketball;
	}

	public void setBasketball(Record basketball) {
		Basketball = basketball;
	}

	public Record getSoccer() {
		return Soccer;
	}

	public void setSoccer(Record soccer) {
		Soccer = soccer;
	}

	public Record getPersonalBestRecordsInPoint() {
		return personalBestRecordsInPoint;
	}

	public void setPersonalBestRecordsInPoint(Record personalBestRecordsInPoint) {
		this.personalBestRecordsInPoint = personalBestRecordsInPoint;
	}

}
