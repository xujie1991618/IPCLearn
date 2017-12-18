package com.crayfish.ipclearn;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/14.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";

    private Messenger messenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MSG:
                    Log.d(TAG, "handleMessage: from client:"+msg.getData().getString("msg"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
