package com.example.avtalemanager_s358979;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MinSendService extends Service {

    private static final String TAG = "MinSendService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MinSendService er kalt.");

        // TODO: Legg til faktisk funksjonalitet her i fremtiden

        return START_NOT_STICKY;
    }
}
