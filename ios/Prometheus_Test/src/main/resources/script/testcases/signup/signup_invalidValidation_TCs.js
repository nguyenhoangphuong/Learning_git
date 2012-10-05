#import "signupFuncs.js"

//======================== Test logic ======================== //
start("SignUpScreen: Validate invalid case");


//var genstring = ;

//navigate

nav.toHome();

//check

checkEmptyEmail();
checkInvalidEmail("@domain.com");
checkInvalidEmail("email@@domain.com");
checkInvalidEmail(".email@domain.com");
checkInvalidEmail("email.@domain.com");
checkInvalidEmail("email@domain");
checkInvalidEmail("email@domain..com");
checkInvalidEmail("email@***$$$.com");
checkInvalidEmail("em%^ail@domain.com");
checkInvalidEmail("email@-domain.com");
checkInvalidEmail("emaildomain.com");
checkInvalidEmail("email@.com");
checkInvalidEmail("$$$$$$"); 
checkEmptyPassword();
checkDuplicatedUser();

//if pass
pass("SignUpScreen: Validate invalid case");

