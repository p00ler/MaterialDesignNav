package com.selyakov.ft51_gym5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class getJSON extends AsyncTask {

    private Context context;
    private String email;
    private String type;
    public String data, ResponseServer;
    private HttpURLConnection connection = null;


    public getJSON(Context context, String type, String email){

        this.context = context;
        this.email = email;
        this.type = type;
    }


    @Override
    public Object doInBackground(Object[] objects) {
        String link ="https://helpio.000webhostapp.com/get.php";
        try {
            connection = (HttpURLConnection) new URL(link).openConnection();

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);

            String param = "email" + "=" + email + "&" + "type" + "=" + type;

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr,"UTF-8"));
            writer.write(param);

            writer.close();
            wr.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            StringBuffer response = new StringBuffer();

            while ((input_line = reader.readLine()) != null){
                response.append(input_line);
            }reader.close();

            connection.connect();

            ResponseServer = response.toString();

            Global.error_code = connection.getResponseCode();
            Global.response = ResponseServer;
            return ResponseServer;




        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.d("GET_ERROR:",Integer.toString(connection.getResponseCode()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

}

