package com.example.rakesh.saipalchatapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Rakesh on 10/17/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String token;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("Token","by service "+token);
    }


}
