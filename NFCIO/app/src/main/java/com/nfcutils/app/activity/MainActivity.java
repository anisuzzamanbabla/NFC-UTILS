package com.nfcutils.app.activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nfcutils.app.R;
import com.nfcutils.app.utils.CommonTask;
import com.nfcutils.app.utils.NFCUtils;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    TextView tvTapText;
    Animation textBlinkAnimation;
    InterstitialAd mInterstitialAd;
    boolean is1k = false, isULC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_main);

        initialization();
    }

    private void initialization() {
        tvTapText = (TextView) findViewById(R.id.tvTapText);
        textBlinkAnimation = AnimationUtils.loadAnimation(this, R.anim.text_blink);
        textBlinkAnimation.setAnimationListener(this);
        tvTapText.setVisibility(View.VISIBLE);
        tvTapText.setAnimation(textBlinkAnimation);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.add_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if(is1k){
                    Intent intent = new Intent(MainActivity.this, TagInfoClassic1kActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }else if(isULC){
                    Intent intent = new Intent(MainActivity.this, TagInfoUltralightCActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, AboutUSActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });

        requestNewInterstitial();

        adapter = NfcAdapter.getDefaultAdapter(this);
        if(adapter == null){
            CommonTask.generalDialog(this, getString(R.string.no_nfc));
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
        if(adapter != null){
            if(!adapter.isEnabled()){
                CommonTask.showWirelessSettingsDialog(this);
            }
            adapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adapter != null)
            adapter.disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_tag_information){
            selectTagInfo();
        }else if(item.getItemId() == R.id.action_aboutUs){
            isULC = is1k = false;
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent intent = new Intent(MainActivity.this, AboutUSActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }else if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        showProgressBar();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setIntent(intent);
                resolveIntent(intent);
            }
        },2000);

    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equalsIgnoreCase(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equalsIgnoreCase(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equalsIgnoreCase(action)){
            getTagInfo(intent);
        }else{
            hideProgressBar();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.tag_discover)
                    .setMessage(R.string.card_tag_not_found)
                    .setCancelable(false)
                    .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    private void getTagInfo(Intent intent) {
        try{
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] tagList = tag.getTechList();
            for(int tagIndex = 0; tagIndex<tagList.length; tagIndex++){
                if(tagList[tagIndex].equals(MifareClassic.class.getName())){
                    MifareClassic mfc = MifareClassic.get(tag);
                    switch (mfc.getType()){
                        case MifareClassic.TYPE_CLASSIC:
                            NFCUtils.readMifareClassicCardInfo(mfc);
                            hideProgressBar();
                            startActivity(new Intent(this, MifareClassicDetailsActivity.class));
                            break;
                        case MifareClassic.TYPE_PLUS:
                            hideProgressBar();
                            showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                            break;
                        case MifareClassic.TYPE_PRO:
                            hideProgressBar();
                            showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                            break;
                        case MifareClassic.TYPE_UNKNOWN:
                            hideProgressBar();
                            showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                            break;
                    }
                }else if(tagList[tagIndex].equals(MifareUltralight.class.getName())){
                    MifareUltralight mifareUltralight = MifareUltralight.get(tag);
                    switch (mifareUltralight.getType()){
                        case MifareUltralight.TYPE_ULTRALIGHT_C:
                            NFCUtils.ultralightCRead(this, mifareUltralight);
                            hideProgressBar();
                            startActivity(new Intent(this, MifareUltralightCActivity.class));
                            break;
                        case MifareUltralight.TYPE_ULTRALIGHT:
                            hideProgressBar();
                            showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                            break;
                        case MifareUltralight.TYPE_UNKNOWN:
                            hideProgressBar();
                            showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                            break;
                    }
                }else if(tagList[tagIndex].equalsIgnoreCase(IsoDep.class.getName())){
                    hideProgressBar();
                    showNotSupportedCardAlert(getString(R.string.supported_tag_type));
                    break;
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void showProgressBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loadCard).setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideProgressBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loadCard).setVisibility(View.GONE);
            }
        });
    }

    private void showNotSupportedCardAlert(String message){
        new AlertDialog.Builder(this)
                .setTitle(R.string.card_support)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.app_exit)
                .setCancelable(false)
                .setPositiveButton(R.string._ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string._cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == textBlinkAnimation){
            tvTapText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public void selectTagInfo()
    {
        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.dialog_tag_info);
        dialog.setTitle(R.string.title_tag_info_dialog);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width= ActionBar.LayoutParams.MATCH_PARENT;

        Button mifareClassic1k = (Button) dialog.findViewById(R.id.btn_1k_info);
        Button mifareUltralightC = (Button) dialog.findViewById(R.id.btn_ultralight_c_info);

        mifareClassic1k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                is1k = true;
                isULC = false;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, TagInfoClassic1kActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });

        mifareUltralightC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                is1k = false;
                isULC = true;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, TagInfoUltralightCActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });

        dialog.show();
    }


}
