
// side: 0 - Front or 1 - Back
// date: card date reference
// button: "btn getthiscard" or "btn shareonfb"
function verifyCardState(side,carddate_ref,button) {

    // button is optional, default = "btn getthiscard"
    if (typeof button == "undefined") {
        button = "btn getthiscard";
    }


    // get the date after deactive
    var carddate_rt = target.frontMostApp().mainWindow().staticTexts()[0].value();

    if (carddate_ref == carddate_rt) {
        UIALogger.logPass("Date of card #" + (i + 1).toString() + " is correct");
    }
    else {
        UIALogger.logFail("Date of card #" + (i + 1).toString() + " is not correct");
    }
    
    // verify side of card 
    target.pushTimeout(5);
    if (target.frontMostApp().mainWindow().buttons()[button].isValid() == side) { 
        UIALogger.logPass("Side of card #" + (i + 1).toString() + " is correct");
    }
    else {
        UIALogger.logFail("Side of card #" + (i + 1).toString() + " is not correct");
    }
}

// side: 0 - Front or 1 - Back
// numcard: number of card need to verify
function C263(side,numcard) {

    pinchtoOpen();
 
    // verify 2 first card
    for (var i = 0; i < numcard; i++) {
        
        openMenu(0,2);

        if (side == 0) {
            // do nothing
        } else if (side == 1) {
            // tab front
            tabOnCard();
        }

        // get the date 
        var carddate_ref = target.frontMostApp().mainWindow().staticTexts()[0].value();
        
        lockApp("Lock",2);

        verifyCardState(side,carddate_ref);

        // go to others menu
        openMenu(1);
        openMenu(0,2);
        verifyCardState(side,carddate_ref);
        openMenu(2);
        openMenu(0,2);
        verifyCardState(side,carddate_ref);
        openMenu(1);
        openMenu(2);
        openMenu(1);
        openMenu(0);
        openMenu(0,2);
        verifyCardState(side,carddate_ref);
        
        if (side == 0) {
            // do nothing
        } else if (side == 1) {
            // tab back
            tabOnCard();
            verifyCardState(0,carddate_ref);
        }

        swipeRight();
        carddate_ref = target.frontMostApp().mainWindow().staticTexts()[0].value();
        openMenu(0,2);
        verifyCardState(side,carddate_ref);

    };
}

// side: 0 - Front or 1 - Back
// numcard: number of card need to verify
function C265(side,numcard) {

    pinchtoOpen();
 
    // verify 2 first card
    for (var i = 0; i <= numcard; i++) {
        
        openMenu(1,2);

        if (side == 0) {
            // do nothing
        } else if (side == 1) {
            // tab front
            tabOnCard();
        }

        // get the date 
        var carddate_ref = target.frontMostApp().mainWindow().staticTexts()[0].value();
        
        target.frontMostApp().mainWindow().staticTexts()["There is no card in the deck yet.\nCollect them from the Misfit Collection."]

        if (numcard == 0) {
            if (target.frontMostApp().mainWindow().staticTexts()["There is no card in the deck yet.\nCollect them from the Misfit Collection."].isValid()) {
                UIALogger.logPass("Empty Deck is correct");
            } else {
                UIALogger.logFail("Empty Deck is not correct");
            }
        }

        lockApp("Lock",2);

        verifyCardState(side,carddate_ref,"btn shareonfb");

        // go to others menu
        openMenu(0);
        openMenu(1,2);
        verifyCardState(side,carddate_ref,"btn shareonfb");
        openMenu(2);
        openMenu(1,2);
        verifyCardState(side,carddate_ref,"btn shareonfb");
        openMenu(0);
        openMenu(2);
        openMenu(0);
        openMenu(1);
        openMenu(1,2);
        verifyCardState(side,carddate_ref,"btn shareonfb");
        
        if (side == 0) {
            // do nothing
        } else if (side == 1) {
            // tab back
            tabOnCard();
            verifyCardState(0,carddate_ref);
        }

        swipeRight();

        carddate_ref = target.frontMostApp().mainWindow().staticTexts()[0].value();
        openMenu(1,2);
        verifyCardState(side,carddate_ref,"btn shareonfb");
    };
}


function C278() {

    pinchtoOpen();
 
    tabMenu();

    lockApp("Lock",2);

    
    for (var i = 0; i <= numcard; i++) {
        
       if (findAvailableCard()) {
        
            // get this card 
            target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
            target.delay(1);

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

            target.popTimeout();

            startShuffleGame();

            target.delay(2);

            lockApp("Lock",2);

            target.frontMostApp().mainWindow().buttons()["btn resume"].tap();
 
        };
    };
}


function C278() {

    pinchtoOpen();
    tabMenu();

    // verify menu
    verifyStaticTextExist("MENU",1);
    verifyButtonExist("btn menu motd",1,0);
    verifyButtonExist("btn menu deck",1,0);
    verifyButtonExist("btn menu aboutus",1,0);


    lockApp("Lock",2);

    // verify menu
    verifyStaticTextExist("MENU",1);
    verifyButtonExist("btn menu motd",1,0);
    verifyButtonExist("btn menu deck",1,0);
    verifyButtonExist("btn menu aboutus",1,0);
    
}


function C279() {
    C220(0,1,1);
}

function C285() {
    C220(0,1,2);
}

function C281() {
    C222(0,0,0,1,1);
}

function C286() {
    C222(0,0,0,1,2);
}