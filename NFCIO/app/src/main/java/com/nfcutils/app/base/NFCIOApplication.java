package com.nfcutils.app.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.nfcutils.app.utils.CommonConstraint;
import com.nfcutils.app.utils.CommonTask;

import java.util.Locale;

/**
 * Created by mhasan on 27-Nov-16.
 */

public class NFCIOApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!CommonTask.getPreferences(this, CommonConstraint.LANG_ENGLISH).isEmpty()){
            changeLanguage(CommonConstraint.LANG_ENGLISH);
        }else if(!CommonTask.getPreferences(this, CommonConstraint.LANG_BANGLE).isEmpty()){
            changeLanguage(CommonConstraint.LANG_BANGLE);
        }else if(!CommonTask.getPreferences(this, CommonConstraint.LANG_GERMANY).isEmpty()){
            changeLanguage(CommonConstraint.LANG_GERMANY);
        }else if(!CommonTask.getPreferences(this, CommonConstraint.LANG_FRANCE).isEmpty()){
            changeLanguage(CommonConstraint.LANG_FRANCE);
        }else if(!CommonTask.getPreferences(this, CommonConstraint.LANG_SPANISH).isEmpty()){
            changeLanguage(CommonConstraint.LANG_SPANISH);
        }else if(!CommonTask.getPreferences(this, CommonConstraint.LANG_RUSSIAN).isEmpty()){
            changeLanguage(CommonConstraint.LANG_RUSSIAN);
        }else{
            CommonTask.savePreferences(this, CommonConstraint.LANG_ENGLISH,"1");
            changeLanguage(CommonConstraint.LANG_ENGLISH);
        }
    }

    private void changeLanguage(String language) {
        try{
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Resources resources = getResources();

            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }
}
