package com.example.avtalemanager_s358979;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReveicer", "forkjolelse");

        // Sjekk om preferansen for tjenesten er satt til ON
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isServiceEnabled = preferences.getBoolean("sms_service_key", false);

        if (isServiceEnabled) {
            Intent serviceIntent = new Intent(context, MinSendService.class);
            context.startService(serviceIntent);
        }
    }
}
