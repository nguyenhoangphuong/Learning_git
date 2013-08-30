package com.misfit.ta.backend.api;

public class Calculation {
	
	public static double calculateDistance(int steps, int mins, int heightInInches) {
		
		double SR = steps * 1d / mins;
		double RSL = (SR < 80 ? 0.33d : 
					 (SR <= 140 ? 0.002 * (SR - 80) + 0.33d :
					 (SR <= 186 ? 0.0085 * (SR - 140) + 0.45 :
					  0.001 * (SR - 186) + 0.841
					 )));
		double DPM = SR * RSL * heightInInches / 12;
		return DPM * mins;
	}
	
	public static double calculatePoint(int steps, int mins) {
		
		double SR = steps * 1d / mins;
		double PPS = 0.25 + (SR > 115 ? 0.01 * (SR - 115) : 0);
		
		return PPS * steps;
	}

}
