package com.example.coach5;

public class User {

    public String finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location;

    public User() {

    }

    public User(String finalAccountType, String name, String surname, String age, String email, String sport1, String sport2, String sport3, String sport1Skill, String sport2Skill, String sport3Skill, String location) {
        this.finalAccountType = finalAccountType;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.sport1 = sport1;
        this.sport2 = sport2;
        this.sport3 = sport3;
        this.sport1Skill = sport1Skill;
        this.sport2Skill = sport2Skill;
        this.sport3Skill = sport3Skill;
        this.location = location;
    }

}
