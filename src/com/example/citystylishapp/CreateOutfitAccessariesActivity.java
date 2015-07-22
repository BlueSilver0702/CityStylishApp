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

public class CreateOutfitAccessariesActivity extends Activity implements OnClickListener{

	// Root Layout.
	LinearLayout rootLayout;
	
	// List
	HorizontalScrollView accessaryListView;
	LinearLayout accessaryListViewLinear;
	
	// Bar Buttons	
	ImageViewButton btnSkip;
	ImageViewButton btnPick;
	ImageViewButton btnDone;	
	
	OutfitInfo m_newInfo;
	int m_indexOfNewInfo;
		
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createoutfit_accessary);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (LinearLayout) findViewById(R.id.createoutfit_accessarys_rootLayout);		
		accessaryListView = (HorizontalScrollView) findViewById(R.id.creatoutfit_accessarys_listview);
		accessaryListViewLinear = new LinearLayout(this);
		accessaryListViewLinear.setOrientation(LinearLayout.HORIZONTAL);
		accessaryListView.addView(accessaryListViewLinear);
		addAccessoriesClosets();
		
		// Get button objects.
		btnSkip = (ImageViewButton) findViewById(R.id.btn_createoutfit_accessarys_skip);
		btnPick = (ImageViewButton) findViewById(R.id.btn_createoutfit_accessarys_pick);
		btnDone = (ImageViewButton) findViewById(R.id.btn_createoutfit_accessarys_done);		
				
		// Set ClickListner to button objects.
		btnSkip.setOnClickListener(this);
		btnPick.setOnClickListener(this);
		btnDone.setOnClickListener(this);	
				
		Bundle extras = getIntent().getExtras();
		m_indexOfNewInfo = extras.getInt("AccessaryArg");
		if(m_indexOfNewInfo == -1)
			m_newInfo = null;
		else
			m_newInfo = Global.personalInfo.arrOutfits.get(m_indexOfNewInfo);
				
	}	
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_createoutfit_accessarys_pick) {			
				
			Log.d("CreateOutfits Accessary page : ", "Clicking pick button");		
			
			// Grasp the selected item.
			int i;
			boolean interactFlag = false;
									
			int outfitsCount = accessaryListViewLinear.getChildCount();
			for(i = 0; i < outfitsCount; i++) {
									
				View childView = accessaryListViewLinear.getChildAt(i);				
				if(isInteractedofRects(childView, v)) {
										
					interactFlag = true;
					break;
				}
			}
									
			if((i == 0) || ( i == 1) || (i > Global.personalInfo.arrAccessories.size() + 1)) {
				
				Log.d("CreateOutfits Accessory page : ", "There is no selected item");
			}else {
			
				if(interactFlag) {
					
					Log.d("CreateOutfits Accessory page : selected item = %d", String.format("%d", i));
									
					// Get a last object.
					if(m_indexOfNewInfo == -1)
						m_newInfo = new OutfitInfo();
					
					if(m_newInfo != null) {
						m_newInfo.accessImage = new MyBitmap();
						//m_newInfo.accessImage.setM_pBitmap(Global.personalInfo.arrAccessories.get(i-2).imageOfWear);
						
						// position setting.
						m_newInfo.accessImage.setCenter(0, 0);
						
						int fixedWidth = 150;
						float fixedScale = 1.0f;
						m_newInfo.accessImage.setM_nWidth(fixedWidth);
						fixedScale = (float)fixedWidth/Global.personalInfo.arrAccessories.get(i-2).imageOfWear.getWidth();
						int fixedHeight = (int)(fixedScale * Global.personalInfo.arrAccessories.get(i-2).imageOfWear.getHeight());
						m_newInfo.accessImage.setM_nHeight(fixedHeight);
						m_newInfo.accessImage.setM_pBitmap(Bitmap.createScaledBitmap(Global.personalInfo.arrAccessories.get(i-2).imageOfWear, fixedWidth, fixedHeight, true));
						
						// size setting
						//m_newInfo.accessImage.setM_nWidth(m_newInfo.accessImage.getM_pBitmap().getWidth());
						//m_newInfo.accessImage.setM_nHeight(m_newInfo.accessImage.getM_pBitmap().getHeight());
						
						// scale setting.
						m_newInfo.accessImage.setM_fScale(1.0f);				
						
						// angle setting.
						m_newInfo.accessImage.setM_fAngle(0.0f);
						
						if(m_indexOfNewInfo == -1) {
							
							Global.personalInfo.arrOutfits.add(m_newInfo);
							m_indexOfNewInfo = Global.personalInfo.arrOutfits.size() - 1;
						}
						
					}		
					
					
					// Go to CreateOutfitFootWear
					Intent createOutfitFootwearIntent=new Intent(v.getContext(), CreateOutfitFootWearActivity.class);	
					createOutfitFootwearIntent.putExtra("FootwearArg", (int)m_indexOfNewInfo);
					startActivity(createOutfitFootwearIntent);
					finish();
				}				
			}		
			
		} else if (v.getId() == R.id.btn_createoutfit_accessarys_skip) {
			
			Log.d("CreateOutfits Accessary page : ", "Clicking skip button");
					
			// Go to CreateOutfitFootWear
			Intent createOutfitFootwearIntent=new Intent(v.getContext(), CreateOutfitFootWearActivity.class);
			createOutfitFootwearIntent.putExtra("FootwearArg", (int)m_indexOfNewInfo);
			startActivity(createOutfitFootwearIntent);
			finish();
				        
		} else if (v.getId() == R.id.btn_createoutfit_accessarys_done) {

			Log.d("CreateOutfits Accessary page : ", "Clicking done button");			
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
	
	private void addAccessoriesClosets() {
		
		ImageView imageView;
		
		// Insert empty childs view.
		if(Global.personalInfo.arrAccessories.size() == 0) {
			
			Log.d("CreateOutfits Accessories page : ", "There is no Accessories.");
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			
			// Insert the second empty child view.
			
			
		}else {
			
			// Insert the real dress/set child views.			
			for(int i = 0; i < Global.personalInfo.arrAccessories.size() + 4; i++) {		
				
				if((i == 0) || (i == 1)) {
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					accessaryListViewLinear.addView(retval);
					
				}else if((0 < i) && (i < (Global.personalInfo.arrAccessories.size() + 2))){		
					
					WearInfo info = (WearInfo) Global.personalInfo.arrAccessories.get(i-2);
					
					imageView = new ImageView (this);
					imageView.setTag(i-2);		
					
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
					//imageView.setLayoutParams(new LayoutParams(120, 120));
					//imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
							200, 
							200);
					layoutParams.gravity=Gravity.CENTER;
					imageView.setLayoutParams(layoutParams);
									
					if(!info.wearInStore) {
						
						imageView.setImageBitmap(info.imageOfWear);
					}			
						
					accessaryListViewLinear.addView(imageView);
					
				}else {
					
					// Insert empty child view.
					View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
					accessaryListViewLinear.addView(retval);
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
