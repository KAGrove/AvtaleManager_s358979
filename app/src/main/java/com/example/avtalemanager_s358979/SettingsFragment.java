package com.example.avtalemanager_s358979;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference smsPreference = findPreference("sms_service_key");
        Preference smsTimePreference = findPreference("sms_time");
        EditTextPreference smsDefaultMessagePreference = findPreference("sms_default_message");

        // Lytt etter endringer i SMS-tjenesten (på/av)
        if (smsPreference != null) {
            smsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isOn = (boolean) newValue;
                Log.d(TAG, "SMS-tjeneste " + (isOn ? "på" : "av") + ".");
                if (isOn) {
                    settPeriodisk();
                } else {
                    stoppPeriodisk();
                }
                return true;
            });
        }

        // Lytt etter endringer i tidspunkt-preferansen
        if (smsTimePreference != null) {
            smsTimePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "SMS-tidspunkt endret til: " + newValue);
                stoppPeriodisk();  // Stopper først tjenesten
                settPeriodisk();   // Starter tjenesten på nytt med oppdatert tid
                return true;
            });
        }

        // Lytt etter endringer i standard SMS-meldingspreferansen
        if (smsDefaultMessagePreference != null) {
            smsDefaultMessagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String newDefaultMessage = (String) newValue;
                Log.d(TAG, "Standard SMS-melding endret til: " + newDefaultMessage);
                stoppPeriodisk();  // Stopper først tjenesten
                settPeriodisk();   // Starter tjenesten på nytt med oppdatert tid
                return true; // Return true for å oppdatere verdien i brukergrensesnittet
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
        // Opprett en Intent som matcher den som brukes for å sette alarmen i MinPeriodisk
        Intent i = new Intent(getActivity(), MinPeriodisk.class);
        // Pass på at requestCode og flags matcher de som ble brukt for å sette opp PendingIntent
        PendingIntent pintent = PendingIntent.getService(getActivity(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            // Bruk den matchende PendingIntent for å kansellere alarmen
            alarm.cancel(pintent);
        }

        // Stopper MinPeriodisk tjenesten
        getActivity().stopService(new Intent(getActivity(), MinPeriodisk.class));
    }

}