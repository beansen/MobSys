package com.farbenachton;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements Runnable, PreviewCallback{

	CameraView camView;
	ImageView imageView;
	TextView test;
	Handler handler = new Handler();
	boolean play = false;
	private MediaPlayer mPlayer1, mPlayer2, mPlayer3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mPlayer1= MediaPlayer.create(getApplicationContext(), R.raw.rhythm);
		mPlayer2= MediaPlayer.create(getApplicationContext(), R.raw.guitar);
		mPlayer3= MediaPlayer.create(getApplicationContext(), R.raw.song);
		camView = (CameraView) findViewById(R.id.camera);
		imageView = (ImageView) findViewById(R.id.imageView1);
		 
		// Create a simple layout and add our image view to it.
		/*RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(imageView, params);
		layout.setBackgroundColor(Color.BLACK);
		 
		// Show this layout in our activity.
		setContentView(layout);*/
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
		/*int width1 = 200;
		int height1 = 100;
		 
		Bitmap bitmap1 = Bitmap.createBitmap(width1, height1, Config.RGB_565);
		Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(bitmap1);
		canvas.drawRect(0, 0, 100, 100, paint);
		/*Paint paint2 = new Paint();
		paint2.setColor(Color.YELLOW);
		Canvas canvas2 = new Canvas(bitmap1);
		canvas2.drawRect(10, 10, 50, 100, paint2);
		
		imageView.setImageBitmap(bitmap1);*/

		if(anzRot > 100 || anzGruen > 100 || anzBlau > 100){
			play = true;
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
	
