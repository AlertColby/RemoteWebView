package com.cqs.weblib.aidl;

import android.os.RemoteException;

import com.cqs.weblib.IWebAction;
import com.cqs.weblib.WebActionCallback;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\16 0016 17:01
 * 邮箱:chen510470614@163.com
 */
public class WebActionImpl extends IWebAction.Stub {

    @Override
    public void jsCall(String actionName, String jsonParam, WebActionCallback callback) throws RemoteException {

    }
}

