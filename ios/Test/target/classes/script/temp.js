#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();


//winParrotGame(2);
//target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.67, y:0.67}});
//target.tap({x:206.00, y:402.50});

target.frontMostApp().logElementTree();


UIALogger.logFail("xxxxxxxxx 0" +app.mainWindow().staticTexts()[0].value());
UIALogger.logFail("xxxxxxxxx 1" +app.mainWindow().staticTexts()[1].value());
UIALogger.logFail("xxxxxxxxx 2" +app.mainWindow().staticTexts()[2].value());
//UIALogger.logFail("xxxxxxxxx 3" +app.mainWindow().staticTexts()[3].value());
//UIALogger.logFail("xxxxxxxxx 4" +app.mainWindow().staticTexts()[4].value());

/*target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.clickVolumeDown();
 

//"Pick a card in 10 seconds"
    var strVerify = /^MISFIT .*$/
    var strCount = app.mainWindow().staticTexts()[1].value();
    // Wait for animation complete
    target.pushTimeout(10);
    if (strVerify.test(strCount)) {
		UIALogger.logFail("Ok" + strCount);
	} else {
		UIALogger.logFail(strCount);
	}
	
 */
 //target.tap({x:318.00, y:478.00});

/*
function winParrotGame() {
	
	var count = 0;
	while (count < 1) {
	    target.tap({x:32.50, y:61.50});
    	target.tap({x:110.50, y:67.00});
	    target.tap({x:183.50, y:66.00});
    	target.tap({x:282.50, y:55.00});
	    target.tap({x:33.00, y:179.00});
    	target.tap({x:120.00, y:183.50});
	    target.tap({x:190.50, y:183.00});
    	target.tap({x:263.50, y:178.50});
    	target.tap({x:41.00, y:299.00});
	    target.tap({x:113.00, y:298.00});
    	target.tap({x:188.50, y:297.50});
 	   	target.tap({x:282.50, y:294.00});
    	target.tap({x:36.00, y:417.00});
 	   	target.tap({x:121.00, y:408.50});
    	target.tap({x:190.00, y:410.50});
    	target.tap({x:276.00, y:409.00});
		count = count + 1;
	}
}

function verifyMenu(item,bside) {

    // bconfirm is optional, default = 1
    if (typeof bside == "undefined") {
        bside = -1;
    }

    // verify menu items
    if (item == 0) {
        name = "MISFIT OF THE DAY";
        if ( target.frontMostApp().mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Menu " + name + " is valid");
        } else {
            UIALogger.logFail("Menu " + name + " is not valid");
        }
    } else if (item == 1) {
        name = "DESK";
        if ( target.frontMostApp().mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Menu " + name + " is valid");
        } else {
            UIALogger.logFail("Menu " + name + " is not valid");
        }

    } else if (item == 2) {
        name = "ABOUT US";
        if ( target.frontMostApp().mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Menu " + name + " is valid");
        } else {
            UIALogger.logFail("Menu " + name + " is not valid");
        }
    }

    // verify card in MISFIT menu
    if (bside >= 0) {
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid() == bside) { 
            UIALogger.logPass("Side of Card" + (i + 1).toString() + " is valid");
        }
        else {
            UIALogger.logFail("side of Card" + (i + 1).toString() + " is not valid");
        }

    }

}
*/		
		//verifyMenu(0);


	 //target.delay(1);
	 //winParrotGame();
	//target.delay(100);


/*

target.frontMostApp().mainWindow().buttons()["Open"].tap();
target.tap({x:172.00, y:53.00});
target.tap({x:142.00, y:338.00});
target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.95, y:0.37}});
target.tap({x:90.50, y:39.00});
target.tap({x:69.50, y:49.00});
target.tap({x:308.50, y:113.00});
target.tap({x:167.50, y:209.00});
target.frontMostApp().mainWindow().staticTexts()["Some quote is here\n\n- Anonymous -"].tapWithOptions({tapOffset:{x:0.63, y:0.70}});
target.tap({x:146.00, y:215.50});
target.frontMostApp().mainWindow().staticTexts()["We are developing highly wearable sensor products and services for wellness and medical applications. \n\n- Sonny Vu -"].tapWithOptions({tapOffset:{x:0.47, y:0.94}});
target.tap({x:173.50, y:223.00});
target.frontMostApp().mainWindow().staticTexts()["We are developing highly wearable sensor products and services for wellness and medical applications. \n\n- Sonny Vu -"].tapWithOptions({tapOffset:{x:0.30, y:0.63}});
target.tap({x:189.00, y:282.50});
target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();


target.tap({x:157.00, y:223.00});
target.tap({x:209.00, y:69.00});
target.tap({x:266.50, y:82.00});
target.tap({x:123.00, y:455.50});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].doubleTap();
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tapWithOptions({tapCount:3});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tapWithOptions({tapCount:4});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tapWithOptions({tapCount:14});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tapWithOptions({tapCount:27});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tapWithOptions({tapCount:4});

target.frontMostApp().mainWindow().buttons()["Open"].tap();
target.tap({x:167.50, y:213.50});
target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
target.tap({x:167.00, y:240.50});
target.frontMostApp().mainWindow().buttons()["btn areyousure yes"].tap();
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
target.tap({x:148.00, y:252.00});
target.tap({x:196.50, y:180.50});
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
target.frontMostApp().mainWindow().staticTexts()["DECK"].tapWithOptions({tapOffset:{x:0.94, y:0.75}});
target.frontMostApp().mainWindow().buttons()[0].tap();



target.tap({x:155.50, y:233.00});
target.tap({x:277.50, y:target.frontMostApp().mainWindow().buttons()["Open"].tap();
		   target.tap({x:145.00, y:452.00});
		   target.tap({x:123.50, y:303.00});
		   target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
		   });
target.tap({x:20.00, y:185.50});
target.tap({x:197.00, y:188.00});
target.tap({x:159.00, y:180.50});


target.tap({x:154.00, y:245.00});
target.tap({x:128.50, y:193.50});
target.tap({x:215.00, y:70.50});
target.tap({x:300.50, y:59.00});
target.tap({x:6.00, y:63.50});
target.tap({x:292.50, y:304.00});
target.tap({x:180.50, y:418.50});
target.tap({x:289.00, y:418.00});
target.tap({x:100.00, y:178.00});
target.tap({x:18.50, y:447.50});



target.tap({x:7.50, y:43.00});
target.tap({x:72.50, y:38.00});
target.tap({x:304.00, y:130.00});
target.tap({x:26.50, y:46.50});
target.tap({x:22.50, y:103.50});
target.tap({x:18.00, y:48.00});
target.tap({x:11.00, y:84.00});
target.tap({x:19.00, y:96.00});
target.tap({x:20.00, y:115.00});
target.tap({x:14.00, y:121.00});
target.tap({x:14.50, y:139.50});
target.tap({x:17.50, y:152.00});
target.tap({x:16.50, y:170.50});
target.tap({x:20.00, y:182.50});
target.tap({x:18.50, y:193.00});
target.tap({x:21.00, y:221.50});
target.tap({x:21.50, y:244.00});
target.tap({x:14.50, y:281.00});
target.tap({x:7.50, y:198.50});
target.tap({x:11.00, y:153.00});
target.tap({x:16.50, y:152.00});
target.tap({x:13.00, y:166.50});
target.touchAndHold({x:6.50, y:162.50}, 1.3);




target.tap({x:32.50, y:61.50});
target.tap({x:110.50, y:67.00});
target.tap({x:183.50, y:66.00});
target.tap({x:282.50, y:55.00});
target.tap({x:33.00, y:179.00});
target.tap({x:120.00, y:183.50});
target.tap({x:190.50, y:183.00});
target.tap({x:263.50, y:178.50});
target.tap({x:41.00, y:299.00});
target.tap({x:113.00, y:298.00});
target.tap({x:188.50, y:297.50});
target.tap({x:282.50, y:294.00});
target.tap({x:36.00, y:417.00});
target.tap({x:121.00, y:408.50});
target.tap({x:190.00, y:410.50});
target.tap({x:276.00, y:409.00});

 target.tap({x:28.50, y:464.50});
 target.tap({x:76.50, y:247.50});
 target.tap({x:167.00, y:149.50});
 target.tap({x:247.50, y:237.50});
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.tap({x:270.50, y:241.50});
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.tap({x:266.00, y:228.50});
 target.clickVolumeUp();
 target.clickVolumeUp();
 target.tap({x:283.00, y:223.50});
 target.tap({x:162.50, y:251.00});
 target.tap({x:252.50, y:217.00});
 target.tap({x:11.00, y:456.50});
 target.tap({x:32.50, y:455.00});
 target.tap({x:233.00, y:244.50});
 target.tap({x:156.50, y:249.50});
 target.tap({x:155.00, y:459.00});
 target.tap({x:19.50, y:457.50});
 target.tap({x:97.00, y:236.00});
 target.tap({x:161.00, y:139.00});
 target.tap({x:82.50, y:213.00});
 target.tap({x:168.50, y:151.50});
 target.tap({x:21.00, y:460.50});
 target.tap({x:84.00, y:225.00});
 target.tap({x:258.50, y:154.00});
 
 target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
 target.frontMostApp().mainWindow().staticTexts()["DECK"].tapWithOptions({tapOffset:{x:0.70, y:0.86}});
 target.tap({x:6.00, y:440.50});
 
 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.46, y:0.88}});
 target.frontMostApp().mainWindow().staticTexts()["MENU"].tapWithOptions({tapOffset:{x:0.92, y:0.49}});
 target.tap({x:234.00, y:417.00});
 target.tap({x:263.50, y:457.00});
 target.tap({x:261.00, y:31.00});
 target.tap({x:284.00, y:26.50});
 target.tap({x:263.50, y:25.00});
 target.tap({x:157.50, y:301.00});
 
 
 target.tap({x:140.50, y:257.50});
 target.frontMostApp().mainWindow().staticTexts()["line 1\nline 2\nline 3\n\n- TEst -"].tapWithOptions({tapOffset:{x:0.35, y:0.63}});
 target.tap({x:131.00, y:212.50});
 target.frontMostApp().mainWindow().staticTexts()["DECK"].tapWithOptions({tapOffset:{x:0.11, y:0.67}});
 target.frontMostApp().mainWindow().buttons()[0].tap();
 target.tap({x:133.50, y:198.00});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.tap({x:10.50, y:348.00});
 target.frontMostApp().mainWindow().elements()["IncorrectCard1"].tapWithOptions({tapOffset:{x:0.61, y:0.43}});
 target.frontMostApp().mainWindow().buttons()["btn areyousure yes"].tap();
target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
 
 );
 target.tap({x:8.50, y:124.50});
 
 
 */


/*
target.pushTimeout(10);
    if (target.frontMostApp().mainWindow().elements()["CorrectCard"].isValid()) {
        
		target.frontMostApp().mainWindow().elements()["CorrectCard"].tap();
	}
    target.popTimeout();


target.frontMostApp().mainWindow().buttons()["Kill"].tap();
 target.tap({x:169.00, y:253.00});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.51, y:0.56}});
 target.frontMostApp().mainWindow().buttons()["btn areyousure yes"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.tap({x:149.00, y:317.50});
 target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
 target.tap({x:125.50, y:214.00});
 target.tap({x:129.50, y:35.50});
 target.frontMostApp().mainWindow().buttons()["btn shareonfb"].tap();
 target.tap({x:109.50, y:45.50});
 target.tap({x:104.00, y:26.50});
 target.tap({x:129.00, y:130.50});
 target.tap({x:113.50, y:37.00});
 target.frontMostApp().mainWindow().buttons()["btn menu motd"].tap();
 target.tap({x:120.50, y:105.00});
 target.tap({x:139.00, y:193.50});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.47, y:0.14}});
 
 target.frontMostApp().mainWindow().staticTexts()["We are busy making the world\n1st mobile healthcare sensor"].tapWithOptions({tapOffset:{x:0.50, y:0.82}});
 target.frontMostApp().mainWindow().staticTexts()["For now, enjoy!"].tapWithOptions({tapOffset:{x:0.52, y:0.64}, tapCount:2});
 target.tap({x:132.00, y:189.00});
 target.frontMostApp().mainWindow().staticTexts()["29/05/2012"].tapWithOptions({tapOffset:{x:0.51, y:0.75}});
 target.frontMostApp().mainWindow().buttons()["Open"].tap();
 target.tap({x:154.50, y:215.50});
 target.tap({x:127.00, y:205.00});
 target.tap({x:168.50, y:222.00});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.tap({x:169.00, y:231.50});
 target.tap({x:172.50, y:399.50});
 target.tap({x:145.00, y:262.00});
 target.tap({x:163.00, y:400.50});
 target.tap({x:171.00, y:232.50});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_LANDSCAPELEFT);
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.31, y:0.61}});
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.48, y:0.67}});
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.55, y:0.32}});
 target.tap({x:310.00, y:468.00});
 target.tap({x:310.00, y:469.00});
 target.tap({x:312.50, y:467.00});
 target.tap({x:308.50, y:461.00});
 target.tap({x:305.00, y:467.50});
 target.tap({x:304.50, y:468.00});
 target.tap({x:312.00, y:466.50});
 target.tap({x:305.50, y:466.00});
 target.tap({x:306.00, y:466.00});
 target.tap({x:308.00, y:463.50});
 target.tap({x:305.50, y:463.50});
 target.tap({x:304.00, y:469.50});
 target.tap({x:308.50, y:458.50});
 target.tap({x:294.00, y:464.50});
 target.tap({x:314.00, y:464.50});
 target.tap({x:312.50, y:465.50});
 target.tap({x:313.00, y:456.00});
 target.tap({x:308.50, y:464.50});
 target.tap({x:308.50, y:468.50});
 target.tap({x:306.00, y:469.00});
 target.tap({x:308.50, y:468.00});
 target.tap({x:306.50, y:468.50});
 target.tap({x:307.50, y:464.00});
 target.tap({x:311.50, y:465.00});
 target.tap({x:310.50, y:466.00});
 target.tap({x:311.50, y:469.50});
 target.tap({x:310.00, y:470.00});
 target.tap({x:307.50, y:467.00});
 target.tap({x:306.50, y:466.00});
 target.tap({x:308.50, y:463.50});
 target.tap({x:310.00, y:465.00});
 target.tap({x:306.50, y:466.50});
 target.tap({x:306.50, y:468.00});
 
target.tap({x:158.50, y:219.50});
 target.dragFromToForDuration({x:164.50, y:34.00}, {x:181.00, y:120.50}, 0.3);
 target.tap({x:105.00, y:306.50});
 target.frontMostApp().mainWindow().staticTexts()["There is no card in the deck yet.\nCollect them from the Misfit Collection."].tapWithOptions({tapOffset:{x:0.31, y:0.98}});
 target.tap({x:164.50, y:293.50});
 target.tap({x:157.00, y:330.50});
 target.tap({x:151.00, y:350.50});
 .tapWithOptions({tapOffset:{x:0.52, y:0.11}});
 target.frontMostApp().mainWindow().staticTexts()["DECK"].tapWithOptions({tapOffset:{x:0.54, y:0.37}});
 target.frontMostApp().mainWindow().buttons()[0].tap();
 target.tap({x:140.00, y:58.00});
 target.frontMostApp().mainWindow().buttons()[1].tap();
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.tap({x:49.50, y:233.00});
 target.tap({x:31.50, y:231.50});
 target.tap({x:27.00, y:230.50});
 target.tap({x:27.50, y:230.00});
 target.tap({x:25.00, y:228.00});
 target.tap({x:22.50, y:228.00});
 target.tap({x:25.50, y:224.00});
 target.tap({x:17.00, y:219.50});
 target.tap({x:26.00, y:225.00});
 target.tap({x:21.50, y:225.00});
 target.tap({x:18.50, y:222.00});
 
 target.frontMostApp().mainWindow().buttons()["btn resume"].tap();
 
 
 target.tap({x:26.50, y:236.50});
 target.tap({x:35.50, y:208.50});
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.41, y:0.12}});
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.36, y:0.13}});
 target.tap({x:152.50, y:229.00});
 target.tap({x:158.50, y:228.00});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.tap({x:160.00, y:257.50});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.frontMostApp().mainWindow().elements()["IncorrectCard1"].tapWithOptions({tapOffset:{x:0.67, y:0.51}});
 
 target.tap({x:156.00, y:208.50});
 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
 target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
 target.frontMostApp().mainWindow().elements()["CorrectCard"].tapWithOptions({tapOffset:{x:0.45, y:0.44}});
 target.frontMostApp().mainWindow().buttons()["btn areyousure yes"].tap();
 target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
 
 
 
*/

