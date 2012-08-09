// lock: > 0 - lock number. 0: on lock at shuffle game
// locktime: wait amount seconds to lock
// bshare: 0: done, 1: share on facebook
//fblock. 1: block before tab, 2: lock after tab. 0: no lock
function C222(atime,lock,locktime,bshare,fblock) {
    
    if (typeof bshare == "undefined") {
        bshare = 0;
    }


    if (typeof fblock == "undefined") {
        fblock = 0;
    }


    //pinchtoOpen();

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

        if (lock > 0) {
            verifyLockDuringGame(lock,locktime);
        }

        loseShuffleGamebyClickWrongCard(0,1,1);

        verifyloseShuffleGame();

        winParrotGame(atime);


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

// bcatch: 0 - not catch. 1 - try to catch 
// bverifycard: 1 - verify card can  getable. 0 - not verify
// lock: > 0 - lock number. 0: on lock at shuffle game
// locktime: wait amount seconds to lock
// lockatcount: 0: no lock; 1: lock at count; 2: lock at confirmation; 3 lock at time's up
// parrotlock: 0: no lock; 1: lock at parrot game
// parrotlocktime: wait amount seconds to lock
function C225(bcatch,bverifycard,lock,locktime,lockatcount,parrotlock,parrotlocktime) {
    

    pinchtoOpen();

    // bcatch is optional, default = 1
    if (typeof bcatch == "undefined") {
        bcatch = 0;
    }

    if (typeof bverifycard == "undefined") {
        bverifycard = 0;
    }


    if (typeof lock == "undefined") {
        lock = 0;
    }

    if (typeof lockatcount == "undefined") {
        lockatcount = 0;
    }

    if (typeof parrotlock == "undefined") {
        parrotlock = 0;
    }

    if (findAvailableCard()) {
        
        // get this card 
        target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();  
        target.delay(1);

        verifyBeforeStartShuffleGame();

        if (lock > 0) {
            verifyLockDuringGame(lock,locktime,"btn taptostart");
        }

        startShuffleGame();

        if (lock > 0) {
            verifyLockDuringGame(lock,locktime);
        }

        loseShuffleGamebyClickWrongCard(0,1,1,lockatcount);

        verifyloseShuffleGame();

        if (bcatch == 0) {
            loseParrotGamebyNoClick(1,parrotlock,parrotlocktime);
        } else {
            loseParrotGamebyWrongClick(1);
        }

        if (bverifycard) {
            
            // tap to show back
            tabOnCard();

            // verify get this card button does not exist
            if (target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid()) { 
                UIALogger.logPass("Card #" + (i + 1).toString() + " is getable");
            }
            else {
                UIALogger.logFail("Card #" + (i + 1).toString() + " is not getable");
            }      

            // tap to show front
            tabOnCard();
        } 

        //openMenu(0);

        // go to next card
        swipeRight();

    }
}

// bside: 0 - front. 1 - back
function C1187(bside) {
    
    pinchtoOpen();

    // bcatch is optional, default = 1
    if (typeof bside == "undefined") {
        bside = 0;
    }

    //C225(0);

    target.delay(2);

    // tab on card to turn back
    if (bside) {
        tabOnCard();
    }

    // open MISFIT menu and verify
    openMenu(0);
    verifyMenu(0,bside);
    
    // open DECK menu and verify
    openMenu(1);
    verifyMenu(1);
    
    // open MISFIT menu and verify
    openMenu(0);
    verifyMenu(0,bside)
 
    // open DECK menu and verify
    openMenu(2);
    verifyMenu(2);

    // open MISFIT menu and verify
    openMenu(0);
    verifyMenu(0,bside);

    // open DECK menu and verify
    openMenu(1);
    verifyMenu(1);

    // open DECK menu and verify
    openMenu(2);
    verifyMenu(2);

    // open DECK menu and verify
    openMenu(1);
    verifyMenu(1);

    // open MISFIT menu and verify
    openMenu(0);
    verifyMenu(0,bside);

    if (bside) {
        tabOnCard();
    }
}


function Cx(atime,lock,locktime,bshare,fblock) {

    tabOnCard();
    target.delay(1);
    tabOnCard();
    target.delay(2);
    target.dragFromToForDuration({x:176, y:402}, {x:740, y:416}, 0.6);
    target.delay(1);
    
}