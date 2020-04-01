package com.thethreebees.poligo;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ShoppingListActivity extends Activity {
    private ListView productList;
    ProductAdapter productAdapter;
    private EditText addNewProduct;
    private Button addButton;
    final int image = R.drawable.lab5_car_icon;
    final int LAUNCH_BARCODE_SCANNING = 1;
    String result;
    HashMap<String, String> params = new HashMap<>();
    ProgressBar progressBar;
    TextView totalSum;
    Context context;
    public ShoppingCart cart;
    SharedPrefManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        context = this;
        sharedPref = SharedPrefManager.getInstance(this);

        productList = findViewById(R.id.list_item);
        addButton = findViewById(R.id.btn_add_prod);
        progressBar = findViewById(R.id.progressBar);
        totalSum = findViewById(R.id.total_sum);

        productAdapter = new ProductAdapter(this);

        productList.setAdapter(productAdapter);

        resumeShopping();
    }

    public void resumeShopping() {
        cart = sharedPref.getShoppingCart();

        for (Pair<Product, Integer> prod : cart.getProducts()) {
            productAdapter.addProduct(prod.first, prod.second);
        }

        totalSum.setText(cart.getTotalSum().toString());

        if (cart.getCount() > 0)
            ((ShoppingListActivity) context).findViewById(R.id.finished_shopping).setVisibility(View.VISIBLE);
    }

    public void onScanCode(View v) {
        Intent scanCodeIntent = new Intent(this, BarcodeScanner.class);
        startActivityForResult(scanCodeIntent, LAUNCH_BARCODE_SCANNING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_BARCODE_SCANNING && resultCode == Activity.RESULT_OK) {
                result = data.getStringExtra("result");

                params.put("SKU", result);
                params.put("api_key", "w/e"); // NOT IMPLEMENTED YET

                Uri.Builder uriEndpoint = new Uri.Builder()
                        .scheme(URLs.PROTOCOL)
                        .encodedAuthority(URLs.AUTHORITY)
                        .appendEncodedPath(URLs.ROOT_PATH)
                        .appendEncodedPath("checkstocks/")
                        .appendQueryParameter("SKU", params.get("SKU"))
                        .appendQueryParameter("api_key", params.get("api_key"));

                String URL = uriEndpoint.build().toString();
                Log.d("URL", URL);

                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);

                                try {
                                    if (response.getInt("code") == 200 && response.getJSONArray("products").length() > 0) {
                                        JSONObject prod = (JSONObject) response.getJSONArray("products").get(0);


                                        Product new_prod = new Product(
                                                prod.getString("SKU"),
                                                prod.getString("name"),
                                                prod.getDouble("price"),
                                                image
                                        );

                                        ((ShoppingListActivity) context).productAdapter.addProduct(new_prod, 1);

//                                        cart.addProduct(new_prod);

                                        sharedPref.registerShoppingCart(cart);

                                        totalSum.setText(cart.getTotalSum().toString());

                                        ((ShoppingListActivity) context).findViewById(R.id.finished_shopping).setVisibility(View.VISIBLE);

                                    } else {
                                        AlertDialog alertDialog = new AlertDialog.Builder((ShoppingListActivity)context )
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
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

            VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
            progressBar.setVisibility(View.VISIBLE);
        }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
    }


    public void onFinishedShopping(View v) {
        Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
        checkoutIntent.putExtra("totalSum", totalSum.getText().toString());
        startActivity(checkoutIntent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure you want to exit?")
                .setMessage("Your shoppings will NOT be saved")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();

    }
}
