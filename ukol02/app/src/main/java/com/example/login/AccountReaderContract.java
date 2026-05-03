package com.example.login;

import android.provider.BaseColumns;

public class AccountReaderContract {
    /**
     * Pouzit tutorial z: https://developer.android.com/training/data-storage/sqlite
     * Datova trida obsahujici pouze nazvy sloupcu a tabulek v databazi
     * Nazvy sloupcu jsou definovany jako nemenne konstanty, proto mohou byt public
     */
    private AccountReaderContract() {}

    public static class AccountReader implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ACCOUNT_NUM = "account_number"; // Format: xxxxxxxx/yyyy
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    public static class PaymentEntry implements BaseColumns {
        public static final String TABLE_NAME = "payment";
        public static final String COLUMN_NAME_FROM_ACCOUNT = "from_account";
        public static final String COLUMN_NAME_TO_ACCOUNT = "to_account";
        public static final String COLUMN_NAME_AMOUNT = "amount"; // Negative for outgoing, Positive for incoming
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_KS = "ks";
        public static final String COLUMN_NAME_SS = "ss";
        public static final String COLUMN_NAME_VS = "vs";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_NOTE_TO_RECIPIENT = "note_to_recipient";
    }

    public static class RecipientEntry implements BaseColumns {
        /**
         * Tabulka pro vyber prijemce platby.
         * Do ni se ukladaji jmena a cisla uctu znamych prijemcu.
         * Ukladani je mozne z detailu platby a behem vytvareni nove platby.
         */
        public static final String TABLE_NAME = "recipient";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ACCOUNT_NUM = "account_number"; // Format: xxxxxxxx/yyyy
        public static final String COLUMN_NAME_OWNER_USERNAME = "owner_username";
    }

    public static final String SQL_CREATE_ACCOUNT = "CREATE TABLE " + AccountReader.TABLE_NAME + " (" +
            AccountReader._ID + " INTEGER PRIMARY KEY," +
            AccountReader.COLUMN_NAME_USERNAME + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_PASSWORD + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_ACCOUNT_NUM + " TEXT NOT NULL, " +
            AccountReader.COLUMN_NAME_BALANCE + " REAL DEFAULT 0.00" +
            ")";

    public static final String SQL_CREATE_PAYMENT = "CREATE TABLE " + PaymentEntry.TABLE_NAME + " (" +
            PaymentEntry._ID + " INTEGER PRIMARY KEY," +
            PaymentEntry.COLUMN_NAME_FROM_ACCOUNT + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_TO_ACCOUNT + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_AMOUNT + " REAL NOT NULL, " +
            PaymentEntry.COLUMN_NAME_DATE + " TEXT NOT NULL, " +
            PaymentEntry.COLUMN_NAME_KS + " TEXT, " +
            PaymentEntry.COLUMN_NAME_SS + " TEXT, " +
            PaymentEntry.COLUMN_NAME_VS + " TEXT, " +
            PaymentEntry.COLUMN_NAME_NOTE + " TEXT, " +
            PaymentEntry.COLUMN_NAME_NOTE_TO_RECIPIENT + " TEXT" +
            ")";

    public static final String SQL_CREATE_RECIPIENT = "CREATE TABLE " + RecipientEntry.TABLE_NAME + " (" +
            // vytvoreni nove tabulky pro ulozeni znamych prijemcu
            RecipientEntry._ID + " INTEGER PRIMARY KEY," +
            RecipientEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
            RecipientEntry.COLUMN_NAME_ACCOUNT_NUM + " TEXT NOT NULL, " +
            RecipientEntry.COLUMN_NAME_OWNER_USERNAME + " TEXT NOT NULL" +
            ")";

    public static final String SQL_DELETE_ACCOUNT = "DROP TABLE IF EXISTS " + AccountReader.TABLE_NAME;
    public static final String SQL_DELETE_PAYMENT = "DROP TABLE IF EXISTS " + PaymentEntry.TABLE_NAME;
    public static final String SQL_DELETE_RECIPIENT = "DROP TABLE IF EXISTS " + RecipientEntry.TABLE_NAME;
}
