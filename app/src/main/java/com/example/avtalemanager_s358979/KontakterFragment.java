package com.example.avtalemanager_s358979;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

public class KontakterFragment extends Fragment implements KontaktAdapter.OnEditButtonClickListener {
    View view;
    Button btnLagre, btnVis;
    EditText editTextFornavn, editTextEtternavn, editTextTelefonnr;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kontakter, container, false);

        // Initierer elementene
        editTextFornavn = view.findViewById(R.id.fornavn);
        editTextEtternavn = view.findViewById(R.id.etternavn);
        editTextTelefonnr = view.findViewById(R.id.telefonnr);
        listView = view.findViewById(R.id.visalle);

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

        showKontakt();

        return view;
    }

    // Metoder for å lagre og vise kontakt
    private void saveKontakt() {
        final String fornavn = editTextFornavn.getText().toString().trim();
        final String etternavn = editTextEtternavn.getText().toString().trim();
        final String telefonNr = editTextTelefonnr.getText().toString().trim();

        class SaveKontakt extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Kontakt kontakt = new Kontakt();
                kontakt.setFirstName(fornavn);
                kontakt.setLastName(etternavn);
                kontakt.setPhoneNumber(telefonNr);
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

                KontaktAdapter adapter = new KontaktAdapter(getActivity(), alle, KontakterFragment.this); // Legg til "this" for å sende denne fragment som en lytter.
                listView.setAdapter(adapter);

                // Implementere metoden fra KontaktAdapter.OnEditButtonClickListener
            }
        }

        ShowKontakt sk = new ShowKontakt();
        sk.execute();
    }

    @Override
    public void onEditButtonClick(Kontakt kontakt){
        // Plasser koden for å vise redigeringsdialogen her
        // Dette vil bli kalt når "Endre" knappen blir trykket i en rad i listen.

        Log.d("KontakterFragment", "1");

        AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View editView = inflater.inflate(R.layout.dialog_edit_kontakt, null);

        EditText editFornavn = editView.findViewById(R.id.editFornavn);
        EditText editEtternavn = editView.findViewById(R.id.editEtternavn);
        EditText editTelefonnr = editView.findViewById(R.id.editTelefonnr);

        // Sett nåværende verdier
        editFornavn.setText(kontakt.getFirstName());
        editEtternavn.setText(kontakt.getLastName());
        editTelefonnr.setText(kontakt.getPhoneNumber());


        editDialog.setView(editView);
        editDialog.setTitle("Rediger kontakt");
        editDialog.setPositiveButton("Lagre", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nyttFornavn = editFornavn.getText().toString();
                String nyttEtternavn = editEtternavn.getText().toString();
                String nyttTelefonnr = editTelefonnr.getText().toString();

                // Oppdater databasen med de nye verdiene
                kontakt.setFirstName(nyttFornavn);
                kontakt.setLastName(nyttEtternavn);
                kontakt.setPhoneNumber(nyttTelefonnr);

                // Her kan du nå oppdatere kontakten i databasen.
                updateKontakt(kontakt);
            }
        });
        editDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        editDialog.show(); // Legg til denne linjen for å vise dialogen
    }

    private void updateKontakt(Kontakt kontakt) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getActivity().getApplicationContext())
                        .getAppDatabase()
                        .kontaktDao()
                        .update(kontakt);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity().getApplicationContext(), "Oppdatert", Toast.LENGTH_LONG).show();
                showKontakt(); // For å oppdatere listen
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
