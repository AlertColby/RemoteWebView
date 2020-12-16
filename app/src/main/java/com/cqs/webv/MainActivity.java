package com.cqs.webv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("c_log", "MainActivity process : " + android.os.Process.myPid());
    }

    public void toWebView(View view){
        Intent intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }

    public void toBindServ(View view){
    }

    public void callProcess(View view){
    }

    public void toBinderPool(View view){
        RemoteActivity.actionStart(this);
    }


}
