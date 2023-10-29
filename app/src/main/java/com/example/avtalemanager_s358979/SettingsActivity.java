package com.example.avtalemanager_s358979;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Starter en Fragment-transaksjon ved å hente FragmentManager, som lar deg legge til, erstatte eller utføre andre operasjoner med Fragments i denne aktiviteten.
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()) // Erstatter det nåværende innholdet i aktiviteten med en ny instans av SettingsFragment.
                .commit(); // Utfører transaksjonen, dette bekrefter endringene som ble lagt til i transaksjonen.
    }
}
