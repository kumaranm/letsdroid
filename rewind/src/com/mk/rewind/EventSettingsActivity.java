package com.mk.rewind;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class EventSettingsActivity extends PreferenceActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_settings);
		/*EditTextPreference timeDefault = (EditTextPreference) findPreference(getString(R.string.pref_default_time_from_now_key));
		timeDefault.getEditText().setKeyListener(DigitsKeyListener.getInstance());*/
	}

	private void importDB()
	{
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite())
			{
				String currentDBPath = "//data//" + "<package name>" + "//databases//" + "<database name>";
				String backupDBPath = "<backup db filename>"; // From SD
																// directory.
				File backupDB = new File(data, currentDBPath);
				File currentDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getApplicationContext(), "Import Successful!", Toast.LENGTH_SHORT).show();

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
				String currentDBPath = "//data//" + "<package name>" + "//databases//" + "<db name>";
				String backupDBPath = "<destination>";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getApplicationContext(), "Backup Successful!", Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e)
		{

			Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT).show();

		}
	}
}
