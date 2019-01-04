package com.example.user.smartplugincontoller;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class test extends AppCompatActivity  implements View.OnClickListener{
    private Button btnOn,btnOff;
    TextView tvLinkify;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_test );

        btnOn = (Button) findViewById( R.id.btn_on );
        btnOff =(Button) findViewById( R.id.btn_off );
        btnOn.setOnClickListener( this );
        btnOff.setOnClickListener( this );
        tvLinkify = (TextView) findViewById(R.id.tv_tampil);
        tvLinkify.setText("http://192.168.43.163:81/?LED=ON");
        // proses menambahkan Links pada TextView
        Linkify.addLinks(tvLinkify, Linkify.ALL);
        webView =(WebView) findViewById( R.id.webView );

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_on){
          String url = "http://192.168.43.163:81/?LED2=ON" ;
            webView.loadUrl(url);


        }
        if (v.getId() == R.id.btn_off){
            String url = "http://192.168.43.163:81/?LED2=OFF" ;
            webView.loadUrl( url );
        }
    }

}
