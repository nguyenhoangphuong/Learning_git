package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.goal.Goal;

public class GoalsResult extends BaseResult {
    // fields
    public Goal[] goals;

    // constructor
    public GoalsResult(ServiceResponse response) {
        super(response);
        // invalid token || not found
        if (this.statusCode == 401 || this.statusCode == 404) {
            goals = null;
            this.pairResult.put("goals", "null");
            return;
        }

        // request ok
        formatOK();
    }

    private void formatOK() {
        // result from search (list result)
        if (!json.isNull("goals")) {
            JSONArray arrJson;
            try {
                arrJson = json.getJSONArray("goals");
                goals = new Goal[arrJson.length()];

                for (int i = 0; i < goals.length; i++) {
                    JSONObject objJson;
                    objJson = arrJson.getJSONObject(i);
                    goals[i] = formatResult(objJson);

                    this.pairResult.put("goals[" + i + "]", goals[i]);
                }

                this.pairResult.put("goals", goals);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } else {
            JSONObject objJson;
            try {
                if (!json.isNull("goals")) {
                    objJson = json.getJSONObject("goals"); 
                } else {
                    objJson = json.getJSONObject("goal");
                }
                goals = new Goal[1];
                goals[0] = formatResult(objJson);

                this.pairResult.put("goals", goals);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          
        }
    }

    private Goal formatResult(JSONObject objJson) {
        Goal goal = new Goal();
        return goal.getGoal(objJson);

    }

}
