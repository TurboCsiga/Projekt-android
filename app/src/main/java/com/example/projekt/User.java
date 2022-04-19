package com.example.projekt;

public class User {
    private int id;
    private String name;
    private String email;
    private int is_admin;
    private int gun_authorized;

    public User(int id, String name, String email, int is_admin, int gun_authorized) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.is_admin = is_admin;
        this.gun_authorized = gun_authorized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public int getGun_authorized() {
        return gun_authorized;
    }

    public void setGun_authorized(int gun_authorized) {
        this.gun_authorized = gun_authorized;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", is_admin=" + is_admin +
                ", gun_authorized=" + gun_authorized +
                '}';
    }
}
