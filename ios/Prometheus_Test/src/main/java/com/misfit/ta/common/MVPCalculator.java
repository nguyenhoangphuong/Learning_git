package com.misfit.ta.common;

import java.util.HashMap;
import java.util.Map;

public class MVPCalculator {

	private static double ALPHA = 0.15d;
	private static double BETA = 1.3d;
	private static double DELTA = 0.925d;
	private static double GAMMA = 0.5d;

	private static double PPS_CYCLING = 0.375d;
	private static double PPS_SWIMMING = 1d;
	private static double PPS_TENNIS = 1.25d;
	private static double PPS_BASKETBALL = 1d;
	private static double PPS_SOCCER = 0.875d;
	private static int MIN_POINT_PER_MINUTE = 162;
	private static double DIVISOR_WALKING = 10;
	private static double DIVISOR_SWIMMING = 22;
	private static double DIVISOR_RUNNING = 30;
	private static final Map<Integer, Double> factorCalculationMap  = new HashMap<Integer, Double>();
    
	static
    {
    	factorCalculationMap.put(MVPEnums.ACTIVITY_TENNIS, PPS_TENNIS);
    	factorCalculationMap.put(MVPEnums.ACTIVITY_SWIMMING, PPS_SWIMMING);
    	factorCalculationMap.put(MVPEnums.ACTIVITY_CYCLING, PPS_CYCLING);
    	factorCalculationMap.put(MVPEnums.ACTIVITY_BASKETBALL, PPS_BASKETBALL);
    	factorCalculationMap.put(MVPEnums.ACTIVITY_SOCCER, PPS_SOCCER);
    }
    
	public static double calculateMiles(int steps, int mins, double heightInInches) {

		double SR = steps * 1d / mins;
		double RSL = (SR < 80 ? 0.33d : (SR <= 140 ? 0.002 * (SR - 80) + 0.33d : (SR <= 186 ? 0.0085 * (SR - 140) + 0.45 : 0.001 * (SR - 186) + 0.841)));
		double DPM = SR * RSL * heightInInches / 12;
		return DPM * mins * 0.000189394;
	}

	public static double calculatePoint(int steps, int minutes, int activityType) {

		// Manual input: real activity points should be floor down before
		// deviding by 2.5
		boolean isWalking = false;
		float stepsPerMin = (float) Math.floor(steps * 1f / minutes);
		double result = 0d;
		if (factorCalculationMap.containsKey(activityType)) {
			result = Math.floor(stepsPerMin * factorCalculationMap.get(activityType)) * minutes;
		} else {
			float realPointsPerMin = (stepsPerMin * (0.25f + 0.01f * (Math.max(115f, stepsPerMin) - 115)) + 0.0001f);
			result = (Math.floor(realPointsPerMin) * minutes) / 2.5f;
			isWalking = true;
		}
		return isWalking ? result : Math.min(result, MIN_POINT_PER_MINUTE * minutes) / 2.5f;
	}

	public static double calculateFullBMR(float weightInLbs, float heightInInches, int age, boolean isMale) {

		double weightInKg = weightInLbs * 0.453592d;
		double heightInCm = heightInInches * 2.54d;
		double genderFactor = isMale ? 5d : -161d;
		double fullBMR = (10d * weightInKg) + (6.25d * heightInCm) - (5d * age) + genderFactor;

		return fullBMR;
	}

	public static double calculateCalories(float points, float weightInLbs, float fullBMR, int currentMinute) {

		double weightInKg = weightInLbs * 0.45359237f;
		double E = ALPHA * points * 2.5f * (weightInKg / 60f) + BETA * (fullBMR * (currentMinute / 1440f));

		return Math.min(E, GAMMA * E + DELTA * fullBMR);
	}

	public static double calculatePointForNewTag(int steps, int mins, int newActivityType) {
		
		int rawPoint = (int) Math.floor(calculatePoint(steps, mins, MVPEnums.ACTIVITY_WALKING));
		
		if(newActivityType == MVPEnums.ACTIVITY_RUNNING || newActivityType == MVPEnums.ACTIVITY_WALKING)
			return rawPoint;
		
		int newPoints = (int) Math.floor(Math.min(steps * factorCalculationMap.get(newActivityType), mins * 162));
		return newPoints / 2.5;
	}
	
	public static int calculateNearestTimeRemainInMinute(int points, int activityType) {
		
		int[] scales = new int[] {0, 5, 10, 15, 20, 30, 45, 60};
		int mins = (int) (points /  (activityType == MVPEnums.ACTIVITY_WALKING ? DIVISOR_WALKING : 
						   		 (activityType == MVPEnums.ACTIVITY_RUNNING ? DIVISOR_RUNNING :
								 (activityType == MVPEnums.ACTIVITY_SWIMMING ? DIVISOR_SWIMMING :
										DIVISOR_WALKING))));
				
		// mins >= 60
		if(mins >= 60) {
			
			if(mins % 60d == 0)
				return (int)mins;
			
			int hours = (int) Math.floor(mins / 60f);
			int remainMins = mins % 60;
			
			if(remainMins < 15)
				return hours * 60;
			else if(remainMins < 45)
				return hours * 60 + 30;
			
			return (hours + 1) * 60;
		}
		
		// mins < 60
		for(int i = 1; i < scales.length; i++)
		{
			if(mins <= scales[i])
			{
				int distanceToLowerScale = Math.abs(scales[i - 1] - mins);
				int distanceToHigherScale = Math.abs(scales[i] - mins);
				
				return distanceToLowerScale < distanceToHigherScale ? scales[i - 1] : scales[i];
			}
		}
		
		return -1;
	}

	public static String convertNearestTimeInMinuteToString(int minutes) {

		if (minutes < 60)
			return minutes + " mins";
		else if (minutes == 60)
			return "1 hour";

		float hours = minutes / 60f;
		if(minutes % 60 == 0)
			return String.format("%.0f hours", hours);
		
		return String.format("%.1f hours", hours);
	}

}
