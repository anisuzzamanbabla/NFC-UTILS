package com.nfcutils.app.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nfcutils.app.R;

public class TagInfoClassic1kActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_info_classic1k);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.tvTagInfoMFC);

        String value = "<html>\n" +
                "<body>\n" +
                getString(R.string.mfc_1k_details)+
                "</body>\n" +
                "</html>\n" +
                "\n";

        webView.loadData(value,"text/html","UTF-8");
    }
}
