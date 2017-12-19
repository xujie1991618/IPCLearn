package com.crayfish.ipclearn.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/19.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class TCPServerService extends Service {
    private static final String TAG = "TCPServerService";

    private boolean mIsServiceDestroyed = false;
    private String[] mMessage = new String[]{
            "你好，哈哈哈",
            "请问你叫啥名字？",
            "今天天气不错",
            "你知道，我们在聊天吧",
            "不知道咋滴"};

    @Override
    public void onCreate() {
        new Thread(new TCPServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TCPServer implements Runnable{
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
                Log.d(TAG, "run: "+e.getMessage());
                e.printStackTrace();
            }
            while (!mIsServiceDestroyed){
                try {
                    //接收客户消息
                    final Socket socket = serverSocket.accept();
                    Log.d(TAG, "run: accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void responseClient(Socket client) throws IOException{
            //用户接收客户端消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
            out.println("欢迎来到聊天室");
            while(!mIsServiceDestroyed){
                String str = reader.readLine();
                Log.d(TAG, "responseClient: form client "+str);
                if(str == null){
                    break;
                }
                int i = new Random().nextInt(mMessage.length);
                out.println(mMessage[i]);
                Log.d(TAG, "responseClient: send "+mMessage[i]);
            }
            Log.d(TAG, "responseClient: quit");
            reader.close();
            out.close();
            client.close();
        }
    }
}
