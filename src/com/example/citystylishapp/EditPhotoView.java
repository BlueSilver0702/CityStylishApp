package com.example.citystylishapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import com.example.custom.Global;
import com.example.custom.MyBitmap;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class EditPhotoView extends View {
	
	public GestureDetector m_pGesture = null;
	public FinallookActivity containerActivity;
	
	/**
	 * the user image
	 */	
	private MyBitmap m_pUserBmp = null;
	public static int m_nSelAccessIndex = -1; // no selection accessory
	/**
	 * @author ComUS
	 * @see Screen Width ,Screen Height
	 */
	public static int _mScreenWidth;
	public static int _mScreenHeight;
	public static float m_fScaleWidth = 0;
	public static float m_fScaleHeight = 0;
	public static int m_nOriginalWidth = 320;
	public static int m_nOriginalHeight = 480;
	private boolean m_bDoubleTap = false;

	public static Bitmap _mSaveBitmap;
	public static Canvas _mSaveCanvas;
	public static ContentResolver mContentResolver;
	// private static Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static Paint m_bmpPaint;
	
	Matrix mat = new Matrix();	

	public EditPhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			m_pGesture = new GestureDetector(new OnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {

				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {

					return false;
				}

				@Override
				public void onLongPress(MotionEvent e) {

					
					int x = (int) e.getX();
					int y = (int) e.getY();

					for (int i = Global.m_pOutfitMyBmpArray.size() - 1; i >= 0; i--) {
						if (Global.m_pOutfitMyBmpArray.get(i)
								.isPtInRect(x, y)) {
							m_nSelAccessIndex = i;
							
							// Check press long the text.
							if(Global.m_pOutfitMyBmpArray.get(i).getM_componentMark() == 8) {
								
								MyBitmap tmp = (MyBitmap)Global.m_pOutfitMyBmpArray.get(i);
								Global.m_pOutfitMyBmpArray.remove(tmp);
								containerActivity.showTextEditView();
								m_nSelAccessIndex = -1;
								invalidate();
								return;
							}
							
							break;
						}
					}

					if (m_nSelAccessIndex == -1)
						return;
					
					handleLongTap(e);
					
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {

					return false;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					
					int x = (int) e.getX();
					int y = (int) e.getY();

					for (int i = Global.m_pOutfitMyBmpArray.size() - 1; i >= 0; i--) {
						if (Global.m_pOutfitMyBmpArray.get(i)
								.isPtInRect(x, y)) {
							m_nSelAccessIndex = i;
							break;
						}
					}

					if (m_nSelAccessIndex == -1)
						return false;
					m_bDoubleTap = false;
					handleDoubleTap(e);
					
					return false;
				}
			});
			m_pGesture.setOnDoubleTapListener(new OnDoubleTapListener() {

				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {

					return false;
				}

				@Override
				public boolean onDoubleTapEvent(MotionEvent e) {

					return false;
				}

				@Override
				public boolean onDoubleTap(MotionEvent e) {
					int x = (int) e.getX();
					int y = (int) e.getY();

					for (int i = Global.m_pOutfitMyBmpArray.size() - 1; i >= 0; i--) {
						if (Global.m_pOutfitMyBmpArray.get(i)
								.isPtInRect(x, y)) {
							m_nSelAccessIndex = i;
							break;
						}
					}

					if (m_nSelAccessIndex == -1)
						return false;
					m_bDoubleTap = true;
					handleDoubleTap(e);
					return false;
				}
			});
			//DeleteSaveBitmap();
			
			_mScreenWidth = ((FinallookActivity) context).getWindowManager()
						.getDefaultDisplay().getWidth();
			_mScreenHeight = ((FinallookActivity) context).getWindowManager()
						.getDefaultDisplay().getHeight();
			m_fScaleWidth = (float) _mScreenWidth
						/ (float) m_nOriginalWidth;
			m_fScaleHeight = (float) _mScreenHeight
						/ (float) m_nOriginalHeight;
			init();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void init() {
		try {
			m_bmpPaint = new Paint();
			m_bmpPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
			
			invalidate();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public void setUserBmp(Bitmap userBmp) {
		
		if(m_pUserBmp != null) {
			m_pUserBmp.setM_pBitmap(null);
			m_pUserBmp = null;
		}
			
		m_pUserBmp = new MyBitmap();	
		m_pUserBmp.setM_pBitmap(userBmp);
		m_pUserBmp.setM_centerX(this.getWidth()/2);
		m_pUserBmp.setM_centerY(this.getHeight()/2);
		int userPhotoWidth = userBmp.getWidth();
		float scale = (float)this.getWidth()/userPhotoWidth;
		m_pUserBmp.setM_fScale(scale);
		m_pUserBmp.setM_componentMark(1);
	}
	
	public void addImage(MyBitmap pBitmap) {

		Bitmap tmp = pBitmap.getM_pBitmap();
		//if (pBitmap == null || pBitmap.getM_pBitmap().getWidth() == 0 || pBitmap.getM_pBitmap().getHeight() == 0)
		if (pBitmap == null || tmp == null)
			return;	

		int nWidth;
		if (Global.WIN_H <= 480)
			nWidth = 150;
		else
			nWidth = 200;
		
		//if(pBitmap.getM_componentMark() != 8)
			if(pBitmap.getM_fScale() == 1.0f)			
				pBitmap.setM_fScale((float) nWidth / pBitmap.getM_nWidth());
		//pBitmap.setM_fScale(0.5f);
		
		//pBitmap.setM_centerX(0);
		//pBitmap.setM_centerY(0);

		Global.m_pOutfitMyBmpArray.add(pBitmap);
		
	}

	public Bitmap scaleAndRotateImage(Bitmap image) {
		int kMaxResolution = 320;
		int width, height;
		float scaleRatio;
		Bitmap targetBmp;
		width = image.getWidth();
		height = image.getHeight();
		int tWidth = width, tHeight = height;
		if (width > kMaxResolution || height > kMaxResolution) {
			float ratio = (float) width / height;
			if (ratio > 1) {
				tWidth = kMaxResolution;
				tHeight = (int) (tWidth / ratio);
			} else {
				tHeight = kMaxResolution;
				tWidth = (int) (tHeight * ratio);
			}
			Log.d("EXCEED MAXRESOLUTION", "WIDTH=" + width);
			Log.d("EXCEED MAXRESOLUTION", "HEIGHT=" + height);
		}
		scaleRatio = (float) tWidth / width;
		scaleRatio = (float) 1.0;// gongli
		Matrix mt = new Matrix();
		mt.postScale(scaleRatio, scaleRatio);
		targetBmp = Bitmap.createBitmap(tWidth, tHeight, image.getConfig());
		Canvas cv = new Canvas(targetBmp);
		Paint mPaint = new Paint();
		mPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
		cv.drawBitmap(image, mt, mPaint);
		return targetBmp;
	}

	void handleDoubleTap(MotionEvent e) {
		
		invalidate();
	}
	void handleLongTap(MotionEvent e) {
		
		if (m_nSelAccessIndex == -1)
			return;
		
		MyBitmap longTappedBitmap = Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex);
		if(longTappedBitmap.getM_componentMark() == 2 || longTappedBitmap.getM_componentMark() == 3) {
			
			if(m_nSelAccessIndex == 0) {
				 
				MyBitmap tempBitmap = longTappedBitmap;
				Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex, Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex + 1));
				Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex + 1, tempBitmap);
			}else {
				
				MyBitmap tempBitmap = longTappedBitmap;
				int longTappedComMark = Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex + 1).getM_componentMark();
				if(longTappedComMark == 2 || longTappedComMark == 3) {
					
					Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex, Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex + 1));
					Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex + 1, tempBitmap);
				}else {
					
					Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex, Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex - 1));
					Global.m_pOutfitMyBmpArray.set(m_nSelAccessIndex - 1, tempBitmap);
				}				
			}
		}
		
		invalidate();
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		try {
			
			mat.reset();
			
			// Adjust the size of User image.
			if (!(m_pUserBmp == null || m_pUserBmp.getM_pBitmap().getWidth() == 0 || m_pUserBmp.getM_pBitmap().getHeight() == 0))
			{
				int nWidth;
				if (Global.WIN_H <= 480)
					nWidth = 120;
				else
					nWidth = 240;
				
				//if(m_pUserBmp.getM_fScale() == 1.0f)			
				//	m_pUserBmp.setM_fScale((float) nWidth / m_pUserBmp.getM_nWidth());
				
				// Draw User photo
				//mat.preTranslate(-m_pUserBmp.getM_nWidth() / 2, -m_pUserBmp.getM_nHeight() / 2);
				mat.preTranslate(0, 0);
				float dx = m_pUserBmp.getM_centerX();
				float dy = m_pUserBmp.getM_centerY();
				mat.postTranslate(dx, dy);
				mat.postScale(m_pUserBmp.getM_fScale(), m_pUserBmp.getM_fScale(),
						dx, dy);
				mat.postRotate(m_pUserBmp.getM_fAngle(), dx, dy);
				canvas.drawBitmap(m_pUserBmp.getM_pBitmap(), mat, m_bmpPaint);
				
			}
			mat.reset();
			for (int i = 0; i < Global.m_pOutfitMyBmpArray.size(); i++) {
				
				MyBitmap bmp = (MyBitmap)Global.m_pOutfitMyBmpArray.get(i);
				Bitmap decored = Bitmap.createBitmap(2 + bmp.getM_nWidth(),
						2 + bmp.getM_nHeight(), Bitmap.Config.ARGB_8888);
				Bitmap img;

				final Canvas canvas1 = new Canvas(decored);
				Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);					
				canvas1.drawBitmap(bmp.getM_pBitmap(), 1, 1, sPaint);					
				img = decored;
				mat.reset();
				mat.preTranslate(-bmp.getM_nWidth() / 2,
						-bmp.getM_nHeight() / 2);
				mat.postTranslate(bmp.getM_centerX(), bmp.getM_centerY());
				mat.postScale(bmp.getM_fScale(), bmp.getM_fScale(),
						bmp.getM_centerX(), bmp.getM_centerY());
				mat.postRotate(bmp.getM_fAngle(), bmp.getM_centerX(),
						bmp.getM_centerY());
				canvas.drawBitmap(img, mat, m_bmpPaint);
				
			}
			
		} catch (Exception e) {
			
			System.out.println(e.toString());
		}
	}

	
	
	public void ResizeUIImage() {
		try {

			Log.e("EditPhotoView", "curWidth=");
			Log.e("EditPhotoView", "curHeight=");

			// Make a new image from the CG Reference
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public Point GetNewSize(int nWidth, int nHeight) {
		int targetWidth = (int) (320);
		int targetHeight = (int) (480);

		targetWidth = (int) (320);
		targetHeight = targetWidth * nHeight / nWidth;

		Point newSize = new Point(targetWidth, targetHeight);
		return newSize;
	}

	int preX, preY;
	boolean isMulti = false;

	public float distTwoPoint(Point p1, Point p2) {
		return (float) (Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
				* (p1.y - p2.y)));
	}

	Point down1 = new Point(0, 0), down2 = new Point(0, 0), move1 = new Point(
			0, 0), move2 = new Point(0, 0);

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int x, y;
		x = (int) event.getX();
		y = (int) event.getY();

		m_bDoubleTap = false;
		if (m_pGesture != null)
			m_pGesture.onTouchEvent(event);

		if (m_bDoubleTap == true)
			return true;

		switch (action) {
		case MotionEvent.ACTION_POINTER_DOWN:
			isMulti = false;
			if (event.getPointerCount() > 1) {
				System.out.println("multi-touch down!!");
				down1 = new Point((int) event.getX(0), (int) event.getY(0));
				down2 = new Point((int) event.getX(1), (int) event.getY(1));
				isMulti = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			
			if (m_nSelAccessIndex >= 0) {
				
				if (event.getPointerCount() == 1){// && isMulti == false) {
					Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex)
							.addCenterX(x - preX);
					Global.m_pOutfitMyBmpArray.get(m_nSelAccessIndex)
								.addCenterY(y - preY);
					preX = x;
					preY = y;
				} else if (event.getPointerCount() > 1 && isMulti == true) {
					// multi touch : zoom
					move1 = new Point((int) event.getX(0), (int) event.getY(0));
					move2 = new Point((int) event.getX(1), (int) event.getY(1));

					if (distTwoPoint(down1, down2) != 0
							&& distTwoPoint(move1, move2) != 0)
						Global.m_pOutfitMyBmpArray
								.get(m_nSelAccessIndex)
								.setM_fScale(
										Global.m_pOutfitMyBmpArray
												.get(m_nSelAccessIndex)
												.getM_fScale()
												* (distTwoPoint(move1, move2) / distTwoPoint(
														down1, down2)));

					float angle1, angle2;

					// if (down1.y - down2.y != 0 && move1.y - move2.y != 0)
					{
						angle1 = (float) (Math.atan2(
								(double) (down1.x - down2.x),
								(down1.y - down2.y)) * 180.0f / Math.PI);
						angle2 = (float) (Math.atan2(
								(double) (move1.x - move2.x),
								(move1.y - move2.y)) * 180.0f / Math.PI);
						Global.m_pOutfitMyBmpArray.get(
								m_nSelAccessIndex).addAngle(angle1 - angle2);
					}
					down1 = move1;
					down2 = move2;				
					
				}					
				
		        preX= x;
		        preY = y;				
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			
	        preX= x;
	        preY = y;
			
			break;
		case MotionEvent.ACTION_DOWN:
						
	        preX= x;
	        preY = y;
	        
	        //containerActivity.addTextEditView(containerActivity.editview.getText().toString(), 0);
			break;
		}
		return true;
	}	
	
	public void DeleteSaveBitmap() {
		if (_mSaveBitmap != null)
			if (!_mSaveBitmap.isRecycled())
				_mSaveBitmap.recycle();
		_mSaveBitmap = null;
	}
	
	public void SaveOutfitComponents(MyBitmap saveBM) {
		
		int componentMark = saveBM.getM_componentMark();
		switch(componentMark) {
		
			case 1:// user photo
				
				break;
			case 2:// top
				containerActivity.specifedOutfitInfo.topImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.topImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.topImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.topImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.topImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.topImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.topImage.setM_nHeight(saveBM.getM_nHeight());
				break;
			case 3:// bottom
				containerActivity.specifedOutfitInfo.bottomImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.bottomImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.bottomImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.bottomImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.bottomImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.bottomImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.bottomImage.setM_nHeight(saveBM.getM_nHeight());
				break;
			case 4:// dress
				containerActivity.specifedOutfitInfo.dressImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.dressImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.dressImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.dressImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.dressImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.dressImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.dressImage.setM_nHeight(saveBM.getM_nHeight());
				break;
			case 7:// accessary
				containerActivity.specifedOutfitInfo.accessImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.accessImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.accessImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.accessImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.accessImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.accessImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.accessImage.setM_nHeight(saveBM.getM_nHeight());
				break;
			case 6:// footwear
				containerActivity.specifedOutfitInfo.footImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.footImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.footImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.footImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.footImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.footImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.footImage.setM_nHeight(saveBM.getM_nHeight());
				break;
			case 5:// jacket
				containerActivity.specifedOutfitInfo.jacketImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.jacketImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.jacketImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.jacketImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.jacketImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.jacketImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.jacketImage.setM_nHeight(saveBM.getM_nHeight());				
				break;
			case 8: // text
				
				containerActivity.specifedOutfitInfo.textImage.setM_componentMark(saveBM.getM_componentMark());
				containerActivity.specifedOutfitInfo.textImage.setM_fScale(saveBM.getM_fScale());
				containerActivity.specifedOutfitInfo.textImage.setM_fAngle(saveBM.getM_fAngle());
				containerActivity.specifedOutfitInfo.textImage.setM_centerX(saveBM.getM_centerX());
				containerActivity.specifedOutfitInfo.textImage.setM_centerY(saveBM.getM_centerY());
				containerActivity.specifedOutfitInfo.textImage.setM_nWidth(saveBM.getM_nWidth());
				containerActivity.specifedOutfitInfo.textImage.setM_nHeight(saveBM.getM_nHeight());
				
				break;
			
				
		}
	}
	public boolean MakeSaveBitmap() {
		if (_mSaveBitmap != null)
			if (!_mSaveBitmap.isRecycled())
				_mSaveBitmap.recycle();
		_mSaveBitmap = null;	

		this.setDrawingCacheEnabled(true);
		_mSaveBitmap = Bitmap.createBitmap(this.getDrawingCache());
		this.setDrawingCacheEnabled(false);
					
		for (int i = 0; i < Global.m_pOutfitMyBmpArray.size(); i++) {			
			
			// Save the current outfit's components.
			SaveOutfitComponents(Global.m_pOutfitMyBmpArray.get(i));
		}
		
		return true;
	}	

	// Action of saving changes.
		class SaveChanged extends AsyncTask<Void, Void, Void> {
			private ProgressDialog progressDialog;
			
			@Override
			protected void onPreExecute() {
				
				progressDialog = ProgressDialog.show(containerActivity,
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
						
			}	
				
		}
	
	public boolean Save(){
		
		if (MakeSaveBitmap() == true){
			
			if(containerActivity.specifedOutfitInfo.imageModeWeared == null)
				containerActivity.specifedOutfitInfo.imageModeWeared = new MyBitmap();
			
			containerActivity.specifedOutfitInfo.imageModeWeared.setM_pBitmap(_mSaveBitmap);
			//Global.saveObject(Global.personalInfo);
			new SaveChanged().execute();
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean MakeModelWeared() {
		
		if (MakeSaveBitmap() == true){
			
			if(containerActivity.specifedOutfitInfo.imageModeWeared == null)
				containerActivity.specifedOutfitInfo.imageModeWeared = new MyBitmap();
			
			containerActivity.specifedOutfitInfo.imageModeWeared.setM_pBitmap(_mSaveBitmap);
			
			return true;
		}
		else{
			return false;
		}
	}
	
	public void dealloc() {
		if (m_pUserBmp != null)
			m_pUserBmp.dealloc();
		m_pUserBmp = null;	
	}
	
	public void insertText(String str, Typeface ft, boolean horizontal){
				
		Bitmap bmp = textAsBitmap(str, ft, (int)Global._getX(85));
		MyBitmap txtBitmap = new MyBitmap();
		txtBitmap.setM_pBitmap(bmp);
		// position setting.
		//txtBitmap.setCenter((int) (Global.WIN_W / 2), (int) (Global.WIN_H / 2));
		txtBitmap.setCenter((int) (this.getWidth() / 2), (int) (this.getHeight()*4 / 5));
		
		// size setting
		txtBitmap.setM_nWidth(txtBitmap.getM_pBitmap().getWidth());
		txtBitmap.setM_nHeight(txtBitmap.getM_pBitmap().getHeight());
		
		// scale setting.
		txtBitmap.setM_fScale(1.0f);				
		
		// angle setting.
		txtBitmap.setM_fAngle(0.0f);
		txtBitmap.setM_componentMark(8);
		addImage(txtBitmap);
		
		this.containerActivity.specifedOutfitInfo.textImage = txtBitmap;
		
		// record that text is set.
		SharedPreferences settings = this.containerActivity.getSharedPreferences("GlobalStates", 0);
		SharedPreferences.Editor editor = settings.edit();     
		editor.putBoolean("text_save", true);
	    editor.commit();
		
		invalidate();
	}
	
	private Bitmap textAsBitmap(String text, Typeface ft,float textSize) {
	    Paint paint = new Paint();

        Rect    bounds = new Rect(); 
	    
	    paint.setTextSize(textSize);
	    paint.setColor(0xFF000000);
	    paint.setTypeface(ft);
	    paint.setTextAlign(Paint.Align.CENTER);
        paint.getTextBounds(text, 0, text.length(), bounds);	    
	    Bitmap image = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(image);
	    canvas.drawText(text, bounds.width()/2, bounds.height(), paint);
	    return image;
	}
	
}
