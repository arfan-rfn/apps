package com.exception.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //https://hacker-news.firebaseio.com/v0/item/15982007.json?print=pretty
    SQLiteDatabase newsDB;
    int k = 0;
    ArrayList<String> titleArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsDB = this.openOrCreateDatabase("NEWS", MODE_PRIVATE, null);

        DownloadNewsID downloadNewsID = new DownloadNewsID();

        try {
            ArrayList<String> newsIDs = downloadNewsID.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
//            Log.i("here ~~~~~>", Integer.toString(k));

            if (newsIDs != null){
                for (String str: newsIDs){

                    DownloadNews downloadNews = new DownloadNews();
                    String[] news = downloadNews.execute("https://hacker-news.firebaseio.com/v0/item/"+ str +".json?print=pretty").get();
                    if (news != null && news[0] != null && news[1] != null){
                        Log.i("here ~~~~~>", Arrays.toString(news));
                           addDataToDatabase(news[0], news[1], k);
                           k++;
                    }

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        ListView titleView = findViewById(R.id.titleView);
        titleArray = getTitleArrayFromDatabase();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArray);
        titleView.setAdapter(arrayAdapter);
        titleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = getDataFromDatabase(position);
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);

            }
        });



    }

    public void addDataToDatabase(String title, String url, int k){
        try {
            newsDB.execSQL("CREATE TABLE IF NOT EXISTS news7 (title VARCHAR, url VARCHAR, id INT(3))");
            String str = String.format("INSERT INTO news7 (title, url, id) VALUES ('%s', '%s', %d)", title, url, k);
            newsDB.execSQL(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDataFromDatabase(int idIndex){
        try {
            Cursor c = newsDB.rawQuery("SELECT * FROM news7", null);
            int titleIndex = c.getColumnIndex("title");
            int urlIndex = c.getColumnIndex("url");
            int keyIndex = c.getColumnIndex("id");
            c.moveToFirst();
            while (c != null){
                if (idIndex == c.getInt(keyIndex)) {
                    Log.i("title ~~~~~~>", c.getString(titleIndex));
                    Log.i("url =======>", c.getString(urlIndex));
                    Log.i("id =======>", String.valueOf(c.getInt(keyIndex)));
                    return c.getString(urlIndex);
                }

                c.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String> getTitleArrayFromDatabase(){
        ArrayList<String> titles = new ArrayList<>();
        try {
            Cursor c = newsDB.rawQuery("SELECT * FROM news7", null);
            int titleIndex = c.getColumnIndex("title");
            c.moveToFirst();
            while (c != null){
                titles.add(c.getString(titleIndex));

                c.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return titles;
    }



    public class DownloadNewsID extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> newsID = new ArrayList<>();
            String newsIdStr = "";

            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){

                    newsIdStr += (char) data;
                    data = reader.read();
                }
//                Pattern pattern = Pattern.compile(" (.*?),");
//                Matcher matcher = pattern.matcher(newsIdStr);
//                while (matcher.find()){
//                    newsID.add(matcher.group(1));
//                }

                JSONArray jsonArray = new JSONArray(newsIdStr);
                for (int i = 0; i < jsonArray.length(); i++){
                    newsID.add(jsonArray.getString( i));
                }
                return newsID;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }
    }

    public class DownloadNews extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String newsIdStr = "";
            String[] news = new String[2];

            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1) {

                    newsIdStr += (char) data;
                    data = reader.read();
                }
                JSONObject obj = new JSONObject(newsIdStr);
                if (!obj.getString("title").equals("") && !obj.getString("url").equals("")) {
                    news[0] = obj.getString("title").replace("'", "");
                    news[1] = obj.getString("url");
                }
                return news;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
