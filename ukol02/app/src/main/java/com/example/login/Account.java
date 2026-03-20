package com.example.login;

public class Account {
    private String accountNumber;
    private int bankcode;
    private double balance;
    private String username;
    private String password;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBankcode() {
        return bankcode;
    }

    public void setBankcode(int bankcode) {
        this.bankcode = bankcode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account(String accountNumber, int bankcode, double balance, String username, String password) {
        this.accountNumber = accountNumber;
        this.bankcode = bankcode;
        this.balance = balance;
        this.username = username;
        this.password = password;
    }

}
