package com.example.user.smartplugincontoller;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends DialogFragment {
   @BindView(R.id.RvCatagory)
   RecyclerView rvCatogry;
    ArrayList<Bluetooth> list;

    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device,container,false);
        ButterKnife.bind(this,view);
        buildDeviceView();
        list = new ArrayList<>();
        list.addAll(DeviceFragment.buildDeviceView());
        showlist();

        return view;
    }

    private void showlist() {
        rvCatogry.setLayoutManager(new LinearLayoutManager(getActivity()));
        MBluetoothAdapter bluetoothAdapterView = new MBluetoothAdapter(getContext());
        bluetoothAdapterView.setBluetooths(list);
        rvCatogry.setAdapter(bluetoothAdapterView);
        ItemClickSupport.addTo(rvCatogry).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(),"MAC "+list.get(position).getAdd(),Toast.LENGTH_SHORT).show();
            onDestroy();
            }
        });
    }

    public static ArrayList<Bluetooth> buildDeviceView() {
       ArrayList<Bluetooth> list = new ArrayList<>();


        BluetoothAdapter Bt= BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> device = Bt.getBondedDevices();
        if(device.size() != 0){
            for(BluetoothDevice item : device){
                Bluetooth bluetooth = new Bluetooth();
                bluetooth.setName(item.getName());
                bluetooth.setAdd(item.getAddress());
                list.add(bluetooth);
            }

        }
       return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyView();
    }
}
