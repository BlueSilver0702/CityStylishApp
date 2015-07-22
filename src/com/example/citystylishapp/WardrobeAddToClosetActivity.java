package com.example.citystylishapp;

import java.util.Timer;
import java.util.TimerTask;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.citystylishapp.OutfitActivity.showPopupTake;
import com.example.custom.Global;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WardrobeAddToClosetActivity extends Activity implements OnClickListener{

	ImageViewButton btnPl, btnVs, btnDone;
	
	boolean isShowPopup;
	
	// Root Layout.
	FrameLayout rootLayout;
	LinearLayout second_rootview;
	
	// Slide Menu layout.
	LinearLayout slideMenuLayout;
	ImageViewButton btnGuys;
	ImageViewButton btnGirls;
	
	// Timer
	Timer timer;
	
	// Timer task for slide popup menu
	class showPopupTake extends TimerTask {

	     @Override
	     public void run() {

	     	if ( !isShowPopup ) {
		    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
	     }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wardrobe_addtocloset);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (FrameLayout) findViewById(R.id.wardrobe_addcloset_rootLayout);
		second_rootview = (LinearLayout) findViewById(R.id.wardrobe_addcloset_secondrootLayout);
		slideMenuLayout = (LinearLayout) findViewById(R.id.wardrobe_addcloset_slidemenu_Layout);
		slideMenuLayout.bringToFront();
		slideMenuLayout.setVisibility(View.INVISIBLE);
		
		
		// Get button objects.
		btnGuys = (ImageViewButton) findViewById(R.id.btn_wardrobe_addcloset_visitstore_guys);
		btnGirls = (ImageViewButton) findViewById(R.id.btn_wardrobe_addcloset_visitstore_girls);
		btnPl = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl);
		btnVs = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_vs);
		btnDone = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_done);
				
		// Set ClickListner to button objects.
		btnGuys.setOnClickListener(this);
		btnGirls.setOnClickListener(this);
		btnPl.setOnClickListener(this);
		btnVs.setOnClickListener(this);
		btnDone.setOnClickListener(this);	
		
		isShowPopup = false;
		
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_wardrobe_addtocloset_pl) {
			
			Log.d("Wardrobe AddToCloset page : ", "Clicking PL btn");
			Intent wardrobeAddToClosetPLIntent=new Intent(v.getContext(),WardrobeAddToClosetPLActivity.class);
	        startActivity(wardrobeAddToClosetPLIntent);
	        finish();
	        
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_vs) {
			
			Log.d("Wardrobe AddToCloset page : ", "Clicking VS btn");
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_done) {

			Log.d("Wardrobe AddToCloset page : ", "Clicking Done btn");
			Intent wardrobeIntent=new Intent(v.getContext(),WardrobeActivity.class);
	        startActivity(wardrobeIntent);
	        finish();
		} else if (v.getId() == R.id.btn_wardrobe_addcloset_visitstore_guys) {
		
			Global.ADDTOSET_VS_TYPE = Global.ADDTOSET_VS_GUY;
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			Log.d("Wardrobe AddToCloset page : ", "Clicking Done btn");
			Intent wardrobeATCVSIntent=new Intent(v.getContext(),WardrobeAddToClosetVSActivity.class);
	        startActivity(wardrobeATCVSIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_wardrobe_addcloset_visitstore_girls) {
			
			Global.ADDTOSET_VS_TYPE = Global.ADDTOSET_VS_GIRL;
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			Log.d("Wardrobe AddToCloset page : ", "Clicking Done btn");
			Intent wardrobeATCVSIntent = new Intent(v.getContext(),WardrobeAddToClosetVSActivity.class);
	        startActivity(wardrobeATCVSIntent);
	        finish();
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void moveViewToScreenCenter(boolean direction )
	{
		//LinearLayout root = (LinearLayout) findViewById( R.id.rootLayout );
	    DisplayMetrics dm = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics( dm );
	    //int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

	    int originalPos[] = new int[2];
	    slideMenuLayout.getLocationOnScreen( originalPos );

	    if(direction) { // from left to right.
	    		    	
//	    	int xDest = dm.widthPixels/2;
//	    	xDest -= (view.getMeasuredWidth()/2);
//	    	int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);// - statusBarOffset;
//
//	    	TranslateAnimation anim = new TranslateAnimation( originalPos[0], xDest - originalPos[0] , 0, 0 );
//	    	anim.setDuration(500);
//	    	anim.setFillAfter( true );
//	    	view.startAnimation(anim);
	    	
	    	// Slide Menu fade in animation.
	    	slideMenuLayout.setVisibility(View.VISIBLE);
	    	Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
	    	fadeInAnimation.setFillAfter(true);
	    	slideMenuLayout.startAnimation(fadeInAnimation );	
	    	
	    	// Second root View fade out animation.
//	    	AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.3f);
//	    	fadeOut.setDuration(200);
//	    	fadeOut.setFillAfter(true);
//	    	second_rootview.startAnimation(fadeOut );
	    	
	    	if (timer != null) {
	    		
	    		timer.cancel();
	    		timer.purge();
	    	}
	    	
	    	// Enable all of buttons on popup
	    	slideMenuLayout.bringToFront();
	    	btnGuys.setEnabled(true);
			btnGirls.setEnabled(true);			
	    	
	    }else { // from right to left    	
	    	
//	    	slideMenuLayout.setVisibility(View.INVISIBLE);
//	    	int xDest = dm.widthPixels/2;
//	    	xDest -= (slideMenuLayout.getMeasuredWidth()/2);;
//	    	
//	    	TranslateAnimation anim = new TranslateAnimation( xDest, originalPos[0] - xDest, 0, 0 );
//	    	anim.setDuration(500);
//	    	anim.setFillAfter( true );
//	    	slideMenuLayout.startAnimation(anim);
	    	
	    	// Slide Menu fade out animation.
	    	Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
	    	fadeOutAnimation.setFillAfter(true);
	    	slideMenuLayout.startAnimation(fadeOutAnimation );	
	    	
	    	// Second root view fade in animation.
//	    	AlphaAnimation fadeIn = new AlphaAnimation(0.3f, 1.0f);
//	    	fadeIn.setDuration(200);
//	    	fadeIn.setFillAfter(true);
//	    	second_rootview.startAnimation(fadeIn );	
	    	
	    	if (timer != null) {
	    		
	    		timer.cancel();
	    		timer.purge();
	    	}
	    	
	    	// Disable all of buttons on popup
//	    	ViewGroup myViewGroup = ((ViewGroup) slideMenuLayout.getParent());
//	        int index = myViewGroup.indexOfChild(slideMenuLayout);
//	        for(int i = 0; i<index; i++)
//	        {
//	            myViewGroup.bringChildToFront(myViewGroup.getChildAt(i));
//	        }
	    	
	    	btnGuys.setEnabled(false);
			btnGirls.setEnabled(false);
	    	
	    }	    
	    
	} 

}
