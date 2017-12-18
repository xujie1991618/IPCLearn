// IBookManager.aidl
package com.crayfish.ipclearn.aidl;

// Declare any non-default types here with import statements
import com.crayfish.ipclearn.aidl.Book;
import com.crayfish.ipclearn.aidl.IOnNewBookArrivedListener;
interface IBookManager {
    List<Book> getListBook();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
