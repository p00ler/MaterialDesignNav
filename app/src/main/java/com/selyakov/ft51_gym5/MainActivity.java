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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

import java.io.Serializable;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;
    private Long id = null;
    private FirebaseDatabase dbase;
    private DatabaseReference f_ref;
    private FirebaseAuth mAuth;
    private String display, email;
    private FirebaseUser user = mAuth.getInstance().getCurrentUser();

    static class Item implements Serializable {
        public String name;
        public String status;

        public Item() {
        }

        Item(String name, String status) {
            this.name = name;
            this.status = status;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FoodList()).commit();

        //Инициализируем Firebase Database
        dbase = FirebaseDatabase.getInstance();
        f_ref = dbase.getReference("peoples");
        display = "dinner";


        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Инициализируем Navigation Drawer
        display = "dinner";
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_table).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_food_list).withIcon(FontAwesome.Icon.faw_apple),
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
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
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

    public void onRadioChange(View view){
        switch (view.getId()){
            case R.id.radioButton1:
                display = "afternoon_tea";
                break;
            case R.id.radioButton2:
                display = "dinner";
                break;
            case R.id.radioButton3:
                display = "breakfast";
        }
        focusCheck();
    }

    private void focusCheck() {
        CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox cb4 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox cb5 = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox cb6 = (CheckBox)findViewById(R.id.checkBox6);

        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        cb5.setChecked(false);
        cb6.setChecked(false);
    }

    public void sendDB(Boolean checked, String email, String day, Item item){
        if (checked && user != null){
            f_ref.child(email).child(day).push().setValue(item);
        }
        else if (checked){
            Toast.makeText(MainActivity.this,"Авторизуйтесь", Toast.LENGTH_SHORT).show();
        }else{
            if(user != null)
                f_ref.child(email).child(day).removeValue();
        }
    }


    public void onCheckboxClicked(View view) {

        final String name = display;

        final String status = "+";

        Item item = new Item(name, status);

        user = mAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = mAuth.getInstance().getCurrentUser().getEmail().replace(".","");
        }


        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {

            case R.id.checkBox1:
                sendDB(checked,email,"monday",item);
                break;

            case R.id.checkBox2:
                sendDB(checked,email,"tuesday",item);
                break;

            case R.id.checkBox3:
                sendDB(checked,email,"wednesday",item);
                break;

            case R.id.checkBox4:
                sendDB(checked,email,"thursday",item);
                break;

            case R.id.checkBox5:
                sendDB(checked,email,"friday",item);
                break;

            case R.id.checkBox6:
                sendDB(checked,email,"saturday",item);
                break;
        }

    }

}