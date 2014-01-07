package com.mk.rewind;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EventSettingActivity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		String[] events = new String[] { "Backup", "Restore" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.setting_row, R.id.settingtext, events);
		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		if (position == 0)
		{
			exportDB();
		}
		else if (position == 1)
		{
			importDB();
		}
	}

	private void importDB()
	{
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite())
			{
				String currentDBPath = "//data//" + "com.mk.rewind" + "//databases//" + DatabaseWrapper.DATABASE_NAME;
				String backupDBPath = "//rewind//backup//"+ DatabaseWrapper.DATABASE_NAME;
				
				File backupDB = new File(data, currentDBPath);
				File currentDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getApplicationContext(), "Import Successful! from " + backupDBPath, Toast.LENGTH_SHORT)
						.show();

			}
		} catch (Exception e)
		{

			Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT).show();

		}
	}

	private void exportDB()
	{
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite())
			{
				String currentDBPath = "//data//" + "com.mk.rewind" + "//databases//" + DatabaseWrapper.DATABASE_NAME;
				String backupDBPath = "//rewind//backup//"+ DatabaseWrapper.DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getApplicationContext(), "Backup Successful! to " + backupDBPath, Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e)
		{

			Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT).show();

		}
	}
}
