package com.example.coach5;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Match  implements Serializable {

    public String userID, coachID, userName, coachName;
    public ArrayList<Message> messages;

    public Match() {

    }

    public Match(String userID, String coachID, String userName, String coachName) {
        this.userID = userID;
        this.coachID = coachID;
        this.userName = userName;
        this.coachName = coachName;
    }

    public Match(String userID, String coachID, String userName, String coachName, ArrayList<Message> messages) {
        this.userID = userID;
        this.coachID = coachID;
        this.userName = userName;
        this.coachName = coachName;
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
    public String getUserName() { return userName; }
    public String getCoachName() { return coachName; }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("coachID", coachID);
        result.put("userName", userName);
        result.put("coachName", coachName);
        result.put("Messages", messages);
        return result;
    }

}
