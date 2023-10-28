package com.example.avtalemanager_s358979;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MinBroadcastReceiver extends BroadcastReceiver {

    public MinBroadcastReceiver() {
        // Constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("I MinBroadcastReceiver", "onReceive");
        Toast.makeText(context, "I BroadcastReceiver", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, MinPeriodisk.class);
        context.startService(i);
    }
}
