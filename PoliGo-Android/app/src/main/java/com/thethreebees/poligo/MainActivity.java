package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;
    TextView id,userName,userEmail,gender;
    Button btnLogout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPrefManager.getInstance(this).clearShoppingCart();

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent  intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        navigationView = (NavigationView) findViewById(R.id.nv);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.theOrange));
        drawerLayout.addDrawerListener(mDrawerToggle);


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();


        navigationView = (NavigationView)findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.mycart:
                        Toast.makeText(MainActivity.this, "My Cart", Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }


                return true;

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view){
        if(view.equals(btnLogout)){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
    }

    private Boolean loggedIn() {
        return (settings.getString("api_key", null) != null);
    }

    public void startShoppingClick(View v) {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }

    public void exitApp(View v) {
        finish();
    }
}
