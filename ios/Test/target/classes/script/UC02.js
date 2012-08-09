function C210() {
    // kill app need to be called first.

    // timeout 3 seconds;
    target.pushTimeout(3);
    verifyMenu(0);
    target.popTimeout();

    var strDate = app.mainWindow().staticTexts()[2].value();

    var today = new Date();

    var strtoday = dateFormat(today, "dd/mm/yyyy");

    if(strDate != strtoday) {
        UIALogger.logFail("Debug: FAIL " + strDate + " != " + strtoday);
    } else {
        UIALogger.logPass("Debug: PASS " + strDate + " == " + strtoday);
    }

}

function C212() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    pinchtoOpen();

    var length_max = LENGTH_OF_QUOTE;
    
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        // tap to show back
        tabOnCard();
        
        // get length of the quote
        var length = target.frontMostApp().mainWindow().staticTexts()[2].value().length;
        
        if (length <= length_max && length > 0) {
            UIALogger.logPass("Quote of card #" + (i + 1).toString() + " is valid");    
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is longer than " + length_max.toString() + " character(s)");   
        }
        
        // verify get this card button 
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass("Get this Card #" + (i + 1).toString() + " is valid");
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is not valid");
        }
        
        /*
        // verify get this card button text
        var button_text = target.frontMostApp().mainWindow().buttons()["btn getthiscard"].caption();
        
        if (button_text == "GET THIS CARD") {   
            UIALogger.logPass("Get this Card #" + (i + 1).toString() + " is correct");
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is not correct" + button_text);
        }
         
        */
        
        // tap to show front
        tabOnCard();
        
        // swipe finger from left to right
        swipeLeft();
    }
}

function C1166() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    pinchtoOpen();
    
    var length_max = LENGTH_OF_QUOTE;
    
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        // tap to show back
        tabOnCard();
        
        // get length of the quote
        var length = target.frontMostApp().mainWindow().staticTexts()[2].value().length;
        
        if (length <= length_max && length > 0) {
            UIALogger.logPass("Quote of card #" + (i + 1).toString() + " is valid");    
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is longer than " + length_max.toString() + " character(s)");   
        }
        
        // verify get this card button 
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass("Get this Card #" + (i + 1).toString() + " is valid");
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is not valid");
        }
        
        /*
        // verify get this card button text
        var button_text = target.frontMostApp().mainWindow().buttons()["btn getthiscard"].caption();
        
        if (button_text == "GET THIS CARD") {   
            UIALogger.logPass("Get this Card #" + (i + 1).toString() + " is correct");
        }
        else {
            UIALogger.logFail("Quote of card #" + (i + 1).toString() + " is not correct" + button_text);
        }
         
        */
        
        // tap to show front
        tabOnCard();
        
        // swipe finger from left to right
        swipeRight();
    }
}