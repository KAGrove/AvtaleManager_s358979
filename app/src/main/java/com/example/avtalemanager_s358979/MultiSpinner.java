package com.example.avtalemanager_s358979;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MultiSpinner extends androidx.appcompat.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {

    private String[] _items = null;
    private boolean[] mSelection = null;
    private ArrayAdapter<String> simple_adapter;

    public MultiSpinner(Context context) {
        super(context);
        simple_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        simple_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void setItems(List<String> items, String allText, MultiSpinnerListener listener) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(allText);
        setListener(listener);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String spinnerText = "";
                for (int i = 0; i < _items.length; i++) {
                    if (mSelection[i]) {
                        spinnerText += _items[i] + ", ";
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{spinnerText});
                setAdapter(adapter);
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;
        }
    }

    public interface MultiSpinnerListener {
        void onItemsSelected(boolean[] selected);
    }

    public void setListener(MultiSpinnerListener listener) {
        setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onItemsSelected(mSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Dette kan være tom
            }
        });
    }

}
