package com.example.user.smartplugincontoller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivityWifi extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnTouchListener {
    int hour1, minute1, Thour1, Tminute1, Tmillis1, hourLeft1, minuteLeft1, secondLeft1;
    int hour2, minute2, Thour2, Tminute2, Tmillis2, hourLeft2, minuteLeft2, secondLeft2;
    private static final int Device1 = 0;
    private static final int Device2 = 1;
    private Button btnStartDevice1, btnStartDevice2, btnStopDevice1, btnStopDevice2;
    private ToggleButton tbDevice1, tbDevice2;
    private EditText etTimer1, etTimer2;
    private TextView tvOutTimer1, tvOutTimer2;
    private CountDownTimer timerLeft1,timerLeft2;
    private boolean mTimerRunning1;
    private boolean mTimerRunning2;
    private WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wifi);
        getRequestedOrientation();

        btnStartDevice1 = findViewById(R.id.bStartDevice1Wifi);
        btnStartDevice1.setOnClickListener(this);
        btnStartDevice2 = findViewById(R.id.bStartDevice2Wifi);
        btnStartDevice2.setOnClickListener(this);
        btnStopDevice1 = findViewById(R.id.bStopDevice1Wifi);
        btnStopDevice1.setOnClickListener(this);
        btnStopDevice2 = findViewById(R.id.bStopDevice2Wifi);
        btnStopDevice2.setOnClickListener(this);
        tbDevice1 = findViewById(R.id.tbDevice1Wifi);
        tbDevice1.setOnCheckedChangeListener(this);
        tbDevice2 = findViewById(R.id.tbDevice2Wifi);
        tbDevice2.setOnCheckedChangeListener(this);
        tvOutTimer1 = findViewById(R.id.outTimer1Wifi);
        tvOutTimer2 = findViewById(R.id.outTimer2Wifi);
        webView = findViewById( R.id.webView );

        etTimer1 = findViewById(R.id.etInDevice1Wifi);
        etTimer1.setOnTouchListener(this);
        etTimer2 = findViewById(R.id.etInDevice2Wifi);
        etTimer2.setOnTouchListener(this);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.etInDevice1Wifi) {
            showDialog(Device1);
        } else if (v.getId() == R.id.etInDevice2Wifi) {
            showDialog(Device2);

        }
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.tbDevice1Wifi) {
            if (isChecked) {
                try {
                    String url = "http://192.168.43.163:81/?LED2=ON" ;
//                    Intent bukabrowser = new Intent(Intent. ACTION_QUICK_VIEW);
//                    bukabrowser.setData(Uri. parse(url));
//                    startActivity(bukabrowser);
                    webView.loadUrl( url );
                    btnStartDevice1.setVisibility(View.VISIBLE);
                    tbDevice1.setBackgroundColor(getColor( R.color.gren));
                    Toast.makeText( this, "Device 1 ON",Toast.LENGTH_SHORT ).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText( this, "Device 1 Failled",Toast.LENGTH_SHORT ).show();
                }

            } else {
                try {
                    String url = "http://192.168.43.163:81/?LED2=OFF" ;
//                    Intent bukabrowser = new Intent(Intent. ACTION_QUICK_VIEW);
//                    bukabrowser.setData(Uri. parse(url));
//                    startActivity(bukabrowser);
                    webView.loadUrl( url );
                    btnStartDevice1.setVisibility(View.INVISIBLE);
                    btnStopDevice1.setVisibility(View.INVISIBLE);
                    tbDevice1.setBackgroundColor(getColor( R.color.red));
                    stp1();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText( this, "Device 1 OFF",Toast.LENGTH_SHORT ).show();
                }
            }
        } else if (buttonView.getId() == R.id.tbDevice2Wifi) {
            if (isChecked) {
                try {
                    String url = "http://192.168.43.163:81/?LED3=ON" ;
//                    Intent bukabrowser = new Intent(Intent. ACTION_QUICK_VIEW);
//                    bukabrowser.setData(Uri. parse(url));
//                    startActivity(bukabrowser);
                    webView.loadUrl( url );
                    btnStartDevice2.setVisibility(View.VISIBLE);
                    tbDevice2.setBackgroundColor(getColor( R.color.gren));
                    Toast.makeText( this, "Device 2 ON",Toast.LENGTH_SHORT ).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText( this, "Device 2 Failled",Toast.LENGTH_SHORT ).show();
                }

            } else {
                try {
                    String url = "http://192.168.43.163:81/?LED3=OFF" ;
//                    Intent bukabrowser = new Intent(Intent. ACTION_QUICK_VIEW);
//                    bukabrowser.setData(Uri. parse(url));
//                    startActivity(bukabrowser);
                    webView.loadUrl( url );
                    tbDevice2.setBackgroundColor(getColor( R.color.red));
                    btnStartDevice2.setVisibility(View.INVISIBLE);
                    btnStopDevice2.setVisibility(View.INVISIBLE);
                    stp2();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText( this, "Device 2 OFF",Toast.LENGTH_SHORT ).show();
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bStartDevice1Wifi) {
            if(etTimer1.getText().toString().length() == 0 )
                etTimer1.setError("Field ini Tidak Boleh Kosong");
            else {
                timer1();
                str1();
                etTimer1.setTextColor(Color.rgb(0, 0, 255));
            }
        } else if (v.getId() == R.id.bStartDevice2Wifi) {
            if(etTimer2.getText().toString().length() == 0 )
                etTimer2.setError("Field ini Tidak Boleh Kosong");
            else {
                timer2();
                str2();
                etTimer2.setTextColor(Color.rgb(0, 0, 255));
            }
        } else if (v.getId() == R.id.bStopDevice1Wifi) {
            stp1();
            tbDevice1.setChecked(false);

        } else if (v.getId() == R.id.bStopDevice2Wifi) {
            stp2();
            tbDevice2.setChecked(false);

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
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
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {

                    hour1 = hourOfDay;
                    minute1 = minuteOfHour;
                    String stime = LPad("" + hour1, "0", 2) + ":" + LPad("" + minute1, "0", 2) + ":00";
                    etTimer1.setText(stime);

                }

            };

    private TimePickerDialog.OnTimeSetListener m2TimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                    hour2 = hourOfDay;
                    minute2 = minuteOfHour;
                    String stime = LPad("" + hour2, "0", 2) + ":" + LPad("" + minute2, "0", 2) + ":00";
                    etTimer2.setText(stime);

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
                tvOutTimer1.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                device1Finish();

            }
        }.start();
        mTimerRunning1 = true;
        tvOutTimer1.setTextColor(Color.rgb(0,255,0));
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
                tvOutTimer2.setText(timeLeftFormatted);
                tvOutTimer2.setTextColor(Color.rgb(0,255,0));
            }

            @Override
            public void onFinish() {
                device2Finish();
            }
        }.start();
    }

    private void str1(){
        Button stop1 = findViewById(R.id.bStopDevice1Wifi);
        Button startTimer1 = findViewById(R.id.bStartDevice1Wifi);
        startTimer1.setVisibility(View.INVISIBLE);
        stop1.setVisibility(View.VISIBLE);
    }
    private void str2(){
        Button stop2 = findViewById(R.id.bStopDevice2Wifi);
        Button startTimer2 = findViewById(R.id.bStartDevice2Wifi);
        startTimer2.setVisibility(View.INVISIBLE);
        stop2.setVisibility(View.VISIBLE);
    }
    private void stp1(){
        tbDevice1.setChecked(false);
        etTimer1.setText( null );
        etTimer1.setTextColor(Color.rgb(255, 0, 0));
        timerLeft1.cancel();
        tvOutTimer1.setText("00:00:00");
        tvOutTimer1.setTextColor(Color.rgb(255, 0, 0));
    }
    private void stp2(){
        tbDevice2.setChecked(false);
        etTimer2.setText( null );
        etTimer2.setTextColor(Color.rgb(255, 0, 0));
        timerLeft2.cancel();
        tvOutTimer2.setText("00:00:00");
        tvOutTimer2.setTextColor(Color.rgb(255, 0, 0));
    }
    private void device1Finish(){
        tvOutTimer1.setText("00:00:00");
        tvOutTimer1.setTextColor(Color.rgb(255,0,0));
        stp1();
    }
    private void device2Finish(){
        tvOutTimer2.setText("00:00:00");
        tvOutTimer2.setTextColor(Color.rgb(255,0,0));
        stp2();
    }


}
