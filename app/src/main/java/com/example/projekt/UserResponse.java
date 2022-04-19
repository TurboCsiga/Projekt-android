package com.example.projekt;

public class UserResponse {

    private User user;
    private String token;

    public UserResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                ", token='" + token + '\'' +
                '}';
    }
}
