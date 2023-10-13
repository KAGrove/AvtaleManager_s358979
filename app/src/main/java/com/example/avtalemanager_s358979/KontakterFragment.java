package com.example.avtalemanager_s358979;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class KontakterFragment extends Fragment {
    View view;
    Button firstButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kontakter, container, false);
        firstButton = (Button) view.findViewById(R.id.button);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
