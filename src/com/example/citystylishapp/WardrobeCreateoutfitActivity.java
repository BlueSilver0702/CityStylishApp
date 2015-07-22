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

public class WardrobeCreateoutfitActivity extends Activity implements OnClickListener{

	ImageViewButton btnTP, btnSD, btnEscape;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wardrobe_createoutfit);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get button objects.
		btnTP = (ImageViewButton) findViewById(R.id.btn_wardrobe_createoutfit_topbottom);
		btnSD = (ImageViewButton) findViewById(R.id.btn_wardrobe_createoutfit_setdress);
		btnEscape = (ImageViewButton) findViewById(R.id.btn_wardrobe_createoutfit_escape);
				
		// Set ClickListner to button objects.
		btnTP.setOnClickListener(this);
		btnSD.setOnClickListener(this);
		btnEscape.setOnClickListener(this);		
		
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_wardrobe_createoutfit_topbottom) {
			
			Log.d("Wardrobe CreateOutfit page : ", "Clicking TP btn");
			Intent createOutfitTopBotemIntent=new Intent(v.getContext(),CreateOutfitTopBottomActivity.class);
	        startActivity(createOutfitTopBotemIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_createoutfit_setdress) {
			
			Log.d("Wardrobe CreateOutfit page : ", "Clicking SD btn");			
			Intent createOutfitSetDressIntent=new Intent(v.getContext(), CreateOutfitSetDressActivity.class);
	        startActivity(createOutfitSetDressIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_createoutfit_escape) {

			Log.d("Wardrobe CreateOutfit page : ", "Clicking Escape btn");
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
