package com.misfit.ta.backend.data;

public class AuthToken {
    public String token;
    public String type;

    public AuthToken(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
