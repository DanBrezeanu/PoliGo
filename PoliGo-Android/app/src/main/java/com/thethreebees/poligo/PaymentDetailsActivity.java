package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentDetailsActivity extends Activity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        context = this;

        mangeInputContext();
    }



    private void mangeInputContext() {
        final EditText cardDateMonth = findViewById(R.id.exp_date_month);
        final EditText cardDateYear = findViewById(R.id.exp_date_year);
        final EditText cardCVV = findViewById(R.id.cvv);
        final EditText cardNumber = findViewById(R.id.card_number);
        final EditText cardHolder = findViewById(R.id.card_holder);
        final ImageView cardCompany = findViewById(R.id.card_company);
        final int visaLogo = R.drawable.visa;
        final int masterCardLogo = R.drawable.mastercard;

        final ArrayList<String> patternList = new ArrayList<>();

        patternList.add("^4[0-9]{6,}$");      // visa
        patternList.add("^5[1-5][0-9]{5,}$"); // mastercard

        cardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                for (String p : patternList)
                    if (cardNumber.getText().toString().replaceAll(" ", "").matches(p))
                        return;

                    if (cardNumber.getText().length() > 0)
                        cardNumber.setError("Invalid card number");

                }
        });

        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count < before && (start == 4 || start == 9 || start == 14)) {    // backspace pressed
                    cardNumber.setText(cardNumber.getText().toString().substring(0, cardNumber.getText().length() - 1));
                    cardNumber.setSelection(cardNumber.getText().length());
                } else if (s.length() == 0)  // no characters yet
                    cardCompany.setVisibility(View.INVISIBLE);
                else if (s.length() == 1) { // determine card company
                    if (s.charAt(0) == '4') {
                        cardCompany.setImageResource(visaLogo);
                        cardCompany.setVisibility(View.VISIBLE);
                    } else if (s.charAt(0) == '5') {
                        cardCompany.setImageResource(masterCardLogo);
                        cardCompany.setVisibility(View.VISIBLE);
                    }
                } else if (s.length() == 4 || s.length() == 9 || s.length() == 14) { // add space for easy reading
                    cardNumber.setText(cardNumber.getText() + " ");
                    cardNumber.setSelection(cardNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 19 && cardNumber.getError() == null) {
                    cardHolder.setFocusable(true);
                    cardHolder.setFocusableInTouchMode(true);
                    cardHolder.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(cardHolder, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });


        cardDateMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    cardDateYear.setFocusable(true);
                    cardDateYear.setFocusableInTouchMode(true);
                    cardDateYear.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(cardDateYear, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        cardDateYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    cardCVV.setFocusable(true);
                    cardCVV.setFocusableInTouchMode(true);
                    cardCVV.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(cardCVV, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        cardDateYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 3) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    imm.hideSoftInputFromWindow(cardCVV.getRootView().getWindowToken(), 0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        cardCVV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    cardCVV.setHint("");
            }
        });

    }


}
