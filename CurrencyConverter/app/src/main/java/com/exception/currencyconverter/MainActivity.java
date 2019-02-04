package com.exception.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void Convert(View view){
        EditText editText = findViewById(R.id.editText);

        if (!editText.getText().toString().equals("")) {
            Double rate = Double .parseDouble(editText.getText().toString()) * 83.26;
            Toast.makeText(MainActivity.this, String.format("৳%.2f", rate), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
