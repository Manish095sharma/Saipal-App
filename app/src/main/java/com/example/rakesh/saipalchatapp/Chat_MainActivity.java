package com.example.rakesh.saipalchatapp;

import android.app.Application;

/**
 * Created by Rakesh on 6/2/2017.
 */

public class Chat_MainActivity extends Application {


    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        Chat_MainActivity.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

