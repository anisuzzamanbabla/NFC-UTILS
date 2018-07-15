package com.nfcutils.app.activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nfcutils.app.R;
import com.nfcutils.app.entity.MifareClassic;
import com.nfcutils.app.utils.CommonTask;
import com.nfcutils.app.utils.CommonValues;
import com.nfcutils.app.utils.NFCUtils;

public class MifareClassic1kWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rgMFC;
    private TextView sector, block1, block2, block3, block4;
    private LinearLayout mfcWritePanel;
    private TextInputLayout tilBlock1Value, tilBlock2Value, tilBlock3Value, tilBlock4Value;
    private Button button_Write;
    private int position;
    private MifareClassic mifareClassic;
    private boolean isWrite = false;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifare_classic1k_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void initialization() {
        rgMFC = (RadioGroup) findViewById(R.id.rgMFC);

        sector = (TextView) findViewById(R.id.tvSector);
        block1 = (TextView) findViewById(R.id.tvBlock1);
        block2 = (TextView) findViewById(R.id.tvBlock2);
        block3 = (TextView) findViewById(R.id.tvBlock3);
        block4 = (TextView) findViewById(R.id.tvBlock4);

        mfcWritePanel = (LinearLayout) findViewById(R.id.mfcWritePanel);

        tilBlock1Value = (TextInputLayout) findViewById(R.id.tilBlock1Value);
        tilBlock2Value = (TextInputLayout) findViewById(R.id.tilBlock2Value);
        tilBlock3Value = (TextInputLayout) findViewById(R.id.tilBlock3Value);
        tilBlock4Value = (TextInputLayout) findViewById(R.id.tilBlock4Value);

        button_Write = (Button) findViewById(R.id.button_Write);

        // initially write panel is disable. So when UI-ASCII choose then it will be visible. Same way button status;
        mfcWritePanel.setVisibility(View.GONE);
        button_Write.setVisibility(View.GONE);

        rgMFC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == R.id.rbHex){
                    mfcWritePanel.setVisibility(View.GONE);
                    button_Write.setVisibility(View.GONE);
                    convertToHex();
                }else{
                    mfcWritePanel.setVisibility(View.VISIBLE);
                    button_Write.setVisibility(View.VISIBLE);
                    convertToAscii();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            position = bundle.getInt("id");
        }

        adapter = NfcAdapter.getDefaultAdapter(this);
        if(adapter == null){
            CommonTask.generalDialog(this, getString(R.string.no_nfc));
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        button_Write.setOnClickListener(this);

        setValueIntoDesireFields();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            if(adapter.isEnabled()){
                adapter.enableForegroundDispatch(this, pendingIntent, null, null);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adapter != null)
            adapter.disableForegroundDispatch(this);
    }

    private void setValueIntoDesireFields() {
        try{
            mifareClassic = CommonValues.getInstance().root.mifareClassics.get(position);

            if(mifareClassic != null){
                sector.setText(getString(R.string.adapter_sector_number)+mifareClassic.sector);
                block1.setText(mifareClassic.block1);
                block2.setText(mifareClassic.block2);
                block3.setText(mifareClassic.block3);
                block4.setText(mifareClassic.block4);
            }

            if(mifareClassic.sector.equals("0")){
                block1.setTextColor(Color.parseColor("#FF0000"));
                block4.setTextColor(Color.parseColor("#FF0000"));
            }else{
                block4.setTextColor(Color.parseColor("#FF0000"));
            }

        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void convertToHex() {

    }

    private void convertToAscii(){
        try{
            if(mifareClassic.sector.equals("0")){
                tilBlock1Value.setVisibility(View.GONE);
                tilBlock4Value.setVisibility(View.GONE);

                tilBlock2Value.setHint(getString(R.string.write_1_value));
                tilBlock3Value.setHint(getString(R.string.write_2_values));

                tilBlock2Value.getEditText().setText(CommonTask.hexToAscii(mifareClassic.block2).trim());
                tilBlock3Value.getEditText().setText(CommonTask.hexToAscii(mifareClassic.block3).trim());
            }else{
                tilBlock4Value.setVisibility(View.GONE);

                tilBlock1Value.setHint(getString(R.string.write_block)+(Integer.parseInt(mifareClassic.sector)*4)+getString(R.string.values));
                tilBlock2Value.setHint(getString(R.string.write_block)+((Integer.parseInt(mifareClassic.sector)*4)+1)+getString(R.string.values));
                tilBlock3Value.setHint(getString(R.string.write_block)+((Integer.parseInt(mifareClassic.sector)*4)+2)+getString(R.string.values));

                tilBlock1Value.getEditText().setText(CommonTask.hexToAscii(mifareClassic.block1).trim());
                tilBlock2Value.getEditText().setText(CommonTask.hexToAscii(mifareClassic.block2).trim());
                tilBlock3Value.getEditText().setText(CommonTask.hexToAscii(mifareClassic.block3).trim());
            }

        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equalsIgnoreCase(action)){
            getTagInfo(intent);
        }
    }

    private void getTagInfo(Intent intent) {
        try{
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] tagList = tag.getTechList();
            for(int tagIndex = 0; tagIndex<tagList.length; tagIndex++){
                if(tagList[tagIndex].equals(android.nfc.tech.MifareClassic.class.getName())){
                    android.nfc.tech.MifareClassic mfc = android.nfc.tech.MifareClassic.get(tag);
                    switch (mfc.getType()){
                        case android.nfc.tech.MifareClassic.TYPE_CLASSIC:
                            if(isWrite){
                                String[] values;
                                if(mifareClassic.sector.equals("0")){
                                    values = new String[]{
                                            tilBlock2Value.getEditText().getText().toString(),
                                            tilBlock3Value.getEditText().getText().toString()
                                    };
                                }else{
                                    values = new String[]{
                                            tilBlock1Value.getEditText().getText().toString(),
                                            tilBlock2Value.getEditText().getText().toString(),
                                            tilBlock3Value.getEditText().getText().toString()
                                    };
                                }
                                NFCUtils.write1kCard(this, mfc, Integer.parseInt(mifareClassic.sector),values);
                                dialog.dismiss();
                                onBackPressed();
                            }
                            break;
                    }
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_Write){
            isWrite = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setCancelable(false);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_card_write, null);
            builder.setView(dialogView);
            final TextView tvWriteCard = (TextView) dialogView.findViewById(R.id.tvWriteCard);
            final Animation animationCardWrite = AnimationUtils.loadAnimation(this, R.anim.text_blink);
            animationCardWrite.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(animation == animationCardWrite){
                        tvWriteCard.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tvWriteCard.setVisibility(View.VISIBLE);
            tvWriteCard.setAnimation(animationCardWrite);
            builder.setPositiveButton(R.string._cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isWrite = false;
                    dialogInterface.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }
}
