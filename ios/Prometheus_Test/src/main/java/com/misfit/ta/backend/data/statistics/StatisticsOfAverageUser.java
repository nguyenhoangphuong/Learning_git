package com.misfit.ta.backend.data.statistics;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class StatisticsOfAverageUser extends Statistics {

	// fields
	protected Integer todayAverage;
	protected Integer todayRecord;
	protected Integer yesterdayAverage;
	protected Integer yesterdayRecord;
	protected Integer weekRecord;
	
	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = super.toJson();
			
			obj.accumulate("todayAverage", todayAverage);
			obj.accumulate("todayRecord", todayRecord);
			obj.accumulate("yesterdayAverage", yesterdayAverage);
			obj.accumulate("yesterdayRecord", yesterdayRecord);
			obj.accumulate("weekRecord", weekRecord);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public StatisticsOfAverageUser fromJson(JSONObject json) {
		StatisticsOfAverageUser obj = new StatisticsOfAverageUser();
		try {
			
			super.fromJson(json);
			
			if (!json.isNull("todayAverage"))
				obj.setTodayAverage(json.getInt("todayAverage"));
			
			if (!json.isNull("todayRecord"))
				obj.setTodayRecord(json.getInt("todayRecord"));
			
			if (!json.isNull("yesterdayAverage"))
				obj.setYesterdayAverage(json.getInt("yesterdayAverage"));
			
			if (!json.isNull("yesterdayRecord"))
				obj.setYesterdayRecord(json.getInt("yesterdayRecord"));
			
			if (!json.isNull("weekRecord"))
				obj.setWeekRecord(json.getInt("weekRecord"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static StatisticsOfAverageUser fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONObject jsonItem = jsonResponse.getJSONObject("statistics");
			StatisticsOfAverageUser statistics = new StatisticsOfAverageUser();
			
			return statistics.fromJson(jsonItem);
		} catch (JSONException e) {
			return null;
		}
	}
	
	
	// getters setters
	public Integer getTodayAverage() {
		return todayAverage;
	}
	
	public void setTodayAverage(Integer todayAverage) {
		this.todayAverage = todayAverage;
	}
	
	public Integer getTodayRecord() {
		return todayRecord;
	}
	
	public void setTodayRecord(Integer todayRecord) {
		this.todayRecord = todayRecord;
	}
	
	public Integer getYesterdayAverage() {
		return yesterdayAverage;
	}
	
	public void setYesterdayAverage(Integer yesterdayAverage) {
		this.yesterdayAverage = yesterdayAverage;
	}
	
	public Integer getYesterdayRecord() {
		return yesterdayRecord;
	}
	
	public void setYesterdayRecord(Integer yesterdayRecord) {
		this.yesterdayRecord = yesterdayRecord;
	}
	
	public Integer getWeekRecord() {
		return weekRecord;
	}
	
	public void setWeekRecord(Integer weekRecord) {
		this.weekRecord = weekRecord;
	}

}
