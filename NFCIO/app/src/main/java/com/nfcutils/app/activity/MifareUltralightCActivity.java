package com.nfcutils.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nfcutils.app.R;
import com.nfcutils.app.adapter.MifareUltralightCAdapter;
import com.nfcutils.app.entity.UltralightC;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonValues;

public class MifareUltralightCActivity extends AppCompatActivity implements itemClick {

    private TextView uid, memory, pagesize, pagecount;
    private RecyclerView recyclerView;
    private MifareUltralightCAdapter adapter;
    InterstitialAd mInterstitialAd;
    int positions;
    private UltralightC ultralightC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifare_ultralight_c);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.add_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

                if(ultralightC != null){
                    if(ultralightC.page.equals("Page 0 to 3") || ultralightC.page.equals("Page 40 to 43") || ultralightC.page.equals("Page 40 to 41")){
                        new AlertDialog.Builder(MifareUltralightCActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage(R.string.uc_help_text)
                                .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create().show();
                    }else{
                        Intent intent = new Intent(MifareUltralightCActivity.this, MifareUltralightCWriteActivity.class);
                        intent.putExtra("id", positions);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }
            }
        });

        requestNewInterstitial();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();
    }

    private void initialization() {
        uid = (TextView) findViewById(R.id.tvUid);
        memory = (TextView) findViewById(R.id.tvmemory);
        pagesize = (TextView) findViewById(R.id.tvPageSize);
        pagecount = (TextView) findViewById(R.id.tvPageCount);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewMFU);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setValue() {
        if(CommonValues.getInstance().ultralightCRoot != null){
            uid.setText(CommonValues.getInstance().ultralightCRoot.uid==null?"":CommonValues.getInstance().ultralightCRoot.uid);
            memory.setText(CommonValues.getInstance().ultralightCRoot.memory==null?"":CommonValues.getInstance().ultralightCRoot.memory);
            pagecount.setText(CommonValues.getInstance().ultralightCRoot.pageCount==null?"":CommonValues.getInstance().ultralightCRoot.pageCount);
            pagesize.setText(CommonValues.getInstance().ultralightCRoot.pageSize==null?"":CommonValues.getInstance().ultralightCRoot.pageSize);

            adapter = new MifareUltralightCAdapter(this, CommonValues.getInstance().ultralightCRoot);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClick(this);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(R.string.card_info)
                    .setMessage(R.string.card_info_not_found)
                    .setCancelable(false)
                    .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    })
                    .create().show();
        }

    }

    @Override
    public void onItemClick(int position) {
        positions = position;
        ultralightC = CommonValues.getInstance().ultralightCRoot.ultralightCs.get(position);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            if(ultralightC != null){
                if(ultralightC.page.equals("Page 0 to 3") || ultralightC.page.equals("Page 40 to 43") || ultralightC.page.equals("Page 40 to 41")){
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.uc_help_text)
                            .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create().show();
                }else{
                    Intent intent = new Intent(this, MifareUltralightCWriteActivity.class);
                    intent.putExtra("id", positions);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        }
    }
}
