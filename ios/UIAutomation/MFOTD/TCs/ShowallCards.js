#import "testcases.js"

test("ShowAllCards", function(target, app) {
	 for (var count =  120; count >= 0; count--) {
	 	tabOnCard();
    	target.delay(0.5);
    	tabOnCard();
    	//target.delay(1);
    	target.dragFromToForDuration({x:137.00, y:170.00}, {x:657.00, y:572.50}, 0.5);
    	//target.delay(1);
	 }
});



