package com.selyakov.ft51_gym5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.gson.Gson;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import com.selyakov.ft51_gym5.ui.BlankFragment;
import com.selyakov.ft51_gym5.ui.FoodList;

import java.util.Objects;
import java.lang.Thread;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;
    private String email;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    protected String mon, tue, wed, thu, fri, sat, type;

    String JSONobject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FoodList()).commit();

        mon="0";tue="0";wed="0";thu="0";fri="0";sat="0";

        new CheckAvailability(this).execute();

        // Инициализируем Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Инициализируем Navigation Drawer
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_food_list).withIcon(FontAwesome.Icon.faw_coffee),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_table),
                        new PrimaryDrawerItem().withName(R.string.scheldure).withIcon(FontAwesome.Icon.faw_clock_o),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_auth).withIcon(FontAwesome.Icon.faw_child),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.contacts).withIcon(FontAwesome.Icon.faw_pencil),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_warning)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        View focusedView = getCurrentFocus();
                        if(focusedView!=null){
                            inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem instanceof Nameable) {
                            switch (MainActivity.this.getString(((Nameable) drawerItem).getNameRes())){
                                case "Авторизация":
                                    Intent auth_act = new Intent("com.selyakov.ft51_gym5.EmailPasswordActivity");
                                    startActivity(auth_act);
                                    break;
                                case "Бланк питания":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FoodList()).commit();
                                    break;
                                case "Расписание уроков":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new BlankFragment()).commit();
                                    break;
                            }
                        }
                        if (drawerItem instanceof Badgeable) {
                            Badgeable badgeable = (Badgeable) drawerItem;
                            if (badgeable.getBadge() != null) {
                                try {
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                } catch (Exception e) {
                                    Log.d("error", "Wrong badge name!");
                                }
                            }
                        }
                    }
                }).build();
        type="2";
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void onRadioChange(View view) {
        getUserEmail();
        switch (view.getId()) {
            case R.id.radioButton1:
                type = "1";
                break;
            case R.id.radioButton2:
                type = "2";
                break;
            case R.id.radioButton3:
                type = "3";
        }
        focusCheck();
    }


    private void focusCheck() {

        CheckBox cb1 = findViewById(R.id.checkBox1);
        CheckBox cb2 = findViewById(R.id.checkBox2);
        CheckBox cb3 = findViewById(R.id.checkBox3);
        CheckBox cb4 = findViewById(R.id.checkBox4);
        CheckBox cb5 = findViewById(R.id.checkBox5);
        CheckBox cb6 = findViewById(R.id.checkBox6);

        cb1.setChecked(false);

        cb2.setChecked(false);

        cb3.setChecked(false);

        cb4.setChecked(false);

        cb5.setChecked(false);

        cb6.setChecked(false);
    }



    public void onCheckboxClicked(View view) {
        CheckBox cb1 = findViewById(R.id.checkBox1);
        CheckBox cb2 = findViewById(R.id.checkBox2);
        CheckBox cb3 = findViewById(R.id.checkBox3);
        CheckBox cb4 = findViewById(R.id.checkBox4);
        CheckBox cb5 = findViewById(R.id.checkBox5);
        CheckBox cb6 = findViewById(R.id.checkBox6);

        getUserEmail();

        JSONobject = new Gson().toJson(email);

        switch(view.getId()) {

            case R.id.checkBox1:
                if(cb1.isChecked()){
                    mon="┼";
                }else{
                    mon=" ";
                }
                break;

            case R.id.checkBox2:
                if(cb2.isChecked()){
                    tue="┼";
                }else{
                    tue=" ";
                }
                break;

            case R.id.checkBox3:
                if(cb3.isChecked()){
                    wed="┼";
                }else{
                    wed=" ";
                }
                break;

            case R.id.checkBox4:
                if(cb4.isChecked()){
                    thu="┼";
                }else{
                    thu=" ";
                }
                break;

            case R.id.checkBox5:
                if(cb5.isChecked()){
                    fri="┼";
                }else{
                    fri=" ";
                }
                break;

            case R.id.checkBox6:
                if(cb6.isChecked()){
                    sat="┼";
                }else{
                    sat=" ";
                }
                break;
        }

    }

    public void onSendClick(View v){
        getUserEmail();
        try{
            if(user != null){
                new GetData(this, type, JSONobject, mon, tue, wed, thu, fri, sat).execute();
                Thread.sleep(500);
                if(Global.error_code==200){
                    Toast.makeText(MainActivity.this,"Успешно!", Toast.LENGTH_SHORT).show();
                }else{Toast.makeText(MainActivity.this,"Повторите попытку", Toast.LENGTH_SHORT).show();}
            }
            else{Toast.makeText(MainActivity.this,"Авторизуйтесь", Toast.LENGTH_SHORT).show();}
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getUserEmail(){
        new CheckAvailability(this).execute();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()).replace(".", "");
        }
    }


}