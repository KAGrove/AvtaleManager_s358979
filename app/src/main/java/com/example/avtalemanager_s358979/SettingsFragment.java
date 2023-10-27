package com.example.avtalemanager_s358979;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference smsPreference = findPreference("sms_service_key");
        Log.d(TAG, "SmsPreferanse: " + smsPreference);
        if (smsPreference != null) {
            smsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((boolean) newValue) {
                        Log.d(TAG, "Preferanse endret til TRUE. Starter tjenesten...");
                        // Start tjenesten
                        settPeriodisk();
                    } else {
                        Log.d(TAG, "Preferanse endret til FALSE. Stopper tjenesten...");
                        // Stopp tjenesten
                        stoppPeriodisk();
                    }
                    return true;
                }
            });
        }

    }

    public void settPeriodisk() {
        Log.d(TAG, "settPeriodisk kalt. Forsøker å starte tjenesten MinPeriodisk...");
        Intent intent = new Intent(getActivity(), MinPeriodisk.class);
        getActivity().startService(intent);
    }

    public void stoppPeriodisk() {
        Log.d(TAG, "stoppPeriodisk kalt. Forsøker å stoppe tjenesten MinPeriodisk...");
        Intent i = new Intent(getActivity(), MinSendService.class);
        PendingIntent pintent = PendingIntent.getService(getActivity(), 0, i, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.cancel(pintent);
        }
    }


}


