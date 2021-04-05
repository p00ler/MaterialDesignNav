package com.selyakov.ft51_gym5;

import android.content.Context;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

public class CheckAvailability extends AsyncTask {

    private Context context;
    private HttpURLConnection connection;

    public CheckAvailability(Context context){
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String link ="https://helpio.000webhostapp.com/get.php"; // ToDo: Поменять сервер
        try {
            connection = (HttpURLConnection) new URL(link).openConnection();
            connection.setConnectTimeout(300);
            Global.error_code = connection.getResponseCode();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
