package Entities;

public class User {
    private int id;
    private String email,password,passwordHashed;

    //Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getPasswordHashed() {
        return passwordHashed;
    }

    //Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Constructor

    public User() {
    }
}
