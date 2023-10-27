package com.example.avtalemanager_s358979;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AvtalerFragment extends Fragment {

    private ListView kontaktListe;
    private List<Kontakt> kontakter;
    private ArrayAdapter<Kontakt> adapter;

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

            // Bruk en egendefinert adapter for å vise kontakter
            adapter = new ArrayAdapter<Kontakt>(getActivity(), R.layout.list_item_checkedtextview, R.id.checked_text_item, kontakter){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = view.findViewById(R.id.checked_text_item);
                    text1.setText(kontakter.get(position).getFirstName() + " " + kontakter.get(position).getLastName());
                    return view;
                }
            };

            kontaktListe.setAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.avtaler, container, false);

        kontaktListe = view.findViewById(R.id.kontaktListe);
        new FetchContactsTask().execute();

        return view;
    }
}

