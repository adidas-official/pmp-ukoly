package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Trida zajistujici pripojeni k databazi
 * Vyuziti navrhoveho vzoru Singleton pro zajisteni pouze jednoho pripojeni.
 */
public class AccountReaderDBHelper extends SQLiteOpenHelper {

    private static AccountReaderDBHelper instance;

    public static final String DATABASE_NAME = "bankApp.db";
    public static final int DATABASE_VERSION = 4;

    // Private constructor for Singleton
    private AccountReaderDBHelper(@Nullable Context context) {
        super(context != null ? context.getApplicationContext() : null, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized AccountReaderDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AccountReaderDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        Cislo uctu se ted uklada do db ve formatu xxxxxxxx/yyyy jako string.
        Zmena oproti predchozi verzi. Tento zpusob je v praxi nejpouzivanejsi.
        Cislo uctu nemelo smysl ukladat jako LONG, lepsi je pouzit string a potom dava
        smysl, aby sloupec obsahoval i kod banky.
         */
        db.execSQL(AccountReaderContract.SQL_CREATE_ACCOUNT);
        db.execSQL(AccountReaderContract.SQL_CREATE_PAYMENT);
        db.execSQL(AccountReaderContract.SQL_CREATE_RECIPIENT);

        ContentValues values = new ContentValues();
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME, "harrypotter");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_PASSWORD, "voldemortsux");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_ACCOUNT_NUM, "2800112233/2010");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE, 100000.0);
        
        db.insert(AccountReaderContract.AccountReader.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(AccountReaderContract.SQL_DELETE_ACCOUNT);
       db.execSQL(AccountReaderContract.SQL_DELETE_PAYMENT);
       db.execSQL(AccountReaderContract.SQL_DELETE_RECIPIENT);
       onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
