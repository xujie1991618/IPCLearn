package com.crayfish.ipclearn.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.URI;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/18.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    public static final String AUTHORITIES = "com.crayfish.ipclearn.provider";
    public static final Uri URI_CONTENT_BOOK = Uri.parse("content://" + AUTHORITIES + "/book");
    public static final Uri URI_CONTENT_USER = Uri.parse("content://" + AUTHORITIES + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITIES,"book",BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITIES,"user",USER_URI_CODE);
    }

    private Context context;
    private SQLiteDatabase mDB;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: "+Thread.currentThread().getName());
        context = getContext();
        initProvider();
        return true;
    }

    private void initProvider(){
        mDB = new DbOpenHelper(context).getWritableDatabase();
        mDB.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDB.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDB.execSQL("insert into book values(3,'Android');");
        mDB.execSQL("insert into book values(4,'IOS');");
        mDB.execSQL("insert into book values(5,'HTML5');");
        mDB.execSQL("insert into user values(1,'Jake',1);");
        mDB.execSQL("insert into user values(2,'Male',0);");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType: ");
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: "+Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        return mDB.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert: "+Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        mDB.insert(table,null,values);
        context.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete: "+Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        int count = mDB.delete(table,selection,selectionArgs);
        if(count > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update: "+Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        int row = mDB.update(table,values,selection,selectionArgs);
        if(row > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    private String getTableName(Uri uri){
        String tableName = null;
        switch (uriMatcher.match(uri)){
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
