package com.thethreebees.poligo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences settings;
    TextView id;
    Button btnLogout;

    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogout = findViewById(R.id.logout);
        new NavDrawer().setNavigationDrawer(this, R.color.theWhite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void onLogout(View view) {
        SharedPrefManager.getInstance(this).logout();
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

    public void onSettings(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
