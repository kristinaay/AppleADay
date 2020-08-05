package com.example.appleaday;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class MainListViewAdapter extends ArrayAdapter<String> {

    List<String> symptoms;
    Context context;

    public MainListViewAdapter(List<String> symptoms, Context context){
        super(context, R.layout.item_layout,symptoms);
        this.context = context;
        this.symptoms = symptoms;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, ViewGroup parent){

        LayoutInflater inf = ((Activity)context).getLayoutInflater();
        View row = inf.inflate(R.layout.main_item_layout, parent, false);
        final TextView symptType = row.findViewById(R.id.symptomText);
        symptType.setText(symptoms.get(pos));
        return row;
    }

}