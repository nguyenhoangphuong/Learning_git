#import "progress_funcs.js"

// ======================= Test Data ==============================
pinfo = 
{
		type : "New",
		name : "Test Plan",
		activities :
		[
		 	{name: "Running", value: 10}, 
		 	{name: "Swimming", value: 550}
		]
}

pactivities = ["Running", "Swimming"];

// ======================= Test Logic =============================

start("Progress testcases");
// ------------------------------------

log("1. To Progress view");
GoToProgress(pinfo);

log("2. Check activities' progress for each day");
VerifyNoActivityProgress(pactivities);

log("3. Check current records is No activity");
VerifyNoActivityRecord(pactivities);

// add some records
for(var a = 0; a < pactivities.length; a++)
	AddRecord(pactivities[a]);

log("4. Check activities' progress is updated after tracking");
VerifyProgressIsUpdated();

log("5. Check current records is updated after tracking");
VerifyRecordsAreUpdated();

// ------------------------------------
pass();