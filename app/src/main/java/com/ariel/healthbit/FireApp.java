package com.ariel.healthbit;

import android.app.Application;
import com.firebase.client.Firebase;
import com.ariel.healthbit.R;

public class FireApp extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
