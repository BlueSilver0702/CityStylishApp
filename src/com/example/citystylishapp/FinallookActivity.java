package com.example.citystylishapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import com.example.citystylishapp.TwitterApp.TwDialogListener;

import android.net.Uri;


import com.example.custom.Global;
import com.example.custom.MyBitmap;
import com.example.custom.OutfitInfo;
import com.example.custom.WearInfo;
import com.example.custom.WishInfo;
import com.example.citystylishapp.EditPhotoView;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.harrison.lee.twitpic4j.TwitPic;
import com.harrison.lee.twitpic4j.TwitPicResponse;
import com.harrison.lee.twitpic4j.exception.TwitPicException;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agilepoet.introduceyourself.gesturedetectors.MoveGestureDetector;
import com.agilepoet.introduceyourself.gesturedetectors.RotateGestureDetector;
import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.androidquery.AQuery;
import com.devsmart.android.ui.HorizontalListView;

public class FinallookActivity extends Activity implements OnClickListener {

	// Root Layout.
	LinearLayout rootLayout;
	
	/* For landscape */
	// Main Screen part
	LinearLayout mainScreenLinearLayout; // fl_mainscreen_Layout
	FrameLayout mainModelLayout; // fl_mainscreen_modelLayout	\
	//LinearLayout mainModelLayout; // fl_mainscreen_modelLayout
	ImageViewButton btnGoBack; // btn_fl_goback
	ImageViewButton btnAddJacket; // btn_fl_add_jacket
	ImageViewButton btnSaveDate; // btn_fl_save_redate
	ImageViewButton btnDelete;
	
	private	EditPhotoView mEditPhotoView;
	private	Typeface mtf;
	public	EditText editview;
	
	// Delete Confirm part.
	LinearLayout deleteConfirmLinearLayout; // fl_deletescreen_Layout
	ImageViewButton btnDeleteYes; // btn_fl_delete_yes
	ImageViewButton btnDeleteNo; // btn_fl_delete_no
	
	// Save Date part
	LinearLayout saveDateLinearLayout;// fl_sd_savedatescreen_Layout
	ImageViewButton btnEscape; // btn_fl_escape
	Button btnDate; // btn_fl_date
	ImageViewButton btnGirlsAm; // btn_fl_girls_am
	ImageViewButton btnGirlsPm; // btn_fl_girls_pm
	ImageViewButton btnGuysAll; // btn_fl_guys_all
	
	OutfitInfo specifedOutfitInfo;
	int m_indexOfNewInfo;
	
	static final int DATE_DIALOG_ID = 100;
	private int mYear;
    private int mMonth;
    private int mDay;
    
    private View 			mLandscapeView;
	private View 			mPortraitView;
	
	/* For portrait mode */
	ImageViewButton btnEmail; // btn_fl_goback
	ImageViewButton btnFacebook; // btn_fl_add_jacket
	ImageViewButton btnTwitter; // btn_fl_save_redate	
	AdsPhotoView    mAdsPhotoView;
	
	/* Facebook */
	private String strResponse;
	AlertDialog.Builder alertbox;	
	private Facebook facebook;
	private String messageToPost;
	private static final String[] PERMISSIONS = new String[]{"publish_stream"};
	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-cerdentials";	
	
	/* Twitter */
	private TwitterApp mTwitter;
	private static final String twitter_consumer_key = "BDYS6pyYsJsBRaNEFl6Kfw";
	private static final String twitter_secret_key = "iK3TQiDUZcawF7ymjCjDQHHWFUqh1i2ZXOa2DNusDVM";
	File uploadfile;
	private		String username = "";
	private		String password;
	
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Get argument from previous activity.
		Bundle extras = getIntent().getExtras();
		m_indexOfNewInfo = extras.getInt("FinallookArg");
		if(m_indexOfNewInfo == -1)
			return;
		else
			specifedOutfitInfo = Global.personalInfo.arrOutfits.get(m_indexOfNewInfo);
		
		// init text save state.
		SharedPreferences settings = this.getSharedPreferences("GlobalStates", 0);
		SharedPreferences.Editor editor = settings.edit();     
		editor.putBoolean("text_save", false);
	    editor.commit();
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    mLandscapeView = getLayoutInflater().inflate(R.layout.activity_finallook_landscape, null);
	    setContentView(mLandscapeView);	    			
		initLandscapeView();
	    
		Display display = getWindowManager().getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//setContentView(mPortraitView);
	    
		} else { // Landscape.	        
			
			
		}
        
	}
	
	private void initPortraitView () {
		
		// Make weared Model.
		mEditPhotoView.MakeModelWeared();
		
		mPortraitView = getLayoutInflater().inflate(R.layout.activity_finallook, null);            
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(mPortraitView);
		
		btnEmail = (ImageViewButton) findViewById(R.id.btn_email);
		btnFacebook = (ImageViewButton) findViewById(R.id.btn_fb);
		btnTwitter = (ImageViewButton) findViewById(R.id.btn_tw);	
		
		btnEmail.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);	
		btnTwitter.setOnClickListener(this);	
		
		// Draw weared Model.
		mAdsPhotoView = (AdsPhotoView) findViewById(R.id.adsphotoview);
		if(specifedOutfitInfo.imageModeWeared != null) {
			
			specifedOutfitInfo.imageModeWeared.setM_centerX((int)Global.WIN_W/4); 
			specifedOutfitInfo.imageModeWeared.setM_centerY((int)Global.WIN_H/2);
			specifedOutfitInfo.imageModeWeared.setM_fAngle(0.0f);
			specifedOutfitInfo.imageModeWeared.setM_fScale(1.0f);
			
			Global.m_pAdsMyBmpArray.clear();
			mAdsPhotoView.addImage(specifedOutfitInfo.imageModeWeared);
			
		}	
		
		// Facebook initialization.
		if(alertbox == null)
			alertbox = new AlertDialog.Builder(this);		
		
	}
	
	private void initLandscapeView () {
		
		// Main screen part
		mainScreenLinearLayout = (LinearLayout) findViewById(R.id.fl_mainscreen_Layout);
		mainModelLayout = (FrameLayout) findViewById(R.id.fl_mainscreen_modelLayout);
		//mainModelLayout = (LinearLayout) findViewById(R.id.fl_mainscreen_modelLayout);

		btnGoBack = (ImageViewButton) findViewById(R.id.btn_fl_goback);
		btnAddJacket = (ImageViewButton) findViewById(R.id.btn_fl_add_jacket);
		btnSaveDate = (ImageViewButton) findViewById(R.id.btn_fl_save_redate);
		btnDelete = (ImageViewButton)findViewById(R.id.btn_fl_delete);
		btnGoBack.setOnClickListener(this);
		btnAddJacket.setOnClickListener(this);
		btnSaveDate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		
		// Delete Confirm part.
		deleteConfirmLinearLayout = (LinearLayout) findViewById(R.id.fl_deletescreen_Layout);
		btnDeleteYes = (ImageViewButton) findViewById(R.id.btn_fl_delete_yes);
		btnDeleteNo = (ImageViewButton) findViewById(R.id.btn_fl_delete_no);
		btnDeleteYes.setOnClickListener(this);
		btnDeleteNo.setOnClickListener(this);	
		
		// Save Date part
		saveDateLinearLayout = (LinearLayout) findViewById(R.id.fl_sd_savedatescreen_Layout);
		btnEscape = (ImageViewButton) findViewById(R.id.btn_fl_escape);
		btnDate = (Button) findViewById(R.id.btn_fl_date);
		
		btnGirlsAm = (ImageViewButton) findViewById(R.id.btn_fl_girls_am);
		btnGirlsPm = (ImageViewButton) findViewById(R.id.btn_fl_girls_pm);
		btnGuysAll = (ImageViewButton) findViewById(R.id.btn_fl_guys_all);
		
		btnEscape.setOnClickListener(this);
		btnDate.setOnClickListener(this);	
		btnGirlsAm.setOnClickListener(this);
		btnGirlsPm.setOnClickListener(this);	
		btnGuysAll.setOnClickListener(this);	
		
		// Hide deleteConfirmLinearLayout and saveDateLinearLayout at first.
		deleteConfirmLinearLayout.setVisibility(View.INVISIBLE);
		saveDateLinearLayout.setVisibility(View.INVISIBLE);	
			        
		if((specifedOutfitInfo.strOutfitDate != null) && (specifedOutfitInfo.strOutfitDate.length() > 0))
			btnDate.setText(specifedOutfitInfo.strOutfitDate);
			
		// Get Edit photo view object
		mEditPhotoView = (EditPhotoView) findViewById(R.id.editphotoview);
        mEditPhotoView.containerActivity = this;
        
        // Add User image.
        if((Global.startWithUserPhoto) && (Global.personalInfo.userPhoto != null)) {
        	        	
        	mEditPhotoView.setUserBmp(Global.personalInfo.userPhoto);        
        }
        /* Add outfit images. */       
        
        // Add top image.
        if(specifedOutfitInfo.topImage != null) {
        	specifedOutfitInfo.topImage.setM_componentMark(2);
        	if(specifedOutfitInfo.topImage.getM_centerX() == 0)
        		specifedOutfitInfo.topImage.setM_centerX(250);
        	if(specifedOutfitInfo.topImage.getM_centerY() == 0)
        	specifedOutfitInfo.topImage.setM_centerY(100);
        	mEditPhotoView.addImage(specifedOutfitInfo.topImage);
        }
        
        // Add bottom image.
        if(specifedOutfitInfo.bottomImage != null) {
        	specifedOutfitInfo.bottomImage.setM_componentMark(3);
        	if(specifedOutfitInfo.bottomImage.getM_centerX() == 0)
        		specifedOutfitInfo.bottomImage.setM_centerX(350);
        	
        	if(specifedOutfitInfo.bottomImage.getM_centerY() == 0)
        		specifedOutfitInfo.bottomImage.setM_centerY(200);
        	mEditPhotoView.addImage(specifedOutfitInfo.bottomImage);
        }
        
        // Add dress image.
        if(specifedOutfitInfo.dressImage != null) {
        	specifedOutfitInfo.dressImage.setM_componentMark(4);
        	if(specifedOutfitInfo.dressImage.getM_centerX() == 0)
        		specifedOutfitInfo.dressImage.setM_centerX(250);
        	
        	if(specifedOutfitInfo.dressImage.getM_centerY() == 0)
        		specifedOutfitInfo.dressImage.setM_centerY(300);
        	mEditPhotoView.addImage(specifedOutfitInfo.dressImage);
        }
        
        // Add jacket image.
        if(specifedOutfitInfo.jacketImage != null) {
        	specifedOutfitInfo.jacketImage.setM_componentMark(5);
        	if(specifedOutfitInfo.jacketImage.getM_centerX() == 0)
        		specifedOutfitInfo.jacketImage.setM_centerX(250);
        	
        	if(specifedOutfitInfo.jacketImage.getM_centerY() == 0)
        		specifedOutfitInfo.jacketImage.setM_centerY(300);
        	mEditPhotoView.addImage(specifedOutfitInfo.jacketImage);
        }
        
        // Add footwear image.
        if(specifedOutfitInfo.footImage != null) {
        	specifedOutfitInfo.footImage.setM_componentMark(6);
        	if(specifedOutfitInfo.footImage.getM_centerX() == 0)
        		specifedOutfitInfo.footImage.setM_centerX(300);
        	if(specifedOutfitInfo.footImage.getM_centerY() == 0)
        	specifedOutfitInfo.footImage.setM_centerY(230);
        	mEditPhotoView.addImage(specifedOutfitInfo.footImage);
        }
        
        // Add accessary image.
        if(specifedOutfitInfo.accessImage != null) {
        	specifedOutfitInfo.accessImage.setM_componentMark(7);
        	if(specifedOutfitInfo.accessImage.getM_centerX() == 0)
        		specifedOutfitInfo.accessImage.setM_centerX(300);
        	if(specifedOutfitInfo.accessImage.getM_centerY() == 0)
        		specifedOutfitInfo.accessImage.setM_centerY(180);
        	mEditPhotoView.addImage(specifedOutfitInfo.accessImage);
        }        
        
        // Add text image.
        if(specifedOutfitInfo.textImage != null) {
        	specifedOutfitInfo.textImage.setM_componentMark(8);
        	mEditPhotoView.addImage(specifedOutfitInfo.textImage);
        	addTextEditView(specifedOutfitInfo.outfitName, 0);
        	
        }else {       
        
//        SharedPreferences settings = Global.g_context.getSharedPreferences("GlobalStates", 0);		
//		boolean textSaved = settings.getBoolean("text_save",false);
//		if(!textSaved) {
        	addTextEditView("TestTest", 1);
			    
		}
        
        // Get the current date.
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);  
	}
	
	public void addTextEditView(String txt, int visiblity) {
		
		mtf = Typeface.createFromAsset(getAssets(), "Arial Narrow.ttf");		
		editview = new EditText(this);
		editview.setText(txt);
		
        FrameLayout.LayoutParams tvp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));        
        editview.setLayoutParams(tvp);       
        editview.setTypeface(mtf);   
        editview.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editview.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	
        	@Override
        	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		
        		if (actionId == EditorInfo.IME_ACTION_DONE ||
        				event.getAction() == KeyEvent.ACTION_DOWN &&
        				event.getAction() == KeyEvent.KEYCODE_ENTER) {
        			
        			specifedOutfitInfo.outfitName = editview.getText().toString();	        			
        			mEditPhotoView.insertText(editview.getText().toString() , mtf, true);
        			editview.setVisibility(View.INVISIBLE);
        			return true;
        		}
        		return false;
        	}
        });
        mainModelLayout.addView(editview); 
        if(visiblity == 1)
        	editview.setVisibility(View.VISIBLE);
        else
        	editview.setVisibility(View.INVISIBLE);
	}
	
	public void showTextEditView() {
		
		editview.setVisibility(View.VISIBLE);
		editview.setText(specifedOutfitInfo.outfitName);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

            Log.e("On Config Change","LANDSCAPE");
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(mLandscapeView);			
			//mEditPhotoView.SaveOutfitComponents(specifedOutfitInfo.imageModeWeared);
			
			initLandscapeView();
			
        }else{

            Log.e("On Config Change","PORTRAIT");
            //mEditPhotoView.SaveOutfitComponents(specifedOutfitInfo.imageModeWeared);
            
			initPortraitView();
        }

    }
	
	private void updateDateDisplay() {
		
		btnDate.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	        		.append(mDay).append("/")
	                .append(mMonth + 1).append("/")	                
	                .append(mYear));
	}
	
	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	   new DatePickerDialog.OnDateSetListener() {

	      public void onDateSet(DatePicker view, int year,
	                            int monthOfYear, int dayOfMonth) {
	         mYear = year;
	         mMonth = monthOfYear;
	         mDay = dayOfMonth;
	         specifedOutfitInfo.outfitDate = new Date(mYear, mMonth, mDay);
	         updateDateDisplay();
	   }
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
	                mDay);
	    }
	    return null;
	}
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(FinallookActivity.this,
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
			// Clear drawing elements 
			Global.m_pOutfitMyBmpArray.clear();
			
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
						
			Intent wardrobeIntent = new Intent(FinallookActivity.this, WardrobeActivity.class);
			startActivity(wardrobeIntent);
			finish();	
		}	
			
	}
	
	public void onClick(View v) {
		
		/* For landscape mode */
		if (v.getId() == R.id.btn_fl_goback) {			
					
			// Clear drawing elements 
			Global.m_pOutfitMyBmpArray.clear();
			
			Intent wardrobeIntent = new Intent(v.getContext(), WardrobeActivity.class);
	        startActivity(wardrobeIntent);
	        finish();
			
		} else if (v.getId() == R.id.btn_fl_add_jacket) {
				
			// Go to CreateOutfitJacketActivity.
			Intent createOutfitJacketIntent=new Intent(v.getContext(), CreateOutfitJacketActivity.class);
			createOutfitJacketIntent.putExtra("JacketArg", (int)m_indexOfNewInfo);
			startActivity(createOutfitJacketIntent);
			finish();
			
		} else if (v.getId() == R.id.btn_fl_save_redate) {
			
			mainScreenLinearLayout.setVisibility(View.INVISIBLE);
			saveDateLinearLayout.setVisibility(View.VISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_delete){ // Size buttons.
			
			mainScreenLinearLayout.setVisibility(View.INVISIBLE);
			deleteConfirmLinearLayout.setVisibility(View.VISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_delete_yes){ // Size buttons.
			
			// Delete the current outfit and go to Wardrobe page.
			Global.personalInfo.arrOutfits.remove(m_indexOfNewInfo);
			//Global.saveObject(Global.personalInfo);
			new SaveChanged().execute();		
			
		} else if (v.getId() == R.id.btn_fl_delete_no){ // Size buttons.
			
			deleteConfirmLinearLayout.setVisibility(View.INVISIBLE);
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_escape){ // Size buttons.
			
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
			saveDateLinearLayout.setVisibility(View.INVISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_date){ // date selection buttons.
			
			showDialog(DATE_DIALOG_ID);
			
		} else if (v.getId() == R.id.btn_fl_girls_am){ // Size buttons.
			
			specifedOutfitInfo.outfitType = Global.OT_LADIES_AM; // OT_LADIES_AM
			String dateStr = (String)btnDate.getText();
			if(!dateStr.equals("00 / 00 / 00"))
				specifedOutfitInfo.strOutfitDate = dateStr;
				
			mEditPhotoView.Save();
			
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
			saveDateLinearLayout.setVisibility(View.INVISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_girls_pm){ // Size buttons.
			
			specifedOutfitInfo.outfitType = Global.OT_LADIES_PM;
			String dateStr = (String)btnDate.getText();
			if(!dateStr.equals("00 / 00 / 00"))
				specifedOutfitInfo.strOutfitDate = dateStr;
			
			mEditPhotoView.Save();
			
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
			saveDateLinearLayout.setVisibility(View.INVISIBLE);
			
		} else if (v.getId() == R.id.btn_fl_guys_all){ // Size buttons.
			
			specifedOutfitInfo.outfitType = Global.OT_MENS;
			String dateStr = (String)btnDate.getText();
			if(!dateStr.equals("00 / 00 / 00"))
				specifedOutfitInfo.strOutfitDate = dateStr;
			
			mEditPhotoView.Save();
			
			mainScreenLinearLayout.setVisibility(View.VISIBLE);
			saveDateLinearLayout.setVisibility(View.INVISIBLE);
		} else if (v.getId() == R.id.btn_email){ // In portrait mode.
			
			Log.d("aaaaa", "************ email click *********************");
			
			MyBitmap logoBmp = new MyBitmap();
			logoBmp.setM_pBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.applogo_ad));
			logoBmp.setM_fScale(1.0f);
			logoBmp.setM_fAngle(0.0f);
			logoBmp.setM_centerX((int)Global.WIN_W/2);
			logoBmp.setM_centerY(0);
			
			mAdsPhotoView.addImage(logoBmp);
			
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "goldmanisme@gmail.com");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Created via citystylistapp.com");
			emailIntent.setType("image/png");
			
			Bitmap adsBmp = mAdsPhotoView.getM_adsBitmap();
			String imgUrl = MediaStore.Images.Media.insertImage(getContentResolver(), adsBmp, "Advertisement", "");
			
			ArrayList<Uri> uris = new ArrayList<Uri>();
			uris.add(Uri.parse(imgUrl));
			emailIntent.putExtra(Intent.EXTRA_STREAM, uris);			
			
			
		} else if (v.getId() == R.id.btn_fb){ // In portrait mode.
			
			Log.d("aaaaa", "************ facebook click *********************");
			
			MyBitmap logoBmp = new MyBitmap();
			logoBmp.setM_pBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.applogo_ad));
			logoBmp.setM_fScale(1.0f);
			logoBmp.setM_fAngle(0.0f);
			logoBmp.setM_centerX((int)Global.WIN_W/2);
			logoBmp.setM_centerY(0);
			
			mAdsPhotoView.addImage(logoBmp);
			
			// Facebook
			if(facebook == null)
				facebook  = new Facebook(Global.FACEBOOK_APP_ID);
			
			restoreCredentials(facebook);
			messageToPost = "Created via citystylistapp.com";			
			if(!facebook.isSessionValid()) {
				loginAndPostToWall();
			}else {
				postToWall();
			}
			
		} else if (v.getId() == R.id.btn_tw){ // In portrait mode.
			
			Log.d("aaaaa", "************ twitter click *********************");
			
			MyBitmap logoBmp = new MyBitmap();
			logoBmp.setM_pBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.applogo_ad));
			logoBmp.setM_fScale(1.0f);
			logoBmp.setM_fAngle(0.0f);
			logoBmp.setM_centerX((int)Global.WIN_W/2);
			logoBmp.setM_centerY(0);
			
			mAdsPhotoView.addImage(logoBmp);
			mAdsPhotoView.MakeAdsBitmap();
			Bitmap adsBmp = mAdsPhotoView.getM_adsBitmap();
			if (adsBmp == null){
				   
				
			}else{
				
				if(mTwitter == null) {
					
					mTwitter = new TwitterApp(this, twitter_consumer_key,twitter_secret_key);
					mTwitter.setListener(mTwLoginDialogListener);
				}
			
				uploadToTwitter(adsBmp);
			}
		}		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/* Facebook functions */
	public boolean saveCredentials(Facebook facebook) {
		
		Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		
		return editor.commit();
		
	}
	public boolean restoreCredentials(Facebook facebook){
		
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}
	
	public void loginAndPostToWall () {
		
		facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}
	
	public void postToWall() {
		
		Bitmap adsBmp = mAdsPhotoView.getM_adsBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		adsBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		final byte[] adsImageData = stream.toByteArray();
		
		Bundle parameters = new Bundle();
		parameters.putString("message", "Message");
		parameters.putByteArray("picture", adsImageData);
		parameters.putString("caption", "App advertisement");
		
		try {
			
			strResponse = facebook.request("me/photos", parameters, "POST");
			Log.d("CityStylist:", "got response:" + strResponse);
			if (strResponse == null || strResponse.equals("") || strResponse.endsWith("false")) {
				
				showToast("Blank response");
			}else {
				
				showToast("Photo posted to your facebook wall!");
			}
		} catch (Exception e) {
			
			showToast("Failed to post to wall!");
			e.printStackTrace();
		}
	}
	
	class LoginDialogListener implements DialogListener {
		
		public void onComplete(Bundle values) {
			
			saveCredentials(facebook);
			if(messageToPost != null) {
				
				postToWall();
			}
		}
		public void onFacebookError(FacebookError error) {
			
			showToast("Authentication with Facebook failed!");
		}
		
		public void onError(DialogError error){
			showToast("Authentication with Facebook failed!");
		}
		
		public void onCancel() {
			
			showToast("Authentication with Facebook cancelled!");
		}
	}
	
	private void showToast(String message) {	
		
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		
	}
	
	/* Twitter functions */
	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
        @Override
        public void onComplete(String value) {
            username    = mTwitter.getUsername();
            username    = (username.equals("")) ? "No Name" : username;
            Toast.makeText(FinallookActivity.this, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
//            postToTwitter(editShare.getText().toString());
//            
            uploadPic();
        }
        @Override
        public void onError(String value) {
        	Toast.makeText(FinallookActivity.this, "Twitter connection failed", Toast.LENGTH_LONG).show();
        }
    };
    
    protected void postToTwitter(String review) {
    	ProgressDialog progressDialog = ProgressDialog.show(FinallookActivity.this,"Please wait...", "Sending...", true);
        try {
            mTwitter.updateStatus(review);
            progressDialog.dismiss();
            Toast.makeText(FinallookActivity.this, "Posted to Twitter", Toast.LENGTH_SHORT).show();
        } catch (TwitterException e) {
        	progressDialog.dismiss();
        	Toast.makeText(FinallookActivity.this, "Post to Twitter failed : " + e.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
	}

	public void uploadPic(){
		ProgressDialog progressDialog = ProgressDialog.show(FinallookActivity.this,"Please wait...", "Sending...", true);
	    try{
	        StatusUpdate status = new StatusUpdate("Created via citystylistapp.com");
	        status.setMedia(uploadfile);
	        mTwitter.updateStatus(status);
	        progressDialog.dismiss();
	        Toast.makeText(FinallookActivity.this, "Posted to Twitter", Toast.LENGTH_SHORT).show();
	    } catch (TwitterException e) {
	    	progressDialog.dismiss();
	    	Toast.makeText(FinallookActivity.this, "Post to Twitter failed : " + e.getErrorMessage(), Toast.LENGTH_LONG).show();
	    }
	}
	
	private void uploadToTwitter(Bitmap bmp){
		
		uploadfile = null;
		Bitmap adsBmp = mAdsPhotoView.getM_adsBitmap();
		String imgUrl = MediaStore.Images.Media.insertImage(getContentResolver(), adsBmp, "Advertisement", "");
		uploadfile = new File(Uri.parse(imgUrl).getPath());
		
	   if (uploadfile == null){
		   
	   }
	   else{
			
			if (mTwitter.hasAccessToken()) {
				uploadPic();
			} else {
				mTwitter.authorize();
			}
	   } 
   }	
}
	