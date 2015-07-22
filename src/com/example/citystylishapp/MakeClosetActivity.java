package com.example.citystylishapp;

import com.agilepoet.introduceyourself.util.ImageViewButton;
import com.example.custom.Global;
import com.example.custom.WearInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MakeClosetActivity extends Activity implements OnClickListener {

	
	ImageViewButton backBtn;
	ImageViewButton resetBtn;
	ImageViewButton doneBtn;
	
	//MakeClosetPhotoEdit mClosetPhotoEdit;
	DrawingView mClosetPhotoEdit;
	LinearLayout mRootLayout;
	FrameLayout mEditLayout;
	ImageView mImageView;
	
	private int selectedCategory = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_makecloset);	
				
		mEditLayout = (FrameLayout)findViewById(R.id.mc_edit_layout);
		mClosetPhotoEdit = (DrawingView)findViewById(R.id.mc_drawing_pad);
		mImageView = (ImageView)findViewById(R.id.mc_drawing_iv);
		
		Bundle extras = getIntent().getExtras();
		selectedCategory = extras.getInt("SelectedCategory");
		
		backBtn = (ImageViewButton)findViewById(R.id.btn_mc_back);
		resetBtn = (ImageViewButton)findViewById(R.id.btn_mc_reset);
		doneBtn = (ImageViewButton)findViewById(R.id.btn_mc_done);
		
		backBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		doneBtn.setOnClickListener(this);		
				
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_mc_back) {
			
			if (Global.fPhoto != null)
				if (!Global.fPhoto.isRecycled())
					Global.fPhoto.recycle();
			Global.fPhoto = null;
				
			Intent wardrobeAddtoClosetPLIntent = new Intent(MakeClosetActivity.this, WardrobeAddToClosetPLActivity.class);
			startActivity(wardrobeAddtoClosetPLIntent);
			finish();
			
		}else if(v.getId() == R.id.btn_mc_reset) {
			
			mClosetPhotoEdit.Reset();
		}else if(v.getId() == R.id.btn_mc_done) {
			
			if(selectedCategory == -1)
				return;
				
			// 
			//Bitmap tmp = mClosetPhotoEdit.getM_closetBitmap();
			//mImageView.setImageBitmap(tmp);
			
			if(selectedCategory == 1) {//Tops
				
				WearInfo topModel = new WearInfo();				
				topModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
//				mImageView.setDrawingCacheEnabled(true);
//				Bitmap capturedBmp = Bitmap.createBitmap(mImageView.getDrawingCache());
//				mImageView.setDrawingCacheEnabled(false);
//				topModel.imageOfWear = capturedBmp;
				topModel.wearInStore = false;						
				Global.addClosetWithModel(topModel, 301);
			}else if(selectedCategory == 2) { // bottoms
				
				WearInfo bottomModel = new WearInfo();
				bottomModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
				bottomModel.wearInStore = false;						
				Global.addClosetWithModel(bottomModel, 302);
			}else if(selectedCategory == 3) { // dresses
				
				WearInfo dressModel = new WearInfo();
				dressModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
				dressModel.wearInStore = false;						
				Global.addClosetWithModel(dressModel, 303);
			}else if(selectedCategory == 4) { // accessary
				
				WearInfo accessoryModel = new WearInfo();
				accessoryModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
				accessoryModel.wearInStore = false;						
				Global.addClosetWithModel(accessoryModel, 304);
			}else if(selectedCategory == 5) { // footwear
				
				WearInfo footWearModel = new WearInfo();
				footWearModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
				footWearModel.wearInStore = false;						
				Global.addClosetWithModel(footWearModel, 305);
			}else if(selectedCategory == 6) { // Jackets
				
				WearInfo jacketModel = new WearInfo();
				jacketModel.imageOfWear = mClosetPhotoEdit.MakeClosetBitmap();
				jacketModel.wearInStore = false;						
				Global.addClosetWithModel(jacketModel, 306);
				
			}
						
			new SaveChanged().execute(); 
		}
	}
	
	// Action of saving changes.
	class SaveChanged extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(MakeClosetActivity.this,
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
									
			Intent wardrobeAddtoClosetPLIntent = new Intent(MakeClosetActivity.this, WardrobeAddToClosetPLActivity.class);
			startActivity(wardrobeAddtoClosetPLIntent);
			finish();	
		}	
			
	}
	
	
	
}
