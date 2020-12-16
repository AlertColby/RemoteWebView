package com.cqs.weblib.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\10 0010 09:52
 * 邮箱:chen510470614@163.com
 */
public class BinderService extends Service {

    private BinderPoolHelper.BinderPoolImpl binderPool = new BinderPoolHelper.BinderPoolImpl();

    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("c_log", "BinderService process : " + android.os.Process.myPid());
    }
}
