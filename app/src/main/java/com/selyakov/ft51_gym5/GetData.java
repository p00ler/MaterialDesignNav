package com.selyakov.ft51_gym5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData extends AsyncTask {

    private Context context;
    private String email;
    protected String mon, tue, wed, thu, fri, sat, type;
    public String data;

    public GetData(Context context, String type, String email, String mon, String tue, String wed, String thu, String fri, String sat){

        this.context = context;
        this.email = email;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.type = type;
    }

    private String[] name_of_param = {"type","mon","tue","wed","thu","fri","sat"};


    @Override
    protected Object doInBackground(Object[] objects) {
        String link ="https://helpio.000webhostapp.com/index.php";
        HttpURLConnection connection = null;
        String ResponseServer = null;
        String[] params = {type,mon,tue,wed,thu,fri,sat};
        try {
            Log.d("Info:","Start connection...");
            connection = (HttpURLConnection) new URL(link).openConnection();

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);

            String param = "email" + "=" + email;
            int i=0;
            while(i!=7){
                param+="&"+name_of_param[i]+"="+params[i];
                i++;
            }
            Log.d("PARAMS", param);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr,"UTF-8"));
            writer.write(param);

            writer.close();
            wr.close();

            //Server answer
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            StringBuffer response = new StringBuffer();

            while ((input_line = reader.readLine()) != null){
                response.append(input_line);
            }reader.close();Log.d("Info:","While ended");

            connection.connect();

            ResponseServer = response.toString();
            Log.d("RESPONSE:",ResponseServer);



        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.d("ERROR:",Integer.toString(connection.getResponseCode()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        return ResponseServer;

    }

    protected void onPostExecute(String message) {
        Type type = new TypeToken<String>() {
        }.getType();

        Gson gson = new Gson();

        String str = gson.fromJson(message, type);

        Log.d("P", str);
    }

}
