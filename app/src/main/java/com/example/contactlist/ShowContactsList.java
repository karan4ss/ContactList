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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    ArrayList<ModelGroupName> groupNameList;
    Spinner groupNameSpinnerInShowList;
    GroupDATABASE groupDATABASE = new GroupDATABASE(this);
    EditText etSearch;

    //

    ArrayList<ContactAddedModelClass> arraylistofselectedusers = new ArrayList<ContactAddedModelClass>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts_list);
        rvContactFromPhone = findViewById(R.id.rvOfPhoneContactList);
        btnAddSingleContacts = findViewById(R.id.btnAddSingleContacts);
        groupNameSpinnerInShowList = findViewById(R.id.spinnerOfGropuNameInShowList);
        etSearch = findViewById(R.id.etSearchContact);
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

               /* Intent intent = new Intent(ShowContactsList.this, MainActivity.class);
                intent.putExtra("selectedItems", new ArrayList<>(arraylistofselectedusers));
                startActivity(intent);*/
                for (int i = 0; i < arraylistofselectedusers.size(); i++) {
                    String name = arraylistofselectedusers.get(i).getName();
                    String mobNo = arraylistofselectedusers.get(i).getPhone_number();
                    String fId = groupNameSpinnerInShowList.getSelectedItem().toString();
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, fId);
                    if (isInsertedGroupNumber == true) {
                        Toast.makeText(ShowContactsList.this, "Import Contacts Susccessfully...!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShowContactsList.this, "Failed To Import Contacts...!", Toast.LENGTH_SHORT).show();
                    }
                }


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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapterPhoneContacts.filter(editable.toString());
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        GroupDATABASE groupDATABASE = new GroupDATABASE(this);
        groupNameList = groupDATABASE.getAllData();
        ArrayList<String> onlyGroupName = new ArrayList<>();
        for (ModelGroupName modelGroupName : groupNameList) {
            onlyGroupName.add(modelGroupName.GroupName);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, onlyGroupName);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        groupNameSpinnerInShowList.setAdapter(arrayAdapter);
    }
}