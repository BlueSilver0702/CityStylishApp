package com.example.citystylishapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;

import com.example.custom.Global;
import com.example.custom.OutfitInfo;
import com.example.custom.WearInfo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

public class OutfitActivity extends Activity implements OnClickListener{

	
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			super.handleMessage(msg);
		}
    }
	
	public MyHandler handler;
	
	boolean isShowPopup;	
	
	// Root Layout.
	FrameLayout rootLayout;
	LinearLayout second_rootview;
	
	HorizontalScrollView outfitLists;
	//LinearLayout sc_linearlayout;
	//HorizontalListView outfitLists;
	LinearLayout outfitListViewLinear;
	
	// Buttons	
	ImageViewButton btnView;
	ImageViewButton btnEscape;	
	
	// Slide Menu layout.
	RelativeLayout slideMenuLayout;
	ImageViewButton btnGirlsAll;
	ImageViewButton btnGirlsAm;
	ImageViewButton btnGirlsPm;
	ImageViewButton btnGuysAll;
	
	int mOriginalPos[];
	private int 	m_outfitType;
	private int     m_countOfOutfitType;
	private int     m_indexOfNewInfo;
	
	// Timer
	Timer timer;
	
	private ArrayList<OutfitInfo> mListOfSelectedOutfits = new ArrayList<OutfitInfo>();
	
	// Timer task for slide popup menu
	class showPopupTake extends TimerTask {

	     @Override
	     public void run() {

	    	 handler.sendMessage(new Message());     	
	     }
	};
		
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewoutfits);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Get root layout.
		rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
		second_rootview = (LinearLayout) findViewById(R.id.second_rootview); 
		
		//outfitLists = (HorizontalListView) findViewById(R.id.listview);	
		outfitLists = (HorizontalScrollView) findViewById(R.id.listview);
		outfitListViewLinear = new LinearLayout(this);
		outfitListViewLinear.setOrientation(LinearLayout.HORIZONTAL);
		outfitLists.addView(outfitListViewLinear);
		
		// Get button objects.
		btnView = (ImageViewButton) findViewById(R.id.btn_outfit_view);
		btnEscape = (ImageViewButton) findViewById(R.id.btn_outfit_escape);		
		
		slideMenuLayout = (RelativeLayout) findViewById(R.id.outfit_slidemenu_Layout);
		slideMenuLayout.bringToFront();
		slideMenuLayout.setVisibility(View.INVISIBLE);
		//slideMenuLayout.getLocationOnScreen( mOriginalPos );
		btnGirlsAll = (ImageViewButton) findViewById(R.id.btn_outfit_girls_all);
		btnGirlsAm = (ImageViewButton) findViewById(R.id.btn_outfit_girls_am);
		btnGirlsPm = (ImageViewButton) findViewById(R.id.btn_outfit_girls_pm);
		btnGuysAll = (ImageViewButton) findViewById(R.id.btn_outfit_guys_all);
		
		// Set ClickListner to button objects.
		btnView.setOnClickListener(this);
		btnEscape.setOnClickListener(this);
		btnGirlsAll.setOnClickListener(this);
		btnGirlsAm.setOnClickListener(this);
		btnGirlsPm.setOnClickListener(this);
		btnGuysAll.setOnClickListener(this);
		
		isShowPopup = false;
		m_outfitType = Global.OT_NONE;
		m_countOfOutfitType = -1;
		Global.selectedOutfitCounts = 0;
		m_indexOfNewInfo = -1;
		
		handler = new MyHandler();
		
		// Display slide Popup menu.
		timer = new Timer();
		timer.schedule(new showPopupTake(), 0, 1000);
	}
	
	
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_outfit_view) {
			
			int i;
			boolean interactFlag = false, found = false;
			
			if(mListOfSelectedOutfits.size() == 0)
				return;
			
			int outfitsCount = outfitListViewLinear.getChildCount();
			for(i = 0; i < outfitsCount; i++) {
				
				View childView = outfitListViewLinear.getChildAt(i);				
				if(isInteractedofRects(childView, v)) {
					
					interactFlag = true;
					break;
				}
			}
			
			if((i == 0) || ( i == 1) || (i > m_countOfOutfitType + 1)) {
				
				Log.d("CreateOutfits Accessory page : ", "There is no selected item");
			}else {
			
				if(interactFlag) {
			
					int limit = i - 1;
					int index = 0;
					// Get something specialized index on the seleted item.
					for(int j = 0; j < Global.personalInfo.arrOutfits.size(); j++) {
								
						
						OutfitInfo outfit = Global.personalInfo.arrOutfits.get(j);
						if(m_outfitType == Global.OT_ALL) {
							
							if(outfit.outfitType == Global.OT_LADIES_AM || 
									outfit.outfitType == Global.OT_LADIES_PM)
								
								index++;
						
						}else { 
							
							if(outfit.outfitType == m_outfitType) {
							
								index++;
							}
						}
						
						if(index == limit) {
							m_indexOfNewInfo = j;
							break;
						}
					}
					
					// Go to final look page.
					Intent finalLookIntent=new Intent(v.getContext(), FinallookActivity.class);
					finalLookIntent.putExtra("FinallookArg", (int)m_indexOfNewInfo);
					startActivity(finalLookIntent);
					finish();
									
				}				
			}
			
		} else if (v.getId() == R.id.btn_outfit_escape) {
			
			Log.d("Outfits page : ", "Clicking Outfits escape button");
					
			// Go to WardrobeView
			Intent wardrobeIntent=new Intent(v.getContext(),WardrobeActivity.class);
			startActivity(wardrobeIntent);
			finish();
				        
		} else if (v.getId() == R.id.btn_outfit_girls_all) {

			Log.d("Outfits page : ", "Clicking girls(all) button");		
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			loadOutfitList(Global.OT_ALL);
			
		}else if (v.getId() == R.id.btn_outfit_girls_am) {

			Log.d("Outfits page : ", "Clicking girls(am) button");			
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			// Load Outfits.
			loadOutfitList(Global.OT_LADIES_AM);
			
		} else if (v.getId() == R.id.btn_outfit_girls_pm) {

			Log.d("Outfits page : ", "Clicking girls(pm) button");
			
			// Disappear slide Popup menu.
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			// Load Outfits.
			loadOutfitList(Global.OT_LADIES_PM);
						
		} else if (v.getId() == R.id.btn_outfit_guys_all) {

			Log.d("Outfits page : ", "Clicking guys(all) button");
			
			// Disappear slide Popup menu.			
			if ( !isShowPopup ) {
    			
	   			moveViewToScreenCenter(true);
	   			isShowPopup = true;
	   		}else {
		    			
	   			moveViewToScreenCenter(false);
	   			isShowPopup = false;
	   		}
			
			// Load Outfits.
			loadOutfitList(Global.OT_MENS);
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
	    	btnGirlsAll.setEnabled(true);
			btnGirlsAm.setEnabled(true);
			btnGirlsPm.setEnabled(true);
			btnGuysAll.setEnabled(true);
			
	    	
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
	    	ViewGroup myViewGroup = ((ViewGroup) slideMenuLayout.getParent());
	        int index = myViewGroup.indexOfChild(slideMenuLayout);
	        for(int i = 0; i<index; i++)
	        {
	            myViewGroup.bringChildToFront(myViewGroup.getChildAt(i));
	        }
	    	
	    	btnGirlsAll.setEnabled(false);
			btnGirlsAm.setEnabled(false);
			btnGirlsPm.setEnabled(false);
			btnGuysAll.setEnabled(false);
	    	
	    }
	    
	    
	} 	
	
	public class CustomComparator implements Comparator<OutfitInfo> {
		
		@Override
		public int compare(OutfitInfo o1, OutfitInfo o2) {
			
			int result = 0;
			if(o1.outfitDate != null && o2.outfitDate != null)
				result = o1.outfitDate.compareTo(o2.outfitDate);
			else if(o1.outfitDate != null && o2.outfitDate == null)
				result = 1;
			else if(o1.outfitDate == null && o2.outfitDate != null)
				result = -1;
			else if(o1.outfitDate == null && o2.outfitDate == null)
				result = 0;
				
			return result;
		}
	}
	
	public void loadOutfitList(int nType )
	{	
		
		m_outfitType = nType;
		
		// Arrange by date order.
    	Collections.sort(Global.personalInfo.arrOutfits, new CustomComparator() );
    	
		// Get the number of specified kind of outfits.
		m_countOfOutfitType = 0;
    	if(m_outfitType == Global.OT_MENS) {
    		
    		for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
            {
    			OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
            	if(outfitInfo.outfitType == m_outfitType) {
            		mListOfSelectedOutfits.add(outfitInfo);
            		m_countOfOutfitType++;
            	}
            }
    		
    	} else {
           
    		if(m_outfitType == Global.OT_ALL){
    			
    			for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
                {
                	OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
                	if((outfitInfo.outfitType == Global.OT_ALL) || 
                			(outfitInfo.outfitType == Global.OT_LADIES_AM) ||
                			(outfitInfo.outfitType == Global.OT_LADIES_PM)) {
                		
                		mListOfSelectedOutfits.add(outfitInfo);
                		m_countOfOutfitType++;
                	}
                }
    		}else {
    			
    			for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
                {
                	OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
                	if(outfitInfo.outfitType == m_outfitType) {
                		
                		mListOfSelectedOutfits.add(outfitInfo);
                		m_countOfOutfitType++;
                	}
                		
                }
    		}  		
    	}  	
    	
    	
    	// Insert empty childs view.
		if(m_countOfOutfitType == 0) {
			
			Log.d("OutfitView page : ", "There is no Outfits.");
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_label_item, null);
			outfitListViewLinear.addView(retval);
			
			// Insert the second empty child view.
						
		}else {			
			
			// Insert 1st empty child view.
			View firstEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(firstEmptyView);
			
			// Insert 2nd empty child view.
			View secondEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(secondEmptyView);
									
			// Insert the real outfit child views.			
			for(int i = 0; i < mListOfSelectedOutfits.size(); i++) {		
				
				OutfitInfo info = (OutfitInfo)mListOfSelectedOutfits.get(i);
	        	if(info.outfitType == Global.OT_DUMMY || info.imageModeWeared == null)
	        			continue;
	        		        	
	        	View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_item, null);
	        	ImageView imageOfOutfit = (ImageView) retval.findViewById(R.id.outfit_listitem_image);
	        	TextView dateOfOutfit = (TextView) retval.findViewById(R.id.outfit_listitem_date);
	        		        	
				imageOfOutfit.setImageBitmap(info.imageModeWeared.getM_pBitmap());
				dateOfOutfit.setText(info.strOutfitDate);
				outfitListViewLinear.addView(retval);
	        					
			}
			// Insert the last two empty views.
			View lastFirstEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(lastFirstEmptyView);
			
			View lastSecondEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(lastSecondEmptyView);
		}
	}	
	
/*	public void loadOutfitList(int nType )
	{	
		
		m_outfitType = nType;
		
		// Arrange by date order.
		Collections.sort(Global.personalInfo.arrOutfits, new CustomComparator() );

		// Get the number of specified kind of outfits.
		m_countOfOutfitType = 0;
    	if(m_outfitType == Global.OT_MENS) {
    		
    		for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
            {
    			OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
            	if(outfitInfo.outfitType == m_outfitType)
            		m_countOfOutfitType++;
            }
    		
    	} else {
           
    		if(m_outfitType == Global.OT_ALL){
    			
    			for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
                {
                	OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
                	if((outfitInfo.outfitType == Global.OT_ALL) || 
                			(outfitInfo.outfitType == Global.OT_LADIES_AM) ||
                			(outfitInfo.outfitType == Global.OT_LADIES_PM))
                		m_countOfOutfitType++;
                }
    		}else {
    			
    			for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
                {
                	OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
                	if(outfitInfo.outfitType == m_outfitType)
                		m_countOfOutfitType++;
                }
    		}  		
    	}
    	
    	// Insert empty childs view.
		if(m_countOfOutfitType == 0) {
			
			Log.d("OutfitView page : ", "There is no Outfits.");
			
			// Insert the first empty child view.
			View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(retval);
			
			// Insert the second empty child view.
						
		}else {
			
			// Insert 1st empty child view.
			View firstEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(firstEmptyView);
			
			// Insert 2nd empty child view.
			View secondEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(secondEmptyView);
									
			// Insert the real outfit child views.			
			for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++) {		
				
				OutfitInfo info = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);
	        	if(info.outfitType == Global.OT_DUMMY || info.imageModeWeared == null)
	        			continue;
	        		        	
	        	View retval = LayoutInflater.from(this).inflate(R.layout.outfit_list_item, null);
	        	ImageView imageOfOutfit = (ImageView) retval.findViewById(R.id.outfit_listitem_image);
	        	TextView dateOfOutfit = (TextView) retval.findViewById(R.id.outfit_listitem_date);
	        				
					
	        	if(m_outfitType != Global.OT_ALL) {
	        			       			
	        		if(m_outfitType == info.outfitType) {
	        				
	        				imageOfOutfit.setImageBitmap(info.imageModeWeared.getM_pBitmap());
	        				dateOfOutfit.setText(info.strOutfitDate);
	        				outfitListViewLinear.addView(retval);
	        		}
	        	}else {
	        		
	        		if(!(info.outfitType == Global.OT_MENS)) {
	        				
	        				imageOfOutfit.setImageBitmap(info.imageModeWeared.getM_pBitmap());
	        				dateOfOutfit.setText(info.strOutfitDate);
	        				outfitListViewLinear.addView(retval);
	        		}
	        			
	        	}						
	        					
			}
			// Insert the last empty view.
			View lastEmptyView = LayoutInflater.from(this).inflate(R.layout.outfit_list_empty_item, null);
			outfitListViewLinear.addView(lastEmptyView);
		}
	}
	*/	
	
//	private BaseAdapter customListAdapter = new BaseAdapter() {  
//		  
//		
//        @Override  
//        public int getCount() {  
//        	
//        	m_countOfOutfitType = 0;
//        	if(m_outfitType == Global.OT_ALL)
//        		
//        		m_countOfOutfitType = Global.personalInfo.arrOutfits.size();
//        	else {
//	           
//        		for(int i = 0; i < Global.personalInfo.arrOutfits.size(); i++)
//	            {
//	            	OutfitInfo outfitInfo = (OutfitInfo)Global.personalInfo.arrOutfits.get(i);           		
//	            	if(outfitInfo.outfitType == m_outfitType)
//	            		m_countOfOutfitType++;
//	            }
//        	}
//        	
//            return m_countOfOutfitType + 4; // including 4 empty list item.
//        }  
//  
//        @Override  
//        public Object getItem(int position) {  
//            return null;  
//        }  
//  
//        @Override  
//        public long getItemId(int position) {  
//            return 0;  
//        }  
//  
//        @Override  
//        public View getView(int position, View convertView, ViewGroup parent) {  
//            
//        	View retval; 	
//        	
//        	if((position == 0) || (position == 1)) {
//        		
//        		// Insert empty child view.
//        		retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.outfit_list_empty_item, null);
//        	}else if((position > 1) && (position < (Global.personalInfo.arrOutfits.size() + 2))) {
//        	
//        		
//        		retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.outfit_list_item, null);
//        		ImageView imageOfOutfit = (ImageView) retval.findViewById(R.id.outfit_listitem_image);
//        		TextView dateOfOutfit = (TextView) retval.findViewById(R.id.outfit_listitem_date);
//        		
//        		OutfitInfo info = (OutfitInfo)Global.personalInfo.arrOutfits.get(position-2);
//        		if(info.outfitType == Global.OT_DUMMY || info.imageModeWeared == null)
//        			return null;
//        		
//        		if(m_outfitType != Global.OT_ALL) {
//        			       			
//        			if(m_outfitType == info.outfitType) {
//        				
//        				imageOfOutfit.setImageBitmap(info.imageModeWeared.getM_pBitmap());
//        				dateOfOutfit.setText(info.strOutfitDate);
//        				Global.selectedOutfitCounts++;
//        			}
//        		}else {
//        		
//        			if(!(info.outfitType == Global.OT_MENS)) {
//        				
//        				imageOfOutfit.setImageBitmap(info.imageModeWeared.getM_pBitmap());
//        				dateOfOutfit.setText(info.strOutfitDate);
//        				Global.selectedOutfitCounts++;
//        			}
//        			
//        		}
//        		
//        	}else {
//        	
//        		// Insert empty child view.
//            	retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.outfit_list_empty_item, null);           		
//            }
//              
//            return retval;  
//        }  
//          
//    };  
    
    /****************************************************************************
     * Check if bounds interects.
     ***************************************************************************/
    private Rect rectOfViewOnScreen(View view) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        return new Rect(x, y, w, h);
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
