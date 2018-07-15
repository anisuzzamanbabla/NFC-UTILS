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
import com.nfcutils.app.adapter.MifareClassicAdapter;
import com.nfcutils.app.entity.MifareClassicRoot;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonConstraint;
import com.nfcutils.app.utils.CommonValues;

public class MifareClassicDetailsActivity extends AppCompatActivity implements itemClick {

    private TextView uid, type, memory, sector, block;
    private RecyclerView recyclerView;
    private MifareClassicAdapter adapter;
    InterstitialAd mInterstitialAd;
    int positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.add_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent intent = new Intent(MifareClassicDetailsActivity.this, MifareClassic1kWriteActivity.class);
                intent.putExtra("id", positions);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
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

    private void initialization() {
        uid = (TextView) findViewById(R.id.tvUid);
        type = (TextView) findViewById(R.id.tvtype);
        memory = (TextView) findViewById(R.id.tvmemory);
        sector = (TextView) findViewById(R.id.tvnos);
        block = (TextView) findViewById(R.id.tvnob);

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewMFC);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    private void setValues() {
        if(CommonValues.getInstance().root != null){
            if(CommonValues.getInstance().root.error == CommonConstraint.CARD_AUTHENTICATION_FAILED){
                new AlertDialog.Builder(this)
                        .setTitle(R.string.nfc_write)
                        .setMessage(R.string.authentication_failed)
                        .setCancelable(false)
                        .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                onBackPressed();
                            }
                        })
                        .create().show();
            }else{
                if(CommonValues.getInstance().root != null){
                    uid.setText(CommonValues.getInstance().root.uid==null?"":CommonValues.getInstance().root.uid);
                    type.setText(CommonValues.getInstance().root.type==null?"":CommonValues.getInstance().root.type);
                    memory.setText(CommonValues.getInstance().root.memory==null?"":(CommonValues.getInstance().root.memory + "Byte"));
                    sector.setText(CommonValues.getInstance().root.nos==null?"":CommonValues.getInstance().root.nos);
                    block.setText(CommonValues.getInstance().root.nob==null?"":CommonValues.getInstance().root.nob);

                    adapter = new MifareClassicAdapter(this, CommonValues.getInstance().root);
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

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(this, MifareClassic1kWriteActivity.class);
            intent.putExtra("id", positions);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

    }
}
