package com.crayfish.ipclearn.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/20.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class BinderPoolService extends Service {

    private Binder binder = new BinderPool.BinderPoolImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
