package com.example.citystylishapp;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.custom.Global;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WardrobeTidyClosetActivity extends Activity implements OnClickListener{

	ImageViewButton btnTops;
	ImageViewButton btnBottoms;
	ImageViewButton btnSets;
	ImageViewButton btnAccessories;
	ImageViewButton btnFootwear;
	ImageViewButton btnJackets;
	ImageViewButton btnDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wardrobe_tidycloset);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get button objects.
		btnTops = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_tops);
		btnBottoms = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_bottoms);
		btnSets = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_sets);
		btnAccessories = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_accessories);
		btnFootwear = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_footwear);
		btnJackets = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_jackets);
		btnDone = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidycloset_done);
		
		// Set ClickListner to button objects.
		btnTops.setOnClickListener(this);
		btnBottoms.setOnClickListener(this);
		btnSets.setOnClickListener(this);
		btnAccessories.setOnClickListener(this);
		btnFootwear.setOnClickListener(this);
		btnJackets.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		
		Global.TIDY_SELECT = Global.TIDY_NONE;
		
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_wardrobe_tidycloset_tops) {
			
			Log.d("Wardrobe TidyCloset page : ", "Clicking closet btn");
			
			Global.TIDY_SELECT = Global.TIDY_TOPS;
			
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_tidycloset_bottoms) {
			
			Log.d("Wardrobe TidyCloset page : ", "Clicking tidy btn");

			Global.TIDY_SELECT = Global.TIDY_BOTTOMS;
			
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_tidycloset_sets) {

			Log.d("Wardrobe TidyCloset page : ", "Clicking view btn");

			Global.TIDY_SELECT = Global.TIDY_DRESSES;
			
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();
			
		}else if (v.getId() == R.id.btn_wardrobe_tidycloset_accessories) {

			Log.d("Wardrobe TidyCloset page : ", "Clicking escape btn");
					
			Global.TIDY_SELECT = Global.TIDY_ACCESSORIES;
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();
			

		} else if (v.getId() == R.id.btn_wardrobe_tidycloset_footwear) {

			Log.d("Wardrobe TidyCloset page : ", "Clicking create btn");
			
			Global.TIDY_SELECT = Global.TIDY_FOOTWEARS;
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();
			
		}
		 else if (v.getId() == R.id.btn_wardrobe_tidycloset_jackets) {

			Log.d("Wardrobe TidyCloset page : ", "Clicking create btn");
			
			Global.TIDY_SELECT = Global.TIDY_JACKETS;
			// Go to TidyOutfitActivity
			Intent tidyOutfitIntent=new Intent(v.getContext(), TidyOutfitActivity.class);
			startActivity(tidyOutfitIntent);
			finish();

		} else if (v.getId() == R.id.btn_wardrobe_tidycloset_done) {

			Log.d("Wardrobe TidyCloset page : ", "Clicking create btn");
			Intent wardrobeIntent=new Intent(v.getContext(),WardrobeActivity.class);
	        startActivity(wardrobeIntent);
	        finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
