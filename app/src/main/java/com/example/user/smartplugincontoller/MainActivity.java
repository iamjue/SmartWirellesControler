package com.example.user.smartplugincontoller;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bMainBluetooth;
    private Button bMainWifi;
    private TextView text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bMainBluetooth = (Button)findViewById(R.id.bMainBluetooth);
        bMainBluetooth.setOnClickListener(this);
        bMainWifi =(Button)findViewById(R.id.bMainWifi);
        bMainWifi.setOnClickListener(this);
        text1 =(TextView)findViewById(R.id.tvText1);
        text2 =(TextView)findViewById(R.id.tvText2);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bMainBluetooth:
                Intent mainBluetooth= new Intent(MainActivity.this, Main2Activity.class);
                startActivity(mainBluetooth);
                break;
            case R.id.bMainWifi:
                Intent mainWifi =new Intent(MainActivity.this, MainActivityWifi.class);
                startActivity(mainWifi);
                break;

        }

    }
}