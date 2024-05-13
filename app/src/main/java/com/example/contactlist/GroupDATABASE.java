package com.example.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

//import com.pcubetechmedia.unique.activity.PAGE1;
//import com.pcubetechmedia.unique.activity.Page1Model;

import java.util.ArrayList;

public class GroupDATABASE extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UP_GROUP_INFO";
    public static final int VERSION_ID = 1;
    public static final String TABLE_GROUPS_NAME = "group_name";
    public static String GRP_ID = "id";
    public static String GRP_NAME = "grp_name";
    public static String CONTACT_NAME = "name";
    public static String GRP_DATE = "grp_date";

    public static final String TABLE_GROUP_NUMBER = "group_number";
    public static String GRP_NUM_ID = "num_id";
    public static String FOREIGN_GRP_ID = "f_id";
    public static String CONTACT = "number";
    public static String CON_STATUS = "status";
    ArrayList<ModelGroupName> objModelArray_lis;
    ArrayList<ContactModel> groupNumbersList;

    Context context;

    public GroupDATABASE(Context context) {
        super(context, DATABASE_NAME, null, VERSION_ID);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  " + TABLE_GROUP_NUMBER +
                " (" + GRP_NUM_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT, "
                + CONTACT_NAME + " ,"
                + CONTACT + "  NOT NULL ,"
                + FOREIGN_GRP_ID + " NOT NULL"+

                ")"
        );

        db.execSQL("CREATE TABLE " + TABLE_GROUPS_NAME +
                " (" + GRP_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT, "
                + GRP_NAME + "  NOT NULL  " +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_NUMBER);

        onCreate(db);
    }

    public boolean insert_grp_name(String grp_name) {
        if (!isNameExists(grp_name)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(GRP_NAME, grp_name);


            long result = db.insert(TABLE_GROUPS_NAME, null, cv);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public boolean insert_grp_number(String c_name, String contact, String fid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONTACT, contact);
        cv.put(CONTACT_NAME, c_name);

        cv.put(FOREIGN_GRP_ID, fid);
       // cv.put(GRP_ID, grpid);


        long result = db.insert(TABLE_GROUP_NUMBER, null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String fetchID(String grpName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + GRP_ID + " FROM " + TABLE_GROUPS_NAME + " WHERE " + GRP_NAME + " =?", new String[]{grpName});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                return name;
            }

        }
        return null;
    }

    public boolean delete_data(String contact) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_GROUP_NUMBER + " where " + CONTACT + "=?", new String[]{contact});
        if (cursor != null) {
            long result = database.delete(TABLE_GROUP_NUMBER, CONTACT + "=?", new String[]{contact});
            if (result == -1) {
                return false;
            } else {

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean delete_data_of_grouprecords(int grp_id, String grp_name) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_GROUP_NUMBER + " where " + FOREIGN_GRP_ID + "=?", new String[]{grp_name});
        if (cursor != null) {
            long result = database.delete(TABLE_GROUP_NUMBER, FOREIGN_GRP_ID + "=?", new String[]{grp_name});
            if (result == -1) {
                return false;
            } else {

                return true;
            }
        } else {
            return false;
        }
    }

    /*public ArrayList<Page1Model> getContact(String show_fid) {
        try {
            ArrayList<Page1Model> r_recycle_lis = new ArrayList<>();
            SQLiteDatabase opDB = this.getReadableDatabase();
            if (opDB != null) {
                Cursor cursor = opDB.rawQuery("select * from  " + TABLE_GROUP_NUMBER + " WHERE " + FOREIGN_GRP_ID + " =?", new String[]{show_fid});
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String fid = cursor.getString(3);
                        String contact = cursor.getString(2);
                        String c_name = cursor.getString(1);


                        r_recycle_lis.add(new Page1Model(fid, contact, c_name));
                    }
                    return r_recycle_lis;
                } else {
                    Toast.makeText(context, "NO DATA IS RETREIVED || EMPTY ", Toast.LENGTH_SHORT).show();
                    return null;

                }
            } else {
                Toast.makeText(context, "DB IS NULL", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "getAllData " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }*/

    public ArrayList<ModelGroupName> getAllData() {

        try {
            objModelArray_lis = new ArrayList<ModelGroupName>();
            SQLiteDatabase opDB = this.getReadableDatabase();
            if (opDB != null) {
                Cursor cursor = opDB.rawQuery("select * from  " + TABLE_GROUPS_NAME, null);
                if (cursor != null) {

                    while (cursor.moveToNext()) {
                        ModelGroupName modelGroupName = new ModelGroupName();
                        modelGroupName.GroupName = cursor.getString(1);
                        modelGroupName.id = Integer.valueOf(cursor.getString(0));
                        //  String name = cursor.getString(1);


                        //objModelArray_lis.add(name);
                        objModelArray_lis.add(modelGroupName);
                    }
                    return objModelArray_lis;
                } else {
                    Toast.makeText(context, "NO DATA IS RETREIVED || EMPTY ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, AddGroups.class);
                    context.startActivity(i);
                    return null;
                }
            } else {
                Toast.makeText(context, "DB IS NULL", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "getName " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }


    public ArrayList<ContactModel> getAllDataOfGroupNumbers() {

        try {
            groupNumbersList = new ArrayList<ContactModel>();
            SQLiteDatabase opDB = this.getReadableDatabase();
            if (opDB != null) {
                Cursor cursor = opDB.rawQuery("select * from  " + TABLE_GROUP_NUMBER, null);
                if (cursor != null) {

                    while (cursor.moveToNext()) {
                        ContactModel contactModel = new ContactModel();
                        contactModel.name = cursor.getString(1);
                        contactModel.number = (cursor.getString(2));
                        contactModel.id = (cursor.getString(3));
                        contactModel.grpnumberid = (cursor.getString(0));
                        //  String name = cursor.getString(1);


                        //objModelArray_lis.add(name);
                        groupNumbersList.add(contactModel);
                    }
                    return groupNumbersList;
                } else {
                    Toast.makeText(context, "NO DATA IS RETREIVED || EMPTY ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, AddGroups.class);
                    context.startActivity(i);
                    return null;
                }
            } else {
                Toast.makeText(context, "DB IS NULL", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "getName " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }


    //
    public boolean deleteGroup(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
       /* ContentValues cv = new ContentValues();
        cv.put(GRP_ID, id);*/
        long result = db.delete(TABLE_GROUPS_NAME, GRP_ID + " = ? ", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public void deleteGroupNumber(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
       /* ContentValues cv = new ContentValues();
        cv.put(GRP_ID, id);*/
        db.delete(TABLE_GROUP_NUMBER, GRP_NUM_ID + " = ? ", new String[]{String.valueOf(id)});

    }

    public boolean isNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS_NAME, null, GRP_NAME + "=?",
                new String[]{name}, null, null, null, null);

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
    //

    public boolean checkContactExist(String user_contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GROUP_NUMBER + " WHERE " + CONTACT + "   = ? ", new String[]{user_contact});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean checkGroupExist(String user_group) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GROUPS_NAME + " WHERE " + GRP_NAME + "   = ? ", new String[]{user_group});
        if (cursor.getCount() > 0) return true;
        else return false;
    }
}