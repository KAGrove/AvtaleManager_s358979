package com.example.avtalemanager_s358979;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.Manifest;


public class MinSendService extends Service {

    private static final String TAG = "MinSendService";
    private AppDatabase db;
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "MinKanal"; // Behold denne linjen

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

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

                // Få dagens dato
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Hent alle avtaler
                List<Avtale> alleAvtaler = avtaleDao.getAll();

                for (Avtale avtale : alleAvtaler) {
                    // Sjekk om avtalens dato er lik dagens dato
                    if (avtale.dato.equals(today)) {
                        // Hent deltakelser for hver avtale
                        List<Deltakelse> deltakelser = deltakelseDao.getDeltakelserForAvtale(avtale.avtaleId);

                        for (Deltakelse deltakelse : deltakelser) {
                            // For hver deltakelse, finn den tilsvarende kontakten
                            Kontakt kontakt = kontaktDao.getKontaktById(deltakelse.kid);
                            Log.d(TAG, "Dato: " + avtale.dato + ", Tlf: " + kontakt.phoneNumber);
                            // Sjekk om tillatelse er gitt
                            if (ContextCompat.checkSelfPermission(MinSendService.this,
                                    Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                                // Send SMS
                                String message = "Din påminnelse for avtale på " + avtale.treffsted + " den " + avtale.dato + " klokken " + avtale.klokkeslett;
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(kontakt.phoneNumber, null, message, null, null);

                                // Logikk for å sende notifikasjon om at SMS er sendt
                                sendNotification("SMS sendt", "En påminnelse ble sendt til " + kontakt.phoneNumber);
                            } else {
                                // Logikk for å sende notifikasjon om at SMS-tillatelse ikke er gitt
                                sendNotification("SMS ikke sendt", "Tillatelse til å sende SMS er ikke gitt.");
                            }
                        }
                    }
                }
            }
        }).start();


        Log.d("I MinSendService", "før Toast");
        Toast.makeText(getApplicationContext(), "I MinSendService", Toast.LENGTH_SHORT).show();
        Log.d("I MinSendService", "etter Toast");

        return START_NOT_STICKY;
    }

    private void sendNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}

