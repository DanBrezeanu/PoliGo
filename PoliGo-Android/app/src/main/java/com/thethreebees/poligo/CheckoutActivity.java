package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class CheckoutActivity extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    int[] cardCompanies;
    String[] cardNumbers;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Spinner spin = (Spinner) findViewById(R.id.dropdown_cards);
        spin.setOnItemSelectedListener(CheckoutActivity.this);

        progressBar = findViewById(R.id.progressBar);

        ArrayList<BankCard> userCards = SharedPrefManager.getInstance(this).getUser().getCards();

        if (userCards != null) {

            cardCompanies = new int[userCards.size()];
            cardNumbers = new String[userCards.size()];

            for (int i = 0; i < userCards.size(); ++i) {
                String cardNumber = userCards.get(i).getNumber();
                String cardCompany = userCards.get(i).getCompany();

                cardNumbers[i] = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
                cardCompanies[i] = cardCompany.equals("visa") ? R.drawable.visa : R.drawable.mastercard;
            }

        } else {
            cardCompanies = new int[1];
            cardNumbers = new String[1];

            cardCompanies[0] = R.drawable.ic_add_circle_black_24dp;
            cardNumbers[0] = "Add new card";
        }

        CardItemAdapter customAdapter = new CardItemAdapter(CheckoutActivity.this,
                cardCompanies, cardNumbers);
        spin.setAdapter(customAdapter);


        TextView totalSum = findViewById(R.id.total_sum);

        Intent parentIntent = getIntent();
        totalSum.setText(String.format("%s RON", parentIntent.getStringExtra("totalSum")));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) { }

    public void onBackToCart(View v) {
        finish();
    }

    public void onPaymentConfirmed(View v) {
        SharedPrefManager.getInstance(this).clearShoppingCart();

        Intent toFinalMessage = new Intent(CheckoutActivity.this, QRCodeActivity.class);
        startActivity(toFinalMessage);
        finish();
    }
}
