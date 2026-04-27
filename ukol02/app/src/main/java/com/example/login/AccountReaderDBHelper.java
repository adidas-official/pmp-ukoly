package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AccountReaderDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bankApp.db";
    public static final int DATABASE_VERSION = 1;

    public AccountReaderDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountReaderContract.SQL_CREATE_ACCOUNT);

        ContentValues values = new ContentValues();
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_USERNAME, "harrypotter");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_PASSWORD, "voldemortsux");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_BANK_CODE_OWNER, 2010);
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_ACCOUNT_NUM, "280011223344");
        values.put(AccountReaderContract.AccountReader.COLUMN_NAME_BALANCE, 100000.0);
        
        db.insert(AccountReaderContract.AccountReader.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(AccountReaderContract.SQL_DELETE_ACCOUNT);
       onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
