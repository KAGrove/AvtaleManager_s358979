package com.example.avtalemanager_s358979;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AvtaleAdapter extends ArrayAdapter<String> {
    private List<String> avtaleListe;
    private List<Avtale> avtaleObjekter;
    private Context context;


    public AvtaleAdapter(Context context, List<String> avtaleListe, List<Avtale> avtaleObjekter) {
        super(context, R.layout.avtale_element, avtaleListe);
        this.avtaleListe = avtaleListe;
        this.avtaleObjekter = avtaleObjekter;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.avtale_element, parent, false);

        TextView avtaleInfo = rowView.findViewById(R.id.avtaleInfo);
        Button slettAvtale = rowView.findViewById(R.id.slettAvtale);

        avtaleInfo.setText(avtaleListe.get(position));

        slettAvtale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Avtale avtaleToRemove = avtaleObjekter.get(position);
                AppDatabase db = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase();
                AvtaleDao avtaleDao = db.avtaleDao();
                new Thread(() -> {
                    avtaleDao.delete(avtaleToRemove);
                }).start();

                DeltakelseDao deltakelseDao = db.deltakelseDao();
                new Thread(() -> {
                    deltakelseDao.deleteByAvtaleId(avtaleToRemove.getAvtaleId());
                }).start();



                // Slett avtalen fra listene og oppdater adapteren
                avtaleObjekter.remove(position);
                avtaleListe.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}

