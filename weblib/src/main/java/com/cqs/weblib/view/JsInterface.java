package com.cqs.weblib.view;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.cqs.weblib.utils.SystemInfoUtil;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\16 0016 16:05
 * 邮箱:chen510470614@163.com
 */
public class JsInterface {

    private Context mContext;

    public JsInterface(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * js只能调用这一个方法，所有的请求都要通过这个方法分发
     * @param cmd
     * @param param
     */
    @JavascriptInterface
    public void post(String cmd, String param){
        String actionName = cmd;
        String jsonParam = param;
        if (SystemInfoUtil.inMainProcess(mContext, android.os.Process.myPid())){
            //如果webview在主进程，则直接执行js方法
        }else{
            //如果是在子进程，则调用aidl方法执行js方法
        }
    }
}
