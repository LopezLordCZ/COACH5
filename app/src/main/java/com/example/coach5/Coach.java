package com.example.coach5;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Coach {

    //Definition
    public String finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location, price;
    public Double lat, lng;

    //User call
    public Coach() {

    }

    //User creation
    public Coach(String finalAccountType, String name, String surname, String age, String email, String sport1, String sport2, String sport3, String sport1Skill, String sport2Skill, String sport3Skill, String location, String price, Double lat, Double lng) {
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
        this.lat = lat;
        this.lng = lng;
    }

    //Hashmap to use for profile edit
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("finalAccountType", finalAccountType);
        result.put("name", name);
        result.put("surname", surname);
        result.put("age", age);
        result.put("email", email);
        result.put("sport1", sport1);
        result.put("sport2", sport2);
        result.put("sport3", sport3);
        result.put("sport1Skill", sport1Skill);
        result.put("sport2Skill", sport2Skill);
        result.put("sport3Skill", sport3Skill);
        result.put("location", location);
        result.put("price", price);
        result.put("lat", lat);
        result.put("lng", lng);

        return result;
    }

    //Call for attributes
    public String getSport1() { return sport1; }
    public String getSport2() { return sport2; }
    public String getSport3() { return sport3; }
    public String getSkill1() { return sport1Skill; }
    public String getSkill2() { return sport2Skill; }
    public String getSkill3() { return sport3Skill; }
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getRate() { return price; }
    public String getLocation() { return location; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }


    //Search criteria
    public Boolean searchCondition(String str) {
        Boolean result = false;
        //Name
        if (this.getName().toLowerCase().contains(str)) {
            result = true;
        }
        //Sport1
        if (this.getSport1().toLowerCase().contains(str)) {
            result = true;
        }
        //Sport2
        if (this.getSport2().toLowerCase().contains(str)) {
            result = true;
        }
        //Sport3
        if (this.getSport3().toLowerCase().contains(str)) {
            result = true;
        }
        //Rate
        if (this.getRate().toLowerCase().contains(str)) {
            result = true;
        }
        //Skill1
        if (this.getSkill1().toLowerCase().contains(str)) {
            result = true;
        }
        //Skill2
        if (this.getSkill2().toLowerCase().contains(str)) {
            result = true;
        }
        //Skill3
        if (this.getSkill3().toLowerCase().contains(str)) {
            result = true;
        }

        return result;
    }


}
