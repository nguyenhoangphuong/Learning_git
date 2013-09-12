package com.misfit.ta.common;

public class MVPCalculator {
	
	private static double ALPHA = 0.15d;
	private static double BETA = 1.3d;
	private static double DELTA = 0.925d;
	private static double GAMMA = 0.5d;
	
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
			return Math.floor(stepsPerMin * 0.375f) * minutes / 2.5f;
		} else if (activityType == MVPEnums.ACTIVITY_SWIMMING) {
			return Math.floor(stepsPerMin) * minutes / 2.5f;
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
	
}
