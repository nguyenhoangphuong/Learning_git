#import "planBuider_funcs.js";

start("Custom Plan Screen: Start");

GoToPlanBuilder()
hr();
VerifyNameNotNull();
hr();
VerifyAddActivity();
hr();
VerifyRemoveActivity();

pass("Custom Plan Screen: End");