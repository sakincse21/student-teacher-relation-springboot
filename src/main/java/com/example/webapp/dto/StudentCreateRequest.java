package com.example.webapp.dto;

public class StudentCreateRequest {

    private String name;
    private String roll;

    // login info (optional)
    private String username;
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoll() { return roll; }
    public void setRoll(String roll) { this.roll = roll; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
