package com.crayfish.ipclearn.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.ConnectionService;

import java.util.concurrent.CountDownLatch;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/20.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class BinderPool {

    public static final int BINDER_NONE = -1;
    public static final int BINDER_SECURITY_CENTER = 0;
    public static final int BINDER_COMPUTE = 1;

    private static volatile BinderPool instance;
    private Context mContext;
    private CountDownLatch countDownLatch;
    private IBinderPool binderPool;

    private BinderPool(Context context){
        mContext = context;
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if(instance == null) {
            synchronized (BinderPool.class) {
                if(instance == null){
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    private synchronized void connectBinderPoolService(){
        countDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(service,connection,Service.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            binderPool.asBinder().unlinkToDeath(deathRecipient,0);
            binderPool = null;
            connectBinderPoolService();
        }
    };

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if(binderPool != null){
                binder = binderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub{
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:break;
            }
            return binder;
        }
    }
}
