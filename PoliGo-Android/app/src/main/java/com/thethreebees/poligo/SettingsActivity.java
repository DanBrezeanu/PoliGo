package com.thethreebees.poligo;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnLogout = findViewById(R.id.logout);
        new NavDrawer().setNavigationDrawer(this, R.color.theWhite);

        int[] countryFlags = {R.drawable.english_lang, R.drawable.romanian_lang};
        String[] languageNames = {"English", "Română"};
        Spinner spin = findViewById(R.id.dropdown_languages);

        CardItemAdapter customAdapter = new CardItemAdapter(SettingsActivity.this,
                countryFlags, languageNames);
        spin.setAdapter(customAdapter);
    }


    @Override
    public void onClick(View v) {
        if(v.equals(btnLogout)){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
    }
}
