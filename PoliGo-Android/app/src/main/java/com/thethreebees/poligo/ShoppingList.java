package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShoppingList extends Activity {
    private ListView productList;
    ProductAdapter productAdapter;
    private EditText addNewProduct;
    private Button addButton;
    final int image = R.drawable.lab5_car_icon;
    final int LAUNCH_BARCODE_SCANNING = 1;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        productList = findViewById(R.id.list_item);
        View footerView =  ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);

        productList.addFooterView(footerView);

        productAdapter = new ProductAdapter(this);

        productList.setAdapter(productAdapter);

        addButton = (Button) footerView.findViewById(R.id.button_add_product);
    }

    public void scanCode(View v) {
        Intent scanCodeIntent = new Intent(this, BarcodeScanner.class);
        startActivityForResult(scanCodeIntent, LAUNCH_BARCODE_SCANNING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_BARCODE_SCANNING) {
            if(resultCode == Activity.RESULT_OK){
                result = data.getStringExtra("result");

                String name = result;

                productAdapter.addProduct(name, image);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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
