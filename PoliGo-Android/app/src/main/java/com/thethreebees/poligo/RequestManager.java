package com.thethreebees.poligo;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestManager {
    private static RequestManager instance = null;
    private VolleySingleton volley;

    private RequestManager(Context applicationContext) {
        volley = VolleySingleton.getInstance(applicationContext);
    }

    public static RequestManager getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new RequestManager(applicationContext);
        }

        return instance;
    }



    public void getShoppingCart(ShoppingListActivity activity) throws JSONException {
        JSONObject params = new JSONObject();

        params.put("quantity", 1);
        params.put("api_key", activity.user.getId());

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URLs.URL_SHOPPING_CART, params,
                response -> {
                    activity.progressBar.setVisibility(View.GONE);

                    try {
                        if (response.getInt("code") == 200) {
                            activity.totalSum.setText(((Double) response.getDouble("totalCost")).toString());
                            activity.productAdapter = new ProductAdapter(activity);
                            activity.productList.setAdapter(activity.productAdapter);

                            JSONArray products = response.getJSONArray("products");

                            for (int i = 0; i < products.length(); ++i) {
                                JSONObject prod = products.getJSONObject(i);

                                Product new_prod = new Product(
                                        prod.getString("SKU"),
                                        prod.getString("name"),
                                        prod.getDouble("price"),
                                        activity.image,
                                        0
                                );

                                activity.productAdapter.addProduct(new_prod, prod.getInt("quantity"));
                            }

                            activity.findViewById(R.id.finished_shopping).setVisibility(View.VISIBLE);

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(response.getString("message"))
                                    .setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    activity.progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        volley.addToRequestQueue(getRequest);
        activity.progressBar.setVisibility(View.VISIBLE);
    }
}
