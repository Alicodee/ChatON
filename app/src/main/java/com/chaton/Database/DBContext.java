package com.chaton.Database;

import android.provider.BaseColumns;

public class DBContext {
    private DBContext() {
    }

    public static class Profile implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL= "email";
        public static final String COLUMN_ABOUT = "about";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ABOUT + " TEXT" + ")";

    }
}
