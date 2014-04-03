package com.misfit.ta.backend.data.graph;

import java.util.List;
import java.util.Vector;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GraphItem {

	// fields
	private Long timestamp;
	private Double averageValue;
	private Double totalValue;
	private String serverId;
	private String localId;
	private Long updatedAt;

	// getters setters
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(Double averageValue) {
		this.averageValue = averageValue;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
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

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	// constructor
	public GraphItem() {

	}

	public GraphItem(long timestamp, double averageValue, long updatedAt) {
		this.timestamp = timestamp;
		this.averageValue = averageValue;
		this.updatedAt = updatedAt;
	}

	// methods
	public static GraphItem getGraphItem(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONObject jsonItem = jsonResponse.getJSONObject("graph_item");

			return GraphItem.fromJson(jsonItem);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static List<GraphItem> getGraphItems(ServiceResponse response) {

		try {
			List<GraphItem> graphItems = new Vector<GraphItem>();
			JSONObject jsonResponse;
			jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonItems = jsonResponse.getJSONArray("graph_items");

			for (int i = 0; i < jsonItems.length(); i++) {
				JSONObject jsonItem = jsonItems.getJSONObject(i);
				GraphItem graphItem = GraphItem.fromJson(jsonItem);
				graphItems.add(graphItem);
			}

			return graphItems;
		} catch (JSONException e) {
			return null;
		}
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

	public static GraphItem fromJson(JSONObject jsonItem) {

		GraphItem graphItem = new GraphItem();
		try {
			if (!jsonItem.isNull("serverId"))
				graphItem.setServerId(jsonItem.getString("serverId"));

			if (!jsonItem.isNull("localId"))
				graphItem.setLocalId(jsonItem.getString("localId"));

			if (!jsonItem.isNull("timestamp"))
				graphItem.setTimestamp(jsonItem.getLong("timestamp"));

			if (!jsonItem.isNull("updatedAt"))
				graphItem.setUpdatedAt(jsonItem.getLong("updatedAt"));

			if (!jsonItem.isNull("averageValue"))
				graphItem.setAverageValue(jsonItem.getDouble("averageValue"));

			if (!jsonItem.isNull("totalValue"))
				graphItem.setTotalValue(jsonItem.getDouble("totalValue"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return graphItem;
	}

}
