package com.example.coach5;

public class Coach {

    public String finalAccountType, name, surname, age, email, football, basketball, tennis, footballSkill, basketballSkill, tennisSkill, location, price;

    public Coach() {

    }

    public Coach(String finalAccountType, String name, String surname, String age, String email, String football, String basketball, String tennis, String footballSkill, String basketballSkill, String tennisSkill, String location, String price) {
        this.finalAccountType = finalAccountType;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.football = football;
        this.basketball = basketball;
        this.tennis = tennis;
        this.footballSkill = footballSkill;
        this.basketballSkill = basketballSkill;
        this.tennisSkill = tennisSkill;
        this.location = location;
        this.price = price;
    }

}
