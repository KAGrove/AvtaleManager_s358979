package com.example.avtalemanager_s358979;

import static com.example.avtalemanager_s358979.MinSendService.CHANNEL_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

public class MinPeriodisk extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Bygg og start forgrunnsvarslingen
        Notification notification = buildForegroundNotification();
        startForeground(MinSendService.NOTIFICATION_ID, notification);

        // Sett opp AlarmManager for å starte MinSendService hver dag klokken 06:00 som standard
        setDailyAlarm();

        return START_STICKY;
    }

    private void setDailyAlarm() {
        // Hent SharedPreferences for å lese innstillinger
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String timeString = prefs.getString("sms_time", "06:00");
        String[] timeParts = timeString.split(":");

        // Sett kalender til å utløse alarmen på det valgte tidspunktet
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        calendar.set(Calendar.SECOND, 0);

        // Hvis tiden allerede har passert for i dag, sett alarmen til å starte neste dag
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(this, MinSendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        }
    }


    private Notification buildForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setDescription("This channel is used by MinPeriodisk service");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service is running in background")
                .setContentText("Doing some work...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }
}
