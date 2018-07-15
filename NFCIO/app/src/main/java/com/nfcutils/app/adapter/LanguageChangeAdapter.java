package com.nfcutils.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.nfcutils.app.R;
import com.nfcutils.app.entity.Languages;
import com.nfcutils.app.interfaces.itemClick;
import com.nfcutils.app.utils.CommonTask;

import java.util.ArrayList;

/**
 * Created by mhasan on 01-Dec-16.
 */

public class LanguageChangeAdapter extends RecyclerView.Adapter<LanguageChangeAdapter.LanguageChangeVH>{

    Languages languages;
    ArrayList<Languages> languagesArrayList;
    itemClick mItemClick;

    public LanguageChangeAdapter (ArrayList<Languages> list){
        languagesArrayList = list;
    }

    @Override
    public LanguageChangeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language_change, parent, false);
        return new LanguageChangeVH(view);
    }

    @Override
    public void onBindViewHolder(LanguageChangeVH holder, int position) {
        try {
            languages = languagesArrayList.get(position);
            holder.languageChangeText.setText(languages.getLanguageName());
            if(languages.isSelected()){
                holder.languageChangeText.setChecked(true);
            }else{
                holder.languageChangeText.setChecked(false);
            }
        }catch (Exception ex){
            CommonTask.showLog(ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return languagesArrayList.size();
    }

    class LanguageChangeVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckedTextView languageChangeText;

        public LanguageChangeVH(View itemView) {
            super(itemView);
            languageChangeText = (CheckedTextView) itemView.findViewById(R.id.languageChangeText);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClick != null)
                mItemClick.onItemClick(getAdapterPosition());
        }
    }

    public void setOnItemClick(itemClick itemClick){
        mItemClick = itemClick;
    }
}
