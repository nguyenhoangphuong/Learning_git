function C202() {
    var timeout = target.timeout();
    var timeout_splash = 3;
    var timeout_greeting = 2;
     
    // Misfit logo
    target.delay(timeout_splash);
    
    // timeou for each greeting
    target.setTimeout(timeout_greeting);
    
    // 1st greeting
    if (target.frontMostApp().mainWindow().staticTexts()["Hello World,\nWe are Misfit Wearables"].isValid()) {
        UIALogger.logPass("greeting1");
    }
    else {
        UIALogger.logFail("greeting1");
    }
    
    // 2nd greeting 
    if (target.frontMostApp().mainWindow().staticTexts()["We are busy making the world\n1st mobile healthcare sensor"].isValid()) {   

        UIALogger.logPass("greeting2");
    }
    else {
        UIALogger.logFail("greeting2");
    }
     
    // 3rd greeting
    if (target.frontMostApp().mainWindow().staticTexts()["For now, enjoy!"].isValid()) {
        UIALogger.logPass("greeting3");
    }
    else {
        UIALogger.logFail("greeting3");
    }
    
    // reset timeout
    target.setTimeout(timeout);
}

function C203() {
    // do steps of C202
    //C202(); // not doing when deploying to CI server since the app is run continuously when testing
    
    target.pushTimeout(20);
    target.frontMostApp().mainWindow().buttons()["Open"].tap();
    target.popTimeout();
    
    if (target.frontMostApp().mainWindow().staticTexts()["Sorry, there was an error with                                              Internet connection. Can you                                              help checking your Internet                                              connection?"].isValid()) {
        UIALogger.logPass("Network error message is valid");
    }
    else {
        UIALogger.logFail("Network error message is invalid");
    }
}

function C205() {
    // do steps of C202
    //C202(); // not doing when deploying to CI server since the app is run continuously when testing
    
    target.pushTimeout(20);
    target.frontMostApp().mainWindow().buttons()["Open"].tap();
    target.popTimeout();
    
    if (target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid()) {
        UIALogger.logPass("Misfit Collection is shown");
    }
    else {
        UIALogger.logFail("Misfit Collection is not shown");
    }
}

function C206() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    //target.delay(10);

    pinchtoOpen();

    var numcard = NUMBER_OF_CARD;
    /*
    for (i = 0 ; i < numcard ; i++)
    {
        target.delay(1);
        target.dragFromToForDuration({x:240.00, y:416.00}, {x:16.00, y:402.00}, 0.6);
        target.delay(1);
    }
    */
    
    for (i = 0 ; i < numcard ; i++)
    {
        //target.delay(1);
        //target.dragFromToForDuration({x:240.00, y:416.00}, {x:16.00, y:402.00}, 0.6);
        //target.delay(1);
        
        tabOnCard();
        
        msg = "Playable Card " + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card " + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        // tab on the card to turn on
        tabOnCard();
        
        // swipe left
        swipeLeft();
        
        
    }
    
    for (i = 0 ; i < numcard ; i++)
    {
        //target.delay(1);
        //target.dragFromToForDuration({x:240.00, y:416.00}, {x:16.00, y:402.00}, 0.6);
        //target.delay(1);
        
        // tab on the card to turn back
        tabOnCard();
        
        msg = "Playable Card" + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card" + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        // tab on the card to turn on
        tabOnCard();
        
        // swipe left
        swipeRight();
        
        
    }
    
}

function C1164() {
    // do steps of C205
    C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    var length_max = 140;
    
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
        
        // tap to show front
        tabOnCard();
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
    }
}

function C1165() {
    // do steps of C205
    C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    for (var i = 0; i < NUMBER_OF_CARD; i++) {
        // tap to show back
        tabOnCard();
        
        // check the button "GET THIS CARD"
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {
            UIALogger.logPass("Card #" + (i + 1).toString() + " is get-able");
        }
        else {
            UIALogger.logFail("Card #" + (i + 1).toString() + " is not get-able");
        }
        
        // tap to show front
        tabOnCard();
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
    }
}

function C1157() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    // wait for cards to be loaded
    target.delay(LOADED_CARD_TIME);
    
    pinchtoOpen();

    for (var i = 0 ; i < NUMBER_OF_CARD ; i++)
    {
        
        openMenu(1);
    
        // verify Deck Collction screen. TODO: need to update: no card
        if (target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid()) {
            UIALogger.logPass("Deck Collection is shown");
        }
        else {
            UIALogger.logFail("Deck Collection is not shown");
        }

        openMenu(2);
    
        // verify About Us screen
        if (target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].isValid()) {
            UIALogger.logPass("About Us screen is shown");
        }
        else {
            UIALogger.logFail("About Us screen is not shown");
        }
        
    
        openMenu(0);
    
        // verify Misfit Collection screen
        if (target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid()) {
            UIALogger.logPass("Misfit Collection is shown");
        }
        else {
            UIALogger.logFail("Misfit Collection is not shown");
        }
        
        // swipe right
        swipeRight();
    }
}

function C1158() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    // wait for cards to be loaded
    //target.delay(LOADED_CARD_TIME);
    
    pinchtoOpen();

    for (var i = 0 ; i < NUMBER_OF_CARD ; i++)
    {
        openMenu(1);
    
        // verify Deck Collction screen. TODO: need to update: no card
        if (target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid()) {
            UIALogger.logPass("Deck Collection is shown");
        }
        else {
            UIALogger.logFail("Deck Collection is not shown");
        }

       openMenu(2);
    
        // verify About Us screen
        if (target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].isValid()) {
            UIALogger.logPass("About Us screen is shown");
        }
        else {
            UIALogger.logFail("About Us screen is not shown");
        }
        
        openMenu(0);
    
        // verify Misfit Collection screen
        if (target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid()) {
            UIALogger.logPass("Misfit Collection is shown");
        }
        else {
            UIALogger.logFail("Misfit Collection is not shown");
        }
        
        // swipe right
        swipeRight();
    }
}

function C1160() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    // wait for cards to be loaded
    target.delay(LOADED_CARD_TIME);
    
    pinchtoOpen();

    for (var i = 0 ; i < NUMBER_OF_CARD ; i++)
    {
        // tab on the card to turn back
        tabOnCard();
        
        msg = "Playable Card" + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card" + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        openMenu(1);
    
        // verify Deck Collction screen. TODO: need to update: no card
        if (target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid()) {
            UIALogger.logPass("Deck Collection is shown");
        }
        else {
            UIALogger.logFail("Deck Collection is not shown");
        }

        openMenu(2);
    
        // verify About Us screen
        if (target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].isValid()) {
            UIALogger.logPass("About Us screen is shown");
        }
        else {
            UIALogger.logFail("About Us screen is not shown");
        }
        
        openMenu(0);
    
        // verify Misfit Collection screen
        if (target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid()) {
            UIALogger.logPass("Misfit Collection is shown");
        }
        else {
            UIALogger.logFail("Misfit Collection is not shown");
        }
        
        tabOnCard();

        msg = "Playable Card" + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card" + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        // tab on the card to turn on
        tabOnCard();
        
        // swipe right
        swipeRight();
    }
}



function C1167() {
    // do steps of C205
    //C205(); // not doing when deploying to CI server since the app is run continuously when testing
    
    // wait for cards to be loaded
    //target.delay(LOADED_CARD_TIME);
    
    pinchtoOpen();

    for (var i = 0 ; i < NUMBER_OF_CARD ; i++)
    {
        // tab on the card to turn back
        tabOnCard();
        
        msg = "Playable Card " + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card " + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        openMenu(1);
    
        // verify Deck Collction screen. TODO: need to update: no card
        if (target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid()) {
            UIALogger.logPass("Deck Collection is shown");
        }
        else {
            UIALogger.logFail("Deck Collection is not shown");
        }

        openMenu(2);
    
        // verify About Us screen
        if (target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].isValid()) {
            UIALogger.logPass("About Us screen is shown");
        }
        else {
            UIALogger.logFail("About Us screen is not shown");
        }
        
        openMenu(0);
    
        // verify Misfit Collection screen
        if (target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid()) {
            UIALogger.logPass("Misfit Collection is shown");
        }
        else {
            UIALogger.logFail("Misfit Collection is not shown");
        }
        
        tabOnCard();
        
        msg = "Playable Card " + i;
        
        target.pushTimeout(5);
        if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) {    
            UIALogger.logPass(msg);
        }
        else {
            msg = "Not Playable Card " + i;
            UIALogger.logFail(msg);
        }
        target.popTimeout();
        
        // tab on the card to turn on
        tabOnCard();
        
        // swipe right
        swipeRight();
    }
}
