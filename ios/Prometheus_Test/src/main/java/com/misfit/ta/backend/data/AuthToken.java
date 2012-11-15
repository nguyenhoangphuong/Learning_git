package com.misfit.ta.backend.data;

public class AuthToken {
    public String token;
    public String type;
    public SyncData syncData;

    public AuthToken(String token, String type, SyncData data) {
        this.token = token;
        this.type = type;
        this.syncData = data;
    }
}
