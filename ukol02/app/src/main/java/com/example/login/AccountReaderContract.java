package com.example.login;

import android.provider.BaseColumns;


public class AccountReaderContract {
    /*
    Helper trida pro definici nazvu sloupcu a tabulky, bez inicializace, jen kontejner s nazvy
    Podle tutorialu na https://developer.android.com/training/data-storage/sqlite#java
    */
    private AccountReaderContract() {}

    /* Inner class that defines the table contents for Accounts */
    public static class AccountReader implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ACCOUNT_NUM = "account_number";
        public static final String COLUMN_NAME_BANK_CODE_OWNER = "bankcode_owner";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    /* Inner class that defines the table contents for Payments */
    public static class PaymentEntry implements BaseColumns {
        public static final String TABLE_NAME = "payment";
        public static final String COLUMN_NAME_FROM_ACCOUNT = "from_account";
        public static final String COLUMN_NAME_TO_ACCOUNT = "to_account";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NOTE = "note";
    }

    public static final String SQL_CREATE_ACCOUNT = "CREATE TABLE " + AccountReader.TABLE_NAME + " (" +
            AccountReader._ID + " INTEGER PRIMARY KEY," +
            AccountReader.COLUMN_NAME_USERNAME + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_PASSWORD + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_ACCOUNT_NUM + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_BANK_CODE_OWNER + " INTEGER NOT NULL, " +
            AccountReader.COLUMN_NAME_BALANCE + " REAL DEFAULT 0.00" +
            ")";

    public static final String SQL_CREATE_PAYMENT = "CREATE TABLE " + PaymentEntry.TABLE_NAME + " (" +
            PaymentEntry._ID + " INTEGER PRIMARY KEY," +
            PaymentEntry.COLUMN_NAME_FROM_ACCOUNT + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_TO_ACCOUNT + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_AMOUNT + " REAL NOT NULL, " +
            PaymentEntry.COLUMN_NAME_DATE + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_NOTE + " TEXT" +
            ")";

    public static final String SQL_DELETE_ACCOUNT = "DROP TABLE IF EXISTS " + AccountReader.TABLE_NAME;
    public static final String SQL_DELETE_PAYMENT = "DROP TABLE IF EXISTS " + PaymentEntry.TABLE_NAME;
}
