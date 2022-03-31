package com.example.coach5;

public class Message {

    public String sender, receiver, message;

    public Message (String senderID, String receiverID, String message){
        this.sender = senderID;
        this.receiver = receiverID;
        this.message = message;
    }

}
