package kz.abzaloid.control3dsound;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	
	public void ShareIt (View v) {
		Intent share = new Intent (android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		String shareBody = "Control 3D Sound in RealTime with your phone. Control virtual bee inside your brain!\n\nhttps://play.google.com/store/apps/details?id=kz.abzaloid.control3dsound";
		String shareSubject = "Feel and Control 3D Sounds in your Brain in RealTime";
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(share, "Share via"));
	}
	
	public void mark (View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=kz.abzaloid.control3dsound"));
		startActivity(intent);
	}

}
