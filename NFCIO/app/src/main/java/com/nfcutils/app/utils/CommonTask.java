package com.nfcutils.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.tech.MifareClassic;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.nfcutils.app.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by mahbubhasan on 11/25/16.
 */

public class CommonTask {

    public static void generalDialog(Context context, String message){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(R.string.app_name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    public static void showWirelessSettingsDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.nfc_disabled);
        builder.setCancelable(false);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(R.string._ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(
                                Settings.ACTION_NFC_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        builder.create().show();
    }

    public static void showLog(String message){
        Log.d(CommonConstraint.TAG, message == null?"":message);
    }

    public static String getHexString(byte[] raw)
            throws UnsupportedEncodingException
    {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    public static final byte[] HEX_CHAR_TABLE = {
            (byte)'0', (byte)'1', (byte)'2', (byte)'3',
            (byte)'4', (byte)'5', (byte)'6', (byte)'7',
            (byte)'8', (byte)'9', (byte)'a', (byte)'b',
            (byte)'c', (byte)'d', (byte)'e', (byte)'f'
    };

    public static  boolean authentication(MifareClassic mfc, int sector){
        try{
            if(mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT)){
                return true;
            }else if(mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)){
                return true;
            }else if(mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_NFC_FORUM)){
                return true;
            }else if(mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_DEFAULT)){
                return true;
            }else if(mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)){
                return true;
            }else if(mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_NFC_FORUM)){
                return true;
            }
        }catch (Exception ex){
            showLog(ex.getMessage());
        }
        return false;
    }

    public static String hexToAscii(String hexValue) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexValue.length(); i += 2) {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferences(Context context, String prefKey) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(prefKey, "");
    }

}
