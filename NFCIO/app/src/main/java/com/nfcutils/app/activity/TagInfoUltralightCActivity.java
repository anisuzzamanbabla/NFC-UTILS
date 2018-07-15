package com.nfcutils.app.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.nfcutils.app.R;

public class TagInfoUltralightCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_info_ultralight_c);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.webView);

        String value = "<html>\n" +
                "<body>\n" +
                    getString(R.string.ulc_details)
                +"</body>\n" +
                "</html>\n" +
                "\n";

        webView.loadData(value,"text/html","UTF-8");
    }
}
