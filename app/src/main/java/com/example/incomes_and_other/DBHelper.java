package com.example.incomes_and_other;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "financeDB";
    public final String TABLE_NAME;

    public static final String STR_INC = "incomes";
    public static final String STR_EXP = "expenses";
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_SUMMA = "summa";
    public static final String KEY_TYPE = "type";


    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, DB_VERSION);
        TABLE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_DATE + " TEXT, " + KEY_SUMMA + " INTEGER, " + KEY_TYPE + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
