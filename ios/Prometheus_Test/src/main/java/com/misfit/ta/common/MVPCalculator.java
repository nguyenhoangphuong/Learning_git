package com.misfit.ta.common;

public class MVPCalculator {
	
	private static double ALPHA = 0.15d;
	private static double BETA = 1.3d;
	private static double DELTA = 0.925d;
	private static double GAMMA = 0.5d;
	
	private static double PPS_NORMAL = 0.25f;
	private static double PPS_CYCLING = 0.375f;
	private static double PPS_SWIMMING = 1f;
	
	private static double DIVISOR_WALKING = 10;
	private static double DIVISOR_SWIMMING = 22;
	private static double DIVISOR_RUNNING = 30;
		
	public static double calculateMiles(int steps, int mins, float heightInInches) {
		
		double SR = steps * 1d / mins;
		double RSL = (SR < 80 ? 0.33d : 
					 (SR <= 140 ? 0.002 * (SR - 80) + 0.33d :
					 (SR <= 186 ? 0.0085 * (SR - 140) + 0.45 :
					  0.001 * (SR - 186) + 0.841
					 )));
		double DPM = SR * RSL * heightInInches / 12;
		return DPM * mins;
	}
	
	public static double calculatePoint(int steps, int minutes, int activityType) {
		
		// Manual input: real activity points should be floor down before
		// deviding by 2.5
		System.out.println("DEBUG ENUM");
		float stepsPerMin = (float) Math.floor(steps * 1f / minutes);
		if (activityType == MVPEnums.ACTIVITY_CYCLING) {
			return Math.floor(stepsPerMin * PPS_CYCLING * minutes) / 2.5f;
		} else if (activityType == MVPEnums.ACTIVITY_SWIMMING) {
			return Math.floor(stepsPerMin * PPS_SWIMMING * minutes) / 2.5f;
		}
		float realPointsPerMin = (stepsPerMin * (0.25f + 0.01f * (Math.max(115f, stepsPerMin) - 115)) + 0.0001f);
		return (Math.floor(realPointsPerMin) * minutes) / 2.5f;
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
		double E = ALPHA * points * 2.5f * (weightInKg / 60f) 
				+ BETA * (fullBMR * (currentMinute / 1440f));
		
		return Math.min(E, GAMMA * E + DELTA * fullBMR);
	}
	
	public static int calculateNearestTimeRemainInMinute(int points, int activityType) {
		
		int[] scales = new int[] {5, 10, 15, 20, 30, 45, 60};
		int mins = (int) (points /  (activityType == MVPEnums.ACTIVITY_WALKING ? DIVISOR_WALKING : 
									(activityType == MVPEnums.ACTIVITY_RUNNING ? DIVISOR_RUNNING :
									(activityType == MVPEnums.ACTIVITY_SWIMMING ? DIVISOR_SWIMMING :
										DIVISOR_WALKING))));
		
		if(mins >= 60) {
			int hours = mins / 60;
			int remainMins = mins % 60;
			
			return hours * 60 + (remainMins >= 30 ? 30 : 0);
		}
		
		for(int i = 0; i < scales.length; i++)
			if(mins <= scales[i])
				return scales[i];
		
		return -1;
	}
	
	public static String convertNearestTimeInMinuteToString(int minutes) {
		
		if(minutes < 60)
			return minutes + " mins";
		else if(minutes == 60)
			return "1 hour";
		
		float hours = minutes / 60;
		return hours + " hours";
	}
	
}
