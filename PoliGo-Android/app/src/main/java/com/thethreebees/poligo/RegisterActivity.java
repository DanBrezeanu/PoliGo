package com.thethreebees.poligo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

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


        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


                Intent intent = new Intent(RegisterActivity.this, PaymentDetailsActivity.class);
                intent.putExtra("username", editTextUsername.getText().toString());
                intent.putExtra("password", editTextPassword.getText().toString());
                intent.putExtra("email", editTextEmail.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
}
