package com.example.jobboard.dto;

public class AuthRequest {

    private String username; 
    private String password;
    private String email;
    private String role; // 👈 add this

    public AuthRequest() {
    }

    // you can include role here too if you want
    public AuthRequest(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {           // 👈 add getter
        return role;
    }

    public void setRole(String role) {  // 👈 add setter
        this.role = role;
    }
}
