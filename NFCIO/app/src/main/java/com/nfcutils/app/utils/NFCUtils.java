package com.nfcutils.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.support.v7.app.AlertDialog;

import com.nfcutils.app.R;
import com.nfcutils.app.activity.MifareClassic1kWriteActivity;
import com.nfcutils.app.entity.MifareClassicRoot;
import com.nfcutils.app.entity.UltralightC;
import com.nfcutils.app.entity.UltralightCRoot;

/**
 * Created by mahbubhasan on 11/25/16.
 */

public class NFCUtils {

    public static void readMifareClassicCardInfo(MifareClassic mfc){
        try{
            mfc.connect();
            CommonValues.getInstance().root = new MifareClassicRoot();
            CommonValues.getInstance().root.uid = CommonTask.getHexString(mfc.getTag().getId());
            CommonValues.getInstance().root.type = String.valueOf(mfc.getType());
            CommonValues.getInstance().root.memory = String.valueOf(mfc.getSize());
            CommonValues.getInstance().root.nob = String.valueOf(mfc.getBlockCount());
            CommonValues.getInstance().root.nos = String.valueOf(mfc.getSectorCount());

            if(CommonValues.getInstance().root.mifareClassics != null &&
                    CommonValues.getInstance().root.mifareClassics.size()>0) {
                CommonValues.getInstance().root.mifareClassics.clear();
            }
            for(int sector = 0; sector < mfc.getSectorCount(); sector++){
                com.nfcutils.app.entity.MifareClassic mifareClassic = new com.nfcutils.app.entity.MifareClassic();
                mifareClassic.sector = String.valueOf(sector);
                if(CommonTask.authentication(mfc, sector)){
                    for(int block = 0; block<mfc.getBlockCountInSector(sector); block++){
                        byte[] readValue = mfc.readBlock((sector*4)+block);
                        String blockValue = CommonTask.getHexString(readValue);
                        switch (block){
                            case 0:
                                mifareClassic.block1 = blockValue;
                                break;
                            case 1:
                                mifareClassic.block2 = blockValue;
                                break;
                            case 2:
                                mifareClassic.block3 = blockValue;
                                break;
                            case 3:
                                mifareClassic.block4 = blockValue;
                                break;
                        }

                    }
                    CommonValues.getInstance().root.mifareClassics.add(mifareClassic);
                }else{
                    CommonValues.getInstance().root.error = CommonConstraint.CARD_AUTHENTICATION_FAILED;
                    break;
                }
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }finally {
            try{

            }catch (Exception ex){
                CommonTask.showLog(ex.getMessage());
            }
        }
    }


    public static void ultralightCRead(Context context, MifareUltralight mfu){
        try {
            mfu.connect();
            CommonValues.getInstance().ultralightCRoot = new UltralightCRoot();
            CommonValues.getInstance().ultralightCRoot.uid = CommonTask.getHexString(mfu.getTag().getId());
            CommonValues.getInstance().ultralightCRoot.memory = "192 bytes";
            CommonValues.getInstance().ultralightCRoot.pageSize = String.valueOf(mfu.PAGE_SIZE);
            CommonValues.getInstance().ultralightCRoot.pageCount = "44";

            for(int page=0;page<11;page++){
                UltralightC ultralightC = new UltralightC();
                ultralightC.page = context.getString(R.string.page)+" "+(page*4)+context.getString(R.string.to)+(((page+1)*4)-1);
                ultralightC.pageValue1 = CommonTask.getHexString(mfu.readPages(page*4)).substring(0,8);
                ultralightC.pageValue2 = CommonTask.getHexString(mfu.readPages((page*4)+1)).substring(0,8);
                if (page == 10 && CommonTask.getHexString(mfu.readPages(41)).contains(CommonTask.getHexString(mfu.readPages(0)).substring(0, 16))) {
                    ultralightC.page = context.getString(R.string.page)+" "+ (page * 4) + context.getString(R.string.to)+"41";
                    CommonValues.getInstance().ultralightCRoot.memory = "168 bytes";
                    CommonValues.getInstance().ultralightCRoot.pageCount = "42";
                    ultralightC.pageValue3 = "";
                    ultralightC.pageValue3 = "";
                    CommonValues.getInstance().ultralightCRoot.ultralightCs.add(ultralightC);

                    break;
                }
                ultralightC.pageValue3 = CommonTask.getHexString(mfu.readPages((page*4)+2)).substring(0,8);
                ultralightC.pageValue4 = CommonTask.getHexString(mfu.readPages((page*4)+3)).substring(0,8);

                CommonValues.getInstance().ultralightCRoot.ultralightCs.add(ultralightC);
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }finally {
            try {
                mfu.close();
            }catch (Exception ex){
                CommonTask.showLog(ex.getMessage());
            }
        }
    }

    public static void write1kCard(final Context context, MifareClassic mfc, int sector, String[] vales){
        try{
            byte[] destinationObject = new byte[16];
            mfc.connect();

            if(CommonTask.authentication(mfc, sector)){
                if(sector == 0){
                    byte[] block1Value = vales[0].getBytes();
                    byte[] block2Value = vales[1].getBytes();

                    System.arraycopy(block1Value, 0, destinationObject, 0, block1Value.length);
                    mfc.writeBlock(1, destinationObject);

                    destinationObject = new byte[16];
                    System.arraycopy(block2Value, 0, destinationObject, 0, block2Value.length);
                    mfc.writeBlock(2, destinationObject);

                }else{
                    byte[] block1Value = vales[0].getBytes();
                    byte[] block2Value = vales[1].getBytes();
                    byte[] block3Value = vales[2].getBytes();


                    System.arraycopy(block1Value, 0, destinationObject, 0, block1Value.length);
                    mfc.writeBlock((sector*4), destinationObject);

                    destinationObject = new byte[16];
                    System.arraycopy(block2Value, 0, destinationObject, 0, block2Value.length);
                    mfc.writeBlock((sector*4)+1, destinationObject);

                    destinationObject = new byte[16];
                    System.arraycopy(block3Value, 0, destinationObject, 0, block3Value.length);
                    mfc.writeBlock((sector*4)+2, destinationObject);
                }

                mfc.close();
                readMifareClassicCardInfo(mfc);
            }else{
                new AlertDialog.Builder(context)
                        .setTitle(R.string.nfc_write)
                        .setMessage(R.string.authentication_failed)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                ((MifareClassic1kWriteActivity)context).onBackPressed();
                            }
                        })
                        .create().show();
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    public static void writeUltralightValue(Context context, MifareUltralight mfa, int page ,String[] values){
        try {
            mfa.connect();
            byte[] destObject = new byte[4];

            System.arraycopy(values[0].getBytes(), 0, destObject, 0, values[0].getBytes().length);
            mfa.writePage((page*4), destObject);

            destObject = new byte[4];
            System.arraycopy(values[1].getBytes(), 0, destObject, 0, values[1].getBytes().length);
            mfa.writePage(((page*4)+1), destObject);

            destObject = new byte[4];
            System.arraycopy(values[2].getBytes(), 0, destObject, 0, values[2].getBytes().length);
            mfa.writePage(((page*4)+2), destObject);

            destObject = new byte[4];
            System.arraycopy(values[3].getBytes(), 0, destObject, 0, values[3].getBytes().length);
            mfa.writePage(((page*4)+3), destObject);

            mfa.close();
            ultralightCRead(context, mfa);
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }
}
