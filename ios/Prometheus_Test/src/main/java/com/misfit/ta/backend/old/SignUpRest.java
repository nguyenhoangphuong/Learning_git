package com.misfit.ta.backend.old;


public class SignUpRest extends SignInRest {
    public SignUpRest(SignInData userData) {
        // parent constructor
        super(userData);
        apiUrl = "signup/";
    }
}
