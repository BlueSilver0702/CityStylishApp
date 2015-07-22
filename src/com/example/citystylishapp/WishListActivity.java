package com.example.citystylishapp;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.citystylishapp.VisitStoreActivity.ATGetWearInfo;
import com.example.citystylishapp.VisitStoreActivity.ATImageViewImageLoad;
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
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.androidquery.AQuery;
import com.devsmart.android.ui.HorizontalListView;

//   paypal_comment
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;


public class WishListActivity extends Activity implements OnClickListener{

	// Root Layout.
	LinearLayout rootLayout;
	
	// Guide part
	ImageViewButton btnBack;
	Button btnCheckOut;
	
	// List
	ListView outfitsListView;
	
	// Bar Buttons	
	
	Button btnPrice;
	Button btnAlpha;
	
	TextView labelTotalExcluding;
	TextView labelGstAmount;
	TextView labelTotalAmount;
	
	private float totalExGSTPrice = 0.0f;
	private float totalGST = 0.0f;
	private float totalAmount = 0.0f;
	
	private boolean bChanged;
	/*
	 * PayPal library related fields
	 */
	//   paypal_comment
	private CheckoutButton launchPayPalButton;
	
	final static public int PAYPAL_BUTTON_ID = 10001;
	private static final int REQUEST_PAYPAL_CHECKOUT = 2;
	// Keeps a reference to the progress dialog
	private ProgressDialog _progressDialog;
	private boolean _paypalLibraryInit = false;
	private boolean _progressDialogRunning = false;	
	
	//Variables to fill out payment
	public String appID;
	public int server;
	
	//Reference to the library and an object of each type
	public PayPal pp;
	public PayPalPayment simplePayment;
	public PayPalAdvancedPayment advancedPayment;
	public boolean initializedLibrary;
	
	
	
	// method to check if the device is connected to network
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	
    // Functions.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist);		
		
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
		// Guide part
		btnBack = (ImageViewButton) findViewById(R.id.btn_wl_tg_back);
		btnCheckOut = (Button) findViewById(R.id.btn_wl_checkout);
		
		// Labels.
		labelTotalExcluding = (TextView) findViewById(R.id.wl_txt_total_price);
		labelGstAmount = (TextView) findViewById(R.id.wl_txt_amount);
		labelTotalAmount = (TextView) findViewById(R.id.wl_txt_total);
				
		// Set ClickListner to button objects.
		btnBack.setOnClickListener(this);
		btnCheckOut.setOnClickListener(this);	
		
		// Load horizontal scroll objects.
		outfitsListView = (ListView)findViewById(R.id.wl_item_listview);
		outfitsListView.setAdapter(customListAdapter);
		
		
		bChanged = false;
		
		// Init paypal
		appID = "APP-80W284485P519543T"; // APP-80W284485P519543T
        server = PayPal.ENV_SANDBOX;// PayPal.ENV_LIVE
        pp = null;
		initializedLibrary = false;		
		
		initLibrary();
		
		// Load below bar.
		loadBelowBar();		
		
	}	
	
//	@Override
//	protected void onPause() {
//	
//		Global.saveObject(Global.personalInfo);	
//	}
//	
//	@Override
//	protected void onDestroy() {
//	
//		Global.saveObject(Global.personalInfo);			
//	}
	
	//Initializes the library and payment objects
    public void initLibrary() {
    	//if(!verifyInfo())
    	//	return;
    	
    	//initWithAppID creates the PayPal instance and initializes things behind the scenes
    	pp = PayPal.getInstance();
   		if(!initializedLibrary) {
   	    	if(pp == null) {
    			pp = PayPal.initWithAppID(this, appID, server);
    			pp.setLanguage("en_US");
    			pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
    			pp.setShippingEnabled(true);
    			pp.setDynamicAmountCalculationEnabled(false);
    			pp.setCancelUrl("https://www.paypal.com");
    	        pp.setReturnUrl("https://www.paypal.com");    			
    		}
			initializedLibrary = true;
   		}    		
    }
    
	// PayPal Activity Results. This handles all the responses from the PayPal
	// Payments Library
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == REQUEST_PAYPAL_CHECKOUT) {
			PayPalActivityResult(requestCode, resultCode, intent);
		} else {
			super.onActivityResult(requestCode, resultCode, intent);
		}
	}
	
	public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			// The payment succeeded
			String payKey = intent
					.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
			this.paymentSucceeded(payKey);
			break;
		case Activity.RESULT_CANCELED:
			// The payment was canceled
			this.paymentCanceled();
			break;
		case PayPalActivity.RESULT_FAILURE:
			// The payment failed -- we get the error from the
			// EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
			String errorID = intent
					.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
			String errorMessage = intent
					.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			this.paymentFailed(errorID, errorMessage);
		}
	}
	
	public void paymentSucceeded(String payKey) {			
		
	}
	
	public void paymentFailed(String errorID, String errorMessage) {
		
	}
	
	public void paymentCanceled() {
		
	}
	
	public void PayPalButtonClick() {
		
		// Create a basic PayPalPayment.
		PayPalPayment payment = new PayPalPayment();
		// Sets the currency type for this payment.
		payment.setCurrencyType("AUD");		
		// Sets the recipient for the payment. This can also be a phone
		// number.
		payment.setRecipient("goldmanisme11111@gmail.com"); // admin@citystylistapp.com 
		// Sets the amount of the payment, not including tax and shipping
		// amounts. 
		BigDecimal st = new BigDecimal(totalAmount);
		st = st.setScale(2, RoundingMode.HALF_UP);
		payment.setSubtotal(st);
		// Sets the payment type. This can be PAYMENT_TYPE_GOODS,
		// PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or
		// PAYMENT_TYPE_NONE.
		payment.setPaymentType(PayPal.PAYMENT_TYPE_NONE);	
		
		// Use checkout to create our Intent.
		Intent checkoutIntent = PayPal.getInstance()
				.checkout(payment, this); //, new ResultDelegate());
		// Use the android's startActivityForResult() and pass in our
		// Intent.
		// This will start the library.
		startActivityForResult(checkoutIntent, REQUEST_PAYPAL_CHECKOUT);
	}	
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(WishListActivity.this,
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
			
			// Go to Home screen.
			Intent homeIntent=new Intent(WishListActivity.this, HomeActivity.class);
	        startActivity(homeIntent);
	        finish();
				
		}	
			
	}
	
	// UI actions.
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_wl_tg_back) {			
					
			// Save personal Info.
			if(bChanged) {		
				
				new SaveChanged().execute();						
			}else {
				
				// Go to Home screen.
				Intent homeIntent=new Intent(WishListActivity.this, HomeActivity.class);
		        startActivity(homeIntent);
		        finish();
			}
			
			
		} else if (v.getId() == R.id.btn_wl_checkout) {
				
			//   paypal_comment
			//PayPalButtonClick();
			
			
		} else if (v.getId() == R.id.btn_wl_item_cu) {
			
			int tag = (Integer) v.getTag();
			int position = (tag - Global.WL_BUTTONBASE - 1)/100;
			
			WishInfo wishInfo = (WishInfo)Global.personalInfo.arrWishes.get(position);
			wishInfo.wearCount++;
			float unitPrice = Float.valueOf(wishInfo.wearInfo.wearPrice);
			float wearCount = (float)wishInfo.wearCount;
			wishInfo.wishTotalPrice = unitPrice * wearCount;
			
			// Refresh the list.
			BaseAdapter adapter = (BaseAdapter)outfitsListView.getAdapter();
			adapter.notifyDataSetChanged();
			
			loadBelowBar();
			
			bChanged = true;
			
			
		} else if (v.getId() == R.id.btn_wl_item_cd) {
			
			int tag = (Integer) v.getTag();
			int position = (tag - Global.WL_BUTTONBASE - 2)/100;
			
			WishInfo wishInfo = (WishInfo)Global.personalInfo.arrWishes.get(position);
			if( wishInfo.wearCount >= 2) {
			
				wishInfo.wearCount--;
				float unitPrice = Float.valueOf(wishInfo.wearInfo.wearPrice);
				float wearCount = (float)wishInfo.wearCount;
				wishInfo.wishTotalPrice = unitPrice * wearCount;
				
				// Refresh the list.
				BaseAdapter adapter = (BaseAdapter)outfitsListView.getAdapter();
				adapter.notifyDataSetChanged();
				
				loadBelowBar();
				
				bChanged = true;
				
				// Save the changes.
				//Global.saveObject(Global.personalInfo);
			}
			
		} else if (v.getId() == R.id.btn_wl_item_remove) {
			
			int tag = (Integer) v.getTag();
			int position = (tag - Global.WL_BUTTONBASE - 3)/100;
			
			
			// Remove the wish item from array of wishes.
			Global.personalInfo.arrWishes.remove(position);
			
			// Refresh the list.
			BaseAdapter adapter = (BaseAdapter)outfitsListView.getAdapter();
			adapter.notifyDataSetChanged();
			
			loadBelowBar();
			
			bChanged = true;
			
			// Save the changes.
			//Global.saveObject(Global.personalInfo);
		}		
	}

	private void loadBelowBar() {
		
		totalAmount = 0;
		totalGST = 0;
		totalExGSTPrice = 0;
		
		for(int i = 0; i < Global.personalInfo.arrWishes.size(); i++) {
			
			WishInfo wishInfo = (WishInfo)Global.personalInfo.arrWishes.get(i);
			totalAmount += wishInfo.wishTotalPrice;
			totalGST += wishInfo.wishTotalPrice/10;
		}
		
		totalExGSTPrice = totalAmount - totalGST;
		
		labelTotalExcluding.setText(Float.toString(totalExGSTPrice));
		labelTotalAmount.setText(Float.toString(totalAmount));
		labelGstAmount.setText(Float.toString(totalGST));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// Custom adapter.
	private BaseAdapter customListAdapter = new BaseAdapter() {  
		  
		
        @Override  
        public int getCount() {  
            return (Global.personalInfo.arrWishes.size());  
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
        	retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, null);
        	
        	WishInfo wishInfo = (WishInfo)Global.personalInfo.arrWishes.get(position);
        	
        	ImageView imageOfWish = (ImageView) retval.findViewById(R.id.wish_listitem_image);        	
        	TextView brand = (TextView) retval.findViewById(R.id.wl_item_txt_line1);
        	TextView styleName = (TextView) retval.findViewById(R.id.wl_item_txt_line2);
        	TextView code = (TextView) retval.findViewById(R.id.wl_item_txt_line3);        	
        	TextView size = (TextView) retval.findViewById(R.id.wl_item_txt_size);
        	TextView count = (TextView) retval.findViewById(R.id.wl_item_txt_count);        	
        	TextView unitPrice = (TextView) retval.findViewById(R.id.wl_item_txt_unitPrice);
        	TextView totalPrice = (TextView) retval.findViewById(R.id.wl_item_txt_totalPrice);
        	       	       	
        	Button btnCountUp = (Button)retval.findViewById(R.id.btn_wl_item_cu);
        	btnCountUp.setTag(Global.WL_BUTTONBASE + position * 100 + 1);
        	btnCountUp.setOnClickListener(WishListActivity.this);
        	
        	Button btnCountDown = (Button)retval.findViewById(R.id.btn_wl_item_cd);
        	btnCountDown.setTag(Global.WL_BUTTONBASE + position * 100 + 2);
        	btnCountDown.setOnClickListener(WishListActivity.this);        	
        	
        	Button btnRemove = (Button)retval.findViewById(R.id.btn_wl_item_remove);
        	btnRemove.setTag(Global.WL_BUTTONBASE + position * 100 + 3);
        	btnRemove.setOnClickListener(WishListActivity.this);
        	
        	// Display infos 
        	if(wishInfo.wearInfo.imageOfWear != null) {
        		
        		imageOfWish.setImageBitmap(wishInfo.wearInfo.imageOfWear);
        	}else {
        		
        		int iTag =  Global.WL_IMAGEBASE + position;
    			String strTag = String.valueOf(iTag);
    			imageOfWish.setTag(strTag);
    			new ATImageViewImageLoad().execute(imageOfWish);
        	}
        	
        	brand.setText(wishInfo.wearInfo.wearBrand);
        	styleName.setText(wishInfo.wearInfo.wearStyleName);
        	code.setText(wishInfo.wearInfo.wearCode);
        	size.setText(wishInfo.strSize);
        	count.setText(String.format("%d", wishInfo.wearCount));
        	
        	String tmpStr1 = String.format("$%s", wishInfo.wearInfo.wearPrice);
        	unitPrice.setText(tmpStr1);
        	
        	String tmpStr2 = String.format("$%s", Float.toString(wishInfo.wishTotalPrice));
        	totalPrice.setText(tmpStr2);
        	        	              
            return retval;  
        }  
          
    };    
    
    
 // ImageLoad action class.
 	class ATImageViewImageLoad extends AsyncTask<ImageView, Void, Void> {
 	
 		private ProgressDialog progressDialog;
 		private int nStatusCode;
 		Drawable thumb_d;
 		ImageView imgView;
 		WishInfo curWishInfo = null;
 		private String strResponse;

 		@Override
 		protected void onPreExecute() {
 		
 			strResponse = "";
 			progressDialog = ProgressDialog.show(WishListActivity.this,
 							"Please wait...", "Loading...", true);
 						
 			thumb_d = null;
 						
 		}

 		@Override
 		protected Void doInBackground(ImageView... params) {
 		
 			try {
 				
 				imgView = (ImageView)params[0];
 				String strTag = (String)imgView.getTag();
 				int iIndex = Integer.valueOf(strTag) - Global.WL_IMAGEBASE;
 				curWishInfo = (WishInfo)Global.personalInfo.arrWishes.get(iIndex);	
 				String photoURL = curWishInfo.wearInfo.wearImageURL1;
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
 			curWishInfo.wearInfo.imageOfWear = bitmap; 			
 		} 		
 	}	
}
	