package com.kumaran.list.mklistapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.Context;

/**
 * Created by kmuruganandham on 12/10/2014.
 */
public class TodoListSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "com.kumaran.list.todo";
    public static final String TABLE_NAME = "TODO_LIST";
    public static final String COL1_TASK = "todo";
    public static final String _ID = BaseColumns._ID;

    public TodoListSQLHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB)
    {
        String createTodoListTable = "CREATE TABLE " + TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_TASK + " TEXT )";
        sqlDB.execSQL(createTodoListTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }
}
