// IBinderPool.aidl
package com.crayfish.ipclearn.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
