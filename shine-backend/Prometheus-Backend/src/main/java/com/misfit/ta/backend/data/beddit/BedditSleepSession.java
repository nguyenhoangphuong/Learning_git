package com.misfit.ta.backend.data.beddit;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class BedditSleepSession {

	// fields
	protected String localId;
	protected String serverId;
	protected Long updatedAt;
	
	protected BedditSleepSessionTimeData timeData;
	protected BedditSleepSessionProperties properties;
	protected List<Object[]> sleepStates;
	protected List<Object[]> heartRates;
	protected List<Object[]> snoringEpisodes;

       
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
        	object.accumulate("localId", localId);
        	object.accumulate("serverId", serverId);
        	object.accumulate("updatedAt", updatedAt);
        	
        	if(timeData != null)
            	object.accumulate("timeData", timeData.toJson());
        	
        	if(properties != null)
            	object.accumulate("properties", properties.toJson());
        	
        	if(sleepStates != null) {

        		JSONArray jsonarr = new  JSONArray();
        		for(int i = 0; i < sleepStates.size(); i++) {
        			JSONArray recordarr = new JSONArray();
        			recordarr.put(sleepStates.get(i)[0]);
        			recordarr.put(sleepStates.get(i)[1]);

        			jsonarr.put(recordarr);
        		}

        		object.put("sleepStates", jsonarr);
        	}

        	if(heartRates != null) {

        		JSONArray jsonarr = new  JSONArray();
        		for(int i = 0; i < heartRates.size(); i++) {
        			JSONArray recordarr = new JSONArray();
        			recordarr.put(heartRates.get(i)[0]);
        			recordarr.put(heartRates.get(i)[1]);

        			jsonarr.put(recordarr);
        		}

        		object.put("heartRates", jsonarr);
        	}

        	if(snoringEpisodes != null) {

        		JSONArray jsonarr = new  JSONArray();
        		for(int i = 0; i < snoringEpisodes.size(); i++) {
        			JSONArray recordarr = new JSONArray();
        			recordarr.put(snoringEpisodes.get(i)[0]);
        			recordarr.put(snoringEpisodes.get(i)[1]);

        			jsonarr.put(recordarr);
        		}

        		object.put("snoringEpisodes", jsonarr);
        	}
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public BedditSleepSession fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("localId"))
				this.setLocalId(json.getString("localId"));
			
			if (!json.isNull("serverId"))
				this.setServerId(json.getString("serverId"));
			
			if (!json.isNull("updatedAt"))
				this.setUpdatedAt(json.getLong("updatedAt"));
			
			if (!json.isNull("timeData"))
				this.setTimeData((new BedditSleepSessionTimeData()).fromJson(json.getJSONObject("timeData")));
			
			if (!json.isNull("properties"))
				this.setProperties((new BedditSleepSessionProperties()).fromJson(json.getJSONObject("properties")));
			
			if (!json.isNull("sleepStates")) {
				JSONArray jsonarr = json.getJSONArray("sleepStates");
				sleepStates = new ArrayList<Object[]>();
				
				for(int i = 0; i < jsonarr.length(); i++) {
					sleepStates.add(new Object[] {jsonarr.getLong(0), jsonarr.getInt(1)});
				}
			}
			
			if (!json.isNull("heartRates")) {
				JSONArray jsonarr = json.getJSONArray("heartRates");
				heartRates = new ArrayList<Object[]>();
				
				for(int i = 0; i < jsonarr.length(); i++) {
					heartRates.add(new Object[] {jsonarr.getLong(0), jsonarr.getDouble(1)});
				}
			}
			
			if (!json.isNull("snoringEpisodes")) {
				JSONArray jsonarr = json.getJSONArray("snoringEpisodes");
				snoringEpisodes = new ArrayList<Object[]>();
				
				for(int i = 0; i < jsonarr.length(); i++) {
					snoringEpisodes.add(new Object[] {jsonarr.getLong(0), jsonarr.getInt(1)});
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
    
    
    // getters setters
	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BedditSleepSessionTimeData getTimeData() {
		return timeData;
	}

	public void setTimeData(BedditSleepSessionTimeData timeData) {
		this.timeData = timeData;
	}

	public BedditSleepSessionProperties getProperties() {
		return properties;
	}

	public void setProperties(BedditSleepSessionProperties properties) {
		this.properties = properties;
	}

	public List<Object[]> getSleepStates() {
		return sleepStates;
	}

	public void setSleepStates(List<Object[]> sleepStates) {
		this.sleepStates = sleepStates;
	}

	public List<Object[]> getHeartRates() {
		return heartRates;
	}

	public void setHeartRates(List<Object[]> heartRates) {
		this.heartRates = heartRates;
	}

	public List<Object[]> getSnoringEpisodes() {
		return snoringEpisodes;
	}

	public void setSnoringEpisodes(List<Object[]> snoringEpisodes) {
		this.snoringEpisodes = snoringEpisodes;
	}
	
}
