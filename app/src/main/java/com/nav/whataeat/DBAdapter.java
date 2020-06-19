package com.nav.whataeat;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    //    VARIABLES
    private static final String databaseName = "WhatAEat";
    private static final int databaseVersion = 13;

    //    DATABASE VARIABLES
    private final Context context;
    private DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    //    CLASS DBAdapter
    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }


    //    DatabaseHelper
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                // Create tables
                db.execSQL("CREATE TABLE IF NOT EXISTS goal (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " goal_id INTEGER," +
                        " goal_current_weight INT," +
                        " goal_target_weight INT," +
                        " goal_i_want_to VARCHAR," +
                        " goal_weekly_goal VARCHAR," +
                        " goal_date DATE," +
                        " goal_energy INT," +
                        " goal_proteins INT," +
                        " goal_carbs INT," +
                        " goal_fat INT," +
                        " goal_notes VARCHAR);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                // Create tables
                db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " user_id INTEGER," +
                        " user_email VARCHAR," +
                        " user_password VARCHAR," +
                        " user_salt VARCHAR," +
                        " user_alias VARCHAR," +
                        " user_dob DATE," +
                        " user_gender INT," +
                        " user_height INT," +
                        " user_activity_level INT," +
                        " user_last_seen TIME," +
                        " user_note VARCHAR);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary_cal_eaten (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " fdce_id INT," +
                        " fdce_meal_no INT," +
                        " fdce_energy INT," +
                        " fdce_date DATE);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " fd_id INTEGER," +
                        " fd_date DATE," +
                        " fd_meal_number INT," +
                        " fd_food_id INT," +
                        " fd_food_energy DOUBLE," +
                        " fd_serving_size_gram DOUBLE);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " category_id INTEGER," +
                        " category_name VARCHAR," +
                        " category_parent_id INT," +
                        " category_icon VARCHAR," +
                        " category_note VARCHAR);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS food (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " food_id INTEGER, " +
                        " food_name VARCHAR," +
                        " food_serving_size DOUBLE," +
                        " food_calorie DOUBLE," +
                        " food_user_id INT," +
                        " food_category VARCHAR," +
                        " food_note VARCHAR);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(("DROP TABLE IF EXISTS goal"));
            db.execSQL(("DROP TABLE IF EXISTS users"));
            db.execSQL(("DROP TABLE IF EXISTS food_diary_cal_eaten"));
            db.execSQL(("DROP TABLE IF EXISTS food_diary"));
            db.execSQL("DROP TABLE IF EXISTS categories");
            db.execSQL("DROP TABLE IF EXISTS food ");

            onCreate(db);

            String TAG = "Tag";
            Log.w(TAG, "Upgrading database from version "+ oldVersion+" to "
                    + newVersion + ", which will destroy all old data.");
        }
    } // end of databaseHelper

    // Open Database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // Close Database
    public void close() {
        DBHelper.close();
    }

    // Quote Smart
    public String quoteSmart(String value) {
        boolean isNumeric = false;
        try {
            double myDouble = Double.parseDouble(value);
            isNumeric = true;
        }
        catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        if(!isNumeric) {
            if(value != null && value.length() > 0) {
                value = value.replace("\\","\\\\");
                value = value.replace("'", "\\'");
                value = value.replace("\0", "\\0");
                value = value.replace("\n", "\\n");
                value = value.replace("\r", "\\r");
                value = value.replace("\"", "\\\"");
                value = value.replace("\\xla", "\\Z");
            }
        }

        value = "'" + value + "'";

        return value;
    }
    public double quoteSmart(double value) {
        return value;
    }
    public int quoteSmart(int value) {
        return value;
    }

    // Inserting Data
    public void insert(String table, String fields, String values){
        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
    }

    // Count
    public int count(String table) {
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + table + "", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public Cursor selectPrimaryKey(String table, String primaryKey, long rowId, String[] fields) throws SQLException {
        Cursor mCursor = db.query(table, fields, primaryKey + "=" + rowId, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor select(String table, String[] fields, String whereClause, String whereCondition) throws SQLException {
        Cursor mCursor = db.query(table, fields, whereClause + " = " + whereCondition, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor select(String table, String[] fields, String[] whereClause, String[] whereCondition, String[] whereAndOr) throws SQLException {

        String where = "";
        int arraySize = whereClause.length;
        for(int x = 0; x < arraySize; x++) {
            if(where.equals("")) {
                where = whereClause[x] + "=" + whereCondition[x];
            }
            else {
                where = where + " " + whereAndOr[x-1] + " " + whereClause[x] + "=" + whereCondition[x];
            }
        }

//        Toast.makeText(context, where, Toast.LENGTH_SHORT).show();

        Cursor mCursor = db.query(table, fields, where, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor select(String table, String[] fields, String whereClause, long whereCondition) throws SQLException {
        Cursor mCursor = db.query(table, fields, whereClause + "=" + whereCondition, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor select(String table, String[] fields, String orderBy) throws SQLException {
        Cursor mCursor = db.query(table, fields, null, null, orderBy, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Update
    public boolean update(String table, String primaryKey, long rowId, String field, String value) {
        value = value.substring(1, value.length()-1);

        ContentValues args = new ContentValues();
        args.put(field, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }

    public boolean update(String table, String primaryKey, long rowId, String[] fields, String[] values) {

        ContentValues args = new ContentValues();
        for(int i = 0; i<fields.length; i++) {
            values[i] = values[i].substring(1, values[i].length() - 1);
            args.put(fields[i], values[i]);
            db.update(table, args, primaryKey + "=" + rowId, null);
        }
        return true;
    }

    public boolean update(String table, String primaryKey, long rowId, String field, double value) {

        ContentValues args = new ContentValues();
        args.put(field, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }

    public boolean update(String table, String primaryKey, long rowId, String field, int value) {

        ContentValues args = new ContentValues();
        args.put(field, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }
}