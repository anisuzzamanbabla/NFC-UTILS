package com.nfcutils.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.nfcutils.app.R;
import com.nfcutils.app.adapter.LanguageChangeAdapter;
import com.nfcutils.app.entity.Languages;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonConstraint;
import com.nfcutils.app.utils.CommonTask;
import com.nfcutils.app.utils.CommonValues;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageChangeActivity extends AppCompatActivity implements itemClick {

    RecyclerView recyclerView;
    ArrayList<Languages> languageList;
    LanguageChangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void initialization() {
        recyclerView = (RecyclerView) findViewById(R.id.lcRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        languageList = new ArrayList<>();

        // for english
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_ENGLISH).isEmpty()){
            languageList.add(new Languages(getString(R.string.eng_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.eng_lang), true));
        }

        // for bangle
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_BANGLE).isEmpty()){
            languageList.add(new Languages(getString(R.string.bang_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.bang_lang), true));
        }

        // for GERMANY
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_GERMANY).isEmpty()){
            languageList.add(new Languages(getString(R.string.germ_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.germ_lang), true));
        }

        // for SPANISH
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_SPANISH).isEmpty()){
            languageList.add(new Languages(getString(R.string.span_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.span_lang), true));
        }

        // for France
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_FRANCE).isEmpty()){
            languageList.add(new Languages(getString(R.string.frnch_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.frnch_lang), true));
        }

        // for Russia
        if(CommonTask.getPreferences(this, CommonConstraint.LANG_RUSSIAN).isEmpty()){
            languageList.add(new Languages(getString(R.string.rush_lang), false));
        }else{
            languageList.add(new Languages(getString(R.string.rush_lang), true));
        }


        adapter = new LanguageChangeAdapter(languageList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClick(this);

    }

    @Override
    public void onItemClick(int position) {
        try{
            Languages languages = languageList.get(position);
            if(languages != null){
                if(languages.getLanguageName().equalsIgnoreCase(getString(R.string.eng_lang))){
                    CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH, "1");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_BANGLE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_GERMANY, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_SPANISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_FRANCE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_RUSSIAN, "");
                    new LanguageChangeAsync().execute(CommonConstraint.LANG_ENGLISH);
                }else if(languages.getLanguageName().equalsIgnoreCase(getString(R.string.bang_lang))){
                    Toast.makeText(this, R.string.resticted_lang, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }else if(languages.getLanguageName().equalsIgnoreCase(getString(R.string.germ_lang))){
                    CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_BANGLE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_GERMANY, "1");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_SPANISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_FRANCE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_RUSSIAN, "");
                    new LanguageChangeAsync().execute(CommonConstraint.LANG_GERMANY);
                }else if(languages.getLanguageName().equalsIgnoreCase(getString(R.string.frnch_lang))){
                    CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_BANGLE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_GERMANY, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_SPANISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_FRANCE, "1");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_RUSSIAN, "");
                    new LanguageChangeAsync().execute(CommonConstraint.LANG_FRANCE);
                }else if(languages.getLanguageName().equalsIgnoreCase(getString(R.string.rush_lang))){
                    CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_BANGLE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_GERMANY, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_SPANISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_FRANCE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_RUSSIAN, "1");
                    new LanguageChangeAsync().execute(CommonConstraint.LANG_RUSSIAN);
                }else{
                    CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_BANGLE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_GERMANY, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_SPANISH, "1");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_FRANCE, "");
                    CommonTask.savePreferences(this, CommonConstraint.LANG_RUSSIAN, "");
                    new LanguageChangeAsync().execute(CommonConstraint.LANG_SPANISH);
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void setLanguage(String type){
        try{

            Locale myLocale = new Locale(type);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            restartApplication();
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    private void restartApplication() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    class LanguageChangeAsync extends AsyncTask<String, Void, String>{
        ProgressDialog languageChangeDialog;

        @Override
        protected void onPreExecute() {
            languageChangeDialog = new ProgressDialog(LanguageChangeActivity.this);
            languageChangeDialog.setTitle(getString(R.string.lang_change_diaglog));
            languageChangeDialog.setMessage(getString(R.string.dialog_wait_mess));
            languageChangeDialog.setCancelable(false);
            languageChangeDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(3000);
                return strings[0];
            }catch (Exception ex){
                CommonTask.showLog(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            languageChangeDialog.dismiss();
            if(!s.isEmpty()){
                setLanguage(s);
            }
        }
    }
}
