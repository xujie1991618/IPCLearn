package com.crayfish.ipclearn.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crayfish.ipclearn.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/19.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class SocketActivity extends AppCompatActivity {

    private static final String TAG = "SocketActivity";

    private static final int MESSAGE_RECEIVER_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECT = 2;

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    private Button btn_send;
    private EditText edit_content;
    private TextView txt_show;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_RECEIVER_NEW_MSG:
                    txt_show.setText(txt_show.getText() + (String)msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECT:
                    btn_send.setEnabled(true);
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        btn_send = findViewById(R.id.btn_send);
        edit_content = findViewById(R.id.edit_context);
        txt_show = findViewById(R.id.txt_show);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edit_content.getText().toString();
                if(!TextUtils.isEmpty(msg)&&mPrintWriter != null){
                    mPrintWriter.println(msg);
                    String time = formatDataTime(System.currentTimeMillis());
                    String showMsg = "self"+ time + ":" + msg + "\n";
                    txt_show.setText(txt_show.getText() + showMsg);
                }else{
                    if(mClientSocket == null) {
                        new Thread() {
                            @Override
                            public void run() {
                                connectTCPServer();
                            }
                        }.start();
                    }else{
                        mPrintWriter.println(msg);
                    }
                }
            }
        });
        Intent intent = new Intent(this,TCPServerService.class);
        startService(intent);
//        new Thread(){
//            @Override
//            public void run() {
//                connectTCPServer();
//            }
//        }.start();
    }

    @Override
    protected void onDestroy() {
        if(mClientSocket != null){
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private String formatDataTime(long time){
        return new SimpleDateFormat("HH:mm:ss").format(new Date(time));
    }

    private void connectTCPServer(){
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("127.0.0.1", 8888);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECT);
                Log.d(TAG, "connectTCPServer: connect success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.d(TAG, "connectTCPServer: connect fail");
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!SocketActivity.this.isFinishing()){
                String msg = reader.readLine();
                Log.d(TAG, "connectTCPServer: msg form server "+msg);
                if(msg != null){
                    String time = formatDataTime(System.currentTimeMillis());
                    String showMsg = "server" + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVER_NEW_MSG,showMsg).sendToTarget();
                }
            }
            Log.d(TAG, "connectTCPServer: quit...");
            reader.close();
            mPrintWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
