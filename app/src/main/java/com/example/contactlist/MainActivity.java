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
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
    ArrayList<ContactAddedModelClass> manualAddedList = new ArrayList();
    ArrayList<ContactAddedModelClass> arrayListofusers = new ArrayList();
    Boolean flag = false;
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
        //setSupportActionBar(myToolbar);
        LayoutInflater inflater = LayoutInflater.from(this);
        View customToolbar = inflater.inflate(R.layout.custom_toolbar, myToolbar, false);
        myToolbar.addView(customToolbar);
        ivBackInToolbar = customToolbar.findViewById(R.id.ivBackInToolbar);
        tvTitlteInToolbar = customToolbar.findViewById(R.id.tvTitlteInToolbar);
        tvTitlteInToolbar.setText("Unique Promotion App");
        ivBackInToolbar.setVisibility(View.VISIBLE);
        ivBackInToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();

        flag = intent.getBooleanExtra("addContacts", false);
        List<ContactAddedModelClass> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");

        String[] groups = {"Group 1", "Group 2", "Group 3", "Group 4", "Group 5", "Group 6"};
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
                        // Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_animationbottomtotop);
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

        if (selectedItems != null) {
            manualAddedList.addAll(selectedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, groupNumbersList, MainActivity.this);
            recyclerView.setAdapter(adapterPhoneContacts);
        }


        btnAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateMobileNumber(etnumber.getText().toString());


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                /*List<ExcelDataModel> excelDataList = readExcelData(uri);
                for (int i = 0; i < excelDataList.size(); i++) {
                    String name = excelDataList.get(i).getName();
                    String mobNo = excelDataList.get(i).getMobno();
                    String fId = groupNameSpinner.getSelectedItem().toString();
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, fId);
                    if (isInsertedGroupNumber == true) {
                        Toast.makeText(MainActivity.this, "Imported Excel Contacts...!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed To Import Excel Contacts...!", Toast.LENGTH_SHORT).show();
                    }
                }*/
                if (uri != null) {

                    processExcelFile(uri);
                }


            }
        }
    }

    private void processExcelFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            Workbook workbook;

            if (fileUri.getPath().endsWith(".xls")) {
                // For Excel files in XLS format (older version)
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileUri.getPath().endsWith(".xlsx")) {
                // For Excel files in XLSX format (newer version)
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // Unsupported file format
                Toast.makeText(this, "Unsupported file format", Toast.LENGTH_SHORT).show();
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelDataModel> excelList = new ArrayList<>();

            // Assuming the first row contains column headers
            Row headerRow = sheet.getRow(0);
            int nameColumnIndex = findColumnIndex(headerRow, "name");
            int mobileNumberColumnIndex = findColumnIndex(headerRow, "mobile number");

            if (nameColumnIndex == -1 || mobileNumberColumnIndex == -1) {
                // Columns not found
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
            Boolean isNumberExist = false;
//            for (ExcelDataModel excelDataModel : excelList) {
//                isNumberExist=group1Records.contains(new ExcelDataModel(excelDataModel.getMobno()));
//                if (isNumberExist) {
//
//                } else {
//                    if (posibleSize > 0 && group1Records.size() < 200) {
//                        filterList.add(excelDataModel);
//                        posibleSize--;
//                    }
//                }
//            }
            // Check if mobile numbers from list1 exist in list2
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
                        filterList.add(item);
                        posibleSize--;
                    }

                }
            }


            if (filterList.size() == 0) {
                Toast.makeText(this, "Already Exist! OR  only add 200 records", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < filterList.size(); i++) {
                    String name = filterList.get(i).getName();
                    String mobNo = filterList.get(i).getMobno();
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, globalGroupName);
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
                return true; // Mobile number found in the list
            }
        }
        return false; // Mobile number not found in the list
    }

    private int findColumnIndex(Row headerRow, String columnName) {
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                if (cell.getCellType() == CellType.STRING && columnName.equalsIgnoreCase(cell.getStringCellValue())) {
                    return i;
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    // Handle numeric value, e.g., convert it to a string and compare
                    if (columnName.equalsIgnoreCase(String.valueOf(cell.getNumericCellValue()))) {
                        return i;
                    }
                }
            }
        }
        return -1; // Column not found
    }

    private String getStringCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                // Handle numeric value, e.g., format it or convert it to a string
                return String.valueOf(cell.getNumericCellValue());
            } else {
                // Handle other cell types as needed
                return ""; // Return an empty string if the cell type is not STRING or NUMERIC
            }
        } else {
            return ""; // Return an empty string if the cell is null
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

            //to add numbers
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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvGroupName.setText(String.valueOf(groupNameSpinner.getSelectedItem().toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onDeleteClick(int position) {

       /* groupDATABASE.deleteGroupNumber(position);
        AdapterAddedPhoneContacts adapterAddedPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, groupNumbersList, MainActivity.this);
        adapterAddedPhoneContacts.notifyItemRemoved(position);
        makeseprateListAndSet();*/
        //  arrayListofusers.remove(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Contact");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupDATABASE.deleteGroupNumber(position);
                AdapterAddedPhoneContacts adapterAddedPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, groupNumbersList, MainActivity.this);
                adapterAddedPhoneContacts.notifyItemRemoved(position);
                makeseprateListAndSet();
                getGroupList();
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


    public void makeseprateListAndSet() {
        groupNumbersList = groupDATABASE.getAllDataOfGroupNumbers();

        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();
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
        ArrayList<ContactModel> group1Records = (ArrayList<ContactModel>) groupedMap.get(groupNameInspinner);


        //
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, group1Records, MainActivity.this);
        recyclerView.setAdapter(adapterPhoneContacts);
    }

    private void animateLinearLayoutVisibility() {
        llofaddcontactsedittexts.setVisibility(View.VISIBLE);

        // Calculate the Y coordinates for the animation based on button and LinearLayout positions
        int[] tvaddcontactLocation = new int[2];
        int[] linearLayoutLocation = new int[2];
        // Get the Y coordinates of the button and LinearLayout
        tvAddContacts.getLocationInWindow(tvaddcontactLocation);
        llofaddcontactsedittexts.getLocationInWindow(linearLayoutLocation);

        // Calculate the Y delta values for the animation
        float fromYDelta = 0;
        float toYDelta = tvAddContacts.getHeight() - 110;
        ;

        // Create a TranslateAnimation from top to bottom
        Animation animation = new TranslateAnimation(0, 0, fromYDelta, toYDelta);

        // Set the duration of the animation in milliseconds
        animation.setDuration(1000);

        // Optionally, set an interpolator for acceleration and deceleration
        // animation.setInterpolator(new AccelerateDecelerateInterpolator());

        // Start the animation
        llofaddcontactsedittexts.startAnimation(animation);
    }

    private void openFilePicker() {
        /*Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // MIME type for Excel files
        startActivityForResult(intent, READ_REQUEST_CODE);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //  intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    public void getGroupList() {
        Map<String, List<ContactModel>> groupedMap = new HashMap<>();
        String groupNameInspinner = groupNameSpinner.getSelectedItem().toString();

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
        group1Records = (ArrayList<ContactModel>) groupedMap.get(groupNameInspinner);

        //
        if (group1Records != null) {
            String sizeOfGroup = String.valueOf(group1Records.size());
            tvdisaplyCountOfGroup.setText("(" + sizeOfGroup + ")");
        } else {
            tvdisaplyCountOfGroup.setText("(" + "0" + ")");
        }
        //


        //
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterPhoneContacts = new AdapterAddedPhoneContacts(MainActivity.this, group1Records, MainActivity.this);
        recyclerView.setAdapter(adapterPhoneContacts);
        if (group1Records != null) {
            posibleSize = 200 - group1Records.size();
        } else {
            posibleSize = 199;
        }
    }

    /*  private List<ExcelDataModel> readExcelData(Uri uri) {
          List<ExcelDataModel> excelDataList = new ArrayList<>();

          try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
              Workbook workbook = WorkbookFactory.create(inputStream);
              Sheet sheet = workbook.getSheetAt(0); // Assuming data is on the first sheet

              for (Row row : sheet) {
                  ExcelDataModel excelData = new ExcelDataModel();
                  Cell cell1 = row.getCell(0); // Adjust index based on your Excel columns
                  excelData.setName(cell1.getStringCellValue());
                  Cell cell2 = row.getCell(1);
                  excelData.setMobno(cell2.getStringCellValue());
                  // Add more lines for additional columns

                  excelDataList.add(excelData);
              }

              workbook.close();
          } catch (IOException e) {
              e.printStackTrace();
          }

          return excelDataList;
      }*/
    private void validateMobileNumber(String mobileNumber) {
        String regex = "^[0-9]{10}$"; // Regular expression for 10 digits
        if (mobileNumber.matches(regex)) {
            // Valid mobile number
            getGroupList();
            String name = etname.getText().toString();
            String mobNo = etnumber.getText().toString();
            String fId = groupNameSpinner.getSelectedItem().toString();
            if (group1Records != null) {
                if (group1Records.size() < 200) {
//                    for (ContactModel contactModel : group1Records) {
//                        if (isMobileNumberInList(mobNo, group1Records)) {
//                            System.out.println("Mobile number " + contactModel.getNumber() + " exists in the second list.");
//                        } else {
//                            System.out.println("Mobile number " + contactModel.getNumber() + " does not exist in the second list.");
//                            if (group1Records.size() < 200) {
//
//                            }
//                        }
//                    }


                    // }
                    if (!mobileNumber.isEmpty()) {

                        //////////////////////////////////
                        boolean phoneNumberExists = false;
                        for (ContactModel groupItem : group1Records) {
                            if (groupItem.getNumber().equals(mobileNumber)) {
                                phoneNumberExists = true;
                                break;
                            }
                        }

                        if (phoneNumberExists) {
                            // Phone number exists in the groupList
                            Toast.makeText(this, "Phone number already exists in the group list", Toast.LENGTH_SHORT).show();
                        } else {
                            // Phone number does not exist in the groupList
                            // Toast.makeText(this, "Phone number is not in the group list", Toast.LENGTH_SHORT).show();
                            Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, fId);
                            if (isInsertedGroupNumber) {
                                Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                                etname.setText("");
                                etnumber.setText("");

                                makeseprateListAndSet();
                                getGroupList();
                                /*if (group1Records != null) {
                                    Integer sizeOfGroup =(group1Records.size() +1);
                                    tvdisaplyCountOfGroup.setText("(" + sizeOfGroup + ")");
                                }*/
                            }

                        }
                        ////////////////////////////////////

                    }

                } else {
                    Toast.makeText(MainActivity.this, "No, Only add 200 Contacts In Group.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // if (group1Records.size() < 200) {
                if (!name.isEmpty() && !mobNo.isEmpty() && !fId.isEmpty()) {
                    Boolean isInsertedGroupNumber = groupDATABASE.insert_grp_number(name, mobNo, fId);
                    if (isInsertedGroupNumber == true) {
                        etname.setText("");
                        etnumber.setText("");
                        Toast.makeText(MainActivity.this, "Number Saved Susccessfully...!", Toast.LENGTH_SHORT).show();
                        /*Map<String, List<ContactModel>> groupedMap = new HashMap<>();
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
                        group1Records = (ArrayList<ContactModel>) groupedMap.get(groupNameInspinner);
                        if (group1Records != null) {
                            String sizeOfGroup = String.valueOf(group1Records.size());
                            tvdisaplyCountOfGroup.setText("(" + sizeOfGroup + ")");
                        } else {
                            tvdisaplyCountOfGroup.setText("(" + "1" + ")");
                        }*/
                        makeseprateListAndSet();
                        getGroupList();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed To Save...!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all the fields...!", Toast.LENGTH_SHORT).show();
                }
                //makeseprateListAndSet();
            }

        } else {
            // Invalid mobile number
            Toast.makeText(this, "Invalid Mobile Number!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNumberInList(String number) {
        return group1Records.contains(number);
    }


}