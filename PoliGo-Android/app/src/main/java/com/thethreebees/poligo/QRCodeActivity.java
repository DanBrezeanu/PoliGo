package com.thethreebees.poligo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bitmap myBitmap = QRCode.from("www.example.org").bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.imageView);
        myImage.setImageBitmap(myBitmap);
    }

    public void onShoppingDone(View v) {
        Intent backToMain = new Intent(QRCodeActivity.this, MainActivity.class);
        startActivity(backToMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
