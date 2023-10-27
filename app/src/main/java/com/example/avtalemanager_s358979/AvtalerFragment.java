package com.example.avtalemanager_s358979;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private ListView avtaleListView;
    private List<String> avtaleListe = new ArrayList<>();
    private AvtaleAdapter avtaleAdapter;


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
        KontaktDao kontaktDao = db.kontaktDao();
        new FetchContactsTask(kontaktDao).execute();


        Button btnLagreAvtale = view.findViewById(R.id.lagreAvtale);
        EditText datoEditText = view.findViewById(R.id.dato);
        EditText klokkeslettEditText = view.findViewById(R.id.klokkeslett);
        EditText stedEditText = view.findViewById(R.id.sted);


        // Initialiser ListView først
        avtaleListView = view.findViewById(R.id.avtaleListView);

        // Deretter opprett og sett adapteren
        avtaleAdapter = new AvtaleAdapter(getContext(), avtaleListe);
        avtaleListView.setAdapter(avtaleAdapter);

        new FetchAvtalerTask().execute();

        avtaleAdapter.notifyDataSetChanged();


        btnLagreAvtale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avtale nyAvtale = new Avtale();

                String datoString = datoEditText.getText().toString();
                nyAvtale.setDato(datoString);

                String klokkeslettString = klokkeslettEditText.getText().toString();
                nyAvtale.setKlokkeslett(klokkeslettString);

                String stedString = stedEditText.getText().toString();
                nyAvtale.setTreffsted(stedString);

                String avtaleTekst = "Avtale: " + klokkeslettString + " - " + stedString;
                avtaleListe.add(avtaleTekst);
                avtaleAdapter.notifyDataSetChanged(); // Oppdater adapteren etter å ha lagt til en avtale.

                lagreAvtale(nyAvtale, valgteKontakter);
            }
        });



        return view;
    }

    private class FetchContactsTask extends AsyncTask<Void, Void, List<Kontakt>> {
        private final KontaktDao kontaktDao;

        public FetchContactsTask(KontaktDao kontaktDao) {
            this.kontaktDao = kontaktDao;
        }

        @Override
        protected List<Kontakt> doInBackground(Void... voids) {
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // 1. Lagre avtalen
                long avtaleId = avtaleDao.insert(avtale);

                // 2. For hver valgt kontakt, lagre en deltakelse
                Log.d("AvtaleApp", "Lagrer avtale med ID: " + avtaleId);
                for (Kontakt kontakt : valgteKontakter) {
                    Deltakelse deltakelse = new Deltakelse();
                    deltakelse.avtaleId = (int) avtaleId;
                    deltakelse.setKid((int) kontakt.getUid());
                    Log.d("AvtaleApp", "Lagrer deltakelse med Avtale ID: " + avtaleId + " og Kontakt ID: " + kontakt.getUid());
                    deltakelseDao.insert(deltakelse);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Her kan du oppdatere brukergrensesnittet etter at databasetransaksjonen er fullført, om nødvendig.
            }
        }.execute();
    }


    private class FetchAvtalerTask extends AsyncTask<Void, Void, List<Pair<Avtale, List<Deltakelse>>>> {

        @Override
        protected List<Pair<Avtale, List<Deltakelse>>> doInBackground(Void... voids) {
            List<Pair<Avtale, List<Deltakelse>>> result = new ArrayList<>();

            // Hent avtalene fra databasen
            List<Avtale> avtaler = avtaleDao.getAll();

            for (Avtale avtale : avtaler) {
                List<Deltakelse> deltakelser = deltakelseDao.getDeltakelserForAvtale(avtale.getAvtaleId());
                result.add(new Pair<>(avtale, deltakelser));
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Pair<Avtale, List<Deltakelse>>> fetchedData) {
            avtaleListe.clear();

            for (Pair<Avtale, List<Deltakelse>> pair : fetchedData) {
                Avtale avtale = pair.first;
                List<Deltakelse> deltakelser = pair.second;

                // Gjør om deltakelser til en streng med kontaktinformasjon
                StringBuilder kontakterBuilder = new StringBuilder();
                for (Deltakelse deltakelse : deltakelser) {
                    kontakterBuilder.append(deltakelse.getKid()).append(", ");
                }
                // Fjerner siste komma og mellomrom for en renere format
                if (kontakterBuilder.length() > 0) {
                    kontakterBuilder.setLength(kontakterBuilder.length() - 2);
                }

                String avtaleTekst = avtale.getDato() + " " + avtale.getKlokkeslett() + " - " + avtale.getTreffsted() + "\nKontakter: " + kontakterBuilder.toString();
                avtaleListe.add(avtaleTekst);
            }

            avtaleAdapter.notifyDataSetChanged();
        }


    }



}

