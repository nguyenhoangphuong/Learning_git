#import "settings_funcs.js"


start("Settings: UserProfile, Rate, EmailSupport, ResetPlan");
// --------------------------------------------------------
GoToSettingScreen();
VerifyUserProfileButton();
VerifySupportButton();
VerifyEmailSupportWithoutAccount();
VerifyResetButton();
//--------------------------------------------------------
pass("Settings: UserProfile, Rate, EmailSupport, ResetPlan");