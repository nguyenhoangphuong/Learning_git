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

log("2. Verify date list and activity list");
VerifyDateRange();
VerifyActivityList(pactivities);

log("3. Verify default percentage");
VerifyDefaultPercent();

log("4. Verify default current records values");
VerifyDefaultRecord(pactivities);

// add some records
for(var a = 0; a < pactivities.length; a++)
	AddRecord(pactivities[a]);

log("5. Check activities' progress is updated after tracking");
VerifyProgressIsUpdated();

log("6. Check current records is updated after tracking");
VerifyRecordsAreUpdated();

// ------------------------------------
pass();