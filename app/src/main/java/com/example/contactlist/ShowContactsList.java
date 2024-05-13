package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AdapterAddedPhoneContacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowContactsList extends AppCompatActivity implements AdapterPhoneContacts.OnItemClickListener {
    RecyclerView rvContactFromPhone;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    ArrayList<ContactModel> filteredList;
    AdapterPhoneContacts adapterPhoneContacts;
    AdapterAddedPhoneContacts adapterAddedPhoneContacts;
    AppCompatButton btnAddSingleContacts;
    ArrayList<String> arrayListofusers = new ArrayList();
    ArrayList<ModelGroupName> groupNameList;
    TextView tvofGropuNameInShowList;
    GroupDATABASE groupDATABASE = new GroupDATABASE(this);
    EditText etSearch;
    ArrayList<ContactModel> sizeOfGroup = new ArrayList<>();
    private static final int PHONE_LOADER_ID = 1;
    private static final int CONTACT_LOADER_ID = 2;
    ArrayList<ContactAddedModelClass> arraylistofselectedusers = new ArrayList<ContactAddedModelClass>();
    String groupname;
    ImageView ivBackInToolbar;
    TextView tvTitlteInToolbar;
    androidx.appcompat.widget.Toolbar myToolbar;
    Boolean checkSuccessorFalse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts_list);
        LayoutInflater inflater = LayoutInflater.from(this);
        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);
        View customToolbar = inflater.inflate(R.layout.custom_toolbar, myToolbar, false);
        myToolbar.addView(customToolbar);
        ivBackInToolbar = customToolbar.findViewById(R.id.ivBackInToolbar);
        tvTitlteInToolbar = customToolbar.findViewById(R.id.tvTitlteInToolbar);
        tvTitlteInToolbar.setText("Search Contacts");
        ivBackInToolbar.setVisibility(View.VISIBLE);
        ivBackInToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvContactFromPhone = findViewById(R.id.rvOfPhoneContactList);
        btnAddSingleContacts = findViewById(R.id.btnAddSingleContacts);
        tvofGropuNameInShowList = findViewById(R.id.tvofGropuNameInShowList);

        etSearch = findViewById(R.id.etSearchContact);
        checkSelfPermission();
        groupname = getIntent().getStringExtra("groupname");

        ArrayList<ContactModel> groupNumbersList;
        groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();
        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        //  String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
        for (ContactModel contactModel : groupNumbersList) {
            String groupName = contactModel.id;

            if (groupedMap.containsKey(groupName)) {
                groupedMap.get(contactModel.id).add(contactModel);
            } else {
                // groupNameWiseList.add(contactModel);
                ArrayList<ContactModel> groupNameWiseList = new ArrayList<>();
                groupNameWiseList.add(contactModel);
                groupedMap.put(groupName, groupNameWiseList);
            }
        }

        ArrayList<ContactModel> group1Records = (ArrayList<ContactModel>) groupedMap.get(groupname);
        if (group1Records != null) {

            sizeOfGroup = (ArrayList<ContactModel>) groupedMap.get(groupname);

        }
        tvofGropuNameInShowList.setText(groupname);
        filteredList = new ArrayList<>(arrayList);
        rvContactFromPhone.setLayoutManager(new LinearLayoutManager(this));
        adapterPhoneContacts = new AdapterPhoneContacts(this, filteredList, this, sizeOfGroup);
        rvContactFromPhone.setAdapter(adapterPhoneContacts);


        rvContactFromPhone.addOnItemTouchListener(new RecyclerItemClickListener(this, rvContactFromPhone, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapterPhoneContacts.toggleSelection(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        btnAddSingleContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < arraylistofselectedusers.size(); i++) {
                    String name = ((ContactModel) arraylistofselectedusers.get(i)).getName();
                    String mobNo = ((ContactModel) arraylistofselectedusers.get(i)).getNumber();
                    //String fId = groupNameSpinnerInShowList.getSelectedItem().toString();
                    String fId = tvofGropuNameInShowList.getText().toString();
                    String groupID = groupDATABASE.fetchID(fId);

                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, groupID);
                    if (isInsertedGroupNumber == true) {
                        checkSuccessorFalse = true;
                        // Toast.makeText(ShowContactsList.this, "Import Contacts Susccessfully...!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkSuccessorFalse = false;
                        // Toast.makeText(ShowContactsList.this, "Failed To Import Contacts...!", Toast.LENGTH_SHORT).show();
                    }
                }
                if (checkSuccessorFalse) {
                    Intent intent = new Intent(ShowContactsList.this, MainActivity.class);
                    intent.putExtra("successorfailed", tvofGropuNameInShowList.getText().toString());
                    startActivity(intent);

                }


            }
        });
        adapterPhoneContacts.filter("");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*if (charSequence.length() > 2 || charSequence.length() == 0) {
                    adapterPhoneContacts.filter(charSequence.toString());
                }*/

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //adapterPhoneContacts.filter(editable.toString());
                if (editable.toString().length() > 0 || editable.toString().length() == 0) {
                    adapterPhoneContacts.filter(editable.toString());
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


    /*  private void getContactList() {
          Uri uri = ContactsContract.Contacts.CONTENT_URI;
          String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC";
          // if (sort!=null){
          Cursor cursor = getContentResolver().query(uri, null, null, null, null);
          //}

          if (cursor != null && cursor.getCount() > 0) {
              while (cursor.moveToNext()) {
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
  //        rvContactFromPhone.addOnItemTouchListener(new RecyclerItemClickListener(this, rvContactFromPhone, new RecyclerItemClickListener.OnItemClickListener() {
  //            @Override
  //            public void onItemClick(View view, int position) {
  //                adapterPhoneContacts.toggleSelection(position);
  //            }
  //
  //            @Override
  //            public void onLongItemClick(View view, int position) {
  //
  //            }
  //        }));


      }*/
    private void getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

        Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                ContactModel contactModel = new ContactModel();
                contactModel.setName(name);
                contactModel.setNumber(number);
                arrayList.add(contactModel);
            }
            cursor.close();
        }
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

        arraylistofselectedusers = dataList;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}