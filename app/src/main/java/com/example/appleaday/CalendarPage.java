package com.example.appleaday;


import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;


import java.util.Calendar;
import java.util.Date;

public class CalendarPage extends AppCompatActivity {
    CalendarView calendar;
    Button infoButton;
    Button covidButton;
    SQLiteDatabase db;
    ColorDrawable red;
    ColorDrawable orange;
    ColorDrawable yellow;
    ColorDrawable lightgreen;
    ColorDrawable green;
    final CaldroidFragment caldroidFragment = new CaldroidFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        red = new ColorDrawable(getResources().getColor(R.color.red));
        orange = new ColorDrawable(getResources().getColor(R.color.orange));
        yellow = new ColorDrawable(getResources().getColor(R.color.yellow));
        lightgreen = new ColorDrawable(getResources().getColor(R.color.lightgreen));
        green = new ColorDrawable(getResources().getColor(R.color.green));

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

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();
        db = openOrCreateDatabase("Database1", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS APPLE (Date String PRIMARY KEY," + "Symptoms String, Comments String, Severity Integer);");

        try {
            Cursor c = db.rawQuery("Select Date, Severity from APPLE", null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String dateString = c.getString(0);
                    SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
                    Date date = null;
                    try {
                        date = format.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int color = c.getInt(1);
                    if (color == 1) caldroidFragment.setBackgroundDrawableForDate(red, date);
                    else if (color == 2) caldroidFragment.setBackgroundDrawableForDate(orange, date);
                    else if (color == 3) caldroidFragment.setBackgroundDrawableForDate(yellow, date);
                    else if (color == 4)
                        caldroidFragment.setBackgroundDrawableForDate(lightgreen, date);
                    else if (color == 5) caldroidFragment.setBackgroundDrawableForDate(green, date);

                }
                c.close();

            }
        }  catch (Exception e) {
            System.out.println("Error retrieving data");
        }

        db.close();



        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {


                Intent myIntent = new Intent(CalendarPage.this, SymptomPage.class);
                myIntent.putExtra("date", date.toString());
                //startActivity(myIntent);
                startActivityForResult(myIntent, 2);


                //caldroidFragment.setBackgroundDrawableForDate(red, date);
                //caldroidFragment.refreshView();
            }
        };



        caldroidFragment.setCaldroidListener(listener);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {

            try {
                Bundle extras = data.getExtras();
                String dateString = extras.getString("date");
                SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
                Date date = null;
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int color = extras.getInt("color");
                if (color == 1) caldroidFragment.setBackgroundDrawableForDate(red, date);
                else if (color == 2) caldroidFragment.setBackgroundDrawableForDate(orange, date);
                else if (color == 3) caldroidFragment.setBackgroundDrawableForDate(yellow, date);
                else if (color == 4)
                    caldroidFragment.setBackgroundDrawableForDate(lightgreen, date);
                else if (color == 5) caldroidFragment.setBackgroundDrawableForDate(green, date);


                caldroidFragment.refreshView();

            } catch (Exception e) {
                System.out.println("No data.");
            }

        }

    }

    public void openDialog() {
        InfoDialog dialog = new InfoDialog();
        dialog.show(getSupportFragmentManager(), "info dialog");
    }



}