#import "settings_funcs.js"


start("Settings: EmailSupport, ResetPlan");
// --------------------------------------------------------
GoToSettingScreen();
VerifyEmailSupportWithoutAccount();
VerifyResetButton();
//--------------------------------------------------------
pass("Settings: EmailSupport, ResetPlan");