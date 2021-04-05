package com.selyakov.ft51_gym5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetData extends AsyncTask {

    private Context context;
    private String email, ResponseServer;
    private String mon, tue, wed, thu, fri, sat, type; // Ммммм, асинхронная передача JSON через HTTP запросы, пока пока безопаность
    // ToDo: Подогнать шифрование

    public GetData(Context context, String type, String email, String mon, String tue, String wed, String thu, String fri, String sat){

        this.context = context; // Получаем все дерьмо из глобального стека данных для формирования запроса
        this.email = email;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.type = type;
    }

    private String[] name_of_param = {"type","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};


    @Override
    protected Object doInBackground(Object[] objects) {
        String link ="https://helpio.000webhostapp.com/index.php"; // ToDo: Найти сервер...
        HttpURLConnection connection = null; // Открываем поток для сервера
        String[] params = {type,mon,tue,wed,thu,fri,sat};
        try { // Без try\catch в ассинхронке никуда
            //Log.d("Info:","Start connection..."); // Дебаг
            connection = (HttpURLConnection) new URL(link).openConnection(); // Открываем новый поток

            connection.setRequestMethod("POST"); // Задаем ПОСТ запрос на сервер

            connection.setUseCaches(false);
            connection.setConnectTimeout(500); // Костыль?? ToDo: Избавиться от простоя ядра при запросе
            connection.setReadTimeout(500);

            String param = "email" + "=" + email; // ToDo: ПИЗДЕЦ, 7 ЗАПРОСОВ ВМЕСТО ОДНОГО, Я ПЬЯНЫЙ ЭТО ПИСАЛ?
            int i=0;
            while(i!=7){
                param+="&"+name_of_param[i]+"="+params[i];
                i++;
            }

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

            return ResponseServer;



        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.d("ERROR:",Integer.toString(connection.getResponseCode()));
                Global.error_code = connection.getResponseCode();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

}
