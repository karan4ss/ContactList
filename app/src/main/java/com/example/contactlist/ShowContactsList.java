package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.AdapterAddedPhoneContacts;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowContactsList extends AppCompatActivity implements AdapterPhoneContacts.OnItemClickListener {
    RecyclerView rvContactFromPhone;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    AdapterPhoneContacts adapterPhoneContacts;
    AdapterAddedPhoneContacts adapterAddedPhoneContacts;
    AppCompatButton btnAddSingleContacts;
    ArrayList<String> arrayListofusers = new ArrayList();

    //

    ArrayList<ContactAddedModelClass> arraylistofselectedusers = new ArrayList<ContactAddedModelClass>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts_list);
        rvContactFromPhone = findViewById(R.id.rvOfPhoneContactList);
        btnAddSingleContacts = findViewById(R.id.btnAddSingleContacts);
        checkSelfPermission();

        btnAddSingleContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*rvContactFromPhone.setLayoutManager(new LinearLayoutManager(ShowContactsList.this));
                adapterAddedPhoneContacts = new AdapterAddedPhoneContacts(ShowContactsList.this, arrayListofusers);
                rvContactFromPhone.setAdapter(adapterAddedPhoneContacts);*/
               /* ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();
                arrayListofusers.add(String.valueOf(contactAddedModelClass));
                Intent intent = new Intent(ShowContactsList.this, MainActivity.class);
                intent.putExtra("addContacts", true);
                intent.putExtra("list", arrayListofusers);
                startActivity(intent);*/

                Intent intent = new Intent(ShowContactsList.this, MainActivity.class);
                intent.putExtra("selectedItems", new ArrayList<>(arraylistofselectedusers));
                startActivity(intent);
            }
        });
    }

    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);

        } else {
            getContactList();
        }
    }


    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC";
        // if (sort!=null){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        //}

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
              /*  @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Log.i("Contacts_Demo", "Name ::: " + contactName + "Phone ::: " + contactNumber);*/
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.Contacts._ID)));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.Contacts.DISPLAY_NAME)));

                @SuppressLint("Range") Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";

                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()) {
                    @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactModel contactModel = new ContactModel();
                    contactModel.setName(name);
                    contactModel.setNumber(number);
                    arrayList.add(contactModel);
                    phoneCursor.close();


                }
            }
            cursor.close();
        }
            rvContactFromPhone.setLayoutManager(new LinearLayoutManager(this));
            adapterPhoneContacts = new AdapterPhoneContacts(this, arrayList, this);
            rvContactFromPhone.setAdapter(adapterPhoneContacts);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            checkSelfPermission();
        }
    }

    @Override
    public void onItemClick(ArrayList<ContactAddedModelClass> dataList) {
        /*Intent intent = new Intent(ShowContactsList.this, MainActivity.class);
        intent.putExtra("selectedItems", new ArrayList<>(dataList));
        startActivity(intent);*/

        arraylistofselectedusers = dataList;
    }
}