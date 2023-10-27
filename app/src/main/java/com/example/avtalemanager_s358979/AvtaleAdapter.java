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

    private List<String> avtaler;
    private Context context;

    public AvtaleAdapter(Context context, List<String> avtaler) {
        super(context, R.layout.avtale_element, avtaler);  // 'avtale_element' er XML-layouten du delte.
        this.avtaler = avtaler;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.avtale_element, parent, false);

        TextView avtaleInfo = rowView.findViewById(R.id.avtaleInfo);
        Button slettAvtale = rowView.findViewById(R.id.slettAvtale);

        avtaleInfo.setText(avtaler.get(position));

        slettAvtale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avtaler.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}

