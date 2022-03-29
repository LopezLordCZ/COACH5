package com.example.coach5;

public class Coach {

    public String finalAccountType, name, surname, age, email, sport, rate;

    public Coach() {

    }

    public Coach(String finalAccountType, String name, String surname, String age, String email) {
        this.finalAccountType = finalAccountType;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.sport = "none";
        this.rate = "none";
    }

    public String getSport() {
        return sport;
    }
    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public String getRate() {
        return rate;
    }


}
