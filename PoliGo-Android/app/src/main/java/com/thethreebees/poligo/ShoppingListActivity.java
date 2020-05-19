package com.thethreebees.poligo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ShoppingListActivity extends Activity {
    public ListView productList;
    ProductAdapter productAdapter;
    public EditText addNewProduct;
    public Button addButton;
    final int image = R.drawable.lab5_car_icon;
    final int LAUNCH_BARCODE_SCANNING = 1;
    String result;
    ProgressBar progressBar;
    TextView totalSum;
    Context context;
    public ShoppingCart cart;
    SharedPrefManager sharedPref;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        context = this;
        sharedPref = SharedPrefManager.getInstance(this);
        user = sharedPref.getUser();

        productList = findViewById(R.id.list_item);
        addButton = findViewById(R.id.btn_add_prod);
        progressBar = findViewById(R.id.progressBar);
        totalSum = findViewById(R.id.total_sum);
        cart = new ShoppingCart();

        resumeShopping();
    }

    @SuppressLint("SetTextI18n")
    public void resumeShopping() {
        productAdapter = new ProductAdapter(this);
        productList.setAdapter(productAdapter);

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

            RequestManager2.getInstance().addToCart(this, result, productAdapter);
            RequestManager2.getInstance().shoppingCart(cart, productAdapter);
        }
    }


    public void onFinishedShopping(View v) {
        Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
        checkoutIntent.putExtra("totalSum", totalSum.getText().toString());
        startActivity(checkoutIntent);
    }

    public void onBackPressed() {
        Intent backToMain = new Intent(ShoppingListActivity.this, MainActivity.class);
        startActivity(backToMain);
        finish();
    }
}
