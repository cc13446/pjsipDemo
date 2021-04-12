package com.chenchen.android.pjsipdemo.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.chenchen.android.pjsipdemo.Dao.DBReaderContract.BuddyEntry;
import com.chenchen.android.pjsipdemo.Logger;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PJSIP_DEMO";
    private static final int DB_VERSION = 1;
    private final String LOG_TAG = DataBaseHelper.class.getSimpleName();
    private static DataBaseHelper mDataBaseHelper = null;


    private static final String SQL_CREATE_BUDDY_ENTRIES =
            "CREATE TABLE " + BuddyEntry.TABLE_NAME + " (" +
                    BuddyEntry._ID + " INTEGER PRIMARY KEY," +
                    BuddyEntry.COLUMN_NAME_NAME + " TEXT unique," +
                    BuddyEntry.COLUMN_NAME_URL + " TEXT)";

    private static final String SQL_DELETE_BUDDY_ENTRIES =
            "DROP TABLE IF EXISTS " + BuddyEntry.TABLE_NAME;

    public static DataBaseHelper getInstance(Context context){
        if(null == mDataBaseHelper){
            mDataBaseHelper = new DataBaseHelper(context);
        }
        return mDataBaseHelper;
    }

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.error(LOG_TAG, "create a sqlite database");
        db.execSQL(SQL_CREATE_BUDDY_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.error(LOG_TAG, "update a sqlite database");
        db.execSQL(SQL_DELETE_BUDDY_ENTRIES);
    }
}
