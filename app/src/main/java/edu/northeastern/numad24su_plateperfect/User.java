package edu.northeastern.numad24su_plateperfect;

public class User {
    String firstName;
    String lastName;
    String username;

    public User() {
        //default constructor
    }

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
