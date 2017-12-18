package com.crayfish.ipclearn.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crayfish.ipclearn.R;
import com.crayfish.ipclearn.aidl.Book;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/18.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        init();
    }

    private void init(){
        Uri uri = Uri.parse(BookProvider.URI_CONTENT_BOOK.toString());
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","KK");
        getContentResolver().insert(uri,values);
        Cursor bookCursor = getContentResolver().query(uri,new String[]{"_id","name"},null,null,null);
        while(bookCursor.moveToNext()){
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.d(TAG, "init: query book" + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse(BookProvider.URI_CONTENT_USER.toString());
        Cursor userCursor = getContentResolver().query(userUri,new String[]{"_id","name","sex"},null,null,null);
        while(userCursor.moveToNext()){
            User user = new User();
            user.id = userCursor.getInt(0);
            user.name = userCursor.getString(1);
            user.sex = userCursor.getInt(2) == 1;
            Log.d(TAG, "init: query user" + user.toString());
        }
        userCursor.close();
    }
}
