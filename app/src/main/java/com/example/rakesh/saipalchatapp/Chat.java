package com.example.rakesh.saipalchatapp;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Rakesh on 6/2/2017.
 */

@IgnoreExtraProperties
public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public String timestamp;

    public Chat() {
    }

    public Chat(String sender_uid, String receiver_uid, String receiver_name, String message, String timestamp) {
     //   this.senderUid = sendar_name;
        this.receiverUid = receiver_uid;
        this.senderUid=sender_uid;
        this.receiver=receiver_name;
        this.message = message;
        this.timestamp = timestamp;
    }
}