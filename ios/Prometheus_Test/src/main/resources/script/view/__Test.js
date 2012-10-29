#import "MVPLibs.js"


start("Demo");

m = new Music();

hr();
m.switchMusic();
m.isSmartDJHidden();
m.isShuffleHidden();

hr();
m.switchMusic(false);
m.isSmartDJHidden();
m.isShuffleHidden();

hr();
m.switchSmartDJ();
m.isSmartDJHidden();
m.isShuffleHidden();

hr();
m.switchSmartDJ(false);
m.isSmartDJHidden();
m.isShuffleHidden();

hr();
m.start();

pass("Demo pass");
