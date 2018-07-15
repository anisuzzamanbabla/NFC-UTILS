package com.nfcutils.app.utils;


import com.nfcutils.app.entity.MifareClassicRoot;
import com.nfcutils.app.entity.UltralightCRoot;

/**
 * Created by mahbubhasan on 11/25/16.
 */
public class CommonValues {
    private static CommonValues ourInstance = new CommonValues();

    public static CommonValues getInstance() {
        return ourInstance;
    }

    public MifareClassicRoot root;
    public UltralightCRoot ultralightCRoot;

    private CommonValues() {
    }
}
