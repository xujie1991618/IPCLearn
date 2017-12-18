// IOnNewBookArrivedListener.aidl
package com.crayfish.ipclearn.aidl;

// Declare any non-default types here with import statements
import com.crayfish.ipclearn.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrive(in Book book);
}
