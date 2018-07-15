package com.nfcutils.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfcutils.app.R;
import com.nfcutils.app.entity.MifareClassic;
import com.nfcutils.app.entity.MifareClassicRoot;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonTask;

/**
 * Created by mahbubhasan on 11/25/16.
 */

public class MifareClassicAdapter extends RecyclerView.Adapter<MifareClassicAdapter.MifareClassicVH> {


    MifareClassic mifareClassic;
    MifareClassicRoot root;
    itemClick mItemClick;
    Context context;

    public MifareClassicAdapter(Context con, MifareClassicRoot mifareClassicRoot){
        root = mifareClassicRoot;
        context = con;
    }

    @Override
    public MifareClassicVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MifareClassicVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mifare_classic, parent, false));
    }

    @Override
    public void onBindViewHolder(MifareClassicVH holder, int position) {
        try {
            mifareClassic = root.mifareClassics.get(position);
            if(mifareClassic != null){
                holder.tvSector.setText(mifareClassic.sector==null?context.getString(R.string.adapter_null):(context.getString(R.string.adapter_sector_number)+mifareClassic.sector));
                if(mifareClassic.sector.equals("0")){
                    holder.tvBlock1.setText(mifareClassic.block1==null?context.getString(R.string.adapter_null):mifareClassic.block1);
                    holder.tvBlock1.setTextColor(Color.parseColor("#FFB42C31"));
                    holder.tvBlock2.setText(mifareClassic.block2==null?context.getString(R.string.adapter_null):mifareClassic.block2);
                    holder.tvBlock3.setText(mifareClassic.block3==null?context.getString(R.string.adapter_null):mifareClassic.block3);
                    holder.tvBlock4.setText(mifareClassic.block4==null?context.getString(R.string.adapter_null):mifareClassic.block4);
                    holder.tvBlock4.setTextColor(Color.parseColor("#FFB42C31"));
                }else{
                    holder.tvBlock1.setText(mifareClassic.block1==null?context.getString(R.string.adapter_null):mifareClassic.block1);
                    holder.tvBlock2.setText(mifareClassic.block2==null?context.getString(R.string.adapter_null):mifareClassic.block2);
                    holder.tvBlock3.setText(mifareClassic.block3==null?context.getString(R.string.adapter_null):mifareClassic.block3);
                    holder.tvBlock4.setText(mifareClassic.block4==null?context.getString(R.string.adapter_null):mifareClassic.block4);
                    holder.tvBlock4.setTextColor(Color.parseColor("#FFB42C31"));
                }
            }

        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return root.mifareClassics.size();
    }

    class MifareClassicVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvSector, tvBlock1, tvBlock2, tvBlock3, tvBlock4;

        public MifareClassicVH(View itemView) {
            super(itemView);
            tvSector = (TextView) itemView.findViewById(R.id.tvSector);
            tvBlock1 = (TextView) itemView.findViewById(R.id.tvBlock1);
            tvBlock2 = (TextView) itemView.findViewById(R.id.tvBlock2);
            tvBlock3 = (TextView) itemView.findViewById(R.id.tvBlock3);
            tvBlock4 = (TextView) itemView.findViewById(R.id.tvBlock4);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClick != null)
                mItemClick.onItemClick(getAdapterPosition());
        }
    }

    public void setOnItemClick(itemClick onItemClick){
        mItemClick = onItemClick;
    }
}
