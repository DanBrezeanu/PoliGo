package com.thethreebees.poligo;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingHistory {
    ArrayList<HashMap<String, String>> cartInfo;
    HashMap<String, ArrayList<Pair<Product, Integer>>> productsInfo;
    Context context;


    final int image = R.drawable.lab5_car_icon;

    public ShoppingHistory(Context context) {
        cartInfo = new ArrayList<>();
        productsInfo = new HashMap<>();
        this.context = context;
    }

    public void fetchServerData() {
        JSONObject cardDetails = new JSONObject();
        final ProgressBar progressBar = ((ShoppingHistoryActivity) context).findViewById(R.id.progressBar);


        try {
            cardDetails.put("card_number", "example");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestManager2.getInstance().shoppingHistory((ShoppingHistoryActivity) context, this);
    }


}
