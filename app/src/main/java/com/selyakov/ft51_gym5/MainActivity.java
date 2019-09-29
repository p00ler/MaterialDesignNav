package com.selyakov.ft51_gym5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Drawer.Result drawerResult = null;
    private Long id = null;
    private FirebaseDatabase dbase;
    private DatabaseReference f_ref;
    private FirebaseAuth mAuth;
    CheckBox checkBox;
    private Button btn;
    private FirebaseUser cur_user;
    private String displayName, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_list);
        btn = (Button)findViewById(R.id.button_ist);
        setContentView(R.layout.activity_main);

        //Инициализируем Firebase Database
        dbase = FirebaseDatabase.getInstance();
        f_ref = dbase.getReference("classes");


        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        checkBox = (CheckBox)findViewById(R.id.checkBox);

        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            displayName = user.getDisplayName();
            email = mAuth.getInstance().getCurrentUser().getEmail().replace(".","");
            }



        // Инициализируем Navigation Drawer
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_table).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_food_list).withIcon(FontAwesome.Icon.faw_apple),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_auth).withIcon(FontAwesome.Icon.faw_child),
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
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            switch (MainActivity.this.getString(((Nameable) drawerItem).getNameRes())){
                                case "Авторизация":
                                    Intent auth_act = new Intent("com.selyakov.ft51_gym5.EmailPasswordActivity");
                                    startActivity(auth_act);
                                    break;
                                case "Бланк питания":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FoodList()).commit();
                                    break;
                                case "Расписание":
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


    public void onSendClick(){
        f_ref.child(email).push().setValue(displayName);
    }


    public void addListener(View v){
        Toast.makeText(MainActivity.this,"WORK BLYAT", Toast.LENGTH_SHORT).show();
        onSendClick();
    }

    @Override
    protected void onResume(){
        super.onResume();

    }
}
