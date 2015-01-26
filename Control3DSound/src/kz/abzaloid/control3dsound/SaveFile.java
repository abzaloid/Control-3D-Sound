package kz.abzaloid.control3dsound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SaveFile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_file);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.save_file, menu);
		return true;
	}
	
	public static final String FileExtension = ".abz";
	public static final String ProgramName = "Control3dSound";
	
	public void SaveMyFile (View v) {
		
		Log.i("SAVING FILE", "Button Pressed");
		
		EditText fName = (EditText) findViewById(R.id.filename);
		TextView Warning = (TextView) findViewById(R.id.warning);
		
		String name = fName.getText().toString();
		
		Boolean CorrectChars = check (name);
		
	    String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, ProgramName);
        
        if(!file.exists())
        	file.mkdirs();
    
		if (!CorrectChars || isUsed(name)) {
			if (!CorrectChars) {
				Warning.setText("You can use only ['a'..'z'], ['A'..'Z'], ['0'..'9']");
			} else {
				Warning.setText("Such name is already used. Choose another one.");
			}
		} else {
			addName(name);
			RecordActivity.FileName = name;
			finish();
		}	
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
	}
	
	public Boolean check (String name) {
		if (name.length() == 0) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			if (((int)name.charAt(i) >= (int)'a' && (int)name.charAt(i) <= (int)'z')
			 || ((int)name.charAt(i) >= (int)'A' && (int)name.charAt(i) <= (int)'Z')
			 || ((int)name.charAt(i) >= (int)'0' && (int)name.charAt(i) <= (int)'9')) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public void addName (String name) {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File Count = new File (filepath + "/" + ProgramName, "Count" + FileExtension);
		if (Count.exists()) {
			Scanner inCount = null;
			try {
				inCount = new Scanner (Count);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			int cnt = inCount.nextInt();
			inCount.close();
			cnt++;
			PrintWriter outCount = null;
			try {
				outCount = new PrintWriter (Count);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			outCount.println(cnt);
			outCount.close();
			FileWriter outList = null;
			try {
				outList = new FileWriter (filepath + "/" + ProgramName + "/" + "List" + FileExtension, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outList.write(name + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outList.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			PrintWriter outCount = null;
			try {
				outCount = new PrintWriter (Count);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			outCount.println(1);
			outCount.close();
			PrintWriter outList = null;
			try {
				outList = new PrintWriter (new File (filepath + "/" + ProgramName, "List" + FileExtension));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			outList.println(name);
			outList.close();
		}
	}
	
	
	public Boolean isUsed (String name) {
		String filepath = Environment.getExternalStorageDirectory().getPath();
        File Count = new File (filepath + "/" + ProgramName, "Count" + FileExtension);
		if (Count.exists()) {
			Scanner inCount = null;
			try {
				inCount = new Scanner (Count);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			int cnt = inCount.nextInt();
			inCount.close();
			if (cnt == 0) {
				return false;
			}
			File FilesList = new File (filepath + "/" + ProgramName, "List" + FileExtension);
			if (FilesList.exists()) {
				Scanner inList = null;
				try {
					inList = new Scanner (FilesList);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Boolean found = false;
				for (int i = 0; i < cnt; i++) {
					String currentFile = inList.nextLine();
					if (currentFile.equals(name)) {
						found = true;
					}
				}
				inList.close();
				return found;
			} else
				return false;
		}
		return false;
	}
}
