package com.exception.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

//    http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22

    public class DownloadTask extends AsyncTask<String, Void, String >{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(inputStream);
//                int data = reader.read();
//                while (data != -1){
//                    char current = (char) data;
//                    result += current;
//                    data = reader.read();
//                }

                Scanner scan = new Scanner(inputStream);
                while (scan.hasNext()){
                    result += scan.next();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "code fucked up!";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("fucking Main ----->", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    Log.i("fucking Main ----->", jsonPart.getString("main"));
                    Log.i("Description ----->", jsonPart.getString("description"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        DownloadTask downloadTask = new DownloadTask();
        String apiResult = "";
        try {
            downloadTask.execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
            textView.setText(apiResult);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
