package com.cqs.webv;

import android.app.Application;
import android.util.Log;
import com.cqs.webv.utils.CrashHandler;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\3 0003 14:09
 * 邮箱:chen510470614@163.com
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("c_log", "--->pid:" + android.os.Process.myPid());
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }
}
