package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class CheckoutActivity extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    int[] cardCompanies;
    String[] cardNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Spinner spin = (Spinner) findViewById(R.id.dropdown_cards);
        spin.setOnItemSelectedListener(CheckoutActivity.this);


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
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) { }

}
