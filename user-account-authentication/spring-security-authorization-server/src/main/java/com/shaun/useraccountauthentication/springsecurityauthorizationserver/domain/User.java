package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain;

public class User {

    private String username;
    private String password;
    private Byte enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }
}
