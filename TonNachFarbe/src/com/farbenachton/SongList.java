package com.farbenachton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;



public class SongList extends Activity{

	Button lied1, lied2, lied3;
	Bundle bundle = new Bundle();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_songlist);
		lied1 = (Button) findViewById(R.id.button1);
		lied2 = (Button) findViewById(R.id.button2);
		lied3 = (Button) findViewById(R.id.button3);
		lied1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { 
            	int lane1 = R.raw.blinkrhythm;
            	int lane2 = R.raw.blinkguitar;
            	int lane3 = R.raw.blinksong;
        		bundle.putInt("pack1", lane1);
        		bundle.putInt("pack2", lane2);
        		bundle.putInt("pack3", lane3);
            	Intent myIntent = new Intent(SongList.this, GameActivity.class);
            	myIntent.putExtras(bundle);
            	startActivity(myIntent);
            }
        });
    
	lied2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
        	int lane1 = R.raw.blocrhythm;
        	int lane2 = R.raw.blocguitar;
        	int lane3 = R.raw.blocsong;
    		bundle.putInt("pack1", lane1);
    		bundle.putInt("pack2", lane2);
    		bundle.putInt("pack3", lane3);
        	Intent myIntent = new Intent(SongList.this, GameActivity.class);
        	myIntent.putExtras(bundle);
        	startActivity(myIntent);
        }
    });
	
	lied3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
        	int lane1 = R.raw.kissrhythm;
        	int lane2 = R.raw.kissguitar;
        	int lane3 = R.raw.kisssong;
    		bundle.putInt("pack1", lane1);
    		bundle.putInt("pack2", lane2);
    		bundle.putInt("pack3", lane3);
        	Intent myIntent = new Intent(SongList.this, GameActivity.class);
        	myIntent.putExtras(bundle);
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

