package com.example.se328project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "students.db";
    public static final String TABLE_NAME = "STUDENT";
    public static final String COL1 = "Student_ID";
    public static final String COL2 = "Student_Name";
    public static final String COL3 = "Student_Father_Name";
    public static final String COL4 = "Student_Surname";
    public static final String COL5 = "Student_NID";
    public static final String COL6 = "Student_DOB";
    public static final String COL7 = "Student_Gender";
    private Context context;
    /* Constructor */
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }

    /* Code runs automatically when the dB is created */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + COL1 + " INTEGER PRIMARY KEY," +
                COL2 + " TEXT," +
                COL3 + " TEXT," +
                COL4 + " TEXT," +
                COL5 + " TEXT," +
                COL6 + " TEXT," +
                COL7 + " TEXT);";
        db.execSQL(createTable);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* Basic function to add data. REMEMBER: The fields
       here, must be in accordance with those in
       the onCreate method above.
    */
    public boolean insertStudent(int studentID, String studentName, String studentFatherName, String surname, String studentNID, String studentDOB, String studentGender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, studentID);
        contentValues.put(COL2, studentName);
        contentValues.put(COL3, studentFatherName);
        contentValues.put(COL4, surname);
        contentValues.put(COL5, studentNID);
        contentValues.put(COL6, studentDOB);
        contentValues.put(COL7, studentGender);

        Cursor cursor = getSpecificStudent(String.valueOf(studentID));
        cursor.moveToFirst();
        System.out.println(String.valueOf(studentID));
        System.out.println(cursor.getCount());
        if(cursor.getCount() == 0) {
            db.insert(TABLE_NAME, null, contentValues);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateStudent(int studentID, String studentName, String studentFatherName, String surname, String studentNID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, studentName);
        contentValues.put(COL3, studentFatherName);
        contentValues.put(COL4, surname);
        contentValues.put(COL5, studentNID);


        long result = db.update(TABLE_NAME, contentValues, COL1 + "=?", new String[]{String.valueOf(studentID)});

        //if data are inserted incorrectly, it will return -1
        if(result == -1) {return false;} else {return true;}
    }

    public boolean deleteStudent(int studentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL1 + "=?", new String[]{String.valueOf(studentID)});

        if(result == -1) {return false;} else {return true;}
    }

    /* Returns only one result */
    public Cursor structuredQuery(int ID) {
        SQLiteDatabase db = this.getReadableDatabase(); // No need to write
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1,
                        COL2, COL3}, COL1 + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    //TODO: Students need to try to fix this
    public Cursor getSpecificStudent(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Student_ID=" + ID,null);
        return data;
    }

    // Return everything inside the dB
    public Cursor getListStudent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM STUDENT", null);
        return data;
    }

    public Cursor getSpecificProduct(String productName){
        Log.d("MyAndroid","DB connection started");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME+" where PRODUCT_NAME=\""+productName+"\"",
                null);
        data.moveToFirst();
        String dataID = data.getString(0); //ID
        String dataName = data.getString( 1); // PRODUCT_NAME
        String dataQuantity = data.getString(2); // Quantity
        Log.d( "George", "dataID:"+dataID);
        Log.d( "George","dataName: "+dataName) ;
        Log.d("George","dataQuantity:"+dataQuantity);
        return data;
    }
}
