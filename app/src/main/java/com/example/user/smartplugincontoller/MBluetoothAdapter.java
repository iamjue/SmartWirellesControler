package com.example.user.smartplugincontoller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MBluetoothAdapter extends RecyclerView.Adapter<MBluetoothAdapter.ViewHolder> {
    ArrayList<Bluetooth> bluetooths;
    Context context ;

    public MBluetoothAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Bluetooth> getBluetooths() {
        return bluetooths;
    }

    public void setBluetooths(ArrayList<Bluetooth> bluetooths) {
        this.bluetooths = bluetooths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddress.setText(getBluetooths().get(position).getAdd());
        holder.tvName.setText(getBluetooths().get(position).getName());

    }

    @Override
    public int getItemCount() {
        return getBluetooths().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvName;
        @BindView(R.id.tv_item_address)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
