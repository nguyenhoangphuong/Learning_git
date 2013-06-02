package com.misfit.ta.backend.data.timeline;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.JSONBuilder;

public class WeatherItem extends TimelineItemBase {

    private int temperatureF;
    private int code;
    private String locationName;

    public WeatherItem(long timestamp, int temperatureF, int code, String locationName) {
        super(TYPE_WEATHER, timestamp);
        this.temperatureF = temperatureF;
        this.code = code;
        this.locationName = locationName;

    }

    public int getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(int temperatureF) {
        this.temperatureF = temperatureF;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.accumulate("temperatureF", temperatureF);
            object.accumulate("code", temperatureF);
            object.accumulate("locationName", locationName);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
}
