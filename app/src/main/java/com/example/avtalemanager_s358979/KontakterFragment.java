package com.example.avtalemanager_s358979;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

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
        visalle = view.findViewById(R.id.visalle);

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
        final String fornavn = editTextFornavn.getText().toString().trim();
        final String etternavn = editTextEtternavn.getText().toString().trim();

        class SaveKontakt extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Kontakt kontakt = new Kontakt();
                kontakt.setFirstName(fornavn);
                kontakt.setLastName(etternavn);
                DatabaseClient.getInstance(getActivity().getApplicationContext())
                        .getAppDatabase()
                        .kontaktDao()
                        .insert(kontakt);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity().getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveKontakt st = new SaveKontakt();
        st.execute();
    }

    private void showKontakt() {
        class ShowKontakt extends AsyncTask<Void, Void, List<Kontakt>> {
            @Override
            protected List<Kontakt> doInBackground(Void... voids) {
                List<Kontakt> alle = DatabaseClient.getInstance(getActivity().getApplicationContext())
                        .getAppDatabase()
                        .kontaktDao()
                        .getAll();
                return alle;
            }

            @Override
            protected void onPostExecute(List<Kontakt> alle) {
                super.onPostExecute(alle);
                Toast.makeText(getActivity().getApplicationContext(), "Vist", Toast.LENGTH_LONG).show();
                String tekst = "";
                for (Kontakt bruker : alle) {
                    tekst += "Fornavn: " + bruker.getFirstName() + " ,Etternavn: " + bruker.getLastName();
                }
                visalle.setText(tekst);
            }
        }

        ShowKontakt sk = new ShowKontakt();
        sk.execute();
    }

}
