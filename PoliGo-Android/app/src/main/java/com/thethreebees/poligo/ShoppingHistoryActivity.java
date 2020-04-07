package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingHistoryActivity extends Activity {

    private ExpandableListView expandableListView;
    public ExpandableListViewAdapter expandableListViewAdapter;
    ShoppingHistory shoppingHistory;

    ArrayList<HashMap<String, String>> cartInfo;
    HashMap<String, ArrayList<Pair<Product, Integer>>> productsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_history);

        expandableListView = findViewById(R.id.expandableListView);
        shoppingHistory = new ShoppingHistory(this);

        setClickListeners();

        cartInfo = new ArrayList<>();
        productsInfo = new HashMap<>();

        expandableListViewAdapter = new ExpandableListViewAdapter(this, shoppingHistory.cartInfo, shoppingHistory.productsInfo);
        expandableListView.setAdapter(expandableListViewAdapter);

        addCartsToView();

    }

    private void setClickListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(ShoppingHistoryActivity.this, "ok", Toast.LENGTH_SHORT).show();
            return false;
        });

        expandableListView.setOnGroupExpandListener(groupPosition -> { });
        expandableListView.setOnGroupCollapseListener(groupPosition -> { });

    }

    private void addCartsToView() {
        shoppingHistory.fetchServerData();
        expandableListViewAdapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        Intent backToMain = new Intent(ShoppingHistoryActivity.this, MainActivity.class);
        startActivity(backToMain);
        finish();
    }

}