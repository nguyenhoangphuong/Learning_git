package com.misfit.ta.backend.data.servercalculation;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ServerCalculationCursor {

	// fields
	protected Long activitySessionCursor;
	protected Long sleepSessionCursor;
	protected Long notableEventCursor;
	protected Long milestoneCursor;
	protected Long progressCursor;
	protected Long statisticCursor;
	protected Long graphItemCursor;
	protected Long updatedAt;

	// methods
	public ServerCalculationCursor fromJson(JSONObject json) {

		try {
			if (!json.isNull("activitySessionCursor")) {
				activitySessionCursor = json.getLong("activitySessionCursor");
			}

			if (!json.isNull("sleepSessionCursor")) {
				sleepSessionCursor = json.getLong("sleepSessionCursor");
			}

			if (!json.isNull("notableEventCursor")) {
				notableEventCursor = json.getLong("notableEventCursor");
			}

			if (!json.isNull("milestoneCursor")) {
				milestoneCursor = json.getLong("milestoneCursor");
			}

			if (!json.isNull("progressCursor")) {
				progressCursor = json.getLong("progressCursor");
			}

			if (!json.isNull("statisticCursor")) {
				statisticCursor = json.getLong("statisticCursor");
			}

			if (!json.isNull("graphItemCursor")) {
				graphItemCursor = json.getLong("graphItemCursor");
			}

			if (!json.isNull("updatedAt")) {
				setUpdatedAt(json.getLong("updatedAt"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static ServerCalculationCursor fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(
					response.getResponseString());
			ServerCalculationCursor profile = new ServerCalculationCursor();

			return profile.fromJson(jsonResponse);
		} catch (JSONException e) {
			return null;
		}
	}

	// getters setters
	public Long getActivitySessionCursor() {
		return activitySessionCursor;
	}

	public void setActivitySessionCursor(Long activitySessionCursor) {
		this.activitySessionCursor = activitySessionCursor;
	}

	public Long getSleepSessionCursor() {
		return sleepSessionCursor;
	}

	public void setSleepSessionCursor(Long sleepSessionCursor) {
		this.sleepSessionCursor = sleepSessionCursor;
	}

	public Long getNotableEventCursor() {
		return notableEventCursor;
	}

	public void setNotableEventCursor(Long notableEventCursor) {
		this.notableEventCursor = notableEventCursor;
	}

	public Long getMilestoneCursor() {
		return milestoneCursor;
	}

	public void setMilestoneCursor(Long milestoneCursor) {
		this.milestoneCursor = milestoneCursor;
	}

	public Long getProgressCursor() {
		return progressCursor;
	}

	public void setProgressCursor(Long progressCursor) {
		this.progressCursor = progressCursor;
	}

	public Long getStatisticCursor() {
		return statisticCursor;
	}

	public void setStatisticCursor(Long statisticCursor) {
		this.statisticCursor = statisticCursor;
	}

	public Long getGraphItemCursor() {
		return graphItemCursor;
	}

	public void setGraphItemCursor(Long graphItemCursor) {
		this.graphItemCursor = graphItemCursor;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

}
