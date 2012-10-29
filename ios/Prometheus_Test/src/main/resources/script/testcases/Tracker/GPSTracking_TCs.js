#import "GPSTracking_Funcs.js"

//======================== Test logic ======================== //
start("Test GPS Tracking");

//navigate
nav.toGPSTracking(null, null, null, pinfonormal, null);

// This test covers: - Do an activity w/ GPS tracking, test the functionality of
// buttons while tracking
testGPSTracking();

//if pass
pass("End: Test GPS Tracking");