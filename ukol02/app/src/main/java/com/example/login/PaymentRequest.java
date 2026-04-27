package com.example.login;

import java.io.Serializable;

/**
 * Transport Design Pattern: Carries payment details from SendPaymentActivity 
 * to CheckPaymentActivity for review and final processing.
 */
public class PaymentRequest implements Serializable {
    private String toAccount;
    private String bankCode;
    private double amount;
    private String ks;
    private String ss;
    private String vs;
    private String note;
    private String note2rec;

    public PaymentRequest(String toAccount, String bankCode, double amount, String ks, String ss, String vs, String note, String note2rec) {
        this.toAccount = toAccount;
        this.bankCode = bankCode;
        this.amount = amount;
        this.ks = ks;
        this.ss = ss;
        this.vs = vs;
        this.note = note;
        this.note2rec = note2rec;
    }

    public String getToAccount() { return toAccount; }
    public String getBankCode() { return bankCode; }
    public double getAmount() { return amount; }
    public String getKs() { return ks; }
    public String getSs() { return ss; }
    public String getVs() { return vs; }
    public String getNote() { return note; }
    public String getNote2rec() { return note2rec; }
}
