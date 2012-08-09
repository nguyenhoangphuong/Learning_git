function findFirstUnraisedSession(target, app) {

    var cells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells();
    for (i = 0 ; i < cells.length ; i++)
    {
        cells[i].tap();
        var tag = target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating_new.png"].buttons()[0].name();
        if (tag == "Agenda list STAR ON")
        {
            target.frontMostApp().navigationBar().leftButton().tap();
        }
        else
        {
            return i;
            target.frontMostApp().navigationBar().leftButton().tap();
            target.frontMostApp().navigationBar().leftButton().tap();        
        }
     
    }
}