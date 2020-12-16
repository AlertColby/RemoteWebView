package com.cqs.weblib.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.cqs.weblib.IBinderPool;
import java.util.concurrent.CountDownLatch;

/**
 * 作者:  陈庆松
 * 创建时间: 2020\12\10 0010 09:43
 * 邮箱:chen510470614@163.com
 */
public class BinderPoolHelper {

    private static final String TAG = "BinderPoolHelper";

    private Context mContext;
    public static final int BINDER_WEBVIEW = 1;

    private static BinderPoolHelper binderPoolHelper;
    private IBinderPool mBinderPool;

    //保证连接过程完成后再进行其他操作
    private CountDownLatch mCountDownLatch;

    private BinderPoolHelper(Context context){
        this.mContext = context;
        connectBinderService();
    }

    private synchronized void connectBinderService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderService.class);
        mContext.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "binder died.");
            mBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mBinderPool = null;
            connectBinderService();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(TAG, "binder service disconnected.");
        }
    };

    public static BinderPoolHelper getInstance(Context context){
        if (binderPoolHelper == null){
            synchronized (BinderPoolHelper.class){
                if (binderPoolHelper == null){
                    binderPoolHelper = new BinderPoolHelper(context);
                }
            }
        }
        return binderPoolHelper;
    }


    /**
     * 供客户端使用的查询Binder的接口
     * @param binderCode
     * @return
     */
    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        if (mBinderPool != null){
            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    /**
     * 描述：AIDL连接池，使用连接池在需要多个模块需要进程间通信时就可以只使用一个Service，而不用一个aidl一个service
     */
    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder iBinder = null;
            switch (binderCode){
                case BINDER_WEBVIEW:
                    iBinder = new WebActionImpl();
                    break;
                default:
                    break;
            }
            return iBinder;
        }
    }

}
