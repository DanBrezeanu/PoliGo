package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {
    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.progressBar);

        //if the user is already logged in we will directly start the MainActivity activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        TextView loginText = findViewById(R.id.textViewLogin);
        String sourceString = loginText.getText().toString() + "<b>Login now</b>";
        loginText.setText(Html.fromHtml(sourceString));


        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            if (TextUtils.isEmpty(editTextUsername.getText())) {
                editTextUsername.setError("Please enter username");
                editTextUsername.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(editTextEmail.getText())) {
                editTextEmail.setError("Please enter your email");
                editTextEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()).matches()) {
                editTextEmail.setError("Enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(editTextPassword.getText())) {
                editTextPassword.setError("Enter a password");
                editTextPassword.requestFocus();
                return;
            }



            JSONObject params = new JSONObject();
            try {
                params.put("username", editTextUsername.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                params.put("email", editTextEmail.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_REGISTER, params,
                    response -> {
                        progressBar.setVisibility(View.GONE);

                        try {
                            Toast.makeText(getApplicationContext(), response.getString("api_key"), Toast.LENGTH_SHORT).show();

                            User user = new User(
                                    response.getString("api_key"),
                                    response.getString("name"),
                                    response.getString("email"),
                                    null
                            );

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            Intent toPayment = new Intent(RegisterActivity.this, PaymentDetailsActivity.class);
                            toPayment.putExtra("nextActivity", MainActivity.class);
                            startActivity(toPayment);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            progressBar.setVisibility(View.VISIBLE);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
        });



        findViewById(R.id.textViewLogin).setOnClickListener(view -> {
            finish();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

    }
}
