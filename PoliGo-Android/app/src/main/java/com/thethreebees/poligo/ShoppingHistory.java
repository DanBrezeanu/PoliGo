package com.thethreebees.poligo;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


        JsonObjectRequest jsonRequest= new JsonObjectRequest(Request.Method.GET, URLs.URL_SHOPPING_HISTORY, cardDetails,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = response;

                            JSONArray shoppingCarts = obj.getJSONArray("carts");

                            for (int i = 0; i < shoppingCarts.length(); ++i) {
                                JSONObject cart = (JSONObject) shoppingCarts.get(i);

                                HashMap<String, String> cartInfoJson = new HashMap<>();
                                String cartNumber = cartInfoJson.put("number", String.format("%d", cart.getInt("number")));
                                cartInfoJson.put("number", cartNumber);
                                cartInfoJson.put("date", cart.getString("date"));
                                cartInfoJson.put("totalSum",  String.format("%f", cart.getDouble("number")));

                                cartInfo.add(cartInfoJson);

                                JSONArray products = cart.getJSONArray("products");
                                ArrayList<Pair<Product, Integer>> newProducts = new ArrayList<>();

                                for (int j = 0; j < products.length(); ++j) {
                                    JSONObject prod = (JSONObject) products.get(i);

                                    Product new_prod = new Product(
                                            prod.getString("SKU"),
                                            prod.getString("name"),
                                            prod.getDouble("price"),
                                            image
                                    );

                                    newProducts.add(new Pair<>(new_prod, prod.getInt("quantity")));
                                }

                                productsInfo.put(cartNumber, newProducts);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        progressBar.setVisibility(View.VISIBLE);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonRequest);
    }


}
