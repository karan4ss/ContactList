package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.AdapterAddedPhoneContacts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterAddedPhoneContacts.OnDeleteClickListener, AdapterView.OnItemSelectedListener {
    Spinner groupNameSpinner;
    TextView tvGroupName, tvaddgrp, tvImportContacts;
    AppCompatButton btnAddContacts;
    ImageView imgImportContacts, imgaddGrups;
    RecyclerView recyclerView;

    AdapterAddedPhoneContacts adapterPhoneContacts;
    ArrayList<ContactAddedModelClass> manualAddedList = new ArrayList();
    ArrayList<ContactAddedModelClass> arrayListofusers = new ArrayList();
    Boolean flag = false;
    AppCompatEditText etname, etnumber;
    List<ContactModel> mydataList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        flag = intent.getBooleanExtra("addContacts", false);
        List<ContactAddedModelClass> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");

        String[] groups = {"Group 1", "Group 2",
                "Group 3", "Group 4",
                "Group 5", "Group 6"};
        //  ArrayList<GroupsModel> groups =new ArrayList<>();

        groupNameSpinner = findViewById(R.id.spinnerOfGropuName);
        tvGroupName = findViewById(R.id.tvGroupName);
        btnAddContacts = findViewById(R.id.btnAddContacts);
        recyclerView = findViewById(R.id.rvAddedContacts);
        imgImportContacts = findViewById(R.id.imgImportContacts);
        imgaddGrups = findViewById(R.id.ivAddGroups);
        etname = findViewById(R.id.etContactName);
        etnumber = findViewById(R.id.etContactNumber);
        tvImportContacts = findViewById(R.id.tvImportContacts);
        tvaddgrp = findViewById(R.id.tvAddGroups);

        groupNameSpinner.setOnItemSelectedListener(MainActivity.this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                groups);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        groupNameSpinner.setAdapter(arrayAdapter);
        tvImportContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  btnGetContactsFromDevice();
                Intent intent = new Intent(MainActivity.this, ShowContactsList.class);
                startActivity(intent);

            }
        });
        tvaddgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGroups.class);
                startActivity(intent);
            }
        });
        if (flag == true) {
           /* ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass();
            arrayListofusers.add(contactAddedModelClass);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapterAddedPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, arrayListofusers);
            recyclerView.setAdapter(adapterAddedPhoneContacts);*/
        }
        if (selectedItems != null) {
            manualAddedList.addAll(selectedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, manualAddedList);
            recyclerView.setAdapter(adapterPhoneContacts);
        }


        btnAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                String mobNo = etnumber.getText().toString();
                if (!name.isEmpty() && !mobNo.isEmpty()) {
                    ContactAddedModelClass contactAddedModelClass = new ContactAddedModelClass(name, mobNo);
                    manualAddedList.add(contactAddedModelClass);

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, manualAddedList);
                recyclerView.setAdapter(adapterPhoneContacts);

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvGroupName.setText(String.valueOf(groupNameSpinner.getSelectedItem().toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void btnGetContactsFromDevice() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);

        }
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("Contacts_Demo", "Name ::: " + contactName + "Phone ::: " + contactNumber);
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        arrayListofusers.remove(position);
        AdapterAddedPhoneContacts adapterAddedPhoneContacts = new AdapterAddedPhoneContacts(this, manualAddedList, this);
        adapterAddedPhoneContacts.notifyItemRemoved(position);
    }
}