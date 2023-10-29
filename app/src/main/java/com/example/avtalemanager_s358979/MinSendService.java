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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                AvtaleDao avtaleDao = db.avtaleDao();
                DeltakelseDao deltakelseDao = db.deltakelseDao();
                KontaktDao kontaktDao = db.kontaktDao();

                // Anta at vi får dagens dato her
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Hent alle avtaler for dagens dato
                // List<Avtale> dagensAvtaler = avtaleDao.getAvtaleKontakterForDato(today);

                List<Avtale> alleAvtaler = avtaleDao.getAll();

                for (Avtale avtale : alleAvtaler) {
                    // Hent deltakelser for hver avtale
                    List<Deltakelse> deltakelser = deltakelseDao.getDeltakelserForAvtale(avtale.avtaleId);

                    for (Deltakelse deltakelse : deltakelser) {
                        // For hver deltakelse, finn den tilsvarende kontakten
                        Kontakt kontakt = kontaktDao.getKontaktById(deltakelse.kid);
                        Log.d(TAG, "Dato: " + avtale.dato + ", Kontakt: " + kontakt.phoneNumber);
                        // Legg til logikk for å sende SMS eller notifikasjon
                    }
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

