package com.crayfish.ipclearn.aidl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crayfish.ipclearn.R;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/20.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class BinderPoolActivity extends AppCompatActivity{
    private static final String TAG = "BinderPoolActivity";

    private ISecurityCenter securityCenter;
    private ICompute compute;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        new Thread(){
            @Override
            public void run() {
                doWork();
            }
        }.start();
    }

    private void doWork(){
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder security = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        securityCenter = SecurityCenterImpl.asInterface(security);
        String msg = "hello world-你好";
        Log.d(TAG, "doWork: " + msg);
        try {
            String password = securityCenter.encrypt(msg);
            Log.d(TAG, "doWork: pre:" + password);
            Log.d(TAG, "doWork: per:" + securityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        compute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG, "doWork: add:" + compute.add(5,8));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
