package com.example.user.smartplugincontoller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceScreen;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.bluetooth.BluetoothAdapter.*;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    int hour1, minute1,Thour1,Tminute1,Tmillis1,hourLeft1,minuteLeft1,secondLeft1;
    int hour2, minute2,Thour2,Tminute2,Tmillis2,hourLeft2,minuteLeft2,secondLeft2;
    static final int Device1 = 0;
    static final int Device2 = 1;
    private boolean mTimerRunning1;
    private boolean mTimerRunning2;
    private TextView outTime1;
    private EditText txtTime1;
    private TextView outTime2;
    private EditText txtTime2;
//    boolean f1 = true ;
//    boolean f2 = true;
    private CountDownTimer timerLeft1,timerLeft2;

    //bluetooth

    @BindView(R.id.tbDevice1)
    ToggleButton tbDevice1;
    @BindView(R.id.tbDevice2)
    ToggleButton tbDevice2;
    @BindView(R.id.lsView2)
    ListView lsView2;
    //    UUID Untuk koneksi sebagai klien ke bluetoot module Hc-05
    // default HC-05 UUID  "00001101-0000-1000-8000-00805F9B34FB"
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //  Request-request code
    int BLUETOOTH_ENABLE = 1;
    //  Difinisi module module yang di butuhkan
    public static BluetoothAdapter Btadapter;
    public static Main2Activity.ConnectedThread connected;
    public static ArrayAdapter<String> mConversationArrayAdapter;
    public static Main2Activity.ConnectThread coneting;
    public static StringBuffer bufferWrite;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
                mTimerRunning1 = true;

        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

                mTimerRunning1 = true;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        Pasang Layout view dengan Butter knife
        ButterKnife.bind(this);

//        Ambil adapter Bluetooth Pada perangkat
        Btadapter = getDefaultAdapter();

//        Jika bluetooth tidak ada
        if(Btadapter == null){
            Toast.makeText(this,"Perangkat anda Tidak support bluetooth",Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetoothOn();

        tbDevice1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            Button startTimer1 = (Button) findViewById(R.id.bStartDevice1);
            Button stop1 = (Button) findViewById(R.id.bStopDevice1);
            @SuppressLint("NewApi")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bluetoothOn();
                    tbDevice1.setBackgroundColor( getColor( R.color.gren ) );
                    Message = "2";
                    startTimer1.setVisibility(View.VISIBLE);

                    if (Btadapter.isEnabled())
                    {
                        coneting.start();
                    }

                } else {
                    Message = "3";
                    tbDevice1.setBackgroundColor( getColor( R.color.red ) );
                    startTimer1.setVisibility(View.INVISIBLE);
                    stop1.setVisibility(View.INVISIBLE);
                    stp1();
                }
//                if (Btadapter.isEnabled()){
                    byte[] p = Message.getBytes();
                    connected.write(p);
//                }
            }
        });

        tbDevice2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String Message = "";
            Button startTimer2 = (Button) findViewById(R.id.bStartDevice2);
            Button stop2 = (Button) findViewById(R.id.bStopDevice2);

            @SuppressLint("NewApi")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        bluetoothOn();
                        tbDevice2.setBackgroundColor( getColor( R.color.gren ) );
                        Message = "4";
                        startTimer2.setVisibility(View.VISIBLE);
                         if (Btadapter.isEnabled())
                         {
                             coneting.start();
                        }

                } else {
                    Message = "5";
                    tbDevice2.setBackgroundColor( getColor( R.color.red ) );
                    startTimer2.setVisibility(View.INVISIBLE);
                    stop2.setVisibility(View.INVISIBLE);
                    stp2();
                }
//                if (Btadapter.isEnabled()) {
                    byte[] p = Message.getBytes();
                    connected.write(p);
//                }
            }
        });

    //bluetooth
        Button startTimer1 = (Button) findViewById(R.id.bStartDevice1);
        startTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etTimerIn1wifi = txtTime1.getText().toString().trim();
                boolean isEmptyFields = false;
                if (TextUtils.isEmpty(etTimerIn1wifi)) {
                    isEmptyFields = true;
                    txtTime1.setError("Field ini Tidak Boleh Kosong");
                }else {
                    timer1();
                    str1();
                    txtTime1.setTextColor(Color.rgb(0, 0, 255));
                }
            }
        });
        Button starTimer2 = (Button) findViewById(R.id.bStartDevice2);
        starTimer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etTimerIn1wifi = txtTime2.getText().toString().trim();
                boolean isEmptyFields = false;
                if (TextUtils.isEmpty(etTimerIn1wifi)) {
                    isEmptyFields = true;
                    txtTime2.setError("Field ini Tidak Boleh Kosong");
                }else {
                    timer2();
                    str2();
                    txtTime2.setTextColor(Color.rgb(0, 0, 255));
                }
            }
        });
        Button stop1 = (Button) findViewById(R.id.bStopDevice1);
        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stp1();
                tbDevice1.setChecked(false);


            }
        });
        Button stop2 = (Button) findViewById(R.id.bStopDevice2);
        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stp2();
                tbDevice2.setChecked(false);

            }
        });

        txtTime1 = (EditText) findViewById(R.id.etInDevice1);
        outTime1 = (TextView) findViewById(R.id.outTimer);

        txtTime1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(Device1);
                return true;
            }
        });
        txtTime2 = (EditText) findViewById(R.id.etInDevice2);
        outTime2 = (TextView) findViewById(R.id.outTimer2);

        txtTime2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                showDialog(Device2);
                return true;
            }
        });

        //================================================================================

        mConversationArrayAdapter = new ArrayAdapter<String >(this, R.layout.message);
        lsView2.setAdapter(mConversationArrayAdapter);
        lsView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lsView2.setStackFromBottom(true);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id){
            case Device1:
                return new TimePickerDialog(
                        this, m1TimeSetListener, hour1, minute1, true);
            case Device2:
                return new TimePickerDialog(
                        this, m2TimeSetListener, hour2, minute2, true);
        }

        return null;
    }

    private TimePickerDialog.OnTimeSetListener m1TimeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                public void onTimeSet(TimePicker view,  int hourOfDay, int minuteOfHour)
                {

                    hour1 = hourOfDay;
                    minute1 = minuteOfHour;
                    String stime = LPad(""+hour1, "0", 2) + ":"+ LPad(""+minute1, "0", 2)+":00";
                        txtTime1.setText(stime);

                }

            };

    private TimePickerDialog.OnTimeSetListener m2TimeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
                {
                    hour2 = hourOfDay;
                    minute2 = minuteOfHour;
                    String stime = LPad(""+hour2, "0", 2) + ":"+ LPad(""+minute2, "0", 2)+":00";
                    txtTime2.setText(stime);

                }
            };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
    }
    private void timer1(){
        Thour1 = (hour1*3600000);
        Tminute1=(minute1*60000);
        Tmillis1 = (Thour1+Tminute1);
        timerLeft1=new CountDownTimer(Tmillis1,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                hourLeft1 = (int) ((millisUntilFinished /(1000*60*60)));
                minuteLeft1= (int) ((millisUntilFinished %(1000*60*60))/(1000*60));
                secondLeft1 = (int) ((millisUntilFinished%(1000*60*60))%(1000*60)/1000);
                final String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hourLeft1,minuteLeft1,secondLeft1);
                    outTime1.setText(timeLeftFormatted);

//                    f1=false;
            }

            @Override
            public void onFinish() {
                device1Finish();
//                f1=true;
//                if (f1==true && f2==true) {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            bluetoothoff();
//                        }
//                    }, 3000L);
//                }
            }
        }.start();
        mTimerRunning1 = true;
        outTime1.setTextColor(Color.rgb(0,255,0));
    }
    private void timer2(){
        Thour2 = (hour2*3600000);
        Tminute2=(minute2*60000);
        Tmillis2 = (Thour2+Tminute2);



        timerLeft2=new CountDownTimer(Tmillis2,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                hourLeft2 = (int) ((millisUntilFinished /(1000*60*60)));
                minuteLeft2= (int) ((millisUntilFinished %(1000*60*60))/(1000*60));
                secondLeft2 = (int) ((millisUntilFinished%(1000*60*60))%(1000*60)/1000);
                final String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hourLeft2,minuteLeft2,secondLeft2);
                outTime2.setText(timeLeftFormatted);
                outTime2.setTextColor(Color.rgb(0,255,0));
//                f2=false;
            }

            @Override
            public void onFinish() {
               device2Finish();
//                f2=true;
//                if (f1==true && f2==true) {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            bluetoothoff();
//                        }
//                    }, 3000L);
//                }
            }
        }.start();
    }
    //Bluetooth
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
                if (Btadapter.isEnabled()) {
                    Log.d("TAG", "onOptionsItemSelected: ");
                    DeviceFragment fragment = new DeviceFragment();
                    fragment.show(getSupportFragmentManager(), DeviceFragment.class.getSimpleName());
                }else{
                    bluetoothOn();
                }
            case R.id.action_connect:
                if (Btadapter.isEnabled()){
//                    BluetoothDevice device = Btadapter.getRemoteDevice("98:D3:34:90:78:19");
                    BluetoothDevice device =Btadapter.getRemoteDevice( "98:D3:32:70:7C:27" );
                    coneting = new ConnectThread(device);
                    coneting.start();
                     Toast.makeText(this, "Terhubung " + Btadapter.getName(), Toast.LENGTH_SHORT).show();
                }else{
                    bluetoothOn();
                }

        }



        return super.onOptionsItemSelected(item);
    }

    private static Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            byte[] readBuf = (byte[]) msg.obj;
            // construct a string from the valid bytes in the buffer
            String readMessage = new String(readBuf, 0, msg.arg1);
            mConversationArrayAdapter.add("Appolo" + ":  " + readMessage);
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
            connected=new ConnectedThread(mmSocket);
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
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
                penting();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    //mengidupkan Bluetooth
    public void bluetoothOn(){
        if(!Btadapter.isEnabled()){
            showDialogPilihan("Bluetooth","Bluetooth belum di aktifkan,Mau mengaktifkan?");
            cekStatus();
        }
    }
    // Metho untuk State Bluetooth
    public void cekStatus(){
        Log.d("TAG", "cekStatus: ");
        BroadcastReceiver bluetoothStatus = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int statusSekarang = intent.getIntExtra(EXTRA_STATE,-1);
                if(statusSekarang == STATE_ON){
                    Toast.makeText(context,"Bluetooth On",Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter bluetoothFilter = new IntentFilter(ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStatus,bluetoothFilter);
    }
    private void str1(){
        Button stop1 = (Button) findViewById(R.id.bStopDevice1);
        Button startTimer1 = (Button) findViewById(R.id.bStartDevice1);
        startTimer1.setVisibility(View.INVISIBLE);
        stop1.setVisibility(View.VISIBLE);
    }
    private void str2(){
        Button stop2 = (Button) findViewById(R.id.bStopDevice2);
        Button startTimer2 = (Button) findViewById(R.id.bStartDevice2);
        startTimer2.setVisibility(View.INVISIBLE);
        stop2.setVisibility(View.VISIBLE);
    }
    private void stp1(){
        tbDevice1.setChecked(false);
        txtTime1.setHint("00:00:00");
        txtTime1.setTextColor(Color.rgb(0, 0, 255));
        timerLeft1.cancel();
        outTime1.setText("00:00:00");
        outTime1.setTextColor(Color.rgb(255, 0, 0));
    }
    private void stp2(){
        tbDevice2.setChecked(false);
        txtTime2.setHint("00:00:00");
        txtTime2.setTextColor(Color.rgb(0, 0, 255));
        timerLeft2.cancel();
        outTime2.setText("00:00:00");
        outTime2.setTextColor(Color.rgb(255, 0, 0));
    }
    private void device1Finish(){
        outTime1.setHint("00:00:00");
        outTime1.setTextColor(Color.rgb(255,0,0));
        stp1();
    }
    private void device2Finish(){
        outTime2.setHint("00:00:00");
        outTime2.setTextColor(Color.rgb(255,0,0));
        stp2();
    }

    private void bluetoothoff() {
            Btadapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_SHORT).show();

    }
    private void penting(){
        Toast.makeText(this,"Aplikasi ini mengharuskan anda mengaktifkan Bluetooth",Toast.LENGTH_SHORT).show();

    }

}

