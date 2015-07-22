package com.example.citystylishapp;

import java.math.BigDecimal;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.custom.Global;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener{

	ImageViewButton btnWardRobe, btnCityStylistCom, btnWishList;
	
	private static final int DENSITY_XHIGH = 320;
	
	// For Application Context.
	static HomeActivity ha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	
		
		ha = this;
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get button objects.
		btnWardRobe = (ImageViewButton) findViewById(R.id.btn_home_wardrobe);
		btnCityStylistCom = (ImageViewButton) findViewById(R.id.btn_home_citystylistappcom);
		btnWishList = (ImageViewButton) findViewById(R.id.btn_home_wishlist);
			
		
		// Load the personal info.
		Global.g_context = this;
		if(Global.personalInfo == null)
			Global.personalInfo = Global.loadSerializedObject(this.getApplicationContext());
		
		// Init Data.
		SharedPreferences settings = getSharedPreferences("GlobalStates", 0);		
		boolean isFirst = settings.getBoolean("isFirst",true);
		if(isFirst) {
			
			Global.initFirstData(this);
			
			SharedPreferences.Editor editor = settings.edit();     
		    editor.putBoolean("isFirst", false);
		    editor.commit();
		}
		
		InitParam();	
		
		// Set ClickListner to button objects.
		btnWardRobe.setOnClickListener(this);
		btnCityStylistCom.setOnClickListener(this);
		btnWishList.setOnClickListener(this);
		
	}
	
	private void InitParam() {
		Display display = getWindowManager().getDefaultDisplay();
		Global.WIN_W = display.getWidth();
		Global.WIN_H = display.getHeight();

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		if((displayMetrics.densityDpi==DisplayMetrics.DENSITY_HIGH)||(displayMetrics.densityDpi==DENSITY_XHIGH))
			Global.hpdi =true;
		
//		if(G.hpdi ==  false){
		Global.DEFAULT_W = 1024;
		Global.DEFAULT_H  = 768;
//		}else{
//			G.DEFAULT_W = 1500;
//			G.DEFAULT_H  = 2000;
//		}

		Global._scaleX = Global.WIN_W / Global.DEFAULT_W;
		Global._scaleY = Global.WIN_H / Global.DEFAULT_H;		
		
		
//		MyBFFAppDelegate.readScore();
	}  
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_home_wardrobe) {
			
			Log.d("Home page : ", "Clicking wardrobe btn");
			Intent profileIntent=new Intent(v.getContext(),ProfileActivity.class);
	        startActivity(profileIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_home_citystylistappcom) {
			
			Log.d("Home page : ", "Clicking citystylistcom btn");
			Intent i  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.citystylistapp.com"));
			startActivity(i);
			
	        
		} else if (v.getId() == R.id.btn_home_wishlist) {

			Log.d("Home page : ", "Clicking wishlist btn");
			Intent wishListIntent=new Intent(v.getContext(), WishListActivity.class);
	        startActivity(wishListIntent);
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
