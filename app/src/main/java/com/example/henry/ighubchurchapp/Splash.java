package com.example.henry.ighubchurchapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final Thread sleep = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();

                }
                finally {
                    startActivity(new Intent(Splash.this, HomePage.class));

                }
            }
        };
        sleep.start();
    }
}
