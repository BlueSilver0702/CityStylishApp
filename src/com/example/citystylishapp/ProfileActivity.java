package com.example.citystylishapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import android.net.Uri;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.custom.Global;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ProfileActivity extends Activity implements OnClickListener{

	boolean isShowPopup;
	Bitmap  userPhoto;	
	
	// Root Layout.
	FrameLayout rootLayout;
	
	// Buttons
	ImageViewButton btnAddPersonalPhoto, btnChangePersonalPhoto, btnEscape, btnContinueWithCurPhoto, btnContinueWithoutPhoto;
	
	
	// Slide Menu layout.
	LinearLayout slideMenuLayout;
	ImageViewButton btnTakePhoto, btnGotoLibrary;
	int mOriginalPos[];
	
	
	// For camera or library.
	private static int    iSelectedPhoto = 0;
    File 			 photoFile;
    String 			 cameraFilePath;
    Bitmap 			 fPhoto;
	private final static int TAKE_PICTURE = 100;
    private final static int SELECT_PICTURE = 200;
	
	
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
		
		// Get button objects.
		btnAddPersonalPhoto = (ImageViewButton) findViewById(R.id.btn_profile_add_personal_photo);
		btnChangePersonalPhoto = (ImageViewButton) findViewById(R.id.btn_profile_change_personal_photo);
		btnEscape = (ImageViewButton) findViewById(R.id.btn_profile_escape);
		btnContinueWithCurPhoto = (ImageViewButton) findViewById(R.id.btn_profile_continue_with_cur_photo);
		btnContinueWithoutPhoto = (ImageViewButton) findViewById(R.id.btn_profile_continue_without_photo);
		
		slideMenuLayout = (LinearLayout) findViewById(R.id.slidemenu_Layout);
		slideMenuLayout.bringToFront();
		slideMenuLayout.setVisibility(View.INVISIBLE);
		//slideMenuLayout.getLocationOnScreen( mOriginalPos );
		btnTakePhoto = (ImageViewButton) findViewById(R.id.btn_profile_take_photo);
		btnTakePhoto.setOnClickListener(this);
		btnGotoLibrary = (ImageViewButton) findViewById(R.id.btn_profile_library_photo);
		btnGotoLibrary.setOnClickListener(this);
		
		// Set ClickListner to button objects.
		btnAddPersonalPhoto.setOnClickListener(this);
		btnChangePersonalPhoto.setOnClickListener(this);
		btnEscape.setOnClickListener(this);
		btnContinueWithCurPhoto.setOnClickListener(this);
		btnContinueWithoutPhoto.setOnClickListener(this);
		
		isShowPopup = false;
		
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_profile_add_personal_photo) {
			
			Log.d("Profile page : ", "Clicking AddPersonalPhoto btn");
			
			// Show popup menu.			
			//showPopupTake(slideMenuLayout);
			if(Global.personalInfo.userPhoto != null) {
			
				Toast toast= Toast.makeText(getApplicationContext(), 
						"You already choosed your personal photo, Please click \"ChangePersonalPhoto\" Button to change the personal photo", Toast.LENGTH_SHORT);  
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}else 
				onAlbum();
			
		} else if (v.getId() == R.id.btn_profile_change_personal_photo) {
			
			Log.d("Profile page : ", "Clicking ChangePersonalPhoto btn");
						
			// Show popup menu.
			//showPopupTake(slideMenuLayout);
			onAlbum();

		} else if (v.getId() == R.id.btn_profile_continue_with_cur_photo) {

			Log.d("Profile page : ", "Clicking ContinueWithCurPhoto btn");
			
			// Return if popupMenu is opened.
			if(isShowPopup)
				return;
			
			// If user photo was not selected.
			if(Global.personalInfo.userPhoto == null) {
				
				Toast toast= Toast.makeText(getApplicationContext(), 
						"You didn't choose your personal photo, Please add personal photo", Toast.LENGTH_SHORT);  
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
				
				return;
			}
			
			Global.startWithUserPhoto = true;		
			
			// Go to WardrobeView
			Intent wardrobeIntent=new Intent(v.getContext(),WardrobeActivity.class);
	        startActivity(wardrobeIntent);
	        finish();

		}else if (v.getId() == R.id.btn_profile_escape) {

			Log.d("Profile page : ", "Clicking escape btn");
			Intent homeIntent=new Intent(v.getContext(),HomeActivity.class);
	        startActivity(homeIntent);
	        finish();
			

		} else if (v.getId() == R.id.btn_profile_continue_without_photo) {
			
			Global.startWithUserPhoto = false;
			Log.d("Profile page : ", "Clicking ContinueWithoutPhoto btn");
			Intent wardrobeIntent=new Intent(v.getContext(),WardrobeActivity.class);
	        startActivity(wardrobeIntent);
	        finish();
		} else if (v.getId() == R.id.btn_profile_take_photo) {

			Log.d("Profile page : ", "Clicking take photo btn");
			onCamera();
			
		} else if (v.getId() == R.id.btn_profile_library_photo) {

			Log.d("Profile page : ", "Clicking library btn");
			onAlbum();
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

	    if(direction) { // from left to right.
	    	
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
	
	public void hidePopupTake () {
		
		if ( isShowPopup ) {
			
			
			
			isShowPopup = false;
		}
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
		
   	int desiredImageWidth = 100;  // pixels
   	int desiredImageHeight = 100; // pixels
   	
		Bitmap bitmap;
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 4;
	     	
	    bitmap = BitmapFactory.decodeFile( Global.settingCameraFilePath, options );
	    Bitmap newImage = Bitmap.createScaledBitmap(bitmap, desiredImageWidth, desiredImageHeight, true);
	    return newImage;	     
	 }
   
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(ProfileActivity.this,
					"Please wait...", "Saving Changes", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			Global.saveObject(Global.personalInfo);	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			progressDialog.dismiss();	
			
			//showPopupTake(slideMenuLayout);	
		}	
			
	}
   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
       if (resultCode != RESULT_OK) {
           return;
       }
       switch (requestCode) {
           case TAKE_PICTURE: {
           	try {  
           		
           		if (Global.personalInfo.userPhoto != null)
       				if (!Global.personalInfo.userPhoto.isRecycled())
       					Global.personalInfo.userPhoto.recycle();
        		Global.personalInfo.userPhoto = null;
        		   
        		Global.personalInfo.userPhoto = onPhotoTaken();
                if (Global.personalInfo.userPhoto != null) {
                                      
                    // Set the personal photo.
             	   	//Global.personalInfo.userPhoto = Global.fPhoto;
             	   	// Save personal Info.
        			//Global.saveObject(Global.personalInfo);
             	   	new SaveChanged().execute();
                }
                
	              
           	} catch (Exception e) {
					e.printStackTrace();
				}
               break;
           }
           case SELECT_PICTURE: {                  
               
        	   try {
               
        		   if (Global.personalInfo.userPhoto != null)
       				if (!Global.personalInfo.userPhoto.isRecycled())
       					Global.personalInfo.userPhoto.recycle();
        		   Global.personalInfo.userPhoto = null;
              		
        		   Uri selectedImage = data.getData();                    
                   InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                   Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                   int fixedWidth = bmp.getWidth()/4;
                   int fixedHeight = bmp.getHeight()/4;
                   Global.personalInfo.userPhoto = Bitmap.createScaledBitmap(bmp, fixedWidth, fixedHeight, true);
                                      	
               } catch (FileNotFoundException e) {
                   
               }        
               
        	   if (Global.personalInfo.userPhoto == null) {
               
        		   Toast.makeText(this, "The photo is not available.", Toast.LENGTH_SHORT).show();
               } else {
               	
            	   Global.iSelectedPhoto = 1;
               	
            	   // Set the personal photo.
            	   //Global.personalInfo.userPhoto = Global.fPhoto;
            	   // Save personal Info.
       			   //Global.saveObject(Global.personalInfo);
            	   new SaveChanged().execute();	            	   
               } 
               break;
           }
       }      
       
	}	
   
   	public String getRealPathFromURI(Uri contentUri) {
       
	   String [] proj={MediaStore.Images.Media.DATA};
       Cursor cursor = managedQuery( contentUri,
                       proj, // Which columns to return
                       null,       // WHERE clause; which rows to return (all rows)
                       null,       // WHERE clause selection arguments (none)
                       null); // Order-by clause (ascending by name)
       
       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();

       return cursor.getString(column_index);
	}
   	
   	

}
