package com.misfit.ta.backend.aut.performance;

public class UserToken {
    String id;
    String username; 
    String token;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    public String toString() {
        return id + " - " + token; 
    }
}
