function test_localize_startup_part() {
	
	// ------------------ launch view
	target.dragFromToForDuration({x: 10, y: 200}, {x: 310, y: 200}, 0.5);
	wait();
	target.dragFromToForDuration({x: 10, y: 200}, {x: 310, y: 200}, 0.5);
	wait();
	target.dragFromToForDuration({x: 10, y: 200}, {x: 310, y: 200}, 0.5);
	wait();
	
	capture("launch_view_slide1");
	target.dragFromToForDuration({x: 310, y: 200}, {x: 10, y: 200}, 0.5);
	wait(2);
	capture("launch_view_slide2");
	target.dragFromToForDuration({x: 310, y: 200}, {x: 10, y: 200}, 0.5);
	wait(2);
	capture("launch_view_slide3");
	target.dragFromToForDuration({x: 310, y: 200}, {x: 10, y: 200}, 0.5);
	wait(2);
	capture("launch_view_slide4");
	
	// ------------------ initial view
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(2);
	capture("initial_view");
	
	// ------------------ sign in view
	target.frontMostApp().mainWindow().buttons()[4].tap();
	wait(2);
	capture("signin_view");
	
	// invalid email alert
	alert.name = "signin_invalid_email_alert";
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	
	// invalid password alert
	alert.name = "signin_invalid_password_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("qa001@a.a");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	
	// invalid credential alert
	alert.name = "signin_invalid_credential_alert";
	alert.choice = -1;
	target.frontMostApp().mainWindow().secureTextFields()[0].setValue("asdfas");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	
	// ------------------ forgot password view
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(2);
	capture("forgot_password");
	
	// invalid email alert
	alert.name = "forgot_password_invalid_email_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	
	// non-existed email
	alert.name = "forgot_password_nonexisted_email_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("valid1325@a.a");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	
	// reset email sent
	alert.name = "forgot_password_email_sent_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("qa001@a.a");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	
	// ------------------ sign up account view
	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait(2);
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(2);
	capture("signup_account_view");
	
	// invalid email
	alert.name = "signup_invalid_email_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("invalid");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	
	// invalid password
	alert.name = "signup_invalid_password_alert";
	target.frontMostApp().mainWindow().textFields()[0].setValue("valid@email.com");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	
	// duplicated email
	alert.name = "signup_duplicated_email_alert";
	target.frontMostApp().mainWindow().secureTextFields()[0].setValue("33wfdas");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	
	// ------------------ sign up profile view
	target.frontMostApp().mainWindow().textFields()[0].setValue(generateSignupAccount());
	target.frontMostApp().mainWindow().secureTextFields()[0].setValue("qqqqqq");
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	capture("signup_profile_view");
	 
	// sign out so soon alert
	alert.name = "signup_profile_signout_alert";
	alert.choice = -1;
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(2);
	
	// forget something to tell alert
	alert.name = "signup_profile_forgot_something_alert";
	target.frontMostApp().mainWindow().buttons()[1].tap();
	
	// ------------------ sign up goal view
	target.frontMostApp().mainWindow().buttons()[3].tap();
	
	target.frontMostApp().mainWindow().staticTexts()[1].tap();
	wait(1);
	target.frontMostApp().windows()[2].toolbar().buttons()[0].tap();
	capture("signup_profile_birthday_picker");
	
	target.frontMostApp().mainWindow().staticTexts()[3].tap();
	wait(0.5);
	target.frontMostApp().windows()[2].toolbar().buttons()[0].tap();
	capture("signup_profile_height_picker");
	
	target.frontMostApp().mainWindow().staticTexts()[5].tap();
	wait(0.5);
	target.frontMostApp().windows()[2].toolbar().buttons()[0].tap();
	capture("signup_profile_weight_picker");
	
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(2);
	capture("signup_goal_view");
	
	// points tutorial
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(2);
	capture("signup_goal_tutorial");
	target.frontMostApp().mainWindow().buttons()[3].tap();
	wait(2);
	
	// activity levels
	target.frontMostApp().mainWindow().images()["active.png"].tap();
	wait();
	capture("signup_goal_active");
	
	target.frontMostApp().mainWindow().images()["highlyactive.png"].tap();
	wait();
	capture("signup_goal_highlyactive");
	
	// ------------------ sign up link shine view
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);
	capture("signup_linking_view");
	
	target.dragFromToForDuration({x: 30, y: 400}, {x: 260, y: 400}, 0.5);
	target.dragFromToForDuration({x: 30, y: 300}, {x: 260, y: 300}, 0.5);
	wait(5);
	
	// ------------------ app tutorial
	capture("app_tutorial_page1");
	swipeHorizontally(300, 20, 350, 0.5);
	wait(2);
	
	capture("app_tutorial_page2");
	swipeHorizontally(300, 20, 350, 0.5);
	wait(2);
	
	capture("app_tutorial_page3");
	swipeHorizontally(300, 20, 350, 0.5);
	wait(2);
	
	capture("app_tutorial_page4");
	target.frontMostApp().mainWindow().scrollViews()[0].buttons()[0].tap();
	wait(2);
	
	// ------------------ homescreen default
	capture("homescreen_default");

}

function test_localize_settings_part() {
	
	// ------------------ turn off oad
	target.frontMostApp().mainWindow().buttons()[5].tap();
	target.frontMostApp().mainWindow().staticTexts()[4].tap();
	target.dragFromToForDuration({x: 160, y: 400}, {x: 160, y: 50}, 0.5);
	target.frontMostApp().mainWindow().tableViews()[0].cells()[8].tap();
	wait();
	target.frontMostApp().mainWindow().switches()[1].setValue(0);
	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait();
	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait();

	// ------------------ menu
	target.frontMostApp().mainWindow().buttons()[4].tap();
	wait(3);
	capture("main_menu");

	// ------------------ my goal settings view
	target.frontMostApp().mainWindow().staticTexts()[2].tap();
	wait(2);
	capture("my_goal_view_1");
	target.frontMostApp().mainWindow().buttons()[3].tap();
	wait();
	capture("my_goal_view_2");

	// don't forget alert
	alert.name = "my_goal_new_goal_alert";
	target.frontMostApp().mainWindow().buttons()[1].tap();
	wait(5);

	// ------------------ misfit labs view
	target.frontMostApp().mainWindow().buttons()[4].tap();
	wait(1);
	target.frontMostApp().mainWindow().staticTexts()[3].tap();
	wait(2);
	target.frontMostApp().mainWindow().tableViews()[0].cells()[1].tap();
	wait(2);
	capture("misfit_labs_view_1");

	target.frontMostApp().mainWindow().tableViews()[0].cells()[0].switches()[0].setValue(0);
	wait();
	capture("mifit_labs_tagging_off");

	target.frontMostApp().mainWindow().tableViews()[0].cells()[0].switches()[0].setValue(1);
	wait();
	target.dragFromToForDuration({x: 160, y: 400}, {x: 160, y: 50}, 0.5);
	wait(2);
	capture("misfit_labs_view_2");

	// suggest wearing position alerts
	alert.name = "misfit_labs_sleep_position_alert";
	target.frontMostApp().mainWindow().tableViews()[0].cells()[1].tap();
	wait(2);

	alert.name = "misfit_labs_sleep_position_alert";
	target.frontMostApp().mainWindow().tableViews()[0].cells()[2].tap();
	wait(2);

	alert.name = "misfit_labs_sleep_position_alert";
	target.frontMostApp().mainWindow().tableViews()[0].cells()[3].tap();
	wait(2);

	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait(2);

	// ------------------- wearing shine
	target.frontMostApp().mainWindow().tableViews()[0].cells()[2].tap();
	wait(2);
	capture("wearing_shine");

	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait(2);

	// ------------------ my shine view
	capture("my_shine_clock_on");

	target.frontMostApp().mainWindow().tableViews()[0].cells()[0].switches()[0].setValue(0);
	wait();
	capture("my_shine_clock_off");

	// unlink alert
	alert.name = "my_shine_unlink_alert";
	alert.choice = -1;
	target.frontMostApp().mainWindow().tableViews()[0].cells()[4].tap();
	wait(2);

	// sync require alert
	alert.name = "my_shine_sync_require";
	alert.choice = -1;
	target.frontMostApp().mainWindow().buttons()[1].tap();

	// ------------------- settings
	target.frontMostApp().mainWindow().buttons()[4].tap();
	wait(2);
	target.frontMostApp().mainWindow().staticTexts()[4].tap();
	wait(2);
	capture("settings_view_1");

	target.dragFromToForDuration({x: 160, y: 400}, {x: 160, y: 50}, 0.5);
	wait(2);
	capture("settings_view_2");

	// ------------------- my profile
	target.dragFromToForDuration({x: 160, y: 50}, {x: 160, y: 400}, 0.5);
	wait();
	target.frontMostApp().mainWindow().tableViews()[0].cells()[0].tap();
	wait(2);
	capture("settings_profile_view");
	target.frontMostApp().mainWindow().buttons()[0].tap();
	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait();
	
}

function test_localize_homescreen_part() {
	
	
	
}












