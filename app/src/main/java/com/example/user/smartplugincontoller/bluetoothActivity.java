


// TIDAK DI PAKAI

package com.example.user.smartplugincontoller;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class bluetoothActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn1)
    ToggleButton btn1;
    @BindView(R.id.btn2)
    ToggleButton btn2;
    @BindView(R.id.btn3)
    ToggleButton btn3;
    @BindView(R.id.btn4)
    ToggleButton btn4;

    @BindView(R.id.lsView)
    ListView lsView;


    //    UUID Untuk koneksi sebagai klien ke bluetoot module Hc-05
    // default HC-05 UUID  "00001101-0000-1000-8000-00805F9B34FB"
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //  Request-request code
    int BLUETOOTH_ENABLE = 1;

    //  Difinisi module module yang di butuhkan
    public static BluetoothAdapter Btadapter;
    public static ConnectedThread connected;
    public static ArrayAdapter<String> mConversationArrayAdapter;
    public static ConnectThread coneting;
    public static StringBuffer bufferWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
//        Pasang Layout view dengan Butter knife
        ButterKnife.bind(this);



//        Ambil adapter Bluetooth Pada perangkat
        Btadapter = BluetoothAdapter.getDefaultAdapter();

//        Jika bluetooth tidak ada
        if(Btadapter == null){
            Toast.makeText(this,"Perangkat anda Tidak support bluetooth",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!Btadapter.isEnabled()){
            showDialogPilihan("Bluetooth","Bluetooth belum di aktifkan,Mau mengaktifkan?");
            cekStatus();
        }
        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Message = "1";

                } else {
                    Message = "0";

                }
                byte[] p = Message.getBytes();
                connected.write(p);
            }
        });
        btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Message = "2";

                } else {
                    Message = "3";

                }
                byte[] p = Message.getBytes();
                connected.write(p);
            }
        });
        btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Message = "4";

                } else {
                    Message = "5";

                }
                byte[] p = Message.getBytes();
                connected.write(p);

            }
        });
        btn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Message = "6";

                } else {
                    Message = "7";

                }
                byte[] p = Message.getBytes();
                connected.write(p);
            }
        });


//        startActivityForResult(
//                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),BLUETOOTH_ENABLE
//        );


        mConversationArrayAdapter = new ArrayAdapter<String >(this, R.layout.message);
        lsView.setAdapter(mConversationArrayAdapter);
        lsView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lsView.setStackFromBottom(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BLUETOOTH_ENABLE){

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_utama,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_scan:
                Log.d("TAG", "onOptionsItemSelected: ");
                DeviceFragment fragment = new DeviceFragment();
                fragment.show(getSupportFragmentManager(),DeviceFragment.class.getSimpleName());

            case R.id.action_connect:
                BluetoothDevice device = Btadapter.getRemoteDevice("98:D3:34:90:78:19");
                coneting = new ConnectThread(device);
                coneting.start();
                Toast.makeText(this,"Conneted"+Btadapter.getName(),Toast.LENGTH_SHORT).show();

        }



        return super.onOptionsItemSelected(item);
    }

    private static Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            byte[] readBuf = (byte[]) msg.obj;
            // construct a string from the valid bytes in the buffer
            String readMessage = new String(readBuf, 0, msg.arg1);
//            mConversationArrayAdapter.add("Appolo" + ":  " + readMessage);
            mConversationArrayAdapter.addAll(readMessage);
            mConversationArrayAdapter.notifyDataSetChanged();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn1){



        }
//        else if(v.getId() == R.id.btnBluetooth) {
////            BluetoothDevice device = adapter.getRemoteDevice("98:D3:34:90:78:19");
////            CO = new ConnectThread(device);
////            CO.start();
////            Toast.makeText(this,"Conneted"+adapter.getName(),Toast.LENGTH_SHORT).show();
//        }
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
//            adapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

//             Do work to manage the connection (in a separate thread)
            connected = new ConnectedThread(mmSocket);
            connected.start();
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    Thread.sleep(100);
                    mHandler.obtainMessage(2, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
    // Method untukk Dialog
    private void showDialogPilihan(String title,String message) {
        String dialogTitle = title;
        String dialogMessage = message;
        AlertDialog.Builder dialog = new AlertDialog.Builder(bluetoothActivity.this);
        dialog.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Btadapter.enable();

                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    // Metho untuk State Bluetooth
    public void cekStatus(){
        Log.d("TAG", "cekStatus: ");
        BroadcastReceiver bluetoothStatus = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int statusSekarang = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
                if(statusSekarang == BluetoothAdapter.STATE_ON){
                    Toast.makeText(context,"Bluetooth On",Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStatus,bluetoothFilter);
    }
}