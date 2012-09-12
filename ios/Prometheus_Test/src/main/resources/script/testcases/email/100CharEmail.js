#import "ValidEmailValidation.js"

var s = "";
for (i=0 ; i < 100; i++)
	s = s + "a";
s = s + "@one-two.com";
checkValidEmail(s,"DashDomain");