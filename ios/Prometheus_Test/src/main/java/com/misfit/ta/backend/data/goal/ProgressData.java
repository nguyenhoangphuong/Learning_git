package com.misfit.ta.backend.data.goal;

import java.util.StringTokenizer;
import java.util.Vector;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ProgressData {

    private Vector<Integer> points = new Vector<Integer>();
    private int steps;
    private int seconds;
    
    public ProgressData() {
        
    }
    
    public ProgressData(Vector<Integer> points, int steps, int seconds) {
        super();
        this.points = points;
        this.steps = steps;
        this.seconds = seconds;
    }
    
    public Vector<Integer> getPoints() {
        return points;
    }
    public void setPoints(Vector<Integer> points) {
        this.points = points;
    }
    public int getSteps() {
        return steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }
    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    public JSONObject toJson() {
        try {
            JSONObject object = new JSONObject();
            
            object.accumulate("points", points);
            object.accumulate("steps", steps);
            object.accumulate("seconds", seconds);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            
            return null;
        }
    }
    
    public ProgressData getProgressData(JSONObject obj) {
        ProgressData data = new ProgressData();
        Vector<Integer> points = new Vector<Integer>();
        try {
            String p = obj.getString("points");
            
            p = p.substring(p.indexOf("[") + 1, p.indexOf("]"));
            StringTokenizer token = new StringTokenizer(p, ",");
            while (token.hasMoreTokens()) {
                String next = token.nextToken().trim();
                points.add(Integer.parseInt(next));
            }

            data.setPoints(points);
            data.setSeconds(obj.getInt("seconds"));
            data.setSteps(obj.getInt("steps"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}
