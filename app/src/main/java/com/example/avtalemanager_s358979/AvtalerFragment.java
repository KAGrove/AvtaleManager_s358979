package com.example.avtalemanager_s358979;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AvtalerFragment extends Fragment {

    private List<Kontakt> kontakter;
    private ArrayAdapter<Kontakt> adapter;

    private MultiSpinner multiSpinner;

    private List<Kontakt> valgteKontakter = new ArrayList<>();

    private AvtaleDao avtaleDao;
    private DeltakelseDao deltakelseDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.avtaler, container, false);

        // Initialiser databasen
        AppDatabase db = DatabaseClient.getInstance(getActivity().getApplicationContext()).getAppDatabase();
        avtaleDao = db.avtaleDao();
        deltakelseDao = db.deltakelseDao();

        multiSpinner = view.findViewById(R.id.kontaktListe);
        new FetchContactsTask().execute();

        return view;
    }

    private class FetchContactsTask extends AsyncTask<Void, Void, List<Kontakt>> {
        @Override
        protected List<Kontakt> doInBackground(Void... voids) {
            // Initialiser databasen
            AppDatabase db = DatabaseClient.getInstance(getActivity().getApplicationContext()).getAppDatabase();
            KontaktDao kontaktDao = db.kontaktDao();

            // Hent kontakter fra databasen
            return kontaktDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Kontakt> fetchedContacts) {
            kontakter = fetchedContacts;

            adapter = new ArrayAdapter<Kontakt>(getActivity(), android.R.layout.simple_spinner_item, kontakter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            multiSpinner.setAdapter(adapter);

            // Sett opp MultiSpinner etter at 'kontakter' er fylt
            List<String> items = new ArrayList<>();
            for (Kontakt kontakt : kontakter) {
                items.add(kontakt.toString());
            }
            multiSpinner.setItems(items, "Velg Kontakter", selected -> {
                valgteKontakter.clear();  // Tømmer listen over valgte kontakter først
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {  // Hvis kontakten er valgt, legg den til i listen
                        valgteKontakter.add(kontakter.get(i));
                    }
                }

                // Vis en melding for å bekrefte valgte kontakter (valgfritt)
                StringBuilder melding = new StringBuilder("Valgte kontakter: ");
                for (Kontakt k : valgteKontakter) {
                    melding.append(k.toString()).append(", ");
                }
                Toast.makeText(getActivity(), melding.toString(), Toast.LENGTH_SHORT).show();
            });
        }

    }

    public void lagreAvtale(Avtale avtale, List<Kontakt> valgteKontakter) {
        // 1. Lagre avtalen
        long avtaleId = avtaleDao.insert(avtale);

        // 2. For hver valgt kontakt, lagre en deltakelse
        for (Kontakt kontakt : valgteKontakter) {
            Deltakelse deltakelse = new Deltakelse();
            deltakelse.avtaleId = (int) avtaleId;
            deltakelse.setKid((int) kontakt.getUid());

            deltakelseDao.insert(deltakelse);
        }
    }


}

