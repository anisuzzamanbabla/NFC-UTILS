package com.nfcutils.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.nfcutils.app.R;

public class SplashScreenActivity extends AppCompatActivity {

    HTextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        text = (HTextView) findViewById(R.id.text);
        text.animateText(getString(R.string.welcome_nfc_note));
        text.setAnimateType(HTextViewType.TYPER);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreenActivity.this.finish();
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }, 5000);
    }
}
