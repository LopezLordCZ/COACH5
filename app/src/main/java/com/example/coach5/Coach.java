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

    public String getSport1() { return sport1; }
    public String getSport2() { return sport2; }
    public String getSport3() { return sport3; }
    public String getSkill1() { return sport1Skill; }
    public String getSkill2() { return sport2Skill; }
    public String getSkill3() { return sport3Skill; }
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getRate() { return price; }

    public Boolean searchCondition(String str) {
        Boolean result = false;
        if (this.getName().toLowerCase().contains(str)) {
            result = true;
        }
        if (this.getSport1().toLowerCase().contains(str)) {
            result = true;
        }
        if (this.getSport2().toLowerCase().contains(str)) {
            result = true;
        }
        if (this.getSport3().toLowerCase().contains(str)) {
            result = true;
        }
        if (this.getRate().toLowerCase().contains(str)) {
            result = true;
        }
        return result;
    }


}
