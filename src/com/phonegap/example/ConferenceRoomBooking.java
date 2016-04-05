package com.phonegap.example;


import android.os.Bundle;
import com.phonegap.*;
/**
 * Called when the app starts and loads the desired url
 * 
 *
 */
public class ConferenceRoomBooking extends DroidGap
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/login.html");
    }
}

