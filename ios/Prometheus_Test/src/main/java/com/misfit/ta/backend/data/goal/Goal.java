package com.misfit.ta.backend.data.goal;

import java.util.Vector;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Goal {

    private double value;
    private long startTime;
    private long endTime;
    private int absoluteLevel;
    private int userRelativeLevel;
    private ProgressData progressData;
    int timeZoneOffsetInSeconds;
    private String localId;
    private long updatedAt;

    public Goal() {

    }

    public Goal(double value, long startTime, long endTime, int absoluteLevel, int userRelativeLevel,
            ProgressData progressData, int timeZoneOffsetInSeconds, String localId, long updatedAt) {
        super();
        this.value = value;
        this.startTime = startTime;
        this.endTime = endTime;
        this.absoluteLevel = absoluteLevel;
        this.userRelativeLevel = userRelativeLevel;
        this.progressData = progressData;
        this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
        this.localId = localId;
        this.updatedAt = updatedAt;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getAbsoluteLevel() {
        return absoluteLevel;
    }

    public void setAbsoluteLevel(int absoluteLevel) {
        this.absoluteLevel = absoluteLevel;
    }

    public int getUserRelativeLevel() {
        return userRelativeLevel;
    }

    public void setUserRelativeLevel(int userRelativeLevel) {
        this.userRelativeLevel = userRelativeLevel;
    }

    public ProgressData getProgressData() {
        return progressData;
    }

    public void setProgressData(ProgressData progressData) {
        this.progressData = progressData;
    }

    public int getTimeZoneOffsetInSeconds() {
        return timeZoneOffsetInSeconds;
    }

    public void setTimeZoneOffsetInSeconds(int timeZoneOffsetInSeconds) {
        this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
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

    public JSONObject toJson() {
        try {

            JSONObject object = new JSONObject();

            object.accumulate("goalValue", value);
            object.accumulate("startTime", startTime);
            object.accumulate("endTime", endTime);
            object.accumulate("absoluteLevel", absoluteLevel);
            object.accumulate("userRelativeLevel", userRelativeLevel);
            object.accumulate("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);
            object.accumulate("progressData", progressData.toJson());
            object.accumulate("localId", localId);
            object.accumulate("updatedAt", updatedAt);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {
        Vector<Integer> points = new Vector<Integer>();
        points.add(new Integer(0));
        points.add(new Integer(1));
        points.add(new Integer(2));
        ProgressData data = new ProgressData(points, 100, 1234);

        Goal goal = new Goal(1, 2, 3, 4, 5, data, 6, "someid", 0);
        System.out.println("LOG [Goal.main]: " + goal.toJson());

        JSONObject json = goal.toJson();

        Goal goal1 = new Goal();
        goal1 = goal1.getGoal(json);
    }

    public Goal getGoal(JSONObject objJson) {
        Goal goal = new Goal();
        try {
            goal.setLocalId(objJson.getString("localId"));
            if (!objJson.isNull("updatedAt")) {
                goal.setUpdatedAt(objJson.getLong("updatedAt"));
            }
            if (!objJson.isNull("goalValue"))
                goal.setValue(objJson.getDouble("goalValue"));
            if (!objJson.isNull("startTime"))
                goal.setStartTime(objJson.getLong("startTime"));
            if (!objJson.isNull("endTime"))
                goal.setEndTime(objJson.getLong("endTime"));
            if (!objJson.isNull("absoluteLevel"))
                goal.setAbsoluteLevel(objJson.getInt("absoluteLevel"));
            if (!objJson.isNull("userRelativeLevel"))
                goal.setUserRelativeLevel(objJson.getInt("userRelativeLevel"));
            if (!objJson.isNull("timeZoneOffsetInSeconds"))
                goal.setTimeZoneOffsetInSeconds(objJson.getInt("timeZoneOffsetInSeconds"));
            ProgressData data = new ProgressData();
            if (!objJson.isNull("progressData"))
                data = data.getProgressData(objJson.getJSONObject("progressData"));
            goal.setProgressData(data);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return goal;
    }
}
