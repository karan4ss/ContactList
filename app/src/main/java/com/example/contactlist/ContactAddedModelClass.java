package com.example.contactlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ContactAddedModelClass implements Parcelable {
    String name, phone_number;
    Boolean isSelected;

    public ContactAddedModelClass() {
    }

    public ContactAddedModelClass(String name, String phone_number,Boolean isSelected) {
        this.name = name;
        this.phone_number = phone_number;
        this.isSelected=isSelected;
    }

    protected ContactAddedModelClass(Parcel in) {
        name = in.readString();
        phone_number = in.readString();
    }

    public static final Creator<ContactAddedModelClass> CREATOR = new Creator<ContactAddedModelClass>() {
        @Override
        public ContactAddedModelClass createFromParcel(Parcel in) {
            return new ContactAddedModelClass(in);
        }

        @Override
        public ContactAddedModelClass[] newArray(int size) {
            return new ContactAddedModelClass[size];
        }
    };

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone_number);
    }
}
