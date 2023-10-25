package com.example.avtalemanager_s358979;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AvtalerFragment extends Fragment {

    private ListView kontaktListe;
    private ArrayList<String> kontakter;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.avtaler, container, false);

        kontaktListe = view.findViewById(R.id.kontaktListe);

        // Fyller listen med eksempelkontakter
        kontakter = new ArrayList<>();
        kontakter.add("Kontakt 1");
        kontakter.add("Kontakt 2");
        kontakter.add("Kontakt 3");

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_checkedtextview, R.id.checked_text_item, kontakter);
        kontaktListe.setAdapter(adapter);

        return view;
    }
}
