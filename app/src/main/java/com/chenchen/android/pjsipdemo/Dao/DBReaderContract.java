package com.chenchen.android.pjsipdemo.Dao;

import android.provider.BaseColumns;

public final class DBReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBReaderContract() {}

    /* Inner class that defines the table contents */
    public static class BuddyEntry implements BaseColumns {
        public static final String TABLE_NAME = "buddies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_MESSAGE = "message";
    }
}
