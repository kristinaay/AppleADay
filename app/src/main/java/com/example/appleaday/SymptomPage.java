package com.example.appleaday;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;

public class SymptomPage extends AppCompatActivity {

    int date;
    ListView listView;
    MainListViewAdapter adapter;
    Button editSympts;
    ImageButton color1;
    ImageButton color2;
    ImageButton color3;
    ImageButton color4;
    ImageButton color5;
    ImageButton colorChoice;
    public static int colorChoiceInt;
    Button submit;
    EditText comments;
    public static String commentsString = "";
    SQLiteDatabase db;
    boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_layout);
        getSymptoms();

        try {
            submitted = getIntent().getExtras().getBoolean("changed");
        } catch (Exception e){
            System.out.println("no intent");
        }

        try {
            date = Integer.parseInt(getIntent().getExtras().getString("dateString"));
            System.out.println(date);
        } catch (Exception e) {
            System.out.println("no date");
        }

        System.out.println("started onCreate");

        listView = findViewById(R.id.mListView);
        adapter = new MainListViewAdapter(SelectSymptoms.selectedSympts, SymptomPage.this);
        if(listView!=null) {
            listView.setAdapter(adapter);
        }

        db = openOrCreateDatabase("Database", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS APPLE (Date Integer PRIMARY KEY," +
                " Symptoms String, Comments String, Severity Integer);");
        // db.execSQL("Delete from APPLE"); //use to delete all tuples in case of error
        // get date from intent
        date = 0;
        if (!submitted){
            try {
                System.out.println("trying"); // query db for record for appropriate date
                Cursor c = db.rawQuery("Select * from APPLE WHERE Date = '" + date + "'", null);
                System.out.println("got cursor");
                if (c != null && c.getCount() > 0){ // check if record exists
                    System.out.println("got tuple");
                    c.moveToNext();
                    String symptomsString = c.getString(1);
                    System.out.println("Symptom String: " + symptomsString);
                    commentsString = c.getString(2);
                    colorChoiceInt = c.getInt(3);
                    String[] symptomsArray = symptomsString.split(",");
                    for (String x : symptomsArray){
                        if (!SelectSymptoms.selectedSympts.contains(x)){
                            SelectSymptoms.selectedSympts.add(x);
                        }
                    } adapter.notifyDataSetChanged();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Error occured when connecting to db");
            }
        }

        editSympts = findViewById(R.id.editSymptoms);
        editSympts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectSymptomsActivity();
            }
        });

        comments = findViewById(R.id.comments);
        comments.setText(commentsString);

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsString = comments.getText().toString();
                //closeKeyboard();
                System.out.println(commentsString);
            }
        });

        if (!commentsString.equals("")){
            comments.setText(commentsString);
        }

        color1 = findViewById(R.id.selectColor);
        color2 = findViewById(R.id.selectColor2);
        color3 = findViewById(R.id.selectColor3);
        color4 = findViewById(R.id.selectColor4);
        color5 = findViewById(R.id.selectColor5);

        final ImageButton colors[] = new ImageButton[5];
        colors[0] = color1;
        colors[1] = color2;
        colors[2] = color3;
        colors[3] = color4;
        colors[4] = color5;

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(color1, colors);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(color2, colors);
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(color3, colors);
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(color4, colors);
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(color5, colors);
            }
        });

        if (colorChoiceInt != 0){
            if (colorChoiceInt == 1) color1.performClick();
            else if (colorChoiceInt == 2) color2.performClick();
            else if (colorChoiceInt == 3) color3.performClick();
            else if (colorChoiceInt == 4) color4.performClick();
            else color5.performClick();
        }

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void colorSelected(ImageButton choice, ImageButton[] colors){
        for (ImageButton color : colors){
            color.setSelected(false);
        }

        colorChoice = choice;

        if (choice == color1) {
            colorChoiceInt = 1;
            color1.setSelected(true);

        } else if (choice == color2) {
            colorChoiceInt = 2;
            color2.setSelected(true);

        } else if (choice == color3) {
            colorChoiceInt = 3;
            color3.setSelected(true);

        } else if (choice == color4){
            colorChoiceInt = 4;
            color4.setSelected(true);

        } else { // choice == color5
            colorChoiceInt = 5;
            color5.setSelected(true);

        }
    }

    private void openSelectSymptomsActivity() {
        commentsString = comments.getText().toString();
        Intent intent = new Intent(this, SelectSymptoms.class);
        intent.putStringArrayListExtra("symptoms", SelectSymptoms.selectedSympts);
        startActivity(intent);
    }

    private void getSymptoms(){
        if (SelectSymptoms.selectedSympts.size() != 0){
            return;
        }
        List<String> items = new ArrayList<>();
        items = Arrays.asList(getResources().getStringArray(R.array.symptomArray));
        Intent intent = getIntent();
        if (intent != null){
            items = intent.getStringArrayListExtra("symptoms");
        }

        if (items != null){
            for (String item : items){
                SelectSymptoms.selectedSympts.add(item);
                System.out.println(item);
            }
        }
    }

    private void submit(){
        commentsString = comments.getText().toString();
        String symptomsString = "";
        for (String s : SelectSymptoms.selectedSympts) {
            System.out.println(s);
            symptomsString += s + ",";
        }
        symptomsString.trim();
        if (!symptomsString.equals(""))
            symptomsString = symptomsString.substring(0, symptomsString.length() - 1);
        System.out.println(commentsString);
        System.out.println(colorChoiceInt);
        int date = 0;
        db.execSQL("REPLACE into APPLE values('" + date + "', '" + symptomsString + "', '" + commentsString + "', '" + colorChoiceInt + "')");
        Cursor c = db.rawQuery("Select * from APPLE", null);
        while(c.moveToNext()) {
            System.out.println(c.getString(1));
        }

        Intent intent = new Intent(this, MainActivity.class);
        // add data to the db
        // create intent to go back to main activity
    }
}