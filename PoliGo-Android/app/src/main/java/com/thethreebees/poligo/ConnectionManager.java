package com.thethreebees.poligo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {
    private String uriBase;
    private Context context;
    private static ConnectionManager instance = null;
    final int image = R.drawable.lab5_car_icon;

    private ConnectionManager(Context context, String ip, Integer port) {
        uriBase = new Uri.Builder()
                .scheme("http")
                .encodedAuthority(ip + ":" + port.toString())
                .path("api/v1/")
                .build()
                .toString();
        this.context = context;
    }

    static public ConnectionManager getInstance() {
        return instance;
    }

    static public ConnectionManager getInstance(Context context, String ip, Integer port) {
        if (instance != null) {
            return instance;
        }

        instance = new ConnectionManager(context, ip, port);

        return instance;
    }

    public void makeRequest(Integer method, HashMap<String, String> parameters, String endpoint) {
        RequestQueue queue = Volley.newRequestQueue(context);
        Uri.Builder uriEndpoint = new Uri.Builder();
        uriEndpoint = uriEndpoint.path(endpoint);


        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            uriEndpoint = uriEndpoint.appendQueryParameter(key, value);
        }

        String url = uriBase + uriEndpoint.build().toString();
        Log.e("INFO::", url);

        JsonObjectRequest getRequest = new JsonObjectRequest(method, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Server response", response.toString());
                            if (response.getInt("code") == 200 && response.getJSONArray("products").length() > 0) {
                                JSONArray products = response.getJSONArray("products");
                                for (int i = 0; i < products.length(); ++i) {
                                    JSONObject prod = (JSONObject) products.get(i);
                                    ((ShoppingList) context).productAdapter.addProduct(
                                            prod.getString("SKU"),
                                            prod.getString("name"),
                                            prod.getDouble("price"),
                                            image
                                    );
                                }
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(context)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setMessage("This product does not exist")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });




        queue.add(getRequest);

    }
}
