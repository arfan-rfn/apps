package com.exception.notetaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;

public class secondActivity extends AppCompatActivity {

    EditText editText;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        final int noteID = intent.getIntExtra("noteID", -1);
        if (noteID != -1) {
            editText.setText(MainActivity.notes.get(noteID));
        }else {
            MainActivity.notes.add("");
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (noteID != -1) {
                    MainActivity.notes.set(noteID, String.valueOf(s));
                }else {
                    MainActivity.notes.set(MainActivity.notes.size()-1, String.valueOf(s));

                }
                HashSet<String > set = new HashSet<>(MainActivity.notes);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.exception.notetaker", Context.MODE_PRIVATE);
                sharedPreferences.edit().putStringSet("notes", set).apply();
                MainActivity.arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


    }

}
