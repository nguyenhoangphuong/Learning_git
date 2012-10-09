#import "history_func.js"

// =================== Test logic =======================
start("History testcases");

// no history
initStartView();
verifyNoHistory();

// added some records
goFromHistoryToStart();


pass("History testcases");
