package kz.abzaloid.control3dsound;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

	public static SoundEnv env;
	public static Source sound;
	public static Buffer sound_source;


	public static String[] titles;
	public static String[] descriptions;

	public static Source [] SoundSources;
	public static Buffer [] Sources;

	String filepath = Environment.getExternalStorageDirectory().getPath();

	
	public static final int numberOfBuiltInFiles = 7; 
	
	public static int numberOfSounds = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int CountOfWavFiles = 0;
        
        List <String> mArrayList = new ArrayList<String>();
        
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	File Count = new File (filepath + "/" + SaveFile.ProgramName, "Count" + SaveFile.FileExtension);
        	if (Count.exists()) {
        		Scanner inCount = null;
        		try {
        			inCount = new Scanner (Count);
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        		}
        		CountOfWavFiles = inCount.nextInt();
        		inCount.close();
        		File myList = new File (filepath + "/" + SaveFile.ProgramName, "List" + SaveFile.FileExtension);
        		if (myList.exists()) {
        			Scanner inList = null;
        			try {
        				inList = new Scanner (myList);
        			} catch (FileNotFoundException e) {
        				e.printStackTrace();
        			}
            		for (int i = 0; i < CountOfWavFiles; i++) {
            			String currentFile = inList.nextLine();
            			mArrayList.add(currentFile);
            		}
        			inList.close();
        		}
        	} else {
        		CountOfWavFiles = 0;
        	}
        }
        int maxNumberOfSounds = 60;
        numberOfSounds = CountOfWavFiles + numberOfBuiltInFiles;
        titles = new String [maxNumberOfSounds];
        descriptions = new String[maxNumberOfSounds];
        SoundSources = new Source[maxNumberOfSounds];
        Sources = new Buffer[maxNumberOfSounds];
        titles[0] = "RECORD";		descriptions[0] = "record sound";
        titles[1] = "Lake";			descriptions[1] = "";
        titles[2] = "UFO";			descriptions[2] = "";
        titles[3] = "Park";			descriptions[3] = "";
        titles[4] = "Bee";			descriptions[4] = "";
        titles[5] = "Cat";			descriptions[5] = "";
        titles[6] = "Evil";			descriptions[6] = "";

        int j = 0;
        for (String item : mArrayList) {
        	titles[numberOfBuiltInFiles + j] = item;
        	descriptions[numberOfBuiltInFiles + j] = "";
        	j++;
        }
        try {
			env = SoundEnv.getInstance(this);
			for (int i = 1; i < numberOfSounds; i++) {
				String currentSound = titles[i];
				if (i < numberOfBuiltInFiles) {
					currentSound = currentSound.toLowerCase();
					Sources[i] = env.addBuffer(currentSound);
				} else {
					Log.i("currentSound", currentSound);
					Log.i("filepath", filepath + "/" + SaveFile.ProgramName + "/" + currentSound + SaveFile.FileExtension);
					Sources[i] = env.addBuffer(currentSound, filepath + "/" + SaveFile.ProgramName + "/" + currentSound + ".wav");
				}
				SoundSources[i] = env.addSource(Sources[i]);
				SoundSources[i].setPosition(0.0f, 0.0f, 0.0f);
			}
		} catch (Exception e) {
			Log.e("Damn", "Could not initialize openal4android(((");
		}    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void goToInstructions (View v) {
    	Intent intent = new Intent (this, Instructions.class);
    	startActivity(intent);    	
    }
    
    public void goToAbout (View v) {
    	Intent intent = new Intent (this, About.class);
    	startActivity(intent);
    }
    
    public void goToStart (View v) {
    	Intent intent = new Intent (this, Choose.class);
    	startActivity(intent);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
        	env.release();
            System.exit(0);
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onResume () {
    	super.onResume();
    	Log.i("MAINACTIVITY", "onResume");
    }
    
    @Override
    public void onPause () {
    	super.onPause();    	
    	Log.i("MAINACTIVITY", "onPause");
    }
    
    @Override
    public void onDestroy () {
    	super.onDestroy();
    	Log.i("MAINACTIVITY", "onDestroy");
    	env.release();
    }
    
}
