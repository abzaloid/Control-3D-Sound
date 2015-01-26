package kz.abzaloid.control3dsound;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;


public class SoundPlayer extends Activity implements SensorEventListener {
		
	
	float cx, cy, radius;
	int width, height;
	private Boolean playing = false;
	DrawBackground pbk;
	DrawCanvasCircle pcc;
	private float pos_x = 0.0f, pos_y = 0.0f, pos_z = 0.0f;
	
	private String sound_name;
	private int sound_position;
	
	SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mMagnetometer;

	RelativeLayout mControls;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

	    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Display display = getWindowManager().getDefaultDisplay();         
        width = display.getWidth(); 
        height = width;
        playing = true;
        cx = (float) width / 2.0f;
        cy = (float) height / 2.0f;
        radius = (float) (Math.min(width, height)) / 2.5f;
        mControls = (RelativeLayout) findViewById (R.id.myLinearLayout);
		
		pbk = new DrawBackground (this);
		Bitmap result = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		pbk.draw(canvas);
		pbk.setLayoutParams(new LayoutParams(width, height));
		mControls.addView(pbk);
		
		pcc = new DrawCanvasCircle(this);
		Bitmap circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas circleCanvas = new Canvas (circleBitmap);
		pcc.draw(circleCanvas);
		pcc.setLayoutParams(new LayoutParams(width, height));
		mControls.addView(pcc);
		Log.i("OnCreate", "main");
		sound_name = getIntent().getExtras().getString("sound_name");
		sound_position = Integer.valueOf(getIntent().getExtras().getString("sound_position"));
		if (sound_position < MainActivity.numberOfBuiltInFiles) {
			// TODO
		} else {
			sound_name = sound_name.toLowerCase();
		}
	}
	
	public void redraw () {
		mControls.removeViewAt(1);
		pcc = new DrawCanvasCircle(this);
		Bitmap circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas circleCanvas = new Canvas (circleBitmap);
		pcc.draw(circleCanvas);
		pcc.setLayoutParams(new LayoutParams(width, height));
		mControls.addView(pcc);
	}

	public class DrawBackground extends View{
	    public DrawBackground(Context mContext) {
	        super(mContext);
	    }
	    public void onDraw(Canvas canvas) {
	        Paint paint = new Paint();
	        paint.setStyle(Paint.Style.FILL);
	        paint.setColor(Color.WHITE);
	        canvas.drawCircle(cx, cy, radius, paint);
	        paint.setColor(Color.BLACK);
	        canvas.drawLine(cx, cy-radius, cx, cy+radius, paint);
	        canvas.drawLine(cx-radius, cy, cx+radius, cy, paint);
	    }
	}
	
	public class DrawCanvasCircle extends View{
	    public DrawCanvasCircle(Context mContext) {
	        super(mContext);
	    }
	    public void onDraw(Canvas canvas) {
	        Paint paint = new Paint();
	        paint.setStyle(Paint.Style.FILL);
	        paint.setColor(Color.RED);
	        canvas.drawCircle(cx + pos_x, cy + pos_y, radius/15, paint);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sound_player, menu);
		return true;
	}
	
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        playing = true;
        MainActivity.SoundSources[sound_position].play(true);
        Log.i("onResume", "slsls");
    }

    @Override
    public void onRestart () {
    	super.onRestart();
    	Log.i("onRestart", "blablabla");
    }
    
    @Override 
    public void onStop () {
    	super.onStop();
    	MainActivity.SoundSources[sound_position].stop();
        playing = false;
    	Log.i("onStop", "blablabla");
    	onDestroy();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        MainActivity.SoundSources[sound_position].stop();
        playing = false;
        mSensorManager.unregisterListener(this);
        Log.i("onPause", "ppppspspsp");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        MainActivity.SoundSources[sound_position].stop();
        playing = false;
        Log.i("onDestroy", "aaaa");
    }

    public void share (View v) {
		Intent share = new Intent (android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		String shareBody = "Control 3D Sound in RealTime with your phone. Control virtual bee inside your brain!\n\nhttps://play.google.com/store/apps/details?id=kz.abzaloid.control3dsound";
		String shareSubject = "Feel and Control 3D Sounds in your Brain in RealTime";
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(share, "Share via"));
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
    private float [] gravity = new float[3];
    private float [] geomag = new float[3];
    private float [] rotationMatrix = new float[16];
    
    @Override
    public void onSensorChanged(SensorEvent event) {
    	int type = event.sensor.getType();
    	
    	if (type == Sensor.TYPE_MAGNETIC_FIELD) {
    		geomag[0] = (geomag[0] * 1 + event.values[0]) * 0.5f;
    		geomag[1] = (geomag[1] * 1 + event.values[1]) * 0.5f;
    		geomag[2] = (geomag[2] * 1 + event.values[2]) * 0.5f;
    	} else 
    	if (type == Sensor.TYPE_ACCELEROMETER) {
    		gravity[0] = (gravity[0] * 2 + event.values[0]) * 0.333334f;
    		gravity[1] = (gravity[1] * 2 + event.values[1]) * 0.333334f;
    		gravity[2] = (gravity[2] * 2 + event.values[2]) * 0.333334f;
    	}
    	if (type == Sensor.TYPE_ACCELEROMETER || type == Sensor.TYPE_MAGNETIC_FIELD) {
    		rotationMatrix = new float[16];
    		SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomag);
    		float angles [] = new float [3];
    		SensorManager.getOrientation(rotationMatrix, angles);
    		TextView textView = (TextView) findViewById(R.id.monitor);
			SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
    		int val = seekBar1.getProgress();
    		if (playing == true) {
    			pos_x = (float) Math.sin(angles[2])  * radius / 1.2f;
    			pos_y = (float) Math.sin(-angles[1]) * radius / 1.2f;
    			pos_z = (float) radius * (100 - val) / 120.0f;
    			redraw();
    			float thresholdValue = 20.0f;
    			float x = thresholdValue * pos_x * 1.2f / radius;
    			float y = thresholdValue * pos_y * 1.2f / radius;
    			float z = thresholdValue * pos_z * 1.2f / radius;
    			MainActivity.SoundSources[sound_position].setPosition(x, y, z);
    			textView.setText ("x = " + String.valueOf(x) + " ;\ny = " + String.valueOf(y) + " ;\nz = " + String.valueOf(z));
    		}
    	}
    }
}
