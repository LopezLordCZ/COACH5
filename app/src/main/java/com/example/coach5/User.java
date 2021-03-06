package com.example.coach5;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    //Definition
    public String finalAccountType, name, surname, age, email, sport1, sport2, sport3, sport1Skill, sport2Skill, sport3Skill, location;
    public Double lat, lng;

    //User call
    public User() {
    }

    //User creation
    public User(String finalAccountType, String name, String surname, String age, String email, String sport1, String sport2, String sport3, String sport1Skill, String sport2Skill, String sport3Skill, String location, Double lat, Double lng) {
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
        this.lat = lat;
        this.lng = lng;

    }

    //Call to get user name
    public String getName() { return name; }
    //Call to get latitude
    public Double getLat() { return lat; }
    //Call to get longitude
    public Double getLng() { return lng; }


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
        result.put("lat", lat);
        result.put("lng", lng);

        return result;
    }

}
