package com.example.coach5;

import java.io.Serializable;

public class Message implements Serializable {

    public String sender, receiver, message;

    public Message (){

    }

    public Message (String senderID, String receiverID, String message){
        this.sender = senderID;
        this.receiver = receiverID;
        this.message = message;
    }

}
