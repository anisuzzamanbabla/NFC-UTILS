package com.nfcutils.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mahbubhasan on 11/25/16.
 */

public class MifareClassicRoot implements Serializable{
    public String uid;
    public String type;
    public String memory;
    public String nos;
    public String nob;
    public int error;
    public ArrayList<MifareClassic> mifareClassics = new ArrayList<>();
}
