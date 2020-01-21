package com.example.newapp;

public class RegToAttend {
    int reg;
    int com;
    int notcom;
    int att;

    public RegToAttend() {

    }

    public RegToAttend(int reg, int com, int notcom, int att) {
        this.reg = reg;
        this.com = com;
        this.notcom = notcom;
        this.att = att;
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
}
