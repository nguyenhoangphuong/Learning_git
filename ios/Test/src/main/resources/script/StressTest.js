
function C255(num) {
    for (var i = num; i >= 0; i--) {
        C1187(0);
        C1187(1); 
    };
}


function C293(num) {
    for (var i = num; i >= 0; i--) {
        C1167(); 
    };
}

function C294(num) {
    // get all cards
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        C216(4);
    }

    for (var i = num; i >= 0; i--) {
        C229(0,1);
        C229(1,1);
    }
}


function C295(num) {

    pinchtoOpen();
    
    openMenu(0);
    verifyMenu(0);

    var strDateREF = app.mainWindow().staticTexts()[2].value();
    tabOnCard();
    var strQuoteREF = app.mainWindow().staticTexts()[2].value();
    tabOnCard();

    for (var i = 0; i < num; i++) {
        strDateRT = app.mainWindow().staticTexts()[2].value();
        tabOnCard();
        strQuoteRT = app.mainWindow().staticTexts()[2].value();
        tabOnCard();

        //verify
        if (strDateREF == strDateRT) {
            UIALogger.logPass("Date of card #" + (i + 1).toString() + " is correct");
        }
        else {
            UIALogger.logFail("Date of card #" + (i + 1).toString() + " is not correct. RT= " +  strDateRT + "REF= " + strDateREF);
        }
        if (strQuoteREF == strQuoteRT) {
            UIALogger.logPass("Quote of card #" + (i + 1).toString() + " is correct");
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is not correct. RT= " +  strQuoteRT + "REF= " + strQuoteREF);
        }
    }
}

function C292() {
    C217(1,1);
}