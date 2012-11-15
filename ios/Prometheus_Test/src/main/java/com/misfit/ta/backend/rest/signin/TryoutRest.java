package com.misfit.ta.backend.rest.signin;

import com.misfit.ta.backend.data.SignInData;

public class TryoutRest extends SignInRest {
    public TryoutRest(SignInData userData) {
        // parent constructor
        super(userData);
        apiUrl = "tryout/";
    }
    
    @Override
    public void formatRequest() {
        SignInData data = (SignInData) requestObj;
        params.add("email", data.email);
//        params.add("password", data.password);
    }
}
