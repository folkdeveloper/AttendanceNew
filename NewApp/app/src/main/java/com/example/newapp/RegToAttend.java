package com.example.newapp;

public class RegToAttend {
    int reg;
    int com;
    int notcom;
    int att;
    int anu;
    int na;
    int cna;

    public RegToAttend() {

    }

    public RegToAttend(int reg, int com, int notcom, int att, int anu, int na, int cna) {
        this.reg = reg;
        this.com = com;
        this.notcom = notcom;
        this.att = att;
        this.anu = anu;
        this.na = na;
        this.cna = cna;
    }

    public int getReg() {
        return reg;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

    public int getCom() {
        return com;
    }

    public void setCom(int com) {
        this.com = com;
    }

    public int getNotcom() {
        return notcom;
    }

    public void setNotcom(int notcom) {
        this.notcom = notcom;
    }

    public int getAtt() {
        return att;
    }

    public void setAtt(int att) {
        this.att = att;
    }

    public int getAnu() {
        return anu;
    }

    public void setAnu(int anu) {
        this.anu = anu;
    }

    public int getNa() {
        return na;
    }

    public void setNa(int na) {
        this.na = na;
    }

    public int getCna() {
        return cna;
    }

    public void setCna(int cna) {
        this.cna = cna;
    }
}
