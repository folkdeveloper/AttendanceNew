package com.example.newapp;

public class RegVsAttended {
    String reg;
    int att;

    public RegVsAttended(){

    }

    public RegVsAttended(String reg, int att) {
        this.reg = reg;
        this.att = att;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getAtt() {
        return att;
    }

    public void setAtt(int att) {
        this.att = att;
    }
}
