package com.example.android.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button searchButton;
    TextView cityName;
    TextView result;


    class Weather extends AsyncTask<String,Void,String>{
        //First String means URL is in String, Void mean nothing,
        // Third String means Return type will be String

        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be send. It acts as array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                Log.i("Content",content);
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    public void search_button(View view){

        searchButton = findViewById(R.id.searchButton);
        cityName = findViewById(R.id.cityName);
        result = findViewById(R.id.result);


        String cName = cityName.getText().toString();

        String content;
        Weather weather = new Weather();

        try {
            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q="+
                    cName+"&units=metric&APPID=39d96fdbde37964b00221e600302c515").get();
            //"https://openweathermap.org/data/2.5/weather?q=London&appid=39d96fdbde37964b00221e600302c515").get();
            //First we will check data is retrieve successfully or not
            Log.i("contentData",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
          //  Log.i("weatherData",weatherData);
            //weather data is in array
            JSONArray array = new JSONArray(weatherData);

            String main = "";
            String description = "";
            String temperature="";
            //Double visibility;
            for (int i=0; i<array.length();i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");

            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature=mainPart.getString("temp");
          //  Log.i("temperature",temperature);

            //visibility = Double.parseDouble((jsonObject.getString("visibility")));

//            Log.i("main",main);
//            Log.i("description",description);


            result.setText("City : "+cName+
                    "\nMain : "+main+
                    "\nDescription : "+description+
                    "\nTemperature : "+temperature+"*C");
                    //+"\nVisibility"+visibility+" m");


        } catch (Exception e) {
            result.setText("Error");
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  Log.i("HEllo World","whats'up");


    }
}