#import "signupFuncs.js"



start("StartScreen: Login backend verification");


//var genstring = ;
//navigate
nav.toHome();

//check
checkValidEmail("email1@one-two.com","DashDomain");
checkValidEmail("email-user@onetwo.com","Dash Username");
checkValidEmail("123456@domain.com","Digit Username");
checkValidEmail("email@sub.domain.com","DotSub Domain");
checkValidEmail("email.user@domain.com","DotUsername");
checkValidEmail("email_user@domain.com","Underscores Username");
checkValidEmail("العربية@Báocáohìnhảnh.com","Unicode domain");
checkValidEmail("áohìnhảnh@domain.com","Unicode Username");

// check 255 char in username
var s = "";
for (i=0 ; i < 250; i++)
	s = s + "a";
s = "username@" + s +".com";
checkValidEmail(s,"255 Chars in domain");

//check 64 char in password
s = "";
for (i=0 ; i < 64; i++)
	s = s + "a";
s = s + "@onetwo.com";
checkValidEmail(s,"64 chars in local part");
wait(10);


//if pass
pass("StartScreen: Login backend verification");