package com.example.login;

import android.provider.BaseColumns;

public class AccountReaderContract {
    private AccountReaderContract() {}

    public static class AccountReader implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ACCOUNT_NUM = "account_number";
        public static final String COLUMN_NAME_BANK_CODE_OWNER = "bankcode_owner";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    public static final String SQL_CREATE_ACCOUNT = "CREATE TABLE " + AccountReader.TABLE_NAME + " (" +
            AccountReader._ID + " INTEGER PRIMARY KEY," +
            AccountReader.COLUMN_NAME_USERNAME + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_PASSWORD + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_ACCOUNT_NUM + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_BANK_CODE_OWNER + " INTEGER NOT NULL, " +
            AccountReader.COLUMN_NAME_BALANCE + " REAL DEFAULT 0.00" +
            ")";

    public static final String SQL_DELETE_ACCOUNT = "DROP TABLE IF EXISTS " + AccountReader.TABLE_NAME;
}
