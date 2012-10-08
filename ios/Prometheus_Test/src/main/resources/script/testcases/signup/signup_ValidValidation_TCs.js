#import "signupFuncs.js"
#import "../../core/common.js"


//======================== Test logic ======================== //
start("SignUpScreen: Valid email validation");



var genstring = generateRandomDigitString();

nav.toHome();
//check
checkValidEmail("email" + genstring+ "@one-two.com","DashDomain");
wait();
checkValidEmail("email-user" + genstring+ "@onetwo.com","Dash Username");
wait();
checkValidEmail("123456" + genstring+ "@domain.com","Digit Username");
wait();
checkValidEmail("email" + genstring+ "@sub.domain.com","DotSub Domain");
wait();
checkValidEmail("email.user" + genstring+ "@domain.com","DotUsername");
wait();
checkValidEmail("email_user" + genstring+ "@domain.com","Underscores Username");
wait();
checkValidEmail("العربية" + genstring+ "@Báocáohìnhảnh.com","Unicode domain");
wait();
checkValidEmail("áohìnhảnh" + genstring+ "@domain.com","Unicode Username");
wait();
// check 255 char in username
var s = "";
for (i=0 ; i < 230; i++)
	s = s + "a";
s = "username@" + s +".com";
checkValidEmail(s + genstring,"255 Chars in domain");
wait();
//check 64 char in password
s = "";
for (i=0 ; i < 44; i++)
	s = s + "a";
s = s + "@onetwo.com";
checkValidEmail(s + genstring,"64 chars in local part");
wait();


//if pass
pass("SignUpScreen: Valid email validation");