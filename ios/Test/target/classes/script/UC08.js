function C244() {
    
    openMenu(2); 
     
    target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus anounce"].tap();
     
    var firstText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].staticTexts()[0].value();
     
    if(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].isValid())
    {
            target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].tap();
     
     
            var secondText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].staticTexts()[0].value();
     
            if(firstText==secondText) {   
                UIALogger.logPass("The details are shown1");
            } else {
                UIALogger.logFail("The details are not shown1. REF=" + firstText + " RT=" + secondText);
            }
     
        
        target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].tap();
        
     
        target.delay(2);
     
        var thirdText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].staticTexts()[0].value();
     
        if(firstText==thirdText)
     
            UIALogger.logPass("The details are not shown2");
     
        else
            
            UIALogger.logFail("The details are still shown2. REF=" + firstText + " RT=" + thirdText);
     
    }

    // verify only 1 annoucement show at a time
    var Things = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells();
    var len = Things.length;
    for (var i = 0; i < Things.length; i++) {
        var firstText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].staticTexts()[0].value();

        
        target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].tap();
        target.delay(2);
        if (Things.length == len + 1) {
            UIALogger.logPass("Annoucement # " + i + " lenght is correct");   
        } else {
            UIALogger.logFail("Annoucement # " + i + " lenght is not correct. REF=" +len+ " RT=" +Things.length);
        }
        var secondText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].staticTexts()[0].value();

        if(firstText==secondText) {   
            UIALogger.logPass("Annoucement # " + i + "The details are shown");
        } else {
            UIALogger.logFail("Annoucement # " + i + "The details are not shown. REF=" + firstText + " RT=" + secondText);
        }


    };
     
    // verify only 1 annoucement show at a time in case tab on the ann 2 times.
    var Things = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells();
    var len = Things.length;
    for (var i = Things.length - 1; i >= 0; i--) {
        var firstText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].staticTexts()[0].value();
        target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].tap();
        target.delay(2);
        if (Things.length == len + 1) {
            UIALogger.logPass("Annoucement # " + i + " lenght is correct");   
        } else {
            UIALogger.logFail("Annoucement # " + i + " lenght is not correct. REF=" +len+ " RT=" +Things.length);
        }

        target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].tap(); 

        if (Things.length == len) {
            UIALogger.logPass("Annoucement # " + i + " lenght is correct");   
        } else {
            UIALogger.logFail("Annoucement # " + i + " lenght is not correct. REF=" +len+ " RT=" +Things.length);
        } 

        var secondText = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[i].staticTexts()[0].value();


        if(firstText==secondText) {   
            UIALogger.logPass("Annoucement # " + i + "The details are shown");
        } else {
            UIALogger.logFail("Annoucement # " + i + "The details are not shown. REF=" + firstText + " RT=" + secondText);
        } 
    };

     
    target.frontMostApp().mainWindow().buttons()["btn web back"].tap();
    openMenu(0);

}
