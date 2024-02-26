package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AdapterAddedPhoneContacts;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.apache.commons.math3.analysis.function.Add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    androidx.appcompat.widget.Toolbar myToolbar;
    ImageView ivBackInToolbar;
    TextView tvTitlteInToolbar;
    //ArrayList<ModelGroupName> arrayListofGroups = new ArrayList();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_groups);
        LayoutInflater inflater = LayoutInflater.from(this);
        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);
        View customToolbar = inflater.inflate(R.layout.custom_toolbar, myToolbar, false);
        myToolbar.addView(customToolbar);
        ivBackInToolbar = customToolbar.findViewById(R.id.ivBackInToolbar);
        tvTitlteInToolbar = customToolbar.findViewById(R.id.tvTitlteInToolbar);
        tvTitlteInToolbar.setText("Create Groups");
        ivBackInToolbar.setVisibility(View.VISIBLE);

        groupNameList = groupDATABASE.getAllData();
        btnsave = findViewById(R.id.btnSave);
        etGroupName = findViewById(R.id.etGroupName);
        rvGroupNames = findViewById(R.id.rvGrpNames);
        etGroupName.requestFocus();

        ivBackInToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    public void onDeleteClick(int position,String group_name) {
        // groupDATABASE.deleteGroup(position);
        //groupNameList.remove(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Group");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupDATABASE.deleteGroup(Integer.parseInt(String.valueOf(position)));
                AdapterGroupNames adapterGroupNames = new AdapterGroupNames(AddGroups.this, groupNameList, AddGroups.this);
                adapterGroupNames.notifyItemRemoved(position);

                //to delete group data

                Boolean isDeleted = groupDATABASE.delete_data_of_grouprecords(position,group_name);
                if (isDeleted) {
                    Toast.makeText(AddGroups.this, "Delete group data Successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddGroups.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                }
                //
                groupNameList = groupDATABASE.getAllData();
                rvGroupNames.setLayoutManager(new LinearLayoutManager(AddGroups.this));
                adapterGroupNames = new AdapterGroupNames(AddGroups.this, groupNameList, (AdapterGroupNames.OnDeleteClickListener) AddGroups.this);
                rvGroupNames.setAdapter(adapterGroupNames);


            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();

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