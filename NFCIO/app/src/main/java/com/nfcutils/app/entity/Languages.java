package com.nfcutils.app.entity;

/**
 * Created by mhasan on 01-Dec-16.
 */

public class Languages {
    private String languageName;
    private boolean isSelected;

    public Languages(String lang, boolean select){
        languageName = lang;
        isSelected = select;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
