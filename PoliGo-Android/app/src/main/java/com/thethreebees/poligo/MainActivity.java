package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    SharedPreferences settings;
    TextView id,userName,userEmail,gender;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent  intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
        Intent intent = new Intent(this, ShoppingList.class);
        startActivity(intent);
    }

    public void exitApp(View v) {
        finish();
    }
}
