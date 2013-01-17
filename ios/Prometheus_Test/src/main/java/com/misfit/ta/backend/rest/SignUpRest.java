package com.misfit.ta.backend.rest;

import com.misfit.ta.backend.data.SignInData;

public class SignUpRest extends SignInRest {
    public SignUpRest(SignInData userData) {
        // parent constructor
        super(userData);
        apiUrl = "signup/";
    }
}
