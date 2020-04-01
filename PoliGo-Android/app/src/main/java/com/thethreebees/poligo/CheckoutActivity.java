package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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

import net.glxn.qrgen.android.QRCode;

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

        cardCompanies = new int[userCards.size()];
        cardNumbers = new String[userCards.size()];

        for (int i = 0; i < userCards.size(); ++i) {
            String cardNumber = userCards.get(i).getNumber();
            String cardCompany = userCards.get(i).getCompany();

            cardNumbers[i] = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
            cardCompanies[i] = cardCompany.equals("visa") ? R.drawable.visa : R.drawable.mastercard;
        }

        CardItemAdapter customAdapter = new CardItemAdapter(getApplicationContext(),
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
