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

import android.net.Uri;

import com.example.custom.Global;
import com.example.custom.OutfitInfo;
import com.example.custom.WearInfo;
import com.example.custom.WishInfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.androidquery.AQuery;
import com.devsmart.android.ui.HorizontalListView;

public class VisitStoreActivity extends Activity implements OnClickListener{

	// Root Layout.
	LinearLayout rootLayout;
	
	// List
	HorizontalListView closetsListView;
	LinearLayout closetsListViewLinear;
	
	// Bar Buttons	
	ImageViewButton btnBack;
	ImageViewButton btnPrice;
	ImageViewButton btnAlpha;
	
	TextView labelSex;
	TextView labelCategory;
	TextView labelText1;
	TextView labelText2;
	TextView labelText3;
	TextView labelText4;
	
	// Array of downloaded infomations.
	int requestedType;
		
	ArrayList<WearInfo> arrOfClosets = new ArrayList<WearInfo>();
	
	private String strResponse;
	AlertDialog.Builder alertbox;
	
	private boolean isChanged;
		
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visit_store);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
		// Labels.
		labelSex = (TextView) findViewById(R.id.vs_tg_sex);
		labelCategory = (TextView) findViewById(R.id.vs_tg_category);
		labelText1 = (TextView) findViewById(R.id.vs_text1);
		labelText1.setText(getResources().getString(R.string.vs_txt1));
		labelText2 = (TextView) findViewById(R.id.vs_text2);
		labelText2.setText(getResources().getString(R.string.vs_txt2));
		labelText3 = (TextView) findViewById(R.id.vs_text3);
		labelText3.setText(getResources().getString(R.string.vs_txt3));
		labelText4 = (TextView) findViewById(R.id.vs_text4);
		labelText4.setText(getResources().getString(R.string.vs_txt4));
		
		// Decide the requested type of wearInfo from user.
		requestedType = DecideNTypeOfWear();
		
		// Get button objects.
		btnBack = (ImageViewButton) findViewById(R.id.btn_vs_tg_back);
		btnPrice = (ImageViewButton) findViewById(R.id.btn_vs_price);
		btnAlpha = (ImageViewButton) findViewById(R.id.btn_vs_alpha);		
				
		// Set ClickListner to button objects.
		btnBack.setOnClickListener(this);
		btnPrice.setOnClickListener(this);
		btnAlpha.setOnClickListener(this);	
		
		isChanged = false;
		
		// Horizontal scroll object.
		closetsListView = (HorizontalListView)findViewById(R.id.vs_productlist_scroll);
		
		// Starting the action of getting the wearinfo list from server.
		if(requestedType != 0)
			new ATGetWearInfo().execute();	
		
	}	
	
	private int DecideNTypeOfWear() {
		
		int nType = 1; // by default girl's top closet style.
		
		if(Global.ADDTOSET_VS_TYPE == Global.ADDTOSET_VS_GUY) {
			
			labelSex.setText("Guys");
			
			if(Global.VS_CATEGORY == Global.VS_CATEGORY_TOPS) {
				
				labelCategory.setText("TOPS");
				nType = 7;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_BOTTOMS) {
				
				labelCategory.setText("BOTTOMS");
				nType = 8;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_DRESSES) {
				
				labelCategory.setText("DRESSES");
				nType = 0;
				
			}
			else if(Global.VS_CATEGORY == Global.VS_CATEGORY_ACCESSORIES) {
				
				labelCategory.setText("ACCESSORIES");
				nType = 9;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_FOOTWEAR) {
				
				labelCategory.setText("FOOTWEARS");
				nType = 10;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_JACKETS) {
			
				labelCategory.setText("JACKETS");
				nType = 11;
			}
		}else {
			
			labelSex.setText("Girls");
			
			if(Global.VS_CATEGORY == Global.VS_CATEGORY_TOPS) {
				
				labelCategory.setText("TOPS");
				nType = 1;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_BOTTOMS) {
				
				labelCategory.setText("BOTTOMS");
				nType = 2;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_DRESSES) {
				
				labelCategory.setText("DRESSES");
				nType = 3;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_ACCESSORIES) {
				
				labelCategory.setText("ACCESSORIES");
				nType = 4;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_FOOTWEAR) {
				
				labelCategory.setText("FOOTWEARS");
				nType = 5;
				
			}else if(Global.VS_CATEGORY == Global.VS_CATEGORY_JACKETS) {
				
				labelCategory.setText("JACKETS");
				nType = 6;
				
			}
		}
		
		return nType;
	}
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(VisitStoreActivity.this,
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
			
			Intent addToClosetVsIntent=new Intent(VisitStoreActivity.this, WardrobeAddToClosetVSActivity.class);
			startActivity(addToClosetVsIntent);
			finish();
		}	
			
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_vs_tg_back) {			
					
			if(isChanged) {
				
				//Global.saveObject(Global.personalInfo);
				new SaveChanged().execute();
			}else {
				Intent addToClosetVsIntent=new Intent(v.getContext(), WardrobeAddToClosetVSActivity.class);
				startActivity(addToClosetVsIntent);
				finish();
			}
			
		} else if (v.getId() == R.id.btn_vs_price) {
				
			// Brand sorting.
			if(arrOfClosets.size() != 0) {
				
				arrOfClosets = ArraySort(arrOfClosets, new Comparator<WearInfo>(){
				   public int compare(WearInfo a, WearInfo b){
				      
					   WearInfo    ja = (WearInfo)a;
					   WearInfo    jb = (WearInfo)b;
				      return ja.wearPrice.toLowerCase().compareTo(jb.wearPrice.toLowerCase());
				   }
				});
			}
			
			// Loading the wearInfo list into horizontal scrollview.
			closetsListView.setAdapter(customListAdapter);
			
		} else if (v.getId() == R.id.btn_vs_alpha) {
			
			// Brand sorting.
			if(arrOfClosets.size() != 0) {
				
				arrOfClosets = ArraySort(arrOfClosets, new Comparator<WearInfo>(){
				   public int compare(WearInfo a, WearInfo b){
				      
					   WearInfo    ja = (WearInfo)a;
					   WearInfo    jb = (WearInfo)b;
				      return ja.wearBrand.toLowerCase().compareTo(jb.wearBrand.toLowerCase());
				   }
				});
			}
			
			// Loading the wearInfo list into horizontal scrollview.
			closetsListView.setAdapter(customListAdapter);
			
		} else { // Size buttons.
			
			Button btn = (Button)v;
			int productIndex = (v.getId() - Global.VS_SIZEBTNBASE) / 100;
			int btnIndex = (v.getId() - Global.VS_SIZEBTNBASE) % 100;
			String btnTitle = (String)btn.getText();
			
			WearInfo wearInfo = (WearInfo)arrOfClosets.get(productIndex);
			Log.d("VisitStore page : selected Wear brand ", wearInfo.wearBrand);
			Log.d("VisitStore page : selected Wear size ", btnTitle);	
			
			// Add to closet array.
			addToArry(btnTitle, wearInfo.wearCode, productIndex);
		}
		
	}
	
	// Sorting functions.
	private ArrayList<WearInfo> ArraySort(ArrayList<WearInfo> array, Comparator<WearInfo> c){
	    
		List<WearInfo>    asList = new ArrayList<WearInfo>(array.size());
	    for (int i=0; i<array.size(); i++){
	      asList.add(array.get(i));
	    }
	    Collections.sort(asList, c);
	    ArrayList<WearInfo>  res = new ArrayList<WearInfo>();
	    
	    for (int j = 0; j < asList.size() ; j++){
	      
	    	WearInfo wi = (WearInfo)asList.get(j);
	    	res.add(wi);	    	
	    }
	    
	    return res;
	}
	
	// Add to closet array.
	private void addToArry(String strSize, String wearCode, int iProductIndex) {
		
		boolean isExist = false;
		boolean wasSelected = false;
		int indexOfExist = 0;
		
		// Check if the selected item exists already in wish arr.
		for(int i = 0; i < Global.personalInfo.arrWishes.size(); i++) {
			
			WishInfo wishInfo = (WishInfo)Global.personalInfo.arrWishes.get(i);
			if (wearCode.equals(wishInfo.wearInfo.wearCode)) {
				
				wasSelected = true;
			}
			
			if (wasSelected & (strSize.equals(wishInfo.strSize))) {
				
				isExist = true;
				indexOfExist = i;
				break;
			}
		}
		
		if (isExist) { // Change the exist one.
			
			WishInfo selectedWishInfo = (WishInfo) Global.personalInfo.arrWishes.get(indexOfExist);
			selectedWishInfo.wearCount++;
			float unitPrice = Float.valueOf(selectedWishInfo.wearInfo.wearPrice);
			float wearCount = (float) selectedWishInfo.wearCount;
			selectedWishInfo.wishTotalPrice = unitPrice * wearCount;
			
			Global.personalInfo.arrWishes.set(indexOfExist, selectedWishInfo);
			
		}else { // Add the new one.
			
			// Add new wish to wish array.
			WearInfo selectedInfo = (WearInfo)arrOfClosets.get(iProductIndex);
			WishInfo newWishInfo = new WishInfo();
			newWishInfo.wearInfo = selectedInfo;
			newWishInfo.strSize = strSize;
			newWishInfo.wearCount = 1;
			float unitPrice = Float.valueOf(newWishInfo.wearInfo.wearPrice);
			float wearCount = (float) newWishInfo.wearCount;
			newWishInfo.wishTotalPrice = unitPrice * wearCount;
			
			Global.addClosetWithWish(newWishInfo);
			
			// Add new closet to closet array.
			if(!wasSelected) {
				
				Global.addClosetWithModel(selectedInfo, Global.VS_CATEGORY );
			}
			
			String closetsCount = String.format("%d", Global.personalInfo.arrWishes.size());
			Log.d("VisitStore---wish count : %d", closetsCount);
			
		}
		
		isChanged = true;
		
		
	}
	
	// Get Bitmap from url.
	private Bitmap getBitmapFromURL(String src) {
	   
		try {
	     
			URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        
	        return null;
	    }
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private BaseAdapter customListAdapter = new BaseAdapter() {  
		  
		
        @Override  
        public int getCount() {  
            return (arrOfClosets.size());  
        }  
  
        @Override  
        public Object getItem(int position) {  
            return null;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return 0;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            
        	View retval;
        	retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.closet_list_item, null);
        	
        	WearInfo wearInfo = (WearInfo)arrOfClosets.get(position);
        	
        	ImageView imageOfCloset = (ImageView) retval.findViewById(R.id.addcloset_listitem_image);        	
        	int iTag =  Global.VS_IMAGEBASE + position;
			String strTag = String.valueOf(iTag);
			imageOfCloset.setTag(strTag);
			new ATImageViewImageLoad().execute(imageOfCloset);
//			AQuery androidAQuery=new AQuery(VisitStoreActivity.this);
//			String photoURL = wearInfo.wearImageURL1;
//			androidAQuery.id(imageOfCloset).image(photoURL, true, true, 0, 0);	
//			wearInfo.imageOfWear = ((BitmapDrawable)imageOfCloset.getDrawable()).getBitmap();	
			
        	TextView brand = (TextView) retval.findViewById(R.id.ac_item_line1);
        	brand.setText(wearInfo.wearBrand);
        	
        	TextView price = (TextView) retval.findViewById(R.id.ac_item_price);
        	price.setText(wearInfo.wearPrice);
        	
        	TextView styleName = (TextView) retval.findViewById(R.id.ac_item_line2);
        	styleName.setText(wearInfo.wearStyleName);
        	
        	TextView code = (TextView) retval.findViewById(R.id.ac_item_line3);
        	code.setText(wearInfo.wearCode);
        	
        	// Placement buttons.
        	String btnArrayString = wearInfo.wearSize;
        	LinearLayout sizeBtnsLayout = (LinearLayout) retval.findViewById(R.id.addcloset_item_sizebtn_list);
        	List<String> list = new ArrayList<String>(Arrays.asList(btnArrayString.split(",")));
        	   
        	// Calc the width of buttons.
        	//int btn_width = 160/list.size();
            for (int i = 0; i < list.size(); i++) {
            	
                Button btnSize = new Button(VisitStoreActivity.this);
                //btnSize.setLayoutParams(new LayoutParams(btn_width, 40));
                String btnTitle = (String)list.get(i);
                //btnSize.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                btnSize.setTextSize(9.0f);
                btnSize.setText(btnTitle);
                int btnTag = Global.VS_SIZEBTNBASE + position * 100 + i;
                btnSize.setId(btnTag);                
                sizeBtnsLayout.addView(btnSize, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));                
                btnSize.setOnClickListener(VisitStoreActivity.this);
            }
        	        	              
            return retval;  
        }  
          
    };
	
	// Action of getting the survey list.
	class ATGetWearInfo extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		private int nStatusCode;
		
		@Override
		protected void onPreExecute() {
			strResponse = "";
			progressDialog = ProgressDialog.show(VisitStoreActivity.this,
					"Please wait...", "Loading...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
			
				HttpClient httpclient = new DefaultHttpClient();
				String strURL = String.format("http://citystylistapp.com/adminCP/api.php?action=products&_type_=%d", requestedType);
				HttpGet httpget = new HttpGet(strURL);
				HttpResponse response = httpclient.execute(httpget);
				nStatusCode = response.getStatusLine().getStatusCode();
				if (nStatusCode == 200) {
					HttpEntity entity = response.getEntity();
					strResponse = EntityUtils.toString(entity);
				}
			
			} catch (Exception e) {
					
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
				progressDialog.dismiss();

			if (nStatusCode != 200) {
					
				alertbox.setTitle("Please retry again.");
				alertbox.setMessage("Getting surveylist failed.");
				alertbox.setNegativeButton("Confirm",
						new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
								}
							});
				alertbox.show();
			} else {
					
				try {						
						
					arrOfClosets.clear();
						
					JSONObject jsonObject = new JSONObject(strResponse);
					Log.d("", strResponse);			

					String resultStr = jsonObject.getString("success");
					if (resultStr.equals("1")) {

							JSONArray dataList = jsonObject.getJSONArray("data");
							int dataNumbers = dataList.length();
							for (int i = 0; i < dataNumbers; i++) {
							
								JSONObject dataObj = dataList.getJSONObject(i);
								
								WearInfo wearObj = new WearInfo();
								wearObj.wearBrand = dataObj.getString("brand");
								wearObj.wearId = dataObj.getInt("id");
								wearObj.wearStyleName = dataObj.getString("line1");
								wearObj.wearCode = dataObj.getString("line2");
								wearObj.wearSize = dataObj.getString("sizes");
								wearObj.wearPrice = dataObj.getString("price");
								wearObj.wearImageURL1 = dataObj.getString("image1");
								wearObj.wearImageURL2 = dataObj.getString("image2");
							
								arrOfClosets.add(wearObj);
							}
							
							// Loading the wearInfo list into horizontal scrollview.
							closetsListView.setAdapter(customListAdapter);
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}	
			
	}
	
	// ImageLoad action class.
	class ATImageViewImageLoad extends AsyncTask<ImageView, Void, Void> {
	
		private ProgressDialog progressDialog;
		private int nStatusCode;
		Drawable thumb_d;
		ImageView imgView;
		WearInfo curWearInfo = null;

		@Override
		protected void onPreExecute() {
		
			strResponse = "";
			progressDialog = ProgressDialog.show(VisitStoreActivity.this,
							"Please wait...", "Loading...", true);
						
			thumb_d = null;
						
		}

		@Override
		protected Void doInBackground(ImageView... params) {
		
			try {
				
				imgView = (ImageView)params[0];
				String strTag = (String)imgView.getTag();
				int iIndex = Integer.valueOf(strTag) - Global.VS_IMAGEBASE;
				curWearInfo = (WearInfo)arrOfClosets.get(iIndex);	
				String photoURL = curWearInfo.wearImageURL1;
				Log.d("", photoURL);					
							
				URL thumb_u = new URL(photoURL);
				thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
																
			} catch (Exception e) {
			
				e.printStackTrace();
				thumb_d = getResources().getDrawable( R.drawable.notfound );
			}		
						
			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) {
		
			progressDialog.dismiss();
			Bitmap bitmap = ((BitmapDrawable) thumb_d).getBitmap();
			imgView.setImageBitmap(bitmap);	
			curWearInfo.imageOfWear = bitmap;
			
			
		}				
		
	}
	
	
}
	