package com.example.login;

import java.io.Serializable;

public class Payment implements Serializable {
    private int id;
    private int from;
    private int bcode_from;
    private int to;
    private int bcode_to;
    private float amount;
    private String date;
    private int vs;
    private int ss;
    private int ks;
    private String note;
    private String note2rec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getBcode_from() {
        return bcode_from;
    }

    public void setBcode_from(int bcode_from) {
        this.bcode_from = bcode_from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getBcode_to() {
        return bcode_to;
    }

    public void setBcode_to(int bcode_to) {
        this.bcode_to = bcode_to;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVs() {
        return vs;
    }

    public void setVs(int vs) {
        this.vs = vs;
    }

    public int getSs() {
        return ss;
    }

    public void setSs(int ss) {
        this.ss = ss;
    }

    public int getKs() {
        return ks;
    }

    public void setKs(int ks) {
        this.ks = ks;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote2rec() {
        return note2rec;
    }

    public void setNote2rec(String note2rec) {
        this.note2rec = note2rec;
    }

    public Payment(int id, int from, int bcode_from, int to, int bcode_to, float amount, String date, int vs, int ss, int ks, String note, String note2rec) {
        this.id = id;
        this.from = from;
        this.bcode_from = bcode_from;
        this.to = to;
        this.bcode_to = bcode_to;
        this.amount = amount;
        this.date = date;
        this.vs = vs;
        this.ss = ss;
        this.ks = ks;
        this.note = note;
        this.note2rec = note2rec;
    }
}
