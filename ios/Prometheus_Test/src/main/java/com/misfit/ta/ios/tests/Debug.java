package com.misfit.ta.ios.tests;

import org.testng.Assert;

import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeSettings;

public class Debug {
	public static void main(String[] args) {
		Gui.init("192.168.1.247");
//		Gui.printViewWithViewName("UILabel");
		int newGoal = HomeSettings.getSpinnerGoal();
		Assert.assertEquals(newGoal, 1200, "Spinner's value is correct");
//		System.out.println(MVPCalculator.calculateMiles(1000, 5, 68));

	}
}
