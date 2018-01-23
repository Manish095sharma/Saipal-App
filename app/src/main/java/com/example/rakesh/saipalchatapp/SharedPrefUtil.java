package com.example.rakesh.saipalchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Rakesh on 12/12/2017.
 */

public class SharedPrefUtil {

    private static final String APP_PREFS = "reference";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SharedPrefUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void saveString(String key,String value)
    {
        mSharedPreferences=mContext.getSharedPreferences(APP_PREFS,Context.MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        mEditor.putString(key,value);
        mEditor.commit();
    }


    public String getString(String key, String defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, defaultValue);
    }

    public String getString(String key)
    {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Log.d("oooo",""+key);
        Log.d("oooo",""+mSharedPreferences.getString(key, null));
        return mSharedPreferences.getString(key, null);

    }
}
