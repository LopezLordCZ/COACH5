package com.example.coach5;

public class User {

    public String finalAccountType, name, surname, age, email;

    public User() {
    }

    public User(String finalAccountType, String name, String surname, String age, String email) {
        this.finalAccountType = finalAccountType;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }




}
