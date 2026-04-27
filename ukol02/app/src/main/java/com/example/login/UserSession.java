package com.example.login;

import java.io.Serializable;

/**
 Navrhovy vzor "Prepravka"
 Udaje o prihlasenem uzivateli vetsinu casu zustavaji stejne. Predchozi verze provadela sdileni dat
 pomoci jednoduchych setExtra metod. Prepravka zajisti, ze se bude mezi aktivitami sdiled serializovatelny
 objekt. Udaje jako username a cislo uctu neni potom nutne hledat v kazde aktivite v databazi, ale
 pouze v objektu UserSession.
 */
public class UserSession implements Serializable {
    private String username;
    private String accountNumber;
    private double balance;
    private int bankCode;

    public UserSession(String username, String accountNumber, double balance, int bankCode) {
        this.username = username;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bankCode = bankCode;
    }

    public String getUsername() { return username; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public int getBankCode() { return bankCode; }

    public void setBalance(double balance) { this.balance = balance; }
}
