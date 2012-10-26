#import "history_func.js"

// ========== init history =============
//logTree();
print("<Init history>");
initStartView();
verifyHistoryVisible();
verifyNoHistory();
// =====================================


// ========== reset plan ===============
print("<Verify Reset Plan>");
resetPlan("Easy", "Easy #1");
verifyNoHistory();
// =====================================



// ========== When add Records =========
print("<Verify Number Of Entries>");
addNumberOfEntryByManualTracking(5);
verifyNumberOfHistory();
// =====================================