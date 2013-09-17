package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class Timeline {
	
	static public String LabelMinutes = "MINUTES";
	static public String LabelPoints = "POINTS";
	static public String LabelMilestone = "MILESTONE";
	static public String LabelPersonalBest = "PERSONAL BEST";
	
	static public String PersonalBestOutdidMessage = "You outdid your previous record by %d points!";
	
	static public String[] DailyGoalMessagesFor100Percent = new String[] {
				"Gooooooooal!",
				"Sweetness!",
				"You hit your goal. Sweetness!",
				"Nailed it. Like a boss.",
				"Goal hit. Mission accomplished.",
				"Goal hit. Check that off your list.",
				"We love seeing that circle light up, too.",
				"Solid.",
				"Take a victory lap. Or a nap, if you'd like.",
				"Makin' it look easy.",
				"That's what I'm talking about!",
				"He shoots, he scores!",
				"She shoots, she scores!",
				"Goooooooal! Now you're moving.",
				"Touchdown!",
				"Ace.",
				"#BringIt",
				"Hammer time. #NailedIt",
				
	};
	static public String[] DailyGoalMessagesFor150Percent = new String[] {
				"Solid.",
				"Killing it!",
				"150% of goal. Crushed it.",
				"150% of goal. Steppin' it up!",
				"150% of goal. Gettin' it done.",
				"150% of goal, and the crowd goes wild!",
				"150% of goal. Pickin' up the pace.",
				"150% of goal. Light 'em up!",
				"150% of goal. Pretty impressive.",
	};
	static public String[] DailyGoalMessagesFor200Percent = new String[] {
				"Crushed it!",
				"You doubled your goal, nice work!",
				"Doubled your goal. Cardio-rifffic!",
				"Doubled your goal. #Winning",
				"Doubled your goal. Hyperactive!",
				"200% of goal. You're golden.",
				"200% of goal. How cool is that?!?",
				"That goal never stood a chance. #NailedIt",
				"Welcome to the Shine All Star Team.",
	};
	static public String[] PersonalBestMessages = new String[] {
		"Cue the Rocky music.",
		"You probably can't hear it, but the Misfit team just cheered.",
		"Go grab a yogurt smoothie. You earned it.",
		"Keep it up and you'll need your own theme song.",
		"That's no small feet! (Ewww, bad pun. Ouch.)",
		"That burn you feel, in your lungs? That's pride.",
		"You're one of those \"over-achiever\" types, aren't you?",
		"And the Gold Medal for Awesome goes to...",
		"We're not worthy! We're not worthy!",
	};
	static public String[] Streak3DaysMessages = new String[] {
		"We can see you're goal oriented.",
		"Hat trick!",
		"You're warming up!",
		"Heating up.",
		"You're streaking!",
	};
	static public String[] Streak4DaysMessages = new String[] {
		"You're on a roll.",
		"Solid.",
		"Hitting your groove.",
		"That's some solid performance right there.",
		"Whoa, déjà vu.",
	};
	static public String[] Streak5to6DaysMessages = new String[] {
		"You're on fire! Can you make it to a week?",
		"You're on fire! Keep it up.",
		"Hitting your rhythm. Think you can make it to a week?",
		"Like clockwork.",
	};
	static public String[] Streak7DaysMessages = new String[] {
		"Nice, an entire week!",
		"A week streak! You're a streak freak!",
		"Security, we have a streaker.",
		"Now that's a perfect week.",
	};
	static public String[] Streak8to12DaysMessages = new String[] {
		"Everyone loves a streaker. Now someone call security.",
		"I'm sensing a pattern here...",
		"You're soooooo predictable.",
		"Wow. You're taking streak to a whole new level.",
		"Take a screenshot and show your friends.",
		"Utter and complete domination.",
		"How long is this going to go on for?",
	};

	static public void dragUpTimeline() {
		
		Gui.dragUpTimeline();
	}
	
	static public void dragDownTimeline() {
		
		Gui.dragDownTimeline();
	}
	
	
	static public boolean isTileExisted(String title) {
	
		return ViewUtils.isExistedView("PTTimelineItemSessionView", title);
	}
	
	static public void openTile(String title) {
		
		Gui.touchAVIew("PTTimelineItemSessionView", title);
	}
	
	static public void closeCurrentTile() {
		
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() - 10);
	}
	
	
	static public boolean isActivityTileCorrect(String startTime, String endTime, 
			int duration, int points, String bottomTitle) {
		
		return  ViewUtils.isExistedView("UILabel", startTime + " - " + endTime) &&
				ViewUtils.isExistedView("UILabel", String.valueOf(duration)) &&
				ViewUtils.isExistedView("UILabel", LabelMinutes) &&
				ViewUtils.isExistedView("UILabel", String.valueOf(points)) &&
				ViewUtils.isExistedView("UILabel", LabelPoints) &&
				ViewUtils.isExistedView("UILabel", bottomTitle);
	}
	
	static public boolean isDailyGoalMilestoneTileCorrect(String time, int points, String[] goalMessages) {
		
		return  ViewUtils.isExistedView("UILabel", time) &&
				ViewUtils.isExistedView("UILabel", String.valueOf(points)) &&
				ViewUtils.isExistedView("UILabel", LabelPoints) &&
				ViewUtils.isExistedView("UILabel", LabelMilestone) &&
				isDynamicMessageDisplayed(goalMessages);
	}
	
	static public boolean isPersonalBestTileCorrect(String time, int points, int lastPoint, String[] messages) {
		
		String outdidMessage = String.format(PersonalBestOutdidMessage, points - lastPoint);
		return  ViewUtils.isExistedView("UILabel", time) &&
				ViewUtils.isExistedView("UILabel", String.valueOf(points)) &&
				ViewUtils.isExistedView("UILabel", LabelPoints.toLowerCase()) &&
				ViewUtils.isExistedView("UILabel", LabelPersonalBest) &&
				(isDynamicMessageDisplayed(messages) || 
				 ViewUtils.isExistedView("UILabel", outdidMessage));
	}
	
	static public boolean isDynamicMessageDisplayed(String[] availableMessages) {
		
		for(String message : availableMessages)
			if(ViewUtils.isExistedView("UILabel", message))
				return true;
		
		return false;
	}
	
}
