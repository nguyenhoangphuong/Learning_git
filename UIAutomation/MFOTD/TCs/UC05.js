
function righttoleft (aquotes,bverify) {
    if (bverify) {
        var quotes = aquotes;
    } else {
        var quotes = new Array();
    }
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        var strDate = app.mainWindow().staticTexts()[2].value();
        var objToday = new Date();

        var myDateREF = objToday.setDate(objToday.getDate()-i);
        var strtoday = dateFormat(objToday, "dd/mm/yyyy");
        
        var cardnum = NUMBER_OF_CARD-i-1;

        if(strDate == strtoday)
        {
            UIALogger.logPass("Pass Card # " + i + ":" + strDate + " == " + strtoday);
        } else {
            UIALogger.logFail("Fail Card # " + i + ":" + strDate + " != " + strtoday);
        }

        // Tap on card to show its back
        tabOnCard();

        if (bverify) {
            var cur_quote = app.mainWindow().staticTexts()[2].value();
            if(quotes[cardnum] == cur_quote)
            {
                UIALogger.logPass("Pass Card # " + cardnum + ":" + quotes[cardnum] + " == " + cur_quote);
            } else {
                UIALogger.logFail("Fail Card # " + cardnum + ":" + quotes[cardnum] + " != " + cur_quote);
            }
        } else {
            // get the Misfit's quote
            quotes[i]  = app.mainWindow().staticTexts()[2].value();
        }


        // Tap on the back of card to show its front
        tabOnCard();

        // Swipe finger to the left to view previous card
        swipeRight();
    }

    if (bverify == 0) {
        return quotes
    }

}

function lefttoright (aquotes,bverify) {
    // verify the card stack is looping
    if (bverify) {
        var quotes = aquotes;
    } else {
        var quotes = new Array();
    }
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        swipeLeft();

        var strDate = app.mainWindow().staticTexts()[2].value();
        var objToday = new Date();

        var myDateREF = objToday.setDate(objToday.getDate()-NUMBER_OF_CARD+i+1);
        var strtoday = dateFormat(objToday, "dd/mm/yyyy");

        var cardnum = NUMBER_OF_CARD-i-1;

        if(strDate == strtoday)
        {
            UIALogger.logPass("Pass Card # " + cardnum + ":" + strDate + " == " + strtoday);
        } else {
            UIALogger.logFail("Fail Card # " + cardnum + ":" + strDate + " != " + strtoday);
        }

        // Tap on card to show its back
        tabOnCard();

        // verify the Misfit's quote

        if (bverify) {
            var cur_quote = app.mainWindow().staticTexts()[2].value();
            if(quotes[cardnum] == cur_quote)
            {
                UIALogger.logPass("Pass Card # " + cardnum + ":" + quotes[cardnum] + " == " + cur_quote);
            } else {
                UIALogger.logFail("Fail Card # " + cardnum + ":" + quotes[cardnum] + " != " + cur_quote);
            }
        } else {
            // get the Misfit's quote
            quotes[i]  = app.mainWindow().staticTexts()[2].value();
        }
        

        // Tap on the back of card to show its front
        tabOnCard();

        // Swipe finger to the left to view next card
        //swipeLeft();
    }
    if (bverify == 0) {
        return quotes
    }
}

// side: 0: right to left; 1: left to right
// imenu: 0: MISFIT, 1: DECK
function C229(bside,imenu) {


    if (typeof imenu == "undefined") {
        imenu = 0;
    }

    pinchtoOpen();


    openMenu(imenu);
    
    //if (isMisfitCollectionScreen() == true) {

        // Verify that the card being shown is the one of the current day (by checking date)
        if (bside) {
            var quotes = lefttoright("",0);
            righttoleft(quotes,1);
        } else {
            var quotes = righttoleft("",0);
            lefttoright(quotes,1);
        }
        // go to new loop
        //swipeLeft();
    //}
    
}