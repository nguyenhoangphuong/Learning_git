/*
--------------------------------------------------------------------------------
FUNCTION LIST
This section is the place where you list functions. Please list them as comments
--------------------------------------------------------------------------------

function convert2DateObject(strDate)
function verifyStaticTextExist(id,timeout)
function verifyButtonExist(id,timeout,btab)

function swipeLeft();
function swipeRight();
function isMisfitCollectionScreen();
function isAboutUsScreen();
function isGiftBox();
function winParrotGame();
function killApp();

function logtree();
function wait(s = 1);
function start(text = "Test started");
function log(text = "Debug log");
function pass(text = "Pass");
function fail(text = "Fail");

function wheelPick(wheel, index = 0, value);
*/


 
/*
--------------------------------------------------------------------------------
IMPLEMENTATION
This section is the place where you implement functions declared above
--------------------------------------------------------------------------------
*/

var target = UIATarget.localTarget();
var app = target.frontMostApp();

/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var dateFormat = function () {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
        timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
        timezoneClip = /[^-+\dA-Z]/g,
        pad = function (val, len) {
            val = String(val);
            len = len || 2;
            while (val.length < len) val = "0" + val;
            return val;
        };

    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date;
        if (isNaN(date)) throw SyntaxError("invalid date");

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        var _ = utc ? "getUTC" : "get",
            d = date[_ + "Date"](),
            D = date[_ + "Day"](),
            m = date[_ + "Month"](),
            y = date[_ + "FullYear"](),
            H = date[_ + "Hours"](),
            M = date[_ + "Minutes"](),
            s = date[_ + "Seconds"](),
            L = date[_ + "Milliseconds"](),
            o = utc ? 0 : date.getTimezoneOffset(),
            flags = {
                d:    d,
                dd:   pad(d),
                ddd:  dF.i18n.dayNames[D],
                dddd: dF.i18n.dayNames[D + 7],
                m:    m + 1,
                mm:   pad(m + 1),
                mmm:  dF.i18n.monthNames[m],
                mmmm: dF.i18n.monthNames[m + 12],
                yy:   String(y).slice(2),
                yyyy: y,
                h:    H % 12 || 12,
                hh:   pad(H % 12 || 12),
                H:    H,
                HH:   pad(H),
                M:    M,
                MM:   pad(M),
                s:    s,
                ss:   pad(s),
                l:    pad(L, 3),
                L:    pad(L > 99 ? Math.round(L / 10) : L),
                t:    H < 12 ? "a"  : "p",
                tt:   H < 12 ? "am" : "pm",
                T:    H < 12 ? "A"  : "P",
                TT:   H < 12 ? "AM" : "PM",
                Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
                o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
            };

        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();

// Some common format strings
dateFormat.masks = {
    "default":      "ddd mmm dd yyyy HH:MM:ss",
    shortDate:      "m/d/yy",
    mediumDate:     "mmm d, yyyy",
    longDate:       "mmmm d, yyyy",
    fullDate:       "dddd, mmmm d, yyyy",
    shortTime:      "h:MM TT",
    mediumTime:     "h:MM:ss TT",
    longTime:       "h:MM:ss TT Z",
    isoDate:        "yyyy-mm-dd",
    isoTime:        "HH:MM:ss",
    isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
    dayNames: [
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ],
    monthNames: [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    ]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
    return dateFormat(this, mask, utc);
};

// strDate in dd/mm/yyyy format
function convert2DateObject(strDate) {
    strDate = strDate.split("/");
    var d = parseInt(strDate[0]);
    var m = parseInt(strDate[1]) - 1;
    var y = parseInt(strDate[2]);
    var showDate = new Date(y, m, d, 0, 0, 0 ,0);
    return showDate;
}

function verifyStaticTextExist(id,timeout) {
    // timeout is optional, default = 1
    if (typeof timeout == "undefined") {
        timeout = 1;
    }

    target.pushTimeout(timeout);
    if (target.frontMostApp().mainWindow().staticTexts()[id].isValid()) { 
        UIALogger.logPass(id + " text is valid");
    } else {
        UIALogger.logFail(id + " text is not valid");
    }
    target.popTimeout();
}

// id: id of button
// timeout: maxt time wait for button appared
// btab: press button
function verifyButtonExist(id,timeout,btab) {
    // timeout is optional, default = 1
    if (typeof timeout == "undefined") {
        timeout = 1;
    }

    if (typeof btab == "undefined") {
        btab = 0;
    }

    target.pushTimeout(timeout);
    if (target.frontMostApp().mainWindow().buttons()[id].isValid()) { 
        UIALogger.logPass(id + " button is valid");
        if (btab) {
            target.frontMostApp().mainWindow().buttons()[id].tap();
        }
    } else {
        UIALogger.logFail(id + " button is not valid");
    }
    target.popTimeout();
}

function regexp(strVerify,strExp) {
    UIALogger.logDebug("strVerify = " + strVerify + " - strExp = " + strExp);
    return strExp.test(strVerify);
}

function swipeLeft() {
	// swipe left
	target.delay(1);
    if (deviceType == "IPOD") {
	    target.dragFromToForDuration({x:240, y:416}, {x:16, y:402}, 0.6);
    } else if (deviceType == "IPAD") {
        // IPAD
        target.dragFromToForDuration({x:440, y:416}, {x:16, y:402}, 0.6);
    }
	target.delay(1);
}

function swipeRight() {
	// swipe right
	target.delay(1);
	if (deviceType == "IPOD") {
        target.dragFromToForDuration({x:16, y:402}, {x:240, y:416}, 0.6);
    } else if (deviceType == "IPAD") {
        // IPAD
        target.dragFromToForDuration({x:16, y:402}, {x:440, y:416}, 0.6);
    }
	target.delay(1);
}

function isMisfitCollectionScreen() {
	return app.mainWindow().staticTexts()["DAILY MISFIT"].isValid();
}

function isAboutUsScreen() {
	return app.mainWindow().staticTexts()["ABOUT US"].isValid();
}

function isWebsiteScreen() {
	return app.mainWindow().staticTexts()["WEBSITE"].isValid();
}

function isAnnouncementScreen() {
	return app.mainWindow().staticTexts()["ANNOUNCEMENTS"].isValid();
}

function isGiftBox() {
	// WARNING: this function can ve used only in debug build since the release
	// build will not have the "Open" button like used in the code below
	return app.mainWindow().buttons()["Open"].isValid();
}


function killApp() {
    // Need go to MISFIT Collection first before call this function
    target.frontMostApp().mainWindow().buttons()["Kill"].tap();
    return 0;
}

function startShuffleGame() {
    
    target.delay(1);
    // Start 
    //target.pushTimeout(10);
    //target.frontMostApp().mainWindow().buttons()["btn taptostart"].tap();
    //target.popTimeout();

}


function pickCorrectCard() {
    target.frontMostApp().mainWindow().elements()["CorrectCard"].tap();
}

function pickWrongCard1() {
    target.frontMostApp().mainWindow().elements()["IncorrectCard1"].tap();
}

function pickWrongCard2() {
    target.frontMostApp().mainWindow().elements()["IncorrectCard2"].tap();
}

// confirmSelectCard with yes (bconfirm = 1) or no (bconfirm = 0)
// bwin: indicated win the game. 0: lose, 1: win.
function confirmSelectCard(bconfirm,bwin) {

    // bconfirm is optional, default = 1
    if (typeof bwin == "undefined") {
        bwin = 1;
    }

    if (bconfirm == 0) {
        //target.frontMostApp().mainWindow().buttons()["btn areyousure no"].tap(); 
    } else {
        //target.frontMostApp().mainWindow().buttons()["btn areyousure yes"].tap();

        if (bwin) {
            tabtoWinCard();
        }
    }
}

function tabtoWinCard() {

     target.delay(5);
    // tap on win card
    target.pushTimeout(10);
    //if (app.mainWindow().staticTexts()["You're a Misfit!"].isValid()) {
    target.frontMostApp().mainWindow().staticTexts()["You're a Misfit!"].tapWithOptions({tapOffset:{x:0.56, y:0.62}});
    //    target.tap({x:157.50, y:301.00});
    //}
    target.popTimeout();

}


// atime:lose shuffleGame at second attime
// bconfirm: confirm the selection: 0 or 1
// wrongcardnum: chose wrong card number: 1 or 2
// lock: 0: not verify. 1: verify lock at countdown. 2: verify lock at confirmation card selected
// tabs: tabs random on the moving card 
function loseShuffleGamebyClickWrongCard(attime,bconfirm,wrongcardnum,lock,btabs) {

    // bconfirm is optional, default = 1
    if (typeof bconfirm == "undefined") {
        bconfirm = 1;
    }

    // bconfirm is optional, default = 1
    if (typeof wrongcardnum == "undefined") {
        wrongcardnum = 1;
    }

    // lock is optional, default = 0
    if (typeof lock == "undefined") {
        lock = 0;
    }

    if (typeof btabs == "undefined") {
        btabs = 0;
    }

    // Wait for animation complete
    target.pushTimeout(10);

    if (btabs) {
        for (var count =  5; count >= 0; count--) {
          
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
        }

    }
    if (checkCountDown(lock)) {
        target.delay(attime);
        if (wrongcardnum == 1) {
            pickWrongCard1();
        } else {
            pickWrongCard2();
        }

        confirmSelectCard(bconfirm,0)
    }
    target.popTimeout();
}

// nsecond: CountDown at nsecond second
function verifyShuffleGameCountDown(nsecond) {

    var name = "Pick a card in " + nsecond.toString() +  " seconds";    
    if (nsecond == 1) {
        name = "Pick a card in " + nsecond.toString() +  " second";
    }
    
    if (app.mainWindow().staticTexts()[name].isValid()) {
        UIALogger.logPass(name + " is valid");
    } else {
        UIALogger.logFail(name + " is not valid");
    }

    if (nsecond == 1) { 
        name = "Time's up"
        if (app.mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Time's up is valid");
        } else {
            UIALogger.logFail("Time's up is not valid");
        }
        //target.frontMostApp().logElementTree();
        /*
        name = "Nah, better next time."
        if (app.mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Nah, better next time is valid");
        } else {
            UIALogger.logFail("Nah, better next time is not valid");
        }

        target.frontMostApp().logElementTree()
        */
    }
}

//btabs
function loseShuffleGamebyTimeout(btabs) {
    // Start the first game
    //startShuffleGame();

    // Wait for animation complete
    target.pushTimeout(15);
    for (var i = 10; i > 0; i--) {
        verifyShuffleGameCountDown(i);
    }
    target.popTimeout();
    target.delay(10);
}

function verifyloseShuffleGame() {

    target.delay(5);
    /*
    name = "Nah, better next time."
    if (app.mainWindow().staticTexts()[name].isValid()) {
        UIALogger.logPass(name + " is valid");
    } else {
        UIALogger.logFail(name + " is not valid");
    }
    */
    // tap to show next
    UIALogger.logDebug("verifyloseShuffleGame: tab to show next");
    tabOnCard();

    target.delay(5);
    
    // Start parrot game state.
}

function startParrotGame() {
    // Wait for animation complete
    target.delay(5);
    target.pushTimeout(10);
    UIALogger.logDebug("startParrotGame: tab to start");
    //if (app.mainWindow().staticTexts()["Want a second chance?\nCatch me in 10 seconds"].isValid()) {
        tabOnCard();
    //}
    target.delay(2);
    target.popTimeout();
}

//btab: tab to go back to MISFIT collection
//lock: 0: no lock; 1: lock at parrot game
//locktime: wait amount seconds to lock
function loseParrotGamebyNoClick(btab,lock,locktime) {

    // lock is optional, default = 0
    if (typeof lock == "undefined") {
        lock = 0;
    }

    // locktime is optional, default = 0
    if (typeof locktime == "undefined") {
        locktime = 0;
    }

    startParrotGame();

    target.delay(locktime);

    lockApp("Lock",2);

    verifyButtonExist("btn resume",1,1);

    // Wait for timeout
    target.delay(10-locktime);

    target.pushTimeout(20);
    name = "Sorry... Time's up"
    if (app.mainWindow().staticTexts()[name].isValid()) {
        UIALogger.logPass(name + " is valid");
    } else {
        UIALogger.logFail(name + " is not valid");
    }
    target.popTimeout();

    //target.tap({x:160.00, y:0.00});

    target.delay(2);
    if (btab) {
        openMenu(0);
    }

}

function loseParrotGamebyWrongClick(btab) {
    startParrotGame();

    // tab on wrong position
    target.tap({x:0.00, y:0.00});
    target.delay(1);
    target.tap({x:0.00, y:410.00});
    target.delay(1);
    target.tap({x:310.00, y:0.00});
    target.delay(1);
    target.tap({x:310.00, y:0.00});
    target.delay(1);
    target.tap({x:0.00, y:240.00});
    target.delay(1);    
    target.tap({x:160.00, y:0.00});
    target.delay(10);

    target.pushTimeout(20);
    name = "Sorry... Time's up"
    if (app.mainWindow().staticTexts()[name].isValid()) {
        UIALogger.logPass(name + " is valid");
    } else {
        UIALogger.logFail(name + " is not valid");
    }
    target.popTimeout();
    
    target.tap({x:160.00, y:0.00});

    target.delay(2);
    if (btab) {
        openMenu(0);
    }

}

// win shuffleGame at second attime
function winParrotGame(attime) {
    
    startParrotGame();

    //target.delay(1);
    
    // Delay
    target.delay(attime);

    //target.frontMostApp().mainWindow().elements()["Parrot"].tap();

    if (deviceType == "IPOD") {
        target.tap({x:318.00, y:478.00});
    } else if (deviceType == "IPAD") {
        target.tap({x:700.00, y:986.00});
        target.tap({x:689.00, y:978.00});
        //target.frontMostApp().logElementTree();
    }    
    // tab on hidden button position.
    //target.tap({x:318.00, y:478.00});

    tabtoWinCard();
    /*
    // Win game by click on postion of parrot as quick as possible
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
    */
}

//attime win shuffleGame at second attime
//bconfirm: confirm = 1, not confirm = 0
function winShuffleGame(attime,bconfirm) {
    // Start the first game
    //startShuffleGame();

    // bconfirm is optional, default = 1
    if (typeof bconfirm == "undefined") {
        bconfirm = 1;
    }

    // bconfirm is optional, default = 1
    if (typeof attime == "undefined") {
        attime = 0;
    }

    //"Pick a card in 10 seconds"
    if (checkCountDown()) {
        target.delay(attime);
        pickCorrectCard();
        confirmSelectCard(bconfirm,1);
    }

}

// lock - 1: verify lock. 0: no verify
function verifywinShuffleGame(lock) {

    // lock is optional, default = 1
    if (typeof lock == "undefined") {
        lock = 0;
    }

    verifyStaticTextExist("You're a Misfit!",6);

    if (lock) {
        lockApp("Lock",2);
        verifyStaticTextExist("You're a Misfit!",6);        
    }

    tabOnCard();

}

function tabMenu() {
    // Open menu
    target.delay(1);
    //target.tap({x:263.50, y:25.00});
     target.frontMostApp().mainWindow().staticTexts()[1].tapWithOptions({tapOffset:{x:0.48, y:0.60}});
    target.delay(1);
}

// MISFIT:      item = 0
// DESK:        item = 1
// ABOUT US:    item = 2
function openMenu(item,delay) {

    // bconfirm is optional, default = 2
    if (typeof delay == "undefined") {
        delay = 2;
    }

    // Open menu
    tabMenu();

    // tab on menu
    target.frontMostApp().mainWindow().buttons()[item].tap();
    target.delay(delay);
}

// item
//      MISFIT:      item = 0
//      DESK:        item = 1
//      ABOUT US:    item = 2
// bside: use to verify side of current card in MISFIT menu. 
//      0:   front
//      1:   back
//      -1:   not verify
function verifyMenu(item,bside) {

    // bconfirm is optional, default = 1
    if (typeof bside == "undefined") {
        bside = -1;
    }

    // verify menu items
    if (item == 0) {
        name = "DAILY MISFIT";
        if ( target.frontMostApp().mainWindow().staticTexts()[name].isValid()) {
            UIALogger.logPass("Menu " + name + " is valid");
        } else {
            UIALogger.logFail("Menu " + name + " is not valid");
        }
    } else if (item == 1) {
        name = "YOUR DECK";
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
    if (bside >= 0 && item == 0) {
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid() == bside) { 
            UIALogger.logPass("Side of Card" + (i + 1).toString() + " is valid");
        } else if (target.frontMostApp().mainWindow().buttons()["btn shareonfb"].isValid() == bside) { 
            UIALogger.logPass("Side of Card" + (i + 1).toString() + " is valid");
        } else {
            UIALogger.logFail("side of Card" + (i + 1).toString() + " is not valid");
        }

    }

}

function winCardDone() {
    target.frontMostApp().mainWindow().buttons()["btn youaremisfit done"].tap();
}

//fblock. 1: block before tab, 2: lock after tab. 0: no lock
function winCardShareFB(fblock) {
    if (fblock == 1) {
        verifyButtonExist("btn youaremisfit fb",2,0);
        lockApp("Lock",2);
        verifyButtonExist("btn youaremisfit fb",2,0);
    }

    target.frontMostApp().mainWindow().buttons()["btn youaremisfit fb"].tap();

    //verify
    verifyStaticTextExist("SHARED",20);

    if (fblock == 2) {
        lockApp("Lock",2);
        verifyStaticTextExist("SHARED",20);
    }
    tabOnCard();
}

// return 0: if can not find a vailable card to play
// return 1: if find a card to play. The playable card will be in back.
function findAvailableCard() {

    // openMenu MIFIT
    openMenu(0);

    for (var i = 0; i < NUMBER_OF_CARD; i++) {

        //  verify the current card


        // tap to show back
        target.delay(1);
        tabOnCard();
        target.delay(1);
        
        // get length of the quote
        //var length = target.frontMostApp().mainWindow().staticTexts()[2].

        //target.pushTimeout(10);

        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) { 
            return 1;
        } 

        //target.popTimeout();

        // tap to show front
        tabOnCard();
        target.delay(1);


        // go to next card
        swipeRight();
        target.delay(2);

    }
    return 0;

}

function tabOnCard() {
    // tap on card
    if (deviceType == "IPOD") {
        target.tap({x:150.00, y:210.00});
    } else if (deviceType == "IPAD") {
        target.tap({x:389.50, y:585.50});
    }
    target.delay(1);
}

/*
type:
    Home: Press home button
    Lock: Press lock button
atime: block in seconds 
*/
function lockApp(type,atime) {
    if (type == "Home") {
        target.deactivateAppForDuration(atime);
        target.delay(2);
    } else if (type == "Lock") {
        target.lockForDuration(atime);
        target.delay(2);
    }
}


function pinchtoOpen() {
    target.pushTimeout(8);
    if (target.frontMostApp().mainWindow().buttons()["Open"].isValid()) { 
        target.frontMostApp().mainWindow().buttons()["Open"].tap();
    }
    target.popTimeout();
}

// lock: 0: not verify. 1: verify lock at countdown. 2: verify lock at confirmation card selected. 3: verify lock at time's up
// locktime: wait amount seconds to lock
// return 1: catch count down. 0: Don't have count down
function checkCountDown(lock,locktime) {

    var Maxtime = 30
    // lock is optional, default = 0
    if (typeof lock == "undefined") {
        lock = 0;
    }

    //"Pick a card in 10 seconds"
    var strVerify = /^Pick a card in .*$/
    if (lock == 3) {
        strVerify = /^Pick a card in 2 second.*$/
    }
    var strCount = app.mainWindow().staticTexts()[0].value();

    //target.delay(10);

    // Wait for animation complete
    var count = 1;
    while ( regexp(strCount,strVerify) == 0 && count < Maxtime ) {
        strCount = app.mainWindow().staticTexts()[0].value();
        target.delay(1);
        count = count + 1;
        UIALogger.logDebug("checkCountDown: " + count);
    }

    if (count < Maxtime) {

        if (lock == 1 || lock == 3) {
            //target.delay(locktime);
            var strCount_ref = app.mainWindow().staticTexts()[0].value();
            lockApp("Lock",2);
            //target.delay(10);
            var strCount_rt = app.mainWindow().staticTexts()[0].value();
            if (strCount_ref == strCount_rt) {
                UIALogger.logPass("Game is spending");
            } else {
                UIALogger.logFail("Game is playing during resume: RT=" + strCount_rt + ". REF=" + strCount_ref);
            }

           
            // time out 1 secs
            target.pushTimeout(1);
            if (target.frontMostApp().mainWindow().buttons()["btn resume"].isValid()) { 
                UIALogger.logPass("Resume button is valid");
                target.frontMostApp().mainWindow().buttons()["btn resume"].tab();
                target.delay(1);
            } else {
                UIALogger.logFail("Resume button is not valid");
            }
            target.popTimeout();
        } else if (lock == 2) {

            pickWrongCard1();

            var strCount_ref = app.mainWindow().staticTexts()[1].value();
            lockApp("Lock",2);
            //target.delay(10);
            var strCount_rt = app.mainWindow().staticTexts()[1].value();
            if (strCount_ref == strCount_rt) {
                UIALogger.logPass("Game is spending");
            } else {
                UIALogger.logFail("Game is playing during resume: RT=" + strCount_rt + ". REF=" + strCount_ref);
            }

            verifyCardConfirmation();

            confirmSelectCard(0);
            
        }
        return 1;

    } else {
        return 0
    }
}

function verifyCardConfirmation() {

    // time out 1 secs
    /*
    target.pushTimeout(1);
    verifyButtonExist("btn areyousure yes");
    verifyButtonExist("btn areyousure no");
    verifyStaticTextExist("Are you sure?");
    target.popTimeout();
    */

}
// lock: > 0 - lock number. 0: on lock 
// locktime: wait amount seconds to lock
// button: "btn resume" or "btn taptostart"
function verifyLockDuringGame(lock,locktime,button) {

    // bconfirm is optional, default = "btn resume"
    if (typeof button == "undefined") {
        button = "btn resume";
    }

    target.delay(locktime);
    for (var j = 0; j < lock; j++) {
        lockApp("Lock",2);
        if (target.frontMostApp().mainWindow().buttons()[button].isValid()) { 
            UIALogger.logPass(button + " Resume button is valid");

            if (button == "btn resume") {
                if (checkCountDown() == 0) {
                    UIALogger.logPass("Game is spending");
                } else {
                    UIALogger.logFail("Game is playing during resume");
                }
                target.frontMostApp().mainWindow().buttons()["btn resume"].tap();
                target.delay(1);
            }
        }
        else {
            UIALogger.logFail(button + "Resume button is not valid");
        }
    }
}

// return current date of the app.
function getCurrentDate() {

}

function verifyBeforeStartShuffleGame() {

    target.delay(1);
    /*
    // verify get this card button 
    target.pushTimeout(20);
    if (target.frontMostApp().mainWindow().buttons()["btn taptostart"].isValid()) { 
        UIALogger.logPass("Tab to start #" + (i + 1).toString() + " is valid");
    }
    else {
        UIALogger.logFail("Tab to start #" + (i + 1).toString() + " is not valid");
    }
    
    target.delay(1);
    // check Orientation 
    if (target.deviceOrientation() == 5 ) {
        UIALogger.logPass("Orientation #" + (i + 1).toString() + " is UIA_DEVICE_ORIENTATION_PORTRAIT");
    }
    else {
        UIALogger.logFail("Orientation #" + (i + 1).toString() + " is not UIA_DEVICE_ORIENTATION_PORTRAIT. It is: " + target.deviceOrientation());
    }
    */
}

// ============== Test helpers

function logTree() 
{
	target.logElementTree();
	wait(1);
}

function wait(s)
{
	if(typeof s == "undefined")
		s = 1;
	target.delay(s);
}

function start(text)
{
	if(typeof text == "undefined")
		text = "Test started";
	UIALogger.logStart(text);
}

function log(text)
{
	if(typeof text == "undefined")
		text = "Debug log";
    UIALogger.logDebug(text);
}

function pass(text)
{
	if (typeof text == "undefined")
		text = "Pass";
	UIALogger.logPass(text);
}

function fail(text)
{
	if(typeof text == "undefined")
		text = "Fail";
	UIALogger.logPass(text);
}

// =============== TA helpers
function staticTextExist(text)
{
	win = app.mainWindow();
	if(win.staticTexts()[text].isValid())
		return true;
	return false;
}

function wheelPick(picker, wheelIndex, value)
{
	if(typeof wheelIndex == "undefined")
		wheelIndex = 0;
		
	wheel = picker.wheels()[wheelIndex];
	items = wheel.values();
	
	if(items.indexOf[value] < 0)
		fail("Wheel has no such value!");

	wheel.selectValue(value);
}

function isKeyboardVisible() {
	return app.keyboard().isVisible();
}