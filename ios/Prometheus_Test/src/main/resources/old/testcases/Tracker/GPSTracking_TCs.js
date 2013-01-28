#import "GPSTracking_Funcs.js"

//======================== Test logic ======================== //
start("Test GPS Tracking");

//navigate
nav.toGPSTracking(null, null, null, pinfonormal, null);

// This test covers: - Do an activity w/ GPS tracking, test the functionality of
// buttons while tracking
wait(2);
log("Wait for 2 seconds to make sure the view is available");
testGPSTracking();

//if pass
pass("End: Test GPS Tracking");