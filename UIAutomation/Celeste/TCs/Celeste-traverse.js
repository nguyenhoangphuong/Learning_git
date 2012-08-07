#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"


var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("Celeste Traverse", function(target, app) {
     
target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["Sponsored by Misfit"].tapWithOptions({tapOffset:{x:0.39, y:0.55}});
target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Maps"].tap();
target.frontMostApp().navigationBar().leftButton().tap();
target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
target.frontMostApp().navigationBar().leftButton().tap();
target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
target.frontMostApp().navigationBar().leftButton().tap();
target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Sponsors"].tap();
target.frontMostApp().navigationBar().leftButton().tap();
target.frontMostApp().mainWindow().pageIndicators()[0].selectPage(0);
target.frontMostApp().mainWindow().pageIndicators()[0].selectPage(1);
target.frontMostApp().mainWindow().pageIndicators()[0].selectPage(2);
target.frontMostApp().mainWindow().pageIndicators()[0].selectPage(1);
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.72, y:0.07}, endOffset:{x:0.04, y:0.06}});
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.72, y:0.07}, endOffset:{x:0.04, y:0.06}});
target.frontMostApp().mainWindow().pageIndicators()[0].selectPage(2);
     
});






