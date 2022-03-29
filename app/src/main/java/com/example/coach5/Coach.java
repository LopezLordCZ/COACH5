package com.example.coach5;

public class Coach {

    public String finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location, price;

    public Coach() {

    }

    public Coach(String finalAccountType, String name, String surname, String age, String email, String sport1, String sport2, String sport3, String sport1Skill, String sport2Skill, String sport3Skill, String location, String price) {
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
        this.price = price;
    }

    public String getSport() {
        return sport1;
    }
    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public String getRate() {
        return price;
    }


}
