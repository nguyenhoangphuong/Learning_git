#import "ValidEmailValidation.js"

var s = "";
for (i=0 ; i < 64; i++)
	s = s + "a";
s = s + "@onetwo.com";
checkValidEmail(s,"64 chars in local part");
wait(10);