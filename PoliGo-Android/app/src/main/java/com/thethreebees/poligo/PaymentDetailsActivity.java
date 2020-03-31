package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentDetailsActivity extends Activity {

    Context context;
    private String username, password, email;
    ProgressBar progressBar;

    EditText cardDateMonth;
    EditText cardDateYear;
    EditText cardCVV;
    EditText cardNumber;
    EditText cardHolder;
    ImageView cardCompany;
    Button buttonRegiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        cardDateMonth = findViewById(R.id.exp_date_month);
        cardDateYear = findViewById(R.id.exp_date_year);
        cardCVV = findViewById(R.id.cvv);
        cardNumber = findViewById(R.id.card_number);
        cardHolder = findViewById(R.id.card_holder);
        cardCompany = findViewById(R.id.card_company);

        context = this;
        Intent parentIntent = getIntent();

        username = parentIntent.getStringExtra("username");
        password = parentIntent.getStringExtra("password");
        email = parentIntent.getStringExtra("email");

        progressBar = findViewById(R.id.progressBar);

        mangeInputContext();
    }

    private void mangeInputContext() {
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


    public void onRegisterUser(View v) {
        //TODO: check card details are valid

        JSONObject cardDetails = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            cardDetails.put("card_number", cardNumber.getText().toString());
            cardDetails.put("card_holder", cardHolder.getText().toString());
            cardDetails.put("card_month", cardDateMonth.getText().toString());
            cardDetails.put("card_year", cardDateYear.getText().toString());
            cardDetails.put("card_cvv", cardCVV.getText().toString());
            cardDetails.put("card_company", (cardNumber.getText().toString().charAt(0) == '4') ? "visa" : "mastercard");


            params.put("username", username);
            params.put("password", password);
            params.put("email", email);
            params.put("card_details", cardDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest= new JsonObjectRequest(Request.Method.POST, URLs.URL_REGISTER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = response;

                            Toast.makeText(getApplicationContext(), obj.getString("api_key"), Toast.LENGTH_SHORT).show();

                            //TODO: get actual data from server, including card numbers
                            User user = new User(
                                    obj.getString("api_key"),
                                    obj.getString("api_key"),
                                    obj.getString("api_key"),
                                    null
                            );

                            ArrayList<BankCard> bankCards = new ArrayList<>();
                            JSONArray responseBankCards = obj.getJSONArray("cards");

                            for (int i = 0; i < responseBankCards.length(); ++i) {
                                JSONObject card = (JSONObject) responseBankCards.get(i);

                                bankCards.add(new BankCard(
                                        card.getString("card_number"),
                                        card.getString("card_holder"),
                                        card.getString("card_expiry_month"),
                                        card.getString("card_expiry_year"),
                                        card.getString("card_cvv"),
                                        card.getString("card_company")
                                ));
                            }

                            user.setCards(bankCards);

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        progressBar.setVisibility(View.VISIBLE);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }
}
