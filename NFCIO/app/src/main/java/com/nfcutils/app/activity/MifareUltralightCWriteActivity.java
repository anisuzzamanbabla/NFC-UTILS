package com.nfcutils.app.activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
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
import com.nfcutils.app.entity.UltralightC;
import com.nfcutils.app.utils.CommonTask;
import com.nfcutils.app.utils.CommonValues;
import com.nfcutils.app.utils.NFCUtils;

public class MifareUltralightCWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rgMFC;
    private TextView page, block1, block2, block3, block4;
    private LinearLayout mfcWritePanel;
    private TextInputLayout tilBlock1Value, tilBlock2Value, tilBlock3Value, tilBlock4Value;
    private Button button_Write;
    private int position;
    private boolean isWrite = false;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private UltralightC mifareUltralight;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifare_ultralight_cwrite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
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

    private void initialization() {
        rgMFC = (RadioGroup) findViewById(R.id.rgMFC);

        page = (TextView) findViewById(R.id.tvPage);
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

    private void setValueIntoDesireFields() {
        try{
            mifareUltralight = CommonValues.getInstance().ultralightCRoot.ultralightCs.get(position);

            if(mifareUltralight != null){
                page.setText(mifareUltralight.page);
                block1.setText(mifareUltralight.pageValue1);
                block2.setText(mifareUltralight.pageValue2);
                block3.setText(mifareUltralight.pageValue3);
                block4.setText(mifareUltralight.pageValue4);
            }

        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void convertToAscii() {
        try{
            tilBlock1Value.setHint(getString(R.string.write_page)+(position*4)+getString(R.string.values));
            tilBlock2Value.setHint(getString(R.string.write_page)+((position*4)+1)+getString(R.string.values));
            tilBlock3Value.setHint(getString(R.string.write_page)+((position*4)+2)+getString(R.string.values));
            tilBlock4Value.setHint(getString(R.string.write_page)+((position*4)+3)+getString(R.string.values));

            tilBlock1Value.getEditText().setText(CommonTask.hexToAscii(mifareUltralight.pageValue1).trim());
            tilBlock2Value.getEditText().setText(CommonTask.hexToAscii(mifareUltralight.pageValue2).trim());
            tilBlock3Value.getEditText().setText(CommonTask.hexToAscii(mifareUltralight.pageValue3).trim());
            tilBlock4Value.getEditText().setText(CommonTask.hexToAscii(mifareUltralight.pageValue4).trim());
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
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
                if(tagList[tagIndex].equals(MifareUltralight.class.getName())){
                    MifareUltralight mifareUltralight = MifareUltralight.get(tag);
                    switch (mifareUltralight.getType()){
                        case MifareUltralight.TYPE_ULTRALIGHT_C:
                            if(isWrite){
                                String[] values = new String[]{
                                        tilBlock1Value.getEditText().getText().toString(),
                                        tilBlock2Value.getEditText().getText().toString(),
                                        tilBlock3Value.getEditText().getText().toString(),
                                        tilBlock4Value.getEditText().getText().toString()
                                };
                                NFCUtils.writeUltralightValue(this, mifareUltralight, position, values);
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
}
