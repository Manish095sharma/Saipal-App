package com.example.rakesh.saipalchatapp;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rakesh on 6/2/2017.
 */

public class User {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("username")
    @Expose
    private String username;

    public String contact;

    public String email;
    public String userId;
    public String firebaseToken;
    String image;
    String thumbnail_image;
    String status;

    public User() {
    }

    public User(String username, String email) {
    }

    public User(String username, String contact, String email, String userId, String token,String image,String thumb_image,String status) {
        this.username = username;
        this.contact=contact;
        this.email = email;
        this.userId = userId;
        this.firebaseToken=token;
        this.image=image;
        this.thumbnail_image=thumb_image;
        this.status=status;

    }
}
