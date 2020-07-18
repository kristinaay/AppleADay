package com.example.appleaday;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    }

    public void openDialog() {
        InfoDialog dialog = new InfoDialog();
        dialog.show(getSupportFragmentManager(), "info dialog");
    }
}