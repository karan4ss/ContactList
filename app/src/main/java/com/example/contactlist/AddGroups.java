package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.AdapterAddedPhoneContacts;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddGroups extends AppCompatActivity implements AdapterGroupNames.OnDeleteClickListener {
    AppCompatButton btnsave;
    TextInputEditText etGroupName;
    AdapterGroupNames adapterGroupNames;
    RecyclerView rvGroupNames;
    //    String url = "http://localhost/ContactsProject/";
    GroupDATABASE groupDATABASE = new GroupDATABASE(this);
    ArrayList<ModelGroupName> groupNameList;
    //ArrayList<ModelGroupName> arrayListofGroups = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_groups);

        groupNameList = groupDATABASE.getAllData();
        btnsave = findViewById(R.id.btnSave);
        etGroupName = findViewById(R.id.etGroupName);
        rvGroupNames = findViewById(R.id.rvGrpNames);
        etGroupName.requestFocus();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = etGroupName.getText().toString();
                if (groupName != null) {
                    if (!groupDATABASE.isNameExists(groupName)) {
                        Boolean isInsertedGroup = groupDATABASE.insert_grp_name(groupName);
                        if (isInsertedGroup) {
                            Toast.makeText(AddGroups.this, "Group Created...!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddGroups.this, "Failed to Create Group...!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddGroups.this, "Already Exist...!", Toast.LENGTH_SHORT).show();
                    }

                }
                groupNameList = groupDATABASE.getAllData();
                rvGroupNames.setLayoutManager(new LinearLayoutManager(AddGroups.this));
                adapterGroupNames = new AdapterGroupNames(AddGroups.this, groupNameList, (AdapterGroupNames.OnDeleteClickListener) AddGroups.this);
                rvGroupNames.setAdapter(adapterGroupNames);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        groupNameList = groupDATABASE.getAllData();
        rvGroupNames.setLayoutManager(new LinearLayoutManager(AddGroups.this));
        adapterGroupNames = new AdapterGroupNames(this, groupNameList, (AdapterGroupNames.OnDeleteClickListener) AddGroups.this);
        rvGroupNames.setAdapter(adapterGroupNames);
    }

    @Override
    public void onDeleteClick(int position) {
        // groupDATABASE.deleteGroup(position);
        //groupNameList.remove(position);
        groupDATABASE.deleteGroup(Integer.parseInt(String.valueOf(position)));
        AdapterGroupNames adapterGroupNames = new AdapterGroupNames(this, groupNameList, this);
        adapterGroupNames.notifyItemRemoved(position);
        groupNameList = groupDATABASE.getAllData();
        rvGroupNames.setLayoutManager(new LinearLayoutManager(AddGroups.this));
        adapterGroupNames = new AdapterGroupNames(this, groupNameList, (AdapterGroupNames.OnDeleteClickListener) AddGroups.this);
        rvGroupNames.setAdapter(adapterGroupNames);
    }
    //    public void insertTODatabase(String groupName) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        myApi myapi = retrofit.create(myApi.class);
//        Call<ModelGroupName> call = myapi.addDatta(groupName);
//        call.enqueue(new Callback<ModelGroupName>() {
//            @Override
//            public void onResponse(Call<ModelGroupName> call, Response<ModelGroupName> response) {
//                etGroupName.setText("");
//                Toast.makeText(AddGroups.this, response.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<ModelGroupName> call, Throwable t) {
//                Toast.makeText(AddGroups.this, t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}