package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    EditText etName, etPassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        progressBar = findViewById(R.id.progressBar);
        etName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etUserPassword);

        TextView registerText = findViewById(R.id.tvRegister);
        String sourceString = registerText.getText().toString() + "<b>Sign Up</b>";
        registerText.setText(Html.fromHtml(sourceString));

        //calling the method userLogin() for login the user
        findViewById(R.id.btnLogin).setOnClickListener(view -> userLogin());

        //if user presses on textview not register calling RegisterActivity
        findViewById(R.id.tvRegister).setOnClickListener(view -> {
            finish();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
    }

    private void userLogin() {
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            etName.setError("Please enter your username");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        JSONObject params = new JSONObject();

        try {
            params.put("username", username);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_LOGIN, params,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        if (response.getInt("code") == 200) {

                            User user = new User(
                                    response.getString("api_key"),
                                    response.getString("name"),
                                    response.getString("email"),
                                    null
                            );

                            ArrayList<BankCard> bankCards = new ArrayList<>();
                            JSONArray responseBankCards = response.getJSONArray("cards");

                            for (int i = 0; i < responseBankCards.length(); ++i) {
                                JSONObject card = (JSONObject) responseBankCards.get(i);

                                bankCards.add(new BankCard(
                                        card.getString("cardNumber"),
                                        card.getString("cardHolder"),
                                        card.getString("cardMonthExpire"),
                                        card.getString("cardYearExpire"),
                                        card.getString("cardCVV"),
                                        card.getString("cardCompany")
                                ));
                            }

                            user.setCards(bankCards);

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            etPassword.setError("Invalid password or username");
                            etPassword.requestFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        return;
    }
}
