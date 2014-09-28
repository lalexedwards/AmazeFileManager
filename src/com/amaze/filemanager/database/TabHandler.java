package com.amaze.filemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 9/17/2014.
 */
public class TabHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "explorer.db";
    private static final String TABLE_TAB = "tab";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TAB_NO = "tab_no";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_PATH = "path";
    Context c;
    public TabHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    this.c=context;}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TAB_TABLE = "CREATE TABLE " + TABLE_TAB + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TAB_NO
                + " INTEGER," + COLUMN_LABEL + " TEXT,"
                + COLUMN_PATH + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TAB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TAB);
        onCreate(sqLiteDatabase);
    }

    public void addTab(Tab tab) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TAB_NO, tab.getTab());
        contentValues.put(COLUMN_LABEL, tab.getLabel());
        contentValues.put(COLUMN_PATH, tab.getPath());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TAB, null, contentValues);
        sqLiteDatabase.close();
    }

    public Tab findTab(String tabLabel) {
        String query = "Select * FROM " + TABLE_TAB + " WHERE " + COLUMN_LABEL + "= \"" + tabLabel + "\"";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Tab tab = new Tab();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            tab.setID(Integer.parseInt(cursor.getString(0)));
            tab.setTab(Integer.parseInt(cursor.getString(1)));
            tab.setLabel(cursor.getString(2));
            tab.setPath(cursor.getString(3));
            cursor.close();
        } else {
            tab = null;
        }
        sqLiteDatabase.close();

        return tab;
    }

    public void updateTab(Tab tab) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LABEL, tab.getLabel());
        contentValues.put(COLUMN_PATH, tab.getPath());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(TABLE_TAB, contentValues, tab.getTab() + "=" + COLUMN_ID, null);
        sqLiteDatabase.close();
    }

    public boolean deleteTab(String tabLabel) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_TAB + " WHERE " + COLUMN_LABEL + " = \"" + tabLabel + "\"";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Tab tab = new Tab();

        if (cursor.moveToFirst()) {
            tab.setID(Integer.parseInt(cursor.getString(0)));
            sqLiteDatabase.delete(TABLE_TAB, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(tab.getID())});
            cursor.close();
            result = true;
        }
        sqLiteDatabase.close();

        return result;
    }

    public List<Tab> getAllTabs() {
        List<Tab> tabList = new ArrayList<Tab>();
        // Select all query
        String query = "Select * FROM " + TABLE_TAB;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // Looping through all rows and adding them to list
        if (cursor.moveToFirst()) {
            do {
                Tab tab = new Tab();
                tab.setID(Integer.parseInt(cursor.getString(0)));
                tab.setTab(Integer.parseInt(cursor.getString(1)));
                tab.setLabel(cursor.getString(2));
                tab.setPath(cursor.getString(3));

                //Adding them to list
                tabList.add(tab);
            } while (cursor.moveToNext());
        }
        return tabList;
    }
public ArrayList<String> getTabsNames(){
    ArrayList<String> ar=new ArrayList<String>();
    for(Tab t:getAllTabs()){
        ar.add(t.getLabel());
    }return  ar;
}
    public int getTabsCount() {
        String countQuery = "Select * FROM " + TABLE_TAB;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }
}
