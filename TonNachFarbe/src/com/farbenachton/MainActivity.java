package com.farbenachton;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	Button b1, b2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_start);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { ;
            	Intent myIntent = new Intent(MainActivity.this, SongList.class);
            	startActivity(myIntent);
            }
        });
		
		b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { ;
            	Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
            	startActivity(myIntent);
            }
        });
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
	
