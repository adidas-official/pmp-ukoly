package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.MessageFormat;

public class dbConnect extends SQLiteOpenHelper {

    private static final String dbName = "BankApp";
    private static final String account_table = "account";
    private static final String payments_table = "payments";
    private static final int version = 1;

//    Columns of Account table
    private static final String account_number = "account_number";
    private static final String bankcode_owner = "bankcode_owner";
    private static final String balance = "balance";
    private static final String username = "username";
    private static final String password = "password";

//    Columns of Payments table
    private static final String id = "id";
    private static final String counter_account = "counter_account";
    private static final String bankcode = "bankcode";
    private static final String amount = "amount";
    private static final String date = "date";
    private static final String vs = "vs";
    private static final String ss = "ss";
    private static final String ks = "ks";
    private static final String note = "note";
    private static final String note2rec = "note2rec";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_create_account = MessageFormat.format("create table {0} ({1} int primary key not null, {2} int not null, {3} float null default 0, {4} text not null, {5} text not null)", account_table, account_number, bankcode_owner, balance, username, password);
//        String query_create_payments = MessageFormat.format("create table {0} ({1} int primary key not null autoincrement, {2} int not null, {3} int not null, {4} float not null, {5} date not null, {6} int null, {7} int null, {8} int null, {9} text null, {10} text null)", payments_table, id, counter_account, bankcode, amount, date, vs, ss, ks, note, note2rec);

        db.execSQL(query_create_account);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query_drop_account_on_upgrade = MessageFormat.format("drop table {0} if exists", account_table);
//        String query_drop_payments_on_upgrade = MessageFormat.format("drop table {0} if exists", payments_table);
        db.execSQL(query_drop_account_on_upgrade);
//        db.execSQL(query_drop_payments_on_upgrade);
        onCreate(db);

    }

    public void addAccount(Account account) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(account_number, account.getAccountNumber());
        values.put(bankcode_owner, account.getBankcode());
        values.put(balance, account.getBalance());
        values.put(username, account.getUsername());
        values.put(password, account.getPassword());

        database.insert(account_table, null, values);
    }

//    public void addPayment(Payment payment) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(id, payment.getId());
//        values.put(counter_account, payment.getFrom());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//        values.put(id, payment.getId());
//    }
}
