package com.example.login;

public class Account {
    private int accountNumber;
    private int bankcode;
    private double balance;
    private String username;
    private String password;

    private Account(int accountNumber, int bankcode, double balance, String username, String password) {
        this.accountNumber = accountNumber;
        this.bankcode = bankcode;
        this.balance = balance;
        this.username = username;
        this.password = password;
    }

    public void makePayment(double amount) {
        if (isEnoughBalance(amount)) {
            this.balance = this.balance - amount;
        }
    }

    public void receivePayment(double amount) {
        this.balance = this.balance + amount;
    }

    private Boolean isEnoughBalance(double amount) {
        if (this.balance > amount) {
            return true;
        }
        return false;
    }
}
