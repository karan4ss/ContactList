package com.example.contactlist;

public class PasteMobilNumberModel {
    String mob_no;
    String mobile_no;

    public PasteMobilNumberModel(String mob_no, String mobile_no) {
        this.mob_no = mob_no;
        this.mobile_no = mobile_no;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
