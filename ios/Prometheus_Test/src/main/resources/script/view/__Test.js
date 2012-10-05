#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "../core/testcaseBase.js"


start("Demo.");
nav.toRunView(null, null, {age: 35, w1: 80, w2: ".5", wu:"kg", h1: 1, h2: ".65", hu: "meter", sex: "female", unit: "si"}, "Running", 22);


pass("Demo pass");
