package com.crayfish.ipclearn.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.crayfish.ipclearn.R;

import java.util.List;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/14.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "handleMessage: new book"+msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            mRemoteBookManager = bookManager;
            try {
                bookManager.addBook(new Book(33,"kill"));
                List<Book> list = bookManager.getListBook();
                Log.d(TAG, "onServiceConnected: type:" + list.getClass().getCanonicalName());
                Log.d(TAG, "onServiceConnected: String:" + list.toString());
                bookManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrive(Book book) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,book).sendToTarget();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if(mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()){
            try{
                Log.d(TAG, "onDestroy: listener"+listener);
                mRemoteBookManager.unregisterListener(listener);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
}
