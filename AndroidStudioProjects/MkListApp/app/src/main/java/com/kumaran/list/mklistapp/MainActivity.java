package com.kumaran.list.mklistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ListActivity {

    private ListAdapter todoListAdapter;
    private TodoListSQLHelper todoListSQLHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateTodoList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_add_task:
                AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(this);
                todoTaskBuilder.setTitle("Add Todo Task Item");
                todoTaskBuilder.setMessage("describe the Todo Task...");
                final EditText todoET = new EditText(this);
                todoTaskBuilder.setView(todoET);
                todoTaskBuilder.setPositiveButton("Add Task",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String todoTaskInput = todoET.getText().toString();
                        todoListSQLHelper = new TodoListSQLHelper(MainActivity.this);
                        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.clear();

                        values.put(todoListSQLHelper.COL1_TASK, todoTaskInput);
                        sqLiteDatabase.insertWithOnConflict(todoListSQLHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                        updateTodoList();
                    }
                });
                todoTaskBuilder.setNegativeButton("Cancel", null);
                todoTaskBuilder.create().show();
                return true;
            default:
                return false;
        }

    }

    public void onDoneButtonClick(View view)
    {
        View v = (View) view.getParent();
        TextView todoTV = (TextView) v.findViewById(R.id.todoTaskTV);
        String todoTaskItem = todoTV.getText().toString();

        String deleteTodoItemSql = "DELETE FROM " + todoListSQLHelper.TABLE_NAME + " WHERE "
                + todoListSQLHelper.COL1_TASK + " = '" + todoTaskItem + "'";
        todoListSQLHelper = new TodoListSQLHelper(MainActivity.this);
        SQLiteDatabase sqlDB = todoListSQLHelper.getWritableDatabase();
        sqlDB.execSQL(deleteTodoItemSql);
        updateTodoList();
    }

    public void updateTodoList()
    {
        todoListSQLHelper = new TodoListSQLHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TodoListSQLHelper.TABLE_NAME, new String[]{todoListSQLHelper._ID, TodoListSQLHelper.COL1_TASK},
                null, null, null, null, null);

        todoListAdapter = new SimpleCursorAdapter(this, R.layout.todotask, cursor, new String[]{ TodoListSQLHelper.COL1_TASK}
        , new int[]{R.id.todoTaskTV},0);
        this.setListAdapter(todoListAdapter);
    }
}
