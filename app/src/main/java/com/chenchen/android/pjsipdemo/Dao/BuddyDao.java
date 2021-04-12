package com.chenchen.android.pjsipdemo.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.chenchen.android.pjsipdemo.DemoApplication;
import com.chenchen.android.pjsipdemo.Domain.SipAccount;
import com.chenchen.android.pjsipdemo.Domain.SipBuddy;
import com.chenchen.android.pjsipdemo.Dao.DBReaderContract.BuddyEntry;

import java.util.ArrayList;
import java.util.List;

public class BuddyDao {

    private static BuddyDao mBuddyDao;
    private DataBaseHelper mDataBaseHelper;

    private BuddyDao() {
        mDataBaseHelper = DataBaseHelper.getInstance(DemoApplication.getInstance().getApplicationContext());
    }

    public static BuddyDao getInstance() {
        if (null == mBuddyDao) {
            mBuddyDao = new BuddyDao();
        }
        return mBuddyDao;
    }

    public List<SipBuddy> QueryAllBuddy() {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                BuddyEntry.COLUMN_NAME_NAME,
                BuddyEntry.COLUMN_NAME_URL
        };

        String sortOrder = BuddyEntry.COLUMN_NAME_NAME + " DESC";
        Cursor cursor = db.query(BuddyEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
        List<SipBuddy> buddies = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BuddyEntry.COLUMN_NAME_NAME));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(BuddyEntry.COLUMN_NAME_URL));
            buddies.add(new SipBuddy(name, url));
        }
        cursor.close();
        return buddies;
    }

    public void insertBuddy(SipBuddy sipBuddy){
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BuddyEntry.COLUMN_NAME_NAME, sipBuddy.getBuddyName());
        values.put(BuddyEntry.COLUMN_NAME_URL, sipBuddy.getBuddyUrl());
        long newRowId = db.insert(BuddyEntry.TABLE_NAME, null, values);
    }

    public SipBuddy QueryBuddy(String buddyName){
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                BuddyEntry.COLUMN_NAME_NAME,
                BuddyEntry.COLUMN_NAME_URL
        };

        String selection = BuddyEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { buddyName };


        String sortOrder = BuddyEntry.COLUMN_NAME_NAME + " DESC";
        Cursor cursor = db.query(BuddyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        SipBuddy buddy = null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BuddyEntry.COLUMN_NAME_NAME));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(BuddyEntry.COLUMN_NAME_URL));
            buddy = new SipBuddy(name, url);
        }
        cursor.close();
        return buddy;
    }

    public void ModifyBuddy(SipBuddy sipBuddy){
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BuddyEntry.COLUMN_NAME_NAME, sipBuddy.getBuddyName());
        values.put(BuddyEntry.COLUMN_NAME_URL, sipBuddy.getBuddyUrl());

        // Which row to update, based on the title
        String selection = BuddyEntry.COLUMN_NAME_NAME+ " = ?";
        String[] selectionArgs = {sipBuddy.getBuddyName()};

        int count = db.update(BuddyEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void DeleteBuddy(SipBuddy sipBuddy){
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        // Which row to update, based on the title
        String selection = BuddyEntry.COLUMN_NAME_NAME+ " = ?";
        String[] selectionArgs = {sipBuddy.getBuddyName()};

        int deletedRows = db.delete(BuddyEntry.TABLE_NAME, selection, selectionArgs);
    }
}
