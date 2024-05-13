package com.example.contactlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AdapterAddedPhoneContacts;

/*import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;*/

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterAddedPhoneContacts.OnDeleteClickListener, AdapterView.OnItemSelectedListener {
    Spinner groupNameSpinner;
    TextView tvGroupName, tvaddgrp, tvImportContacts, tvAddContacts, tvImportExcelSheet, tvTitlteInToolbar, tvdisaplyCountOfGroup;
    AppCompatButton btnAddContacts;
    ImageView imgImportContacts, imgaddGrups;
    RecyclerView recyclerView;
    AdapterAddedPhoneContacts adapterPhoneContacts;
    Boolean flag = false;
    ArrayList<PasteMobilNumberModel> pastenumbersList = new ArrayList<>();
    String groupNameIntent;
    String groupId;
    AppCompatEditText etname, etnumber;
    ArrayList<ModelGroupName> groupNameList;
    ArrayList<ContactModel> groupNumbersList;
    LinearLayout llofaddcontactsedittexts;
    private static final int PICK_FILE_REQUEST_CODE = 42;
    String globalGroupName;
    ImageView ivBackInToolbar;

    GroupDATABASE groupDATABASE = new GroupDATABASE(this);
    private List<String> excelDataList = new ArrayList<>();
    androidx.appcompat.widget.Toolbar myToolbar;
    ArrayList<ContactModel> group1Records;
    Integer posibleSize = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);
        LayoutInflater inflater = LayoutInflater.from(this);
        View customToolbar = inflater.inflate(R.layout.custom_toolbar, myToolbar, false);
        myToolbar.addView(customToolbar);
        ivBackInToolbar = customToolbar.findViewById(R.id.ivBackInToolbar);
        tvTitlteInToolbar = customToolbar.findViewById(R.id.tvTitlteInToolbar);
        tvTitlteInToolbar.setText("Your Groups");
        ivBackInToolbar.setVisibility(View.VISIBLE);
        ivBackInToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        flag = intent.getBooleanExtra("addContacts", false);
        groupNameIntent = intent.getStringExtra("successorfailed");


        groupNameSpinner = findViewById(R.id.spinnerOfGropuName);
        tvGroupName = findViewById(R.id.tvGroupName);
        btnAddContacts = findViewById(R.id.btnAddContacts);
        recyclerView = findViewById(R.id.rvAddedContacts);
        imgImportContacts = findViewById(R.id.imgImportContacts);
        imgaddGrups = findViewById(R.id.ivAddGroups);
        etname = findViewById(R.id.etContactName);
        etnumber = findViewById(R.id.etnumber);
        tvImportContacts = findViewById(R.id.tvImportContacts);
        tvaddgrp = findViewById(R.id.tvAddGroups);
        tvAddContacts = findViewById(R.id.tvAddContacts);
        llofaddcontactsedittexts = findViewById(R.id.llofContactNumber);
        tvImportExcelSheet = findViewById(R.id.tvImportExcelorCsv);
        tvdisaplyCountOfGroup = findViewById(R.id.tvdisaplyCountOfGroup);


        groupNameSpinner.setOnItemSelectedListener(MainActivity.this);

        tvAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGroupName = groupNameSpinner.getSelectedItem().toString();
                if (globalGroupName != null) {
                    if (llofaddcontactsedittexts.getVisibility() == View.VISIBLE) {
                        // Animation animation = AnimationUtils.loadAnimation(gettvAddContactsApplicationContext(), R.anim.move_animationbottomtotop);
                        // llofaddcontactsedittexts.startAnimation(animation);
                        llofaddcontactsedittexts.setVisibility(View.GONE);

                    } else {
                        /// animateLinearLayoutVisibility();
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideanimationfromtoptobottom);
                        llofaddcontactsedittexts.startAnimation(animation);
                        llofaddcontactsedittexts.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        tvImportExcelSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroupList();
                globalGroupName = groupNameSpinner.getSelectedItem().toString();
                if (globalGroupName != null) {
                    openFilePicker();
                }

            }
        });

        tvImportContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  btnGetContactsFromDevice();
                globalGroupName = groupNameSpinner.getSelectedItem().toString();
                if (globalGroupName != null) {
                    Intent intent = new Intent(MainActivity.this, ShowContactsList.class);
                    intent.putExtra("groupname", globalGroupName);
                    startActivity(intent);
                }


            }
        });
        tvaddgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGroupName = groupNameSpinner.getSelectedItem().toString();
                if (globalGroupName != null) {
                    Intent intent = new Intent(MainActivity.this, AddGroups.class);
                /*Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvaddgrp, "tv_addGroup");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                startActivity(intent, activityOptions.toBundle());*/
                    startActivity(intent);
                }


            }
        });
        btnAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMobileNumber(etnumber.getText().toString());
            }
        });

        etnumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handlePaste();
                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    processExcelFile(uri);
                   /* Uri fixedUri = fixUri(uri);
                    processExcelFile(fixedUri);*/
                }
            }
        }
    }

    private Uri fixUri(Uri uri) {
        if (uri.toString().startsWith("content://com.android.providers.media.documents")) {
            String[] split = uri.getPath().split(":");
            String path = "/storage/" + split[1];
            return Uri.parse("content://com.android.externalstorage.documents" + path);
        } else {
            return uri;
        }
    }

    private void processExcelFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            Workbook workbook;

            if (fileUri.getPath().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileUri.getPath().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
              /*  Toast.makeText(this, "Unsupported file format", Toast.LENGTH_SHORT).show();
                return;*/
                workbook = new XSSFWorkbook(inputStream);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelDataModel> excelList = new ArrayList<>();


            Row headerRow = sheet.getRow(0);
            int nameColumnIndex = findColumnIndex(headerRow, "name");
            int mobileNumberColumnIndex = findColumnIndex(headerRow, "mobile number");

            if (nameColumnIndex == -1 || mobileNumberColumnIndex == -1) {
                Toast.makeText(this, "Columns not found in Excel file", Toast.LENGTH_SHORT).show();
                workbook.close();
                return;
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                String name = getStringCellValue(row, nameColumnIndex);
                String mobileNumber = getStringCellValue(row, mobileNumberColumnIndex);
                double mobileNumberRegular = Double.parseDouble(mobileNumber);
                DecimalFormat decimalFormat = new DecimalFormat("#");
                String formattedNumber = decimalFormat.format(mobileNumberRegular);
                ExcelDataModel excelDataModel = new ExcelDataModel(name, String.valueOf(formattedNumber));
                excelList.add(excelDataModel);

            }
            ArrayList<ExcelDataModel> filterList = new ArrayList<>();

            if (group1Records != null) {
                for (ExcelDataModel item1 : excelList) {
                    if (isMobileNumberInList(item1.getMobno(), group1Records)) {
                        System.out.println("Mobile number " + item1.getMobno() + " exists in the second list.");
                    } else {
                        System.out.println("Mobile number " + item1.getMobno() + " does not exist in the second list.");
                        if (posibleSize > 0 && group1Records.size() < 200) {
                            filterList.add(item1);
                            posibleSize--;
                        }
                    }
                }

            } else {
                if (posibleSize > 0) {
                    for (ExcelDataModel item : excelList) {
                        if (posibleSize >= 0) {
                            filterList.add(item);
                            posibleSize--;
                        }

                    }

                }
            }


            if (filterList.size() == 0) {
                Toast.makeText(this, "Already Exist! OR  only add 200 records", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < filterList.size(); i++) {
                    String name = filterList.get(i).getName();
                    String mobNo = filterList.get(i).getMobno();
                    String fId = groupNameSpinner.getSelectedItem().toString();
                    String groupID = groupDATABASE.fetchID(fId);
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, groupID);
                    if (isInsertedGroupNumber == true) {
                        if (i == filterList.size() - 1) {
                            Toast.makeText(MainActivity.this, "Import Excel Contacts Susccessfully...!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed To Import Excel Contacts...!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading Excel file", Toast.LENGTH_SHORT).show();
        }
    }


    private static boolean isMobileNumberInList(String number, List<ContactModel> list) {
        for (ContactModel item : list) {
            if (number.equals(item.getNumber())) {
                return true;
            }
        }
        return false;
    }

    private int findColumnIndex(Row headerRow, String columnName) {
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                if (cell.getCellType() == CellType.STRING && columnName.equalsIgnoreCase(cell.getStringCellValue())) {
                    return i;
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    if (columnName.equalsIgnoreCase(String.valueOf(cell.getNumericCellValue()))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private String getStringCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else {
                return "";
            }
        } else {
            return "";
        }
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

        if (onlyGroupName.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, AddGroups.class);
            startActivity(intent);
            finish();
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, onlyGroupName);
            arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            groupNameSpinner.setAdapter(arrayAdapter);
            groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();
            groupNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tvGroupName.setText(groupNameSpinner.getSelectedItem().toString());
                    getGroupList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (groupNameIntent != null) {
            int position = getPositionByName(groupNameIntent, groupNameList);
            if (position != -1) {
                groupNameSpinner.setSelection(position);
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvGroupName.setText(String.valueOf(groupNameSpinner.getSelectedItem().toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onDeleteClick(int position, String name) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Contact" + " " + name);
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupDATABASE.deleteGroupNumber(position);
                getGroupListNew();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }


    public void makeseprateListAndSet() {
        groupNumbersList.clear();
        groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();

        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
        for (ContactModel contactModel : groupNumbersList) {
            String groupName = contactModel.id;

            if (groupedMap.containsKey(groupName)) {
                groupedMap.get(contactModel.id).add(contactModel);
            } else {
                ArrayList<ContactModel> groupNameWiseList = new ArrayList<>();
                groupNameWiseList.add(contactModel);
                groupedMap.put(groupName, groupNameWiseList);
            }
        }
        ArrayList<ContactModel> group1Records = (ArrayList<ContactModel>) groupedMap.get(groupNameInspinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, group1Records, MainActivity.this);
        recyclerView.setAdapter(adapterPhoneContacts);
    }


    private void openFilePicker() {
        ///Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/*");
        // intent.setType("application/vnd.ms-excel");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }


    public void getGroupList() {
        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
        String groupId = groupDATABASE.fetchID(groupNameInspinner);
        if (groupId != null) {
            for (ContactModel contactModel : groupNumbersList) {
                String groupName = contactModel.id;
                String grpid = contactModel.getId();

                if (groupedMap.containsKey(grpid)) {
                    groupedMap.get(contactModel.id).add(contactModel);

                } else {
                    // groupNameWiseList.add(contactModel);
                    ArrayList<ContactModel> groupNameWiseList = new ArrayList<>();
                    groupNameWiseList.add(contactModel);
                    groupedMap.put(grpid, groupNameWiseList);
                }
            }
            group1Records = (ArrayList<ContactModel>) groupedMap.get(groupId);

            //
            if (group1Records != null) {
                String sizeOfGroup = String.valueOf(group1Records.size());
                tvdisaplyCountOfGroup.setText("(" + sizeOfGroup + ")");
            } else {
                tvdisaplyCountOfGroup.setText("(" + "0" + ")");
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, group1Records, MainActivity.this);
            recyclerView.setAdapter(adapterPhoneContacts);
            if (group1Records != null) {
                posibleSize = 200 - group1Records.size();
            } else {
                posibleSize = 199;
            }
        }


    }

    public void getGroupListNew() {
        groupNumbersList.clear();
        groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();

        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
        String groupId = groupDATABASE.fetchID(groupNameInspinner);

        if (groupId != null) {
            for (ContactModel contactModel : groupNumbersList) {
                String groupName = contactModel.id;
                String grpid = contactModel.getId();

                if (groupedMap.containsKey(grpid)) {
                    groupedMap.get(contactModel.id).add(contactModel);
                } else {
                    ArrayList<ContactModel> groupNameWiseList = new ArrayList<>();
                    groupNameWiseList.add(contactModel);
                    groupedMap.put(grpid, groupNameWiseList);
                }
            }
            ArrayList<ContactModel> group1Records = (ArrayList<ContactModel>) groupedMap.get(groupId);

            adapterPhoneContacts.setData(group1Records);

            if (group1Records != null) {
                String sizeOfGroup = String.valueOf(group1Records.size());
                tvdisaplyCountOfGroup.setText("(" + sizeOfGroup + ")");
            } else {
                tvdisaplyCountOfGroup.setText("(" + "0" + ")");
            }

            if (group1Records != null) {
                posibleSize = 200 - group1Records.size();
            } else {
                posibleSize = 199;
            }
        }

    }

    private void validateMobileNumber(String mobileNumber) {
        // String regex = "^[0-9]{10}$"; // Regular expression for 10 digits
        // if (mobileNumber.matches(regex)) {
        // Valid mobile number
        getGroupList();
        // String name = etname.getText().toString();

        /////
        String fId = groupNameSpinner.getSelectedItem().toString();
        for (int i = 0; i < pastenumbersList.size(); i++) {
            String mobNo = pastenumbersList.get(i).mob_no;


            //  String mobNo = etnumber.getText().toString();

            if (group1Records != null) {
                if (group1Records.size() < 200) {
                    if (!mobNo.isEmpty()) {
                        boolean phoneNumberExists = false;
                        for (ContactModel groupItem : group1Records) {
                            if (groupItem.getNumber().equals(mobNo)) {
                                phoneNumberExists = true;
                                break;
                            }
                        }
                        if (phoneNumberExists) {
                            //  Toast.makeText(this, "Phone number already exists in the group list", Toast.LENGTH_SHORT).show();
                        } else {
                            String groupID = groupDATABASE.fetchID(fId);
                            /* if (groupID != null) {
                                Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(mobNo, mobNo, groupID);
                                if (isInsertedGroupNumber) {
                                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                                    etname.setText("");
                                    etnumber.setText("");

                                    pastenumbersList.clear();

                                    // makeseprateListAndSet();
                                    getGroupListNew();


                                    recyclerView.scrollToPosition(adapterPhoneContacts.getItemCount() - 1);

                                }

                            } else {
                                // Group ID not found
                            }*/
                            if (!mobNo.isEmpty() && !mobNo.isEmpty() && !fId.isEmpty()) {
                                //String groupID = groupDATABASE.fetchID(fId);
                                Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(mobNo, mobNo, groupID);
                                if (isInsertedGroupNumber == true) {
                                    etname.setText("");
                                    etnumber.setText("");
                                    llofaddcontactsedittexts.setVisibility(View.GONE);
                                    // Toast.makeText(MainActivity.this, "Number Saved Susccessfully...!", Toast.LENGTH_SHORT).show();


                                    makeseprateListAndSet();
                                    getGroupList();
                                    recyclerView.scrollToPosition(adapterPhoneContacts.getItemCount() - 1);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed To Save...!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Please fill all the fields...!", Toast.LENGTH_SHORT).show();
                            }


                        }


                    }

                } else {
                    Toast.makeText(MainActivity.this, "No, Only add 200 Contacts In Group.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!mobNo.isEmpty() && !mobNo.isEmpty() && !fId.isEmpty()) {
                    String groupID = groupDATABASE.fetchID(fId);
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(mobNo, mobNo, groupID);
                    if (isInsertedGroupNumber == true) {
                        etname.setText("");
                        etnumber.setText("");
                        llofaddcontactsedittexts.setVisibility(View.GONE);
                        // Toast.makeText(MainActivity.this, "Number Saved Susccessfully...!", Toast.LENGTH_SHORT).show();


                        makeseprateListAndSet();
                        getGroupList();
                        recyclerView.scrollToPosition(adapterPhoneContacts.getItemCount() - 1);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed To Save...!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all the fields...!", Toast.LENGTH_SHORT).show();
                }
            }

        /*} else {
            Toast.makeText(this, "Invalid Mobile Number!", Toast.LENGTH_SHORT).show();
        }*/
        }
    }
    /////


    private int getPositionByName(String name, ArrayList<ModelGroupName> groupNameList) {
        for (int i = 0; i < groupNameList.size(); i++) {
            if (groupNameList.get(i).getGroupName().equals(name)) {
                return i;
            }
        }
        return -1; // If not found
    }

    private void handlePaste() {
        etnumber.post(new Runnable() {
            @Override
            public void run() {
                String pasteData = "";
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipboard != null && clipboard.hasPrimaryClip()) {
                    android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    pasteData = item.getText().toString();
                }

                String[] numbers = pasteData.split("\\s+");
                StringBuilder formattedText = new StringBuilder();
                PasteMobilNumberModel pasteMobilNumberModel;

                for (String number : numbers) {
                    String trimmedNumber = number.trim();
                    if (isValidMobileNumber(trimmedNumber)) {
                        pasteMobilNumberModel = new PasteMobilNumberModel(trimmedNumber, trimmedNumber);
                        pastenumbersList.add(pasteMobilNumberModel);
                        formattedText.append(trimmedNumber).append("\n");
                    }
                }
                etnumber.setText(formattedText.toString());
                etnumber.setSelection(etnumber.getText().length());
            }
        });
    }


    private boolean isValidMobileNumber(String number) {
        return !TextUtils.isEmpty(number) && TextUtils.isDigitsOnly(number) && number.length() >= 7 && number.length() <= 15;
    }
}