package com.example.avtalemanager_s358979;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class KontakterFragment extends Fragment {
    View view;
    Button btnLagre, btnVis;
    EditText editTextFornavn, editTextEtternavn;
    TextView visalle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kontakter, container, false);

        // Initierer elementene
        editTextFornavn = view.findViewById(R.id.fornavn);
        editTextEtternavn = view.findViewById(R.id.etternavn);
        visalle = view.findViewById(R.id.vis);

        btnLagre = view.findViewById(R.id.lagre);
        btnLagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveKontakt();
            }
        });

        btnVis = view.findViewById(R.id.vis);
        btnVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKontakt();
            }
        });

        return view;
    }

    // Metoder for å lagre og vise kontakt
    private void saveKontakt() {
        // Implementer koden her for å lagre kontakten
    }

    private void showKontakt() {
        // Implementer koden her for å vise kontakten
    }
}
