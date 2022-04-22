package com.example.coach5;

import java.io.Serializable;

public class Message implements Serializable {

    //Definition
    public String sender, receiver, message;

    //Call message
    public Message (){

    }

    //New message creation
    public Message (String senderID, String receiverID, String message){
        this.sender = senderID;
        this.receiver = receiverID;
        this.message = message;
    }

}
