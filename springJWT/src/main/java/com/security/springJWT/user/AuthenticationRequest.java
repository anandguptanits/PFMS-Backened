package com.security.springJWT.user;

public class AuthenticationRequest {

    private String userEmail;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(AuthenticationRequest req) {
        this.userEmail = req.userEmail;
        this.password = req.password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
