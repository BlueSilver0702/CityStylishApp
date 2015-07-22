package com.example.citystylishapp;

import com.agilepoet.introduceyourself.util.ImageViewButton;

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

public class WardrobeActivity extends Activity implements OnClickListener{

	ImageViewButton btnCloset, btnCreateOutfits, btnEscape, btnTidyOutfits, btnViewOutfits;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wardrobe);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get button objects.
		btnCloset = (ImageViewButton) findViewById(R.id.btn_wardrobe_closet);
		btnCreateOutfits = (ImageViewButton) findViewById(R.id.btn_wardrobe_create_outfit);
		btnEscape = (ImageViewButton) findViewById(R.id.btn_wardrobe_escape);
		btnTidyOutfits = (ImageViewButton) findViewById(R.id.btn_wardrobe_tidy_outfits);
		btnViewOutfits = (ImageViewButton) findViewById(R.id.btn_wardrobe_view_outfits);
		
		// Set ClickListner to button objects.
		btnCloset.setOnClickListener(this);
		btnCreateOutfits.setOnClickListener(this);
		btnEscape.setOnClickListener(this);
		btnTidyOutfits.setOnClickListener(this);
		btnViewOutfits.setOnClickListener(this);
		
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_wardrobe_closet) {
			
			Log.d("Wardrobe page : ", "Clicking closet btn");			
			Intent wardrobeAddToClosetIntent=new Intent(v.getContext(),WardrobeAddToClosetActivity.class);
	        startActivity(wardrobeAddToClosetIntent);
	        finish();
	        
		} else if (v.getId() == R.id.btn_wardrobe_tidy_outfits) {
			
			Log.d("Wardrobe page : ", "Clicking tidy btn");
			Intent wardrobeTidyClosetIntent=new Intent(v.getContext(),WardrobeTidyClosetActivity.class);
	        startActivity(wardrobeTidyClosetIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_view_outfits) {

			Log.d("Wardrobe page : ", "Clicking view btn");
			
			Intent viewOutfitsIntent=new Intent(v.getContext(), OutfitActivity.class);
	        startActivity(viewOutfitsIntent);
	        finish();

		}else if (v.getId() == R.id.btn_wardrobe_escape) {

			Log.d("Wardrobe page : ", "Clicking escape btn");
			Intent profileIntent=new Intent(v.getContext(),ProfileActivity.class);
	        startActivity(profileIntent);
	        finish();
			

		} else if (v.getId() == R.id.btn_wardrobe_create_outfit) {

			Log.d("Wardrobe page : ", "Clicking create btn");			
			Intent createOutfitIntent = new Intent(v.getContext(),WardrobeCreateoutfitActivity.class);
	        startActivity(createOutfitIntent);
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
