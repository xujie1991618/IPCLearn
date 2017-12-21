package com.crayfish.ipclearn.aidl;

import android.os.RemoteException;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/20.
 * 描    述：Binder连接池
 * 修改历史：
 * ===========================
 */

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
