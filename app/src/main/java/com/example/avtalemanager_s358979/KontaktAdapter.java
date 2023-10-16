package com.example.avtalemanager_s358979;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class KontaktAdapter extends ArrayAdapter<Kontakt> {
    private Context context;
    private List<Kontakt> kontakter;
    private OnEditButtonClickListener mListener; // Legg til denne linjen

    // Legg til denne interfacet
    public interface OnEditButtonClickListener {
        void onEditButtonClick(Kontakt kontakt);
    }

    public KontaktAdapter(Context context, List<Kontakt> list, OnEditButtonClickListener listener) {
        super(context, R.layout.kontakt_list_item, list);
        this.context = context;
        this.kontakter = list;
        this.mListener = listener; // Sett listeneren
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.kontakt_list_item, parent, false);

        TextView textView = rowView.findViewById(R.id.kontaktName);
        Kontakt currentKontakt = kontakter.get(position);
        textView.setText(currentKontakt.getFirstName() + " " + currentKontakt.getLastName());

        Button btnEndre = rowView.findViewById(R.id.btnEndre);
        btnEndre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("KontaktAdapter", "Endre knappen ble trykket!");
                if (mListener != null) {
                    mListener.onEditButtonClick(currentKontakt);
                } else {
                    Log.d("KontaktAdapter", "mListener er null!");
                }
            }
        });


        return rowView;
    }
}
