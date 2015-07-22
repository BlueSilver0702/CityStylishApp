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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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

public class CreateOutfitTopBottomActivity extends Activity implements OnClickListener{

	// Root Layout.
	LinearLayout rootLayout;
	
	// List
	HorizontalScrollView topListView;
	LinearLayout topListViewLinear;
	HorizontalScrollView bottomListView;
	LinearLayout bottomListViewLinear;
	
	// Bar Buttons	
	ImageViewButton btnBack;
	ImageViewButton btnPick;
	ImageViewButton btnDone;	
	
	OutfitInfo m_newInfo;
	int m_indexOfNewInfo;
		
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createoutfit_topbottom);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (LinearLayout) findViewById(R.id.createoutfit_topbottom_rootLayout);
				
		topListView = (HorizontalScrollView) findViewById(R.id.creatoutfit_top_listview);
		topListViewLinear = new LinearLayout(this);
		topListViewLinear.setOrientation(LinearLayout.HORIZONTAL);
		topListView.addView(topListViewLinear);
		addTopClosets();
		
		bottomListView = (HorizontalScrollView) findViewById(R.id.creatoutfit_bottom_listview);
		bottomListViewLinear = new LinearLayout(this);
		bottomListViewLinear.setOrientation(LinearLayout.HORIZONTAL);
		bottomListView.addView(bottomListViewLinear);
		addBottomClosets();
		
		// Get button objects.
		btnBack = (ImageViewButton) findViewById(R.id.btn_createoutfit_top_back);
		btnPick = (ImageViewButton) findViewById(R.id.btn_createoutfit_top_pick);
		btnDone = (ImageViewButton) findViewById(R.id.btn_createoutfit_top_done);		
				
		// Set ClickListner to button objects.
		btnBack.setOnClickListener(this);
		btnPick.setOnClickListener(this);
		btnDone.setOnClickListener(this);		
		
		m_newInfo = null;
		m_indexOfNewInfo = -1;
		
	}
	
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_createoutfit_top_pick) {			
				
			Log.d("CreateOutfits page : ", "Clicking pick button");	
			
			int i;
			boolean interactFlag = false;
				
			// For top.
			int outfitsCount = topListViewLinear.getChildCount();
			for(i = 0; i < outfitsCount; i++) {
									
				View childView = topListViewLinear.getChildAt(i);				
				if(isInteractedofRects(childView, v)) {
										
					interactFlag = true;
					break;
				}
			}
									
			if((i == 0) || ( i == 1) || (i > Global.personalInfo.arrTops.size() + 1)) {
				
				Log.d("CreateOutfits Top page : ", "There is no selected top item");
			}else {
			
				if(interactFlag) {
					
					Log.d("CreateOutfits Top page : selected top item = %d", String.format("%d", i));
					m_newInfo = new OutfitInfo();
//					if (Global.personalInfo.userPhoto != null) {
//						
//						if(m_newInfo.modelPhoto == null)
//							m_newInfo.modelPhoto = new MyBitmap();
//						m_newInfo.modelPhoto.setM_pBitmap(Global.personalInfo.userPhoto);
//					}
					
					Log.d("CreateOutfits Top page : the number of top items = %d", String.format("%d", Global.personalInfo.arrTops.size()));
					m_newInfo.topImage = new MyBitmap();				
					
					// position setting.
					m_newInfo.topImage.setCenter(0, 0);
					
					int fixedWidth = 150;
					float fixedScale = 1.0f;
					m_newInfo.topImage.setM_nWidth(fixedWidth);
					fixedScale = (float)fixedWidth/Global.personalInfo.arrTops.get(i-2).imageOfWear.getWidth();
					int fixedHeight = (int)(fixedScale * Global.personalInfo.arrTops.get(i-2).imageOfWear.getHeight());
					m_newInfo.topImage.setM_nHeight(fixedHeight);
					m_newInfo.topImage.setM_pBitmap(Bitmap.createScaledBitmap(Global.personalInfo.arrTops.get(i-2).imageOfWear, fixedWidth, fixedHeight, true));
					
					// scale setting.
					m_newInfo.topImage.setM_fScale(1.0f);				
					
					// angle setting.
					m_newInfo.topImage.setM_fAngle(0.0f);
										
					Global.personalInfo.arrOutfits.add(m_newInfo);
					m_indexOfNewInfo = Global.personalInfo.arrOutfits.size() - 1;
				}
			}
			
			// For Bottom.
			interactFlag = false;
			outfitsCount = bottomListViewLinear.getChildCount();
			for(i = 0; i < outfitsCount; i++) {
									
				View childView = bottomListViewLinear.getChildAt(i);				
				if(isInteractedofRects(childView, v)) {
										
					interactFlag = true;
					break;
				}
			}
									
			if((i == 0) || ( i == 1) || (i > Global.personalInfo.arrBottoms.size() + 1)) {
				
				Log.d("CreateOutfits Bottom page : ", "There is no selected bottom item");
			}else {
			
				if(interactFlag) {
					
					Log.d("CreateOutfits Bottom page : selected bottom item = %d", String.format("%d", i));
										
					if(m_indexOfNewInfo == -1)						
						m_newInfo = new OutfitInfo();
																
					m_newInfo.bottomImage = new MyBitmap();
					//m_newInfo.bottomImage.setM_pBitmap(Global.personalInfo.arrBottoms.get(i-2).imageOfWear);
						
					// position setting.
					m_newInfo.bottomImage.setCenter(0, 0);
						
					int fixedWidth = 150;
					float fixedScale = 1.0f;
					m_newInfo.bottomImage.setM_nWidth(fixedWidth);
					fixedScale = (float)fixedWidth/Global.personalInfo.arrBottoms.get(i-2).imageOfWear.getWidth();
					int fixedHeight = (int)(fixedScale * Global.personalInfo.arrBottoms.get(i-2).imageOfWear.getHeight());
					m_newInfo.bottomImage.setM_nHeight(fixedHeight);
					m_newInfo.bottomImage.setM_pBitmap(Bitmap.createScaledBitmap(Global.personalInfo.arrBottoms.get(i-2).imageOfWear, fixedWidth, fixedHeight, true));
					
					// scale setting.
					m_newInfo.bottomImage.setM_fScale(1.0f);	
					
					// size setting
					//m_newInfo.bottomImage.setM_nWidth(m_newInfo.bottomImage.getM_pBitmap().getWidth());
					//m_newInfo.bottomImage.setM_nHeight(m_newInfo.bottomImage.getM_pBitmap().getHeight());
					
					// scale setting.
					m_newInfo.bottomImage.setM_fScale(1.0f);				
						
					// angle setting.
					m_newInfo.bottomImage.setM_fAngle(0.0f);					
						
					if(m_indexOfNewInfo == -1) {
						
						Global.personalInfo.arrOutfits.add(m_newInfo);
						m_indexOfNewInfo = Global.personalInfo.arrOutfits.size() - 1;
					}					
				}				
			}	
			
			Intent createOutfitAccessaryIntent=new Intent(v.getContext(),CreateOutfitAccessariesActivity.class);
			createOutfitAccessaryIntent.putExtra("AccessaryArg", (int)m_indexOfNewInfo);
			startActivity(createOutfitAccessaryIntent);
			finish();
			
			
		} else if (v.getId() == R.id.btn_createoutfit_top_back) {
			
			Log.d("CreateOutfits page : ", "Clicking back button");
					
			// Go to WardrobeView
			Intent wardrobecreateoutfitIntent=new Intent(v.getContext(),WardrobeCreateoutfitActivity.class);
			startActivity(wardrobecreateoutfitIntent);
			finish();
				        
		} else if (v.getId() == R.id.btn_createoutfit_top_done) {

			Log.d("CreateOutfits page : ", "Clicking done button");			
			Intent finalLookIntent=new Intent(v.getContext(), FinallookActivity.class);
			finalLookIntent.putExtra("FinallookArg", (int)m_indexOfNewInfo);
			startActivity(finalLookIntent);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}   
	
	private void addTopClosets() {
		
		ImageView imageView;
		
		// Insert empty childs view.
		if(Global.personalInfo.arrFootWears.size() == 0) {
			
			Log.d("CreateOutfits FootWear page : ", "There is no FootWears.");
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			
			// Insert the second empty child view.
			
			
		}else {
			
			// Insert the real dress/set child views.			
			for(int i = 0; i < Global.personalInfo.arrTops.size() + 4; i++) {		
				
				if((i == 0) || (i == 1)) {
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					topListViewLinear.addView(retval);
					
				}else if((1 < i) && (i < (Global.personalInfo.arrTops.size() + 2))){		
					
					WearInfo info = (WearInfo) Global.personalInfo.arrTops.get(i-2);
					
					imageView = new ImageView (this);
					imageView.setTag(i-2);		
					
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
					LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
												150, 
												150);
					layoutParams.gravity=Gravity.CENTER;
					imageView.setLayoutParams(layoutParams);
									
					if(!info.wearInStore) {
						
						imageView.setImageBitmap(info.imageOfWear);
					}			
						
					topListViewLinear.addView(imageView);
					
				}else {				
					
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					topListViewLinear.addView(retval);
				}				
			}
		}		
		
	}
	
	private void addBottomClosets() {
		
		ImageView imageView;
		
		// Insert empty childs view.
		if(Global.personalInfo.arrBottoms.size() == 0) {
			
			Log.d("CreateOutfits FootWear page : ", "There is no FootWears.");
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			
			// Insert the second empty child view.
			
			
		}else {
			
			// Insert the real dress/set child views.			
			for(int i = 0; i < Global.personalInfo.arrBottoms.size() + 4; i++) {		
				
				if((i == 0) || (i == 1)) {
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					bottomListViewLinear.addView(retval);
					
				}else if((1 < i) && (i < (Global.personalInfo.arrBottoms.size() + 2))){		
					
					WearInfo info = (WearInfo) Global.personalInfo.arrBottoms.get(i-2);
					
					imageView = new ImageView (this);
					imageView.setTag(i-2);		
					
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//					LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
//												120, 
//												120);
//					layoutParams.gravity=Gravity.CENTER;
//					imageView.setLayoutParams(layoutParams);
									
					if(!info.wearInStore) {
						
						imageView.setImageBitmap(info.imageOfWear);
					}			
						
					bottomListViewLinear.addView(imageView);
					
				}else {				
					
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					bottomListViewLinear.addView(retval);
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
