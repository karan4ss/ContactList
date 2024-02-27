package com.example.contactlist;

import java.util.Objects;

public class ExcelDataModel {
    String name,mobno;

    public ExcelDataModel(String name, String mobno) {
        this.name = name;
        this.mobno = mobno;
    }

    public ExcelDataModel() {
    }

    public ExcelDataModel(String mobno) {
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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExcelDataModel model = (ExcelDataModel) obj;
        return Objects.equals(mobno, model.mobno);
    }
}
