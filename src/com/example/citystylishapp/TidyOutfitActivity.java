package com.example.citystylishapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;

import com.example.custom.Global;
import com.example.custom.MyBitmap;
import com.example.custom.OutfitInfo;
import com.example.custom.WearInfo;

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
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.devsmart.android.ui.HorizontalListView;

public class TidyOutfitActivity extends Activity implements OnClickListener{

	// Root Layout.
	LinearLayout rootLayout;
	
	// List
	HorizontalScrollView outfitsListView;
	LinearLayout outfitsListViewLinear;
	
	// Bar Buttons	
	ImageViewButton btnUndo;
	ImageViewButton btnDelete;
	ImageViewButton btnFinished;
		
	ArrayList<WearInfo> arrCurrent = null;
	ArrayList<WearInfo> arrTemp = null;
	boolean bChanged = false;
	
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tidyoutfit);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (LinearLayout) findViewById(R.id.tidyoutfit_rootLayout);				
		
		outfitsListView = (HorizontalScrollView) findViewById(R.id.tidyoutfit_horizontalScroll);
		outfitsListViewLinear = new LinearLayout(this);
		outfitsListViewLinear.setOrientation(LinearLayout.HORIZONTAL);
		outfitsListView.addView(outfitsListViewLinear);
		
		// Add closets to horizontal scrollview.
		
		
		// Get button objects.
		btnUndo = (ImageViewButton) findViewById(R.id.btn_tidyoutfit_undo);
		btnDelete = (ImageViewButton) findViewById(R.id.btn_tidyoutfit_delete);
		btnFinished = (ImageViewButton) findViewById(R.id.btn_tidyoutfit_finished);		
				
		// Set ClickListner to button objects.
		btnUndo.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnFinished.setOnClickListener(this);	
		
		if(Global.TIDY_SELECT == Global.TIDY_TOPS) {
			
			arrCurrent = Global.personalInfo.arrTops;
		}else if(Global.TIDY_SELECT == Global.TIDY_BOTTOMS) {
			arrCurrent = Global.personalInfo.arrBottoms;
		}else if(Global.TIDY_SELECT == Global.TIDY_DRESSES) {
			arrCurrent = Global.personalInfo.arrDresses;
		}else if(Global.TIDY_SELECT == Global.TIDY_ACCESSORIES) {
			arrCurrent = Global.personalInfo.arrAccessories;
		}else if(Global.TIDY_SELECT == Global.TIDY_FOOTWEARS) {
			arrCurrent = Global.personalInfo.arrFootWears;
		}else if(Global.TIDY_SELECT == Global.TIDY_JACKETS) {
			arrCurrent = Global.personalInfo.arrJacketWears;
		}
		
		addClosets();
		bChanged = false;
	}	
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(TidyOutfitActivity.this,
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
			
			// Go to WardrobeTidyClosetActivity
			Intent wardrobeTidyClosetIntent=new Intent(TidyOutfitActivity.this, WardrobeTidyClosetActivity.class);
			startActivity(wardrobeTidyClosetIntent);
			finish();
		}	
			
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_tidyoutfit_delete) {			
				
			Log.d("TidyOutfits page : ", "Clicking delete button");		
			
			int i;
			boolean interactFlag = false;
			
			int outfitsCount = outfitsListViewLinear.getChildCount();
			for(i = 0; i < outfitsCount; i++) {
				
				View childView = outfitsListViewLinear.getChildAt(i);				
				if(isInteractedofRects(childView, v)) {
					
					interactFlag = true;
					break;
				}
			}			
			
			Log.d("TidyOutfits page : selected item = %d", String.format("%d", i));		
			
			if((i == 0) || ( i == 1) || (i > arrCurrent.size() + 1)) {
				
				
			}else {
				if(interactFlag) {
				
					// Remove the selected 
					arrTemp = null;
					arrTemp = (ArrayList<WearInfo>)arrCurrent.clone();
					arrCurrent.remove(i-2);
					bChanged = true;
					
					// Refresh list of WearInfos.
					outfitsListViewLinear.removeAllViewsInLayout();
					addClosets();					
				}
			}
			
		} else if (v.getId() == R.id.btn_tidyoutfit_undo) {
			
			Log.d("TidyOutfits page : ", "Clicking undo button");		
			
			if(Global.TIDY_SELECT == Global.TIDY_TOPS) {
				
				Global.personalInfo.arrTops = null;
				Global.personalInfo.arrTops = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrTops;
			}else if(Global.TIDY_SELECT == Global.TIDY_BOTTOMS) {				
				Global.personalInfo.arrBottoms = null;
				Global.personalInfo.arrBottoms = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrBottoms;
			}else if(Global.TIDY_SELECT == Global.TIDY_DRESSES) {
				Global.personalInfo.arrDresses = null;
				Global.personalInfo.arrDresses = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrDresses;
			}else if(Global.TIDY_SELECT == Global.TIDY_ACCESSORIES) {
				Global.personalInfo.arrAccessories = null;
				Global.personalInfo.arrAccessories = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrAccessories;
			}else if(Global.TIDY_SELECT == Global.TIDY_FOOTWEARS) {
				Global.personalInfo.arrFootWears = null;
				Global.personalInfo.arrFootWears = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrFootWears;
			}else if(Global.TIDY_SELECT == Global.TIDY_JACKETS) {
				Global.personalInfo.arrJacketWears = null;
				Global.personalInfo.arrJacketWears = (ArrayList<WearInfo>)arrTemp.clone();
				arrCurrent = Global.personalInfo.arrJacketWears;
			}		
			
			// Refresh list of WearInfos.
			outfitsListViewLinear.removeAllViewsInLayout();
			addClosets();
				        
		} else if (v.getId() == R.id.btn_tidyoutfit_finished) {

			Log.d("TidyOutfits page : ", "Clicking finished button");
			if(bChanged)
				new SaveChanged().execute();
			else {
				
				// Go to WardrobeTidyClosetActivity
				Intent wardrobeTidyClosetIntent=new Intent(TidyOutfitActivity.this, WardrobeTidyClosetActivity.class);
				startActivity(wardrobeTidyClosetIntent);
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}   
	
	private void addClosets() {
		
		ImageView imageView;
		
		if(arrCurrent.size() == 0) {
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
						
			// Insert the second empty child view.
			
			
		}else {		
			
			// Insert the real dress/set child views.			
			for(int i = 0; i < arrCurrent.size() + 4; i++) {		
				
				if((i == 0) || (i == 1)) {
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);					
					outfitsListViewLinear.addView(retval);
					
				}else if((0 < i) && (i < (arrCurrent.size() + 2))){		
					
					WearInfo info = (WearInfo) arrCurrent.get(i-2);
					
					imageView = new ImageView (this);
					imageView.setTag(i-2);		
					
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
					LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
												LayoutParams.FILL_PARENT, 
												LayoutParams.WRAP_CONTENT);
					layoutParams.gravity=Gravity.CENTER;
					imageView.setLayoutParams(layoutParams);
					//imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
														
					if(!info.wearInStore) {
						
						imageView.setImageBitmap(info.imageOfWear);
					}			
						
					outfitsListViewLinear.addView(imageView);
					
				}else {				
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					outfitsListViewLinear.addView(retval);
				}				
			}
			
		}
	}
	
	private boolean isInteractedofRects(View view1, View view2) {
    	
    	boolean returnFlag = false;
    	
    	int[] l1 = new int[2];
        view1.getLocationOnScreen(l1);
        int xView1 = l1[0];
        int yView1 = l1[1];
        int wView1 = view1.getWidth();
        
        int[] l2 = new int[2];
        view2.getLocationOnScreen(l2);
        int xView2 = l2[0];
        int yView2 = l2[1];
        int wView2 = view2.getWidth();       
        
    	// Check if they are interacted each other.
        if(((xView1+wView1) <= xView2) || (xView2 <= (xView1))) {
        	
        	return false;
        }
        
    	if(((xView1 < xView2) && ((xView1 + wView1) > (xView2 + wView2))) ||
    	   ((xView1 < xView2) && ((xView1 + wView1) > (xView2 + wView2))) ||
    	   ((xView1 > xView2) && ((xView1 + wView1) > (xView2 + wView2)))){
    			
    		returnFlag = true;    			
    	}
    	
    	return returnFlag;
    }


}
