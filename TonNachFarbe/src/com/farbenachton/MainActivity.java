package com.farbenachton;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;


public class MainActivity extends Activity implements Runnable, PreviewCallback{

	CameraView camView;
	TextView test;
	Handler handler = new Handler();
	boolean play = false;
	private MediaPlayer mPlayer1, mPlayer2, mPlayer3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mPlayer1= MediaPlayer.create(getApplicationContext(), R.raw.rhythm);
		mPlayer2= MediaPlayer.create(getApplicationContext(), R.raw.guitar);
		mPlayer3= MediaPlayer.create(getApplicationContext(), R.raw.song);
		camView = (CameraView) findViewById(R.id.camera);
		test = (TextView) findViewById(R.id.textView1);
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
		playback();
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
				/*r = r/1000;
				g = g/1000;
				b = b/1000;*/
				if(r>180000 && b<140000 && g<140000){
					anzRot++;
				}
				if(g>140000 && b<120000 && r<120000){
					anzGruen++;
				}
				if(b>140000 && r<120000 && g<120000){
					anzBlau++;
				}
			}
		}
		if(anzRot > 5 || anzGruen > 5 || anzBlau > 5){
			play = true;
		}
		if(anzRot <= 5){
			mPlayer1.setVolume(0, 0);
		}else{
			mPlayer1.setVolume(1,1);
		}
		if(anzBlau <= 5){
			mPlayer2.setVolume(0, 0);
		}else{
			mPlayer2.setVolume(1,1);
		}
		if(anzGruen <= 5){
			mPlayer3.setVolume(0, 0);
		}else{
			mPlayer3.setVolume(1,1);
		}
		String k = "r:" + anzRot + " g:" + anzGruen + " b:" + anzBlau;
		test.setText(k);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		camView.setOneShotPreviewCallback(this);
		playback();
		handler.postDelayed(this, 200);
	}  
	void playback(){
		if(play){
			mPlayer1.start();
			mPlayer2.start();
			mPlayer3.start();
		}
	}
	@Override
	public void onStop(){
		mPlayer1.stop();
		mPlayer2.stop();
		mPlayer3.stop();
	}


}
	
