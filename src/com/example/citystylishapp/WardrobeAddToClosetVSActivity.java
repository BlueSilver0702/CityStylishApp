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

public class WardrobeAddToClosetVSActivity extends Activity implements OnClickListener{

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
		setContentView(R.layout.activity_wardrobe_addtocloset_vs);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get button objects.
		btnTops = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_tops);
		btnBottoms = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_bottoms);
		btnSets = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_sets);
		btnAccessories = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_accessories);
		btnFootwear = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_footwear);
		btnJackets = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_jackets);
		btnDone = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs_done);
		
		// Set ClickListner to button objects.
		btnTops.setOnClickListener(this);
		btnBottoms.setOnClickListener(this);
		btnSets.setOnClickListener(this);
		btnAccessories.setOnClickListener(this);
		btnFootwear.setOnClickListener(this);
		btnJackets.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_tops) {
			
			Global.VS_CATEGORY = Global.VS_CATEGORY_TOPS;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_bottoms) {
			
			Global.VS_CATEGORY = Global.VS_CATEGORY_BOTTOMS;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();

		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_sets) {

			Global.VS_CATEGORY = Global.VS_CATEGORY_DRESSES;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();

		}else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_accessories) {

			Global.VS_CATEGORY = Global.VS_CATEGORY_ACCESSORIES;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();
						

		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_footwear) {

			Global.VS_CATEGORY = Global.VS_CATEGORY_FOOTWEAR;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();

		}
		 else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_jackets) {

			Global.VS_CATEGORY = Global.VS_CATEGORY_JACKETS;
			Intent vsIntent=new Intent(v.getContext(), VisitStoreActivity.class);
	        startActivity(vsIntent);
	        finish();

		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs_done) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking create btn");
			Intent addToClosetIntent=new Intent(v.getContext(),WardrobeAddToClosetActivity.class);
	        startActivity(addToClosetIntent);
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
