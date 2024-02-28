package com.example.contactlist;

import java.util.Objects;

public class ContactModel extends ContactAddedModelClass {
    String grpnumberid, id, name, number;
    boolean isselectContact;

    public ContactModel(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public ContactModel() {
    }

    public ContactModel(String name) {
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

    public boolean isIsselectContact() {
        return isselectContact;
    }

    public void setIsselectContact(boolean isselectContact) {
        this.isselectContact = isselectContact;
    }

    //    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        ContactModel contactModel = (ContactModel) obj;
//        return Objects.equals(phone_number, contactModel.phone_number);
//    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ContactModel other = (ContactModel) obj;
        // Compare relevant fields for equality
        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.phone_number, other.phone_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone_number);
    }
}
