package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startShoppingClick(View v) {
        Intent intent = new Intent(this, ShoppingList.class);
        startActivity(intent);
    }

    public void exitApp(View v) {
        finish();
    }

    public void testRequest(View v) {
//        final EditText textView = (EditText) findViewById(R.id.editText);

        RequestQueue queue = Volley.newRequestQueue(this);

        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority("192.168.0.248:8000")
                .path("api/v1/checkstocks")
                .build();

        Log.d("INFO::", uri.toString());
        String url = uri.toString();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.d("response.code", response.get("code").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        queue.add(getRequest);
    }
}
