package com.myapp.app04;

public class RateItem {
    private int id;
    private String CurName;
    private String CurRate;

    public int getId() {
        return id;
    }

    public String getCurName() {
        return CurName;
    }

    public String getCurRate() {
        return CurRate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurName(String curName) {
        CurName = curName;
    }

    public void setCurRate(String curRate) {
        CurRate = curRate;
    }
}
