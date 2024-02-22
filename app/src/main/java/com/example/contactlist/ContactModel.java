package com.example.contactlist;

public class ContactModel {
    String grpnumberid, id, name, number;

    public ContactModel(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public ContactModel() {
    }

    public String getGrpnumberid() {
        return grpnumberid;
    }

    public void setGrpnumberid(String grpnumberid) {
        this.grpnumberid = grpnumberid;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
