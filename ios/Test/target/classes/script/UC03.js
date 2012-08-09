
function C214() {
    
    //var length_max = LENGTH_OF_QUOTE;
    
    pinchtoOpen();

    for (var i = 0; i < NUMBER_OF_CARD; i++) {
    
        if (findAvailableCard()) {   
                
            // tap to show back
            //tabOnCard();
            
            // get this card 
            target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
            
            verifyBeforeStartShuffleGame();

            startShuffleGame();

            winShuffleGame(2);

            // verify winShuffleGame 
            verifywinShuffleGame();

            winCardDone();

            openMenu(0);

            swipeRight();

            //loseShuffleGamebyClickWrongCard(2);

            //loseParrotGamebyNoClick();

            //winParrotGame(2);

            /*
            // tap to show front
            target.tap({x:150.00, y:210.00});
            target.delay(1);
            
            // swipe finger from left to right
            target.flickFromTo({x:50, y:250},{x:260, y:250});
            target.delay(1);
            */
        }
    }
}

function C215(lock) {
    
    // lock is optional, default = 0ins
    if (typeof lock == "undefined") {
        lock = 0;
    }

    //var length_max = LENGTH_OF_QUOTE;
    
    //for (var i = 0; i < NUMBER_OF_CARD; i++) {
    
    pinchtoOpen();

    if (findAvailableCard()) {
        
            
        // tap to show back
        //tabOnCard();
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        
        verifyBeforeStartShuffleGame();

        startShuffleGame();

        loseShuffleGamebyTimeout();

        verifyloseShuffleGame();

        //winParrotGame(2);

        loseParrotGamebyNoClick(1,0);

        //openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

// atime: win at atime seconds
// bverifycard: verify card is not getable
// lock: > 0 - lock number. 0: on lock 
// locktime: wait amount seconds to lock
// lockwin: lock at you are misfit greeting
function C216(atime,bverifycard,lock,locktime,lockwin) {
    
    pinchtoOpen();

    // bconfirm is optional, default = 1
    if (typeof atime == "undefined") {
        atime = 0;
    }

    // bconfirm is optional, default = 1
    if (typeof bverifycard == "undefined") {
        bverifycard = 0;
    }

    if (typeof lock == "undefined") {
        lock = 0;
    }

    if (typeof locktime == "undefined") {
        locktime = 1;
    }

    if (typeof lockwin == "undefined") {
        lockwin = 0;
    }

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        startShuffleGame();

        if (lock > 0) {
            verifyLockDuringGame(lock,locktime);
        }

        winShuffleGame(atime);

        // verify winShuffleGame 
        verifywinShuffleGame(lockwin);

        winCardDone();

        openMenu(0);

        if (bverifycard) {
            
            // tap to show back
            tabOnCard();

            // verify get this card button does not exist
            if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid() == 0) { 
                UIALogger.logPass("Card #" + (i + 1).toString() + " is not getable");
            }
            else {
                UIALogger.logFail("Card #" + (i + 1).toString() + " is getable");
            }   

            // tap to show front
            tabOnCard();
      
        } 


        // go to next card
        swipeRight();
        
        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

// bshare: 0: done, 1: share on facebook
//fblock. 1: block before tab, 2: lock after tab. 0: no lock
function C220(lock,bshare,fblock) {
    
    // lock is optional, default = 0
    if (typeof lock == "undefined") {
        lock = 0;
    }


    if (typeof bshare == "undefined") {
        bshare = 0;
    }


    if (typeof fblock == "undefined") {
        fblock = 0;
    }


    pinchtoOpen();

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        target.popTimeout();

        startShuffleGame();

        loseShuffleGamebyClickWrongCard(0,0);
        
        winShuffleGame(1);
        
        // verify winShuffleGame 
        verifywinShuffleGame(lock);

        if (bshare == 1) {
            winCardShareFB(fblock);
        } else {
           winCardDone();
        }

        openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

function C1184() {
    
    pinchtoOpen();

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        startShuffleGame();

        loseShuffleGamebyClickWrongCard(0,0,1);
        
        loseShuffleGamebyClickWrongCard(0,0,2);

        loseShuffleGamebyClickWrongCard(0,0,2);
        
        loseShuffleGamebyClickWrongCard(0,0,1);

        loseShuffleGamebyClickWrongCard(0,0,1);

        winShuffleGame(1);
        
        // verify winShuffleGame 
        verifywinShuffleGame(lock);

        winCardDone();

        openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

function C217(atime,btabs) {
    
    if (typeof btabs == "undefined") {
        btabs = 0;
    }


    pinchtoOpen();

    // bconfirm is optional, default = 1
    if (typeof atime == "undefined") {
        atime = 0;
    }

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        startShuffleGame();

        loseShuffleGamebyClickWrongCard(atime,1,1,0,1);

        verifyloseShuffleGame();

        //winParrotGame(2);

        loseParrotGamebyNoClick(1);


        openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

function C221(atime) {
    
    pinchtoOpen();


    // bconfirm is optional, default = 1
    if (typeof atime == "undefined") {
        atime = 0;
    }

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        startShuffleGame();

        loseShuffleGamebyClickWrongCard(0,0,1);
        winShuffleGame(0,0);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,1,2);

        verifyloseShuffleGame();

        //winParrotGame(2);

        loseParrotGamebyNoClick(1);


        openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}

function C218(atime) {
    
    pinchtoOpen();

    // bconfirm is optional, default = 1
    if (typeof atime == "undefined") {
        atime = 0;
    }

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        startShuffleGame();

        loseShuffleGamebyClickWrongCard(0,0,1);
        winShuffleGame(0,0);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,1);
        winShuffleGame(0,0);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,1);
        winShuffleGame(0,0);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        winShuffleGame(0,0);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,2);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,0,1);
        loseShuffleGamebyClickWrongCard(0,1,2);

        verifyloseShuffleGame();

        //winParrotGame(2);

        loseParrotGamebyNoClick(1);


        openMenu(0);

        // go to next card
        swipeRight();

        //loseShuffleGamebyClickWrongCard(2);

        //loseParrotGamebyNoClick();

        //winParrotGame(2);

        /*
        // tap to show front
        target.tap({x:150.00, y:210.00});
        target.delay(1);
        
        // swipe finger from left to right
        target.flickFromTo({x:50, y:250},{x:260, y:250});
        target.delay(1);
        */
    }
}