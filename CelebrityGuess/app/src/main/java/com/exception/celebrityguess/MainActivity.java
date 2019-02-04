package com.exception.celebrityguess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView celebrityImage;
    Button[] options = new Button[4];

    HashMap<String, Bitmap> celebrities;
    ArrayList<String> celebrityName;
    Random random = new Random();
    int currOption;
    int rightCelebrity;

    public void guessCelebrity(View view){
        if (view.getTag().equals(String.valueOf(currOption))){
            Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "Wrong! That was " + celebrityName.get(rightCelebrity), Toast.LENGTH_SHORT).show();
        }
        gameBoard();
    }

    public void gameBoard(){
        currOption = random.nextInt(4);
        rightCelebrity = random.nextInt(celebrityName.size())+1;
        for (int i = 0; i< 4; i++){
            if (String.valueOf(currOption).equals(options[i].getTag())){
                celebrityImage.setImageBitmap(celebrities.get(celebrityName.get(rightCelebrity)));
                options[i].setText(celebrityName.get(rightCelebrity));
                options[i].setTag(String.valueOf(i));
            }else {
                options[i].setText(celebrityName.get(random.nextInt(celebrityName.size())));
                options[i].setTag(String.valueOf(i));

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityImage = findViewById(R.id.celebrityImage);

        options[0] = findViewById(R.id.option0);
        options[1] = findViewById(R.id.option1);
        options[2] = findViewById(R.id.option2);
        options[3] = findViewById(R.id.option3);

        DownloadHtml downloadHtml = new DownloadHtml();
        try {
            celebrities = downloadHtml.execute("http://www.posh24.se/kandisar").get();
            celebrityName = new ArrayList<>(celebrities.keySet());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        gameBoard();
    }

    public class DownloadHtml extends AsyncTask<String, Void, HashMap<String, Bitmap> >{

        @Override
        protected HashMap<String, Bitmap> doInBackground(String... strings) {

            HashMap<String, Bitmap> celebrities = new HashMap<>();

            String httpPage = "";
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){
                    char current = (char) data;
                    httpPage += current;
                    data = reader.read();
                }

                Pattern pattern = Pattern.compile("<img src=\"(.*?)\" alt=\"(.*?)\"");
                Matcher matcher = pattern.matcher(httpPage);
                while (matcher.find()){
//                    System.out.println(matcher.group(1));
//                    System.out.println(matcher.group(2));

                    url = new URL(matcher.group(1));
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    inputStream = httpURLConnection.getInputStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    celebrities.put(matcher.group(2), image);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return celebrities;
        }
    }
}
