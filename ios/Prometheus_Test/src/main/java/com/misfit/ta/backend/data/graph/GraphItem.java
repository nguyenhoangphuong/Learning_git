package com.misfit.ta.backend.data.graph;

import java.util.List;
import java.util.Vector;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GraphItem {
	private long timestamp;
    private double averageValue;
    private double totalValue;
    private String serverId;
    private String localId;
    private long updatedAt;
    
    public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(double averageValue) {
		this.averageValue = averageValue;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    public GraphItem(long timestamp, double averageValue, long updatedAt) {
        this.timestamp = timestamp;
        this.averageValue = averageValue;
        this.updatedAt = updatedAt;
    }

	public static List<GraphItem> getGraphItems(ServiceResponse response)
			throws JSONException {
		List<GraphItem> graphItems = new Vector<GraphItem>();
		JSONObject jsonResponse = new JSONObject(response.getResponseString());
		JSONArray jsonItems = jsonResponse.getJSONArray("graph_items");

		for (int i = 0; i < jsonItems.length(); i++) {
			JSONObject jsonItem = jsonItems.getJSONObject(i);
			
			GraphItem graphItem = new GraphItem(jsonItem.getLong("timestamp"),
					jsonItem.getDouble("averageValue"),
					jsonItem.getLong("updatedAt"));
			
			graphItems.add(graphItem);
		}
		
		return graphItems;
	}
	
	public JSONObject toJson() {
        try {
        	JSONObject object = new JSONObject();
        	
            object.accumulate("timestamp", timestamp);
            object.accumulate("averageValue", averageValue);
            object.accumulate("totalValue", totalValue);
            object.accumulate("serverId", serverId);
            object.accumulate("localId", localId);
            object.accumulate("updatedAt", updatedAt);
            
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            
            return null;
        }
    }
}
