package kz.abzaloid.control3dsound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class Choose extends Activity implements OnItemClickListener, OnItemLongClickListener {


    ListView listView;
    List <RowItem> rowItems;
	
    public static String newSound = "";
    public static int newSoundCount = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < MainActivity.numberOfSounds; i++) {
            RowItem item = new RowItem(MainActivity.titles[i], MainActivity.descriptions[i]);
            rowItems.add(item);
        }
        listView = (ListView) findViewById(R.id.listView1);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.my_list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
	}

	String filepath = Environment.getExternalStorageDirectory().getPath();

	
	@Override
	public void onResume () {
		super.onResume();
		Log.i("Choose", "onResume");
		Log.i("newSound", newSound);
		if (!newSound.equals("")) {
			newSoundCount++;
			rowItems.add(new RowItem(newSound, ""));
			listView = (ListView) findViewById(R.id.listView1);
			CustomListViewAdapter newAdapter = new CustomListViewAdapter (this, R.layout.my_list_item, rowItems);
			listView.setAdapter(newAdapter);
			listView.setOnItemClickListener(this);
			listView.setOnItemLongClickListener(this);
			MainActivity.titles[MainActivity.numberOfSounds] = newSound;
			MainActivity.descriptions[MainActivity.numberOfSounds] = "";
			try {
				MainActivity.Sources[MainActivity.numberOfSounds] = MainActivity.env.addBuffer(newSound, filepath + "/" + SaveFile.ProgramName + "/" + newSound + ".wav");
				MainActivity.SoundSources[MainActivity.numberOfSounds] = MainActivity.env.addSource(MainActivity.Sources[MainActivity.numberOfSounds]);
				MainActivity.SoundSources[MainActivity.numberOfSounds].setPosition(0.0f, 0.0f, 0.0f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			MainActivity.numberOfSounds++;
		}
		newSound = "";
	}
	
	private static int numberOfDeletes = 0;
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {   
		if (position >= MainActivity.numberOfBuiltInFiles ) {
			final String erasingFile = rowItems.get(position).getTitle();
			new AlertDialog.Builder(this)
    	    .setTitle("Warning!")
    	    .setMessage("Erase " + erasingFile + ".wav ?")
    	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) {

    	        	numberOfDeletes++;
    	        	
    	        	// read List.abz --> rewrite all titles into it without with index position
    	        	PrintWriter outList = null;
					try {
						outList = new PrintWriter (new File (filepath + "/" + SaveFile.ProgramName + "/" + "List" + SaveFile.FileExtension));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
    	        	for (int i = MainActivity.numberOfBuiltInFiles; i < MainActivity.numberOfSounds; i++) {
    	        		if (MainActivity.titles[i].equals(erasingFile) || MainActivity.titles[i].equals("publicstaticvoidmainstringargs")) {
    	        			MainActivity.titles[i] = "publicstaticvoidmainstringargs";
    	        		} else {
    	        			outList.println(MainActivity.titles[i]);
    	        		}
    	        	}
    	        	outList.close();
    	        	
    	        	// read Count.abz --> decrease by one
    	        	
    	        	PrintWriter outCount = null;
    	        	try {
    	        		outCount = new PrintWriter (new File (filepath + "/" + SaveFile.ProgramName + "/" + "Count" + SaveFile.FileExtension));
    	        	} catch (FileNotFoundException e) {
    	        		e.printStackTrace();
    	        	}
    	        	outCount.println(MainActivity.numberOfSounds - numberOfDeletes - MainActivity.numberOfBuiltInFiles);
    	        	outCount.close();
    	        	
    	        	
    	        	// delete erasingFile.wav
    	        	
    	        	File eFile = new File (filepath + "/" + SaveFile.ProgramName + "/" + erasingFile + ".wav");
    	        	if (eFile.exists()) {
    	        		eFile.delete();
    	        	}
    	        	
    	        	// try to change MainActivity in such way so that this file disappears
    	        	
    	        	generateListView(erasingFile);        	
    	        	
    	        }
    	     })
    	     .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            return;
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	    .show();
			
			
			
		} else {
			new AlertDialog.Builder(this)
    	    .setTitle("Warning!")
    	    .setMessage("You cannot erase built-in files")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            return;
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	    .show();
		}
		return true;
	}
	
	public void generateListView (String erasingFile) {
		List <RowItem> rItems = new ArrayList<RowItem> ();
		for (RowItem item : rowItems) {
			if (item.getTitle().equals(erasingFile) || item.getTitle().equals("publicstaticvoidmainstringargs")) {
				
			} else {
				rItems.add(item);
			}
		}
		rowItems.clear();
		rowItems = rItems;
		listView = (ListView) findViewById(R.id.listView1);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.my_list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
	}
	
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        
        if (position == 0) {
        	if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            	Intent RecordSound = new Intent (this, RecordActivity.class);
            	startActivity(RecordSound);        		
        	} else {
        		new AlertDialog.Builder(this)
        	    .setTitle("Warning!")
        	    .setMessage("Please insert SD card")
        	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	            return;
        	        }
        	     })
        	    .setIcon(android.R.drawable.ic_dialog_alert)
        	    .show();
        	}
        } else {
        	Intent SoundPlayer = new Intent (this, SoundPlayer.class);
        	if (position < MainActivity.numberOfSounds) {
        		RowItem currentItem = rowItems.get(position);
        		int globalPosition = 0;
        		for (int i = 0; i < MainActivity.numberOfSounds + newSoundCount; i++) {
        			if (MainActivity.titles[i].equals(currentItem.getTitle())) {
        				globalPosition = i;
        				break;
        			}
        		}
        		SoundPlayer.putExtra("sound_name", MainActivity.titles[globalPosition]);
        		SoundPlayer.putExtra("sound_type", "usual");
        		SoundPlayer.putExtra("sound_position", String.valueOf(globalPosition));
        	} else {
        		SoundPlayer.putExtra("sound_name", newSound);
        		SoundPlayer.putExtra("sound_type", "new");
        		SoundPlayer.putExtra("sound_position", MainActivity.numberOfSounds + newSoundCount);
        	}
        	startActivity(SoundPlayer);
        }	
       }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose, menu);
		return true;
	}

}
