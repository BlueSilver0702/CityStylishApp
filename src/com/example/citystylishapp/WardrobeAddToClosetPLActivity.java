package com.example.citystylishapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.custom.Global;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WardrobeAddToClosetPLActivity extends Activity implements OnClickListener{

	boolean isShowPopup;
	
	// Root Layout.
	FrameLayout rootLayout;
	
	// Slide Menu layout.
	LinearLayout slideMenuLayout;
	ImageViewButton btnTakePhoto, btnGotoLibrary;
	int mOriginalPos[];
	
	ImageViewButton btnTops;
	ImageViewButton btnBottoms;
	ImageViewButton btnSets;
	ImageViewButton btnAccessories;
	ImageViewButton btnFootwear;
	ImageViewButton btnJackets;
	ImageViewButton btnDone;
	
	private final static int TAKE_PICTURE = 500;
    private final static int SELECT_PICTURE = 600;
    
    private int selectedCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wardrobe_addtocloset_pl);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (FrameLayout) findViewById(R.id.wd_ac_pl_rootLayout);
		
		// Get button objects.
		btnTops = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_tops);
		btnBottoms = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_bottoms);
		btnSets = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_sets);
		btnAccessories = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_accessories);
		btnFootwear = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_footwear);
		btnJackets = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_jackets);
		btnDone = (ImageViewButton) findViewById(R.id.btn_wardrobe_addtocloset_pl_done);
		
		// Set ClickListner to button objects.
		btnTops.setOnClickListener(this);
		btnBottoms.setOnClickListener(this);
		btnSets.setOnClickListener(this);
		btnAccessories.setOnClickListener(this);
		btnFootwear.setOnClickListener(this);
		btnJackets.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		
		slideMenuLayout = (LinearLayout) findViewById(R.id.wr_ac_pl_slidemenu_Layout);
		slideMenuLayout.bringToFront();
		slideMenuLayout.setVisibility(View.INVISIBLE);
		//slideMenuLayout.getLocationOnScreen( mOriginalPos );
		btnTakePhoto = (ImageViewButton) findViewById(R.id.btn_wr_ac_pl_take_photo);
		btnTakePhoto.setOnClickListener(this);
		btnGotoLibrary = (ImageViewButton) findViewById(R.id.btn_wr_ac_pl_library_photo);
		btnGotoLibrary.setOnClickListener(this);
		
		isShowPopup = false;
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_tops) {
			
			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking closet btn");
			
			selectedCategory = 1;
			// Show popup menu.			
			showPopupTake(slideMenuLayout);
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_bottoms) {
			
			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking tidy btn");
			
			selectedCategory = 2;
			//Show popup menu.			
			showPopupTake(slideMenuLayout);
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_sets) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking view btn");
			
			selectedCategory = 3;
			// Show popup menu.			
			showPopupTake(slideMenuLayout);
		}else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_accessories) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking escape btn");
			
			selectedCategory = 4;
			// Show popup menu.			
			showPopupTake(slideMenuLayout);			
			
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_footwear) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking create btn");
			
			selectedCategory = 5;
			// Show popup menu.			
			showPopupTake(slideMenuLayout);
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_jackets) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking create btn");
			
			selectedCategory = 6;
			// Show popup menu.			
			showPopupTake(slideMenuLayout);
		} else if (v.getId() == R.id.btn_wardrobe_addtocloset_pl_done) {

			Log.d("Wardrobe AddToCloset-PhotoLibrary page : ", "Clicking create btn");
			Intent addToClosetIntent=new Intent(v.getContext(),WardrobeAddToClosetActivity.class);
	        startActivity(addToClosetIntent);
	        finish();
		} else if (v.getId() == R.id.btn_wr_ac_pl_take_photo) {

			onCamera();
		} else if (v.getId() == R.id.btn_wr_ac_pl_library_photo) {

			onAlbum();
		}

	}

	public void showPopupTake(View v) {
		
		if ( !isShowPopup ) {
			
			moveViewToScreenCenter( v, true);
			isShowPopup = true;
		}else {
			
			moveViewToScreenCenter( v, false);
			isShowPopup = false;
		}
	}
	
	private void moveViewToScreenCenter( View view, boolean direction )
	{
		//LinearLayout root = (LinearLayout) findViewById( R.id.rootLayout );
	    DisplayMetrics dm = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics( dm );
	    //int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

	    int originalPos[] = new int[2];
	    view.getLocationOnScreen( originalPos );

	    if(direction) { // from left to right
	    	
	    	// Slide Menu fade in animation.
	    	Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
	    	fadeInAnimation.setFillAfter(true);
	    	view.startAnimation(fadeInAnimation );	
	    	
	    	// Root View fade out animation.
	    	AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.3f);
	    	fadeOut.setDuration(200);
	    	fadeOut.setFillAfter(true);
	    	rootLayout.startAnimation(fadeOut );	
	    	
	    }else { // from right to left
	    	
	    	// Slide Menu fade out animation.
	    	Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
	    	fadeOutAnimation.setFillAfter(true);
	    	view.startAnimation(fadeOutAnimation );	
	    	
	    	// Root View fade in animation.
	    	AlphaAnimation fadeIn = new AlphaAnimation(0.3f, 1.0f);
	    	fadeIn.setDuration(200);
	    	fadeIn.setFillAfter(true);
	    	rootLayout.startAnimation(fadeIn );	
	    	
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**************************************************************************
	 *   Take photo or photo library.
	 *************************************************************************/
	protected void onCamera() {
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        StringBuilder stringbuilder = new StringBuilder("tmp_")
        .append(Calendar.getInstance().getTimeInMillis())
        .append(".jpg");
        Global.settingCameraFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + 
		String.format("/%s", stringbuilder.toString());
        
		Uri uri = Uri.fromFile(new File(Global.settingCameraFilePath));
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
		this.startActivityForResult(intent, TAKE_PICTURE);
	}
	
	protected void onAlbum() {
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PICTURE);
	}
	
	protected Bitmap onPhotoTaken() {
		
	   	int desiredImageWidth = (int)Global.WIN_W;  // pixels
	   	int desiredImageHeight = (int)Global.WIN_H; // pixels
	   	
		Bitmap bitmap;
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 4;
	     	
	    bitmap = BitmapFactory.decodeFile( Global.settingCameraFilePath, options );
	    Bitmap newImage = Bitmap.createScaledBitmap(bitmap, desiredImageWidth, desiredImageHeight, true);
	    
	    // Free old bitmap.
	    if (bitmap != null)
			if (!bitmap.isRecycled())
				bitmap.recycle();
	    bitmap = null;
	    
	    return newImage;	     
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
       if (resultCode != RESULT_OK) {
           return;
       }
       switch (requestCode) {
           case TAKE_PICTURE: {
           	try {  
           		if (Global.fPhoto != null)
    				if (!Global.fPhoto.isRecycled())
    					Global.fPhoto.recycle();
           		Global.fPhoto = null;
           		
           		Global.fPhoto = onPhotoTaken();
                if (Global.fPhoto != null) {                                     
                	
                	showPopupTake(slideMenuLayout);
                    // Go to Edit Image page.
                	Intent editClosetIntent=new Intent(this, MakeClosetActivity.class);
                	editClosetIntent.putExtra("SelectedCategory", selectedCategory);
 				    startActivity(editClosetIntent);
 				    finish();
                }
                	
	              
           	} catch (Exception e) {
					e.printStackTrace();
				}
               break;
           }
           case SELECT_PICTURE: {                  
               
        	   try {
        		   	
        		   if (Global.fPhoto != null)
        				if (!Global.fPhoto.isRecycled())
        					Global.fPhoto.recycle();
        		   Global.fPhoto = null;
        		   
        		   Uri selectedImage = data.getData();                    
                   InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                   //Bitmap selectedBmp = BitmapFactory.decodeStream(imageStream);
                   Global.fPhoto = BitmapFactory.decodeStream(imageStream);
                   //Global.fPhoto = Bitmap.createScaledBitmap(selectedBmp, (int)(selectedBmp.getWidth()*0.7), (int)(selectedBmp.getHeight()*0.7), true);
                   //Global.fPhoto = Bitmap.createScaledBitmap(selectedBmp, selectedBmp.getWidth(), selectedBmp.getHeight(), true);
                   //selectedBmp.recycle();
                   //selectedBmp = null;
                   	
               } catch (FileNotFoundException e) {
                   
               }        
               
        	   if (Global.fPhoto == null) {
               
        		   Toast.makeText(this, "The photo is not available.", Toast.LENGTH_SHORT).show();
               } else {
               	
            	   showPopupTake(slideMenuLayout);
            	   // Go to Edit Image page.
               		Intent editClosetIntent=new Intent(this, MakeClosetActivity.class);
               		editClosetIntent.putExtra("SelectedCategory", selectedCategory);
				    startActivity(editClosetIntent);
				    finish();            	   
               } 
               break;
           }
       }      
       
	}
	
	

}
