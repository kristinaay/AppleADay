package com.example.appleaday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.TextView;

public class SymptomPage extends AppCompatActivity {

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
    static String day = "";
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_layout);
        date = (TextView) findViewById(R.id.date);

        System.out.println("starting");
        for (String x : SelectSymptoms.selectedSympts)
            System.out.println(x);

        try {
            submitted = getIntent().getExtras().getBoolean("changed");
            System.out.println("Submitted: " + submitted);
        } catch (Exception e){
            System.out.println("no intent");
        }



        try {
            String[] toParse = getIntent().getExtras().getString("date").split(" ");
            System.out.println("got date string");
            day = toParse[1] + " " + toParse[2] + " " + toParse[5];
            System.out.println(day);
            date.setText(day);
        } catch (Exception e) {
            System.out.println("no date");
        }

        System.out.println("started onCreate");


        db = openOrCreateDatabase("Database1", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS APPLE (Date String PRIMARY KEY," +
                " Symptoms String, Comments String, Severity Integer);");
        //db.execSQL("Delete from APPLE"); //use to delete all tuples in case of error
        if (!submitted){
            try {
                System.out.println("trying"); // query db for record for appropriate date
                Cursor c = db.rawQuery("Select * from APPLE WHERE Date = '" + day + "'", null);
                System.out.println("got cursor");
                if (c != null && c.getCount() > 0){ // check if record exists\
                    System.out.println("got tuple");
                    c.moveToNext();
                    SelectSymptoms.selectedSympts.clear();
                    String symptomsString = c.getString(1);
                    System.out.println("Symptom String: " + symptomsString);
                    commentsString = c.getString(2);
                    colorChoiceInt = c.getInt(3);
                    String[] symptomsArray = symptomsString.split(",");
                    for (String x : symptomsArray){
                        if (!SelectSymptoms.selectedSympts.contains(x)){
                            SelectSymptoms.selectedSympts.add(x);
                        }
                    }
                    if (SelectSymptoms.selectedSympts.contains("")){
                        SelectSymptoms.selectedSympts.remove("");
                    }
                } else {
                    System.out.println("else");
                    SelectSymptoms.selectedSympts.clear();
                    commentsString = "";
                    colorChoiceInt = 0;
                }
                c.close();
            } catch (Exception e) {
                System.out.println("Error occured when connecting to db");
            }
        }

        listView = findViewById(R.id.mListView);
        adapter = new MainListViewAdapter(SelectSymptoms.selectedSympts, SymptomPage.this);
        if(listView!=null) {
            listView.setAdapter(adapter);
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
                closeKeyboard();
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

    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
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


        intent.putExtra("date", date.getText());
        System.out.println(date.getText());
        intent.putStringArrayListExtra("symptoms", SelectSymptoms.selectedSympts);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            if (SelectSymptoms.selectedSympts.size() != 0){
                return;
            }
            List<String> items;
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
    }

    private void submit(){
        commentsString = comments.getText().toString();
        String symptomsString = "";
        System.out.println("Day: " + day);
        for (String s : SelectSymptoms.selectedSympts) {
            System.out.println(s);
            symptomsString += s + ",";
        }
        symptomsString.trim();
        if (!symptomsString.equals(""))
            symptomsString = symptomsString.substring(0, symptomsString.length() - 1);
        System.out.println(commentsString);
        System.out.println(colorChoiceInt);
        db.execSQL("REPLACE into APPLE values('" + day + "', '" + symptomsString + "', '" + commentsString + "', '" + colorChoiceInt + "')");
        Cursor c = db.rawQuery("Select * from APPLE", null);
        while(c.moveToNext()) {
            System.out.println(c.getString(0));
        }
        c.close();
        db.close();


        Intent intent = new Intent(this, CalendarPage.class);
        intent.putExtra("date", day);
        intent.putExtra("color", colorChoiceInt);
        setResult(2, intent);
        finish();
        //startActivity(intent);
    }
}
