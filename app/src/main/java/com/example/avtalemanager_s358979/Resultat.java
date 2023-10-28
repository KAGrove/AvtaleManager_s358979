package com.example.avtalemanager_s358979;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Resultat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("I Resultat", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);
    }
}
