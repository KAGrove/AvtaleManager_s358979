package com.example.avtalemanager_s358979;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import java.util.List;

public class MinSendService extends Service {

    private static final String TAG = "MinSendService";
    private AppDatabase db;
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "MinKanal"; // Behold denne linjen

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialiser databasen
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "MyUsers").build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MinSendService er kalt.");

        // Hent telefonnumre fra databasen
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Kontakt> kontakter = db.kontaktDao().getAll(); // Forutsetter at du har en DAO-metode som heter getAllKontakter
                for (Kontakt kontakt : kontakter) {
                    Log.d(TAG, "Telefonnummer: " + kontakt.getPhoneNumber());
                }
            }
        }).start();

        Log.d("I MinSendService", "før Toast");
        Toast.makeText(getApplicationContext(), "I MinSendService", Toast.LENGTH_SHORT).show();
        Log.d("I MinSendService", "etter Toast");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, Resultat.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        Notification notifikasjon = new NotificationCompat.Builder(this, "MinKanal")
                .setContentTitle("MinNotifikasjon")
                .setContentText("Tekst")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent)
                .build();

        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(88, notifikasjon);

        startForeground(NOTIFICATION_ID, notifikasjon);

        return START_NOT_STICKY;
    }

}

