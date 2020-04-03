package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(ShoppingHistoryActivity.this, "ok", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) { }
        });

        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) { }
        });

    }

    private void addCartsToView() {
        shoppingHistory.fetchServerData();
        expandableListViewAdapter.notifyDataSetChanged();
    }

}