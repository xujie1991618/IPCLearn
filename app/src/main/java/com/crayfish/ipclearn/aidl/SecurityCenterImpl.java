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

public class SecurityCenterImpl extends ISecurityCenter.Stub {
    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (char c:chars) {
            c ^= '^';
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
