package com.example.contactlist;

public class ExcelDataModel {
    String name,mobno;

    public ExcelDataModel(String name, String mobno) {
        this.name = name;
        this.mobno = mobno;
    }

    public ExcelDataModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }
}
