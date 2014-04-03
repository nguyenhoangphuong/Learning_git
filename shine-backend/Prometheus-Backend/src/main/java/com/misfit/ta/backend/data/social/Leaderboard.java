package com.misfit.ta.backend.data.social;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Leaderboard {

    // fields
    protected List<SocialUserInLeaderBoard> today;
    protected List<SocialUserInLeaderBoard> yesterday;

    // getters setters
    public List<SocialUserInLeaderBoard> getToday() {
        return today;
    }

    public void setToday(List<SocialUserInLeaderBoard> today) {
        this.today = today;
    }

    public List<SocialUserInLeaderBoard> getYesterday() {
        return yesterday;
    }

    public void setYesterday(List<SocialUserInLeaderBoard> yesterday) {
        this.yesterday = yesterday;
    }

    // methods
    public JSONObject toJson() {
        try {

            JSONObject obj = new JSONObject();

            if (today != null) {
                for (int i = 0; i < today.size(); i++) {
                    obj.accumulate("today", today.get(i).toJson());
                }
            }

            if (yesterday != null) {
                for (int i = 0; i < yesterday.size(); i++) {
                    obj.accumulate("yesterday", yesterday.get(i).toJson());
                }
            }

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Leaderboard fromJson(JSONObject json) {

        try {

            if (!json.isNull("today")) {

                JSONArray todayJsonArr = json.getJSONArray("today");
                List<SocialUserInLeaderBoard> todayList = new ArrayList<SocialUserInLeaderBoard>();

                for (int i = 0; i < todayJsonArr.length(); i++) {
                    SocialUserInLeaderBoard record = new SocialUserInLeaderBoard();
                    record.fromJson(todayJsonArr.getJSONObject(i));
                    todayList.add(record);
                }

                setToday(todayList);
            }

            if (!json.isNull("yesterday")) {

                JSONArray yesterdayJsonArr = json.getJSONArray("yesterday");
                List<SocialUserInLeaderBoard> yesterdayList = new ArrayList<SocialUserInLeaderBoard>();

                for (int i = 0; i < yesterdayJsonArr.length(); i++) {
                    SocialUserInLeaderBoard record = new SocialUserInLeaderBoard();
                    record.fromJson(yesterdayJsonArr.getJSONObject(i));
                    yesterdayList.add(record);
                }

                setYesterday(yesterdayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }

    public static Leaderboard fromResponse(ServiceResponse response) {
        try {

            JSONObject jsonResponse = new JSONObject(response.getResponseString());
            JSONObject jsonLeaderboard = jsonResponse.getJSONObject("leaderboard");

            Leaderboard leaderboard = new Leaderboard();
            return leaderboard.fromJson(jsonLeaderboard);

        } catch (JSONException e) {
            return null;
        }
    }

    public int getLeaderPointToday() {
        if (today != null && !today.isEmpty()) {
            SocialUserInLeaderBoard first = today.get(0);
            return first.getPoints();
        } else
            return -1;
    }
    
    public String getLeaderNameToday() {
        if (today != null && !today.isEmpty()) {
            SocialUserInLeaderBoard first = today.get(0);
            return first.getName();
        } else
            return "";
    }

}
