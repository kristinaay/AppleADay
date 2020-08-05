package com.example.appleaday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

public class CalendarPage extends AppCompatActivity {
    CalendarView calendar;
    Button infoButton;
    Button covidButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        calendar = findViewById(R.id.calendar);
        infoButton = findViewById(R.id.helpbutton);
        covidButton = findViewById(R.id.button2);

        infoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        covidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CalendarPage.this, ResourcesPage.class);
                startActivity(myIntent);
            }
        });
        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();
        final ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.red));
        final ColorDrawable orange = new ColorDrawable(getResources().getColor(R.color.orange));
        final ColorDrawable yellow = new ColorDrawable(getResources().getColor(R.color.yellow));
        final ColorDrawable lightgreen = new ColorDrawable(getResources().getColor(R.color.lightgreen));
        final ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.green));

        SharedPreferences sharedPref = CalendarPage.this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();


        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                editor.putString("currentDate", date.toString());
                editor.apply();
                Intent myIntent = new Intent(CalendarPage.this, SymptomPage.class);
                startActivity(myIntent);

                caldroidFragment.setBackgroundDrawableForDate(red, date);
                caldroidFragment.refreshView();
            }
        };



        caldroidFragment.setCaldroidListener(listener);

    }

    public void openDialog() {
        InfoDialog dialog = new InfoDialog();
        dialog.show(getSupportFragmentManager(), "info dialog");
    }

}