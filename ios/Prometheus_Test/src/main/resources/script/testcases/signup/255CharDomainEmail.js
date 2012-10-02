#import "ValidEmailValidation.js"

var s = "";
for (i=0 ; i < 250; i++)
	s = s + "a";
s = "username@" + s +".com";
checkValidEmail(s,"255 Chars in domain");