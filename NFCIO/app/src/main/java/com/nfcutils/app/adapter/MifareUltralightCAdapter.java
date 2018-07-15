package com.nfcutils.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfcutils.app.R;
import com.nfcutils.app.entity.UltralightC;
import com.nfcutils.app.entity.UltralightCRoot;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonTask;

/**
 * Created by mahbubhasan on 11/26/16.
 */

public class MifareUltralightCAdapter extends RecyclerView.Adapter<MifareUltralightCAdapter.MifareUltralightCVH>{

    UltralightC ultralightC;
    UltralightCRoot ultralightCRoot;
    itemClick mItemClick;
    Context context;

    public MifareUltralightCAdapter(Context con, UltralightCRoot root){
        ultralightCRoot = root;
        context = con;
    }

    @Override
    public MifareUltralightCVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MifareUltralightCVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mifare_ultralight, parent, false));
    }

    @Override
    public void onBindViewHolder(MifareUltralightCVH holder, int position) {
        try {
            ultralightC = ultralightCRoot.ultralightCs.get(position);
            if(ultralightC != null){
                if(ultralightC.page.equals(context.getString(R.string.adapter_page1)) || ultralightC.page.equals(context.getString(R.string.adapter_page2)) || ultralightC.page.equals(context.getString(R.string.adapter_page3))){
                    holder.tvBlock1.setTextColor(Color.parseColor("#FF0000"));
                    holder.tvBlock2.setTextColor(Color.parseColor("#FF0000"));
                    holder.tvBlock3.setTextColor(Color.parseColor("#FF0000"));
                    holder.tvBlock4.setTextColor(Color.parseColor("#FF0000"));
                }
                holder.tvPages.setText(ultralightC.page==null?"":ultralightC.page);
                holder.tvBlock1.setText(ultralightC.pageValue1==null?"":ultralightC.pageValue1);
                holder.tvBlock2.setText(ultralightC.pageValue2==null?"":ultralightC.pageValue2);
                holder.tvBlock3.setText(ultralightC.pageValue3==null?"":ultralightC.pageValue3);
                holder.tvBlock4.setText(ultralightC.pageValue4==null?"":ultralightC.pageValue4);
            }

        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return ultralightCRoot.ultralightCs.size();
    }

    class MifareUltralightCVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvPages, tvBlock1, tvBlock2, tvBlock3, tvBlock4;

        public MifareUltralightCVH(View itemView) {
            super(itemView);
            tvPages = (TextView) itemView.findViewById(R.id.tvPage);
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
