package com.example.user.smartplugincontoller;

import android.view.View;

public class CostumeOnClickListener implements View.OnClickListener {
    private  int pos;
    private  OnItemClickCallback onItemClickCallback;
    public CostumeOnClickListener(int pos, OnItemClickCallback onItemClickCallback){
        this.pos =pos;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v,pos);

    }
    public interface OnItemClickCallback{
        void onItemClicked(View v, int position);
    }
}
