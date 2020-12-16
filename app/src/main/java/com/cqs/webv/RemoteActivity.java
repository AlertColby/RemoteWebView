package com.cqs.webv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class RemoteActivity extends AppCompatActivity {

    public static void actionStart(Context context){
        Intent intent = new Intent(context, RemoteActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
    }

    public void testBinder(View view){
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
