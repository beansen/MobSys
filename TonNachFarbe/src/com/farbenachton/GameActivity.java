package com.farbenachton;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends Activity implements Runnable, PreviewCallback{

	int count = 0;
	CameraView camView;
	Handler handler = new Handler();
	boolean play = false;
	private MediaPlayer mPlayer1, mPlayer2, mPlayer3;
	String[] facts;
	int randInt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle target = getIntent().getExtras();
		int lane1 = target.getInt("pack1");
		int lane2 = target.getInt("pack2");
		int lane3 = target.getInt("pack3");
		mPlayer1= MediaPlayer.create(getApplicationContext(), lane1);
		mPlayer2= MediaPlayer.create(getApplicationContext(), lane2);
		mPlayer3= MediaPlayer.create(getApplicationContext(), lane3);
		camView = (CameraView) findViewById(R.id.camera);
		Resources res = getResources();
		facts = res.getStringArray(R.array.facts_array);
		run();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		camView = (CameraView) findViewById(R.id.camera);
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		int width = camera.getParameters().getPreviewSize().width;
		int height = camera.getParameters().getPreviewSize().height;
		if (camera.getParameters().getPreviewFormat() == ImageFormat.NV21) {
			pixel(data, width, height);
			}	
	}

	public void pixel(byte[] bild, int breite, int hoehe){
		int frameSize = breite * hoehe;
		int anzRot = 0;
		int anzBlau = 0;
		int anzGruen = 0;
		for(int i = 0; i < hoehe; i=i+5){
			int uvp = frameSize + (i >> 1) * breite;
			for(int j=0; j < breite; j = j+5){
				int Y = (0xff & ((int) bild[breite*i + j])) - 16;
				if (Y < 0) Y = 0;
				int v = (0xff & bild[uvp+2*(j/2)]) - 128;
				int u = (0xff & bild[uvp+2*(j/2)+1]) - 128;	
				int y1192 = 1192 * Y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);
				if (r < 0) r = 0; else if (r > 262143) r = 262143;
				if (g < 0) g = 0; else if (g > 262143) g = 262143;
				if (b < 0) b = 0; else if (b > 262143) b = 262143;
				int c = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
				if(Color.red(c)-(Color.red(c)+Color.green(c)+Color.blue(c))/3 > 50){
					anzRot++;
				}
				if(Color.green(c)-(Color.red(c)+Color.green(c)+Color.blue(c))/3 > 50){
					anzGruen++;
				}
				if(Color.blue(c)-(Color.red(c)+Color.green(c)+Color.blue(c))/3 > 50){
					anzBlau++;
				}
			}
		}
		count++;
		if(count == 20){
			randInt = new Random().nextInt(17);
		}else if(20 < count && count < 25 ){
			Toast.makeText(this, facts[randInt], Toast.LENGTH_SHORT).show();
		}
		if(count == 80){
			randInt = new Random().nextInt(17);
		}else if(80 < count && count < 85){
			Toast.makeText(this, facts[randInt], Toast.LENGTH_SHORT).show();
		}
		if(count == 140){
			randInt = new Random().nextInt(17);
		}else if(140 < count && count < 145){
			Toast.makeText(this, facts[randInt], Toast.LENGTH_SHORT).show();
		}
		if(count == 200){
			randInt = new Random().nextInt(17);
		}else if(200 < count && count < 205){
			Toast.makeText(this, facts[randInt], Toast.LENGTH_SHORT).show();
		}
		

		if(anzRot > 100 || anzGruen > 100 || anzBlau > 100){
			mPlayer1.start();
			mPlayer2.start();
			mPlayer3.start();
		}
		if(anzRot <= 100){
			mPlayer1.setVolume(0, 0);
		}else{
			mPlayer1.setVolume(1,1);
		}
		if(anzBlau <= 100){
			mPlayer2.setVolume(0, 0);
		}else{
			mPlayer2.setVolume(1,1);
		}
		if(anzGruen <= 100){
			mPlayer3.setVolume(0, 0);
		}else{
			mPlayer3.setVolume(1,1);
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		camView.setOneShotPreviewCallback(this);
		handler.postDelayed(this, 200);
	}  
	@Override
	public void onStop(){
		super.onStop();
		mPlayer1.release();
    	mPlayer2.release();
    	mPlayer3.release();
    	handler.removeCallbacks(this);
	}
	 @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	mPlayer1.release();
	    	mPlayer2.release();
	    	mPlayer3.release();
	    	handler.removeCallbacks(this);
	    }
	
}
