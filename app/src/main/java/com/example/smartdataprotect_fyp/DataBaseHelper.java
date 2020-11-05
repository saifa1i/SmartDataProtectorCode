package com.example.smartdataprotect_fyp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="SmartDataProtector.db";
    public static final String TABLE_NAME="User_table";
    // public static final String COL_1 = "ID";
    public static final String COL_1 = "UserID";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Password";
    public static final String COL_4 = "Phone";
    public static final String COL_5 = "Emails";

    //Table Commands

    public static final String TABLE_NAME1="Command_table";

    public static final String COL_11 = "Gps";
    public static final String COL_12 = "Ring";
    public static final String COL_13 = "Custom";
    public static final String COL_14 = "Unlock";
    public static final String COL_15 = "Wipedata";
    public static final String COL_16 = "Call";
    public static final String COL_17 = "userid";

    public DataBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME + "(UserID Text PRIMARY KEY, Name Text,  Password Text, Phone Text, Emails Text);");
        db.execSQL("CREATE TABLE "+TABLE_NAME1 + "(Gps Text , Ring Text,  Custom Text, Unlock Text, Wipedata Text, Call Text, UserID Text, FOREIGN KEY (UserID) REFERENCES User_table(UserID));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
    }


    public boolean insertCommands(String Gps, String Ring,  String Custom, String Unlock, String Wipedata, String Call, String userid){
        SQLiteDatabase db = this.getWritableDatabase();
       /* String query = "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
       */
        ContentValues cv = new ContentValues();
        cv.put(COL_11,Gps);
        cv.put(COL_12,Ring);
        cv.put(COL_13,Custom);
        cv.put(COL_14,Unlock);
        cv.put(COL_15,Wipedata);
        cv.put(COL_16,Call);
        cv.put(COL_17,userid);

        long result = db.insert(TABLE_NAME1,null,cv);
        db.close();

        if (result == -1){
            return false;
        }
        else {
            return true;
        }


    }

    public boolean insertData(String UserID, String Name,  String Password, String Phone, String Emails){
        SQLiteDatabase db = this.getWritableDatabase();
       /* String query = "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
       */
        ContentValues cv = new ContentValues();
        // cv.put(COL_1,count);
        cv.put(COL_1,UserID);
        cv.put(COL_2,Name);
        cv.put(COL_3,Password);
        cv.put(COL_4,Phone);
        cv.put(COL_5,Emails);

        long result = db.insert(TABLE_NAME,null,cv);
        db.close();

        if (result == -1){
            return false;
        }
        else {
            return true;
        }


    }
    public  boolean checkUser(String User,String Password){
        String[] columns ={COL_1,COL_3};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_1 + "=?" + " and "+ COL_3 +"=?";
        String[] selectionArgs = {User, Password};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }


    public  boolean checkUserID(String User){
        String[] columns ={COL_1};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_1 + "=?";
        String[] selectionArgs = {User};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }

    public  boolean checkRing(String ring){
        String[] columns ={COL_12};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_12 + "=?";
        String[] selectionArgs = {ring};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }

    public  boolean checkCall(String call){
        String[] columns ={COL_16};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_16 + "=?";
        String[] selectionArgs = {call};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }
    public  boolean checkGps(String gps){
        String[] columns ={COL_11};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_11 + "=?";
        String[] selectionArgs = {gps};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }
    public  boolean checkCustom(String custom){
        String[] columns ={COL_13};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_13 + "=?";
        String[] selectionArgs = {custom};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }
    public  boolean checkUnlock(String unlock){
        String[] columns ={COL_14};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_14 + "=?";
        String[] selectionArgs = {unlock};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }
    public  boolean checkWipe(String wipe){
        String[] columns ={COL_15};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_15 + "=?";
        String[] selectionArgs = {wipe};
        Cursor cursor = db.query(TABLE_NAME1,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count>0){
            return  true;
        }
        else {
            return false;
        }

    }

    public boolean updatePass(String UserID,String Password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL_3,Password);

        int result = db.update(TABLE_NAME,cv,"UserID=?",new String[]{UserID});

        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateCommand(String Gps, String Ring,  String Custom, String Unlock, String Wipedata, String Call, String userid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL_11,Gps);
        cv.put(COL_12,Ring);
        cv.put(COL_13,Custom);
        cv.put(COL_14,Unlock);
        cv.put(COL_15,Wipedata);
        cv.put(COL_16,Call);
        cv.put(COL_17,userid);
        int result = db.update(TABLE_NAME1,cv,"UserID=?",new String[]{userid});

        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }
    public Cursor showData (){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return cursor;
    }

}
