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

        RequestManager2.getInstance().login(username, password, this);
        progressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        return;
    }
}
