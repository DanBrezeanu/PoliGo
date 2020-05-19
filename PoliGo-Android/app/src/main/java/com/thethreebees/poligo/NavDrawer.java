package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavDrawer {
    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    void setNavigationDrawer(AppCompatActivity context, Integer navigationIconColor) {
        drawerLayout = (DrawerLayout) context.findViewById(R.id.activity_main);
        navigationView = (NavigationView) context.findViewById(R.id.nv);
        drawerToggle = new ActionBarDrawerToggle(context, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(context, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.getDrawerArrowDrawable().setColor(context.getResources().getColor(navigationIconColor));
        drawerLayout.addDrawerListener(mDrawerToggle);


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();


        navigationView = (NavigationView) context.findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.account:
                    Toast.makeText(context, "My Account", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.history:
                    Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
                    Intent toShoppingHistory = new Intent(context, ShoppingHistoryActivity.class);
                    context.startActivity(toShoppingHistory);
                    context.finish();
                    break;
                case R.id.mycart:
                    Intent toCart = new Intent(context, ShoppingListActivity.class);
                    context.startActivity(toCart);
                    context.finish();
                    break;
                default:
                    return true;
            }

            return true;
        });

        gestureDetector = new GestureDetector(context, new SwipeDrawerDetection(drawerLayout));
        gestureListener = (v, event) -> gestureDetector.onTouchEvent(event);

        ConstraintLayout background = context.findViewById(R.id.background_layout);
        background.setOnClickListener((View.OnClickListener) context);
        background.setOnTouchListener(gestureListener);
    }
}
