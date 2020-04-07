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
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id)
            {
            case R.id.account:
                Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                break;
            case R.id.history:
                Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                Intent toShoppingHistory = new Intent(MainActivity.this, ShoppingHistoryActivity.class);
                startActivity(toShoppingHistory);
                finish();
                break;
            case R.id.mycart:
                Intent toCart = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(toCart);
                finish();
                break;
            default:
                return true;
            }

            return true;
        });

        gestureDetector = new GestureDetector(this, new SwipeDrawerDetection());
        gestureListener = (v, event) -> gestureDetector.onTouchEvent(event);

        ConstraintLayout background = findViewById(R.id.background_layout);
        background.setOnClickListener(MainActivity.this);
        background.setOnTouchListener(gestureListener);

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

    @Override
    public void onBackPressed() {
        finish();
    }


    class SwipeDrawerDetection extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    drawerLayout.closeDrawer(Gravity.LEFT);

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}
