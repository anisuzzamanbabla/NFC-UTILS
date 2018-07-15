package com.nfcutils.app.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.nfcutils.app.R;

public class AboutUSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textview4 = (TextView) findViewById(R.id.textView4);
        textview4.setClickable(true);
        textview4.setMovementMethod(LinkMovementMethod.getInstance());
        String text1 = "<a href='http://www.muktolab.com'>About Mukto Lab</a>";
        textview4.setText(Html.fromHtml(text1));
    }

}
