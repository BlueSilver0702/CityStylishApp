package com.example.citystylishapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import com.example.custom.Global;
import com.example.custom.MyBitmap;

import android.content.ContentResolver;
import android.content.Context;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class AdsPhotoView extends View {	
	
	public FinallookActivity containerActivity;
	
	public static int m_nOriginalWidth = 320;
	public static int m_nOriginalHeight = 480;

	public static Bitmap _mAdsBitmap;
	public static Canvas _mAdsCanvas;
	public static ContentResolver mContentResolver;
	// private static Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static Paint m_bmpPaint;
	
	Matrix mat = new Matrix();	

	public AdsPhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			
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
	
	public void addImage(MyBitmap pBitmap) {

		if (pBitmap == null || pBitmap.getM_pBitmap().getWidth() == 0 || pBitmap.getM_pBitmap().getHeight() == 0)
			return;	

		int nWidth;
		if (Global.WIN_H <= 480)
			nWidth = 120;
		else
			nWidth = 240;
		//pBitmap.setM_fScale((float) nWidth / pBitmap.getM_nWidth());

		Global.m_pAdsMyBmpArray.add(pBitmap);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		try {
			
			mat.reset();
			for (int i = 0; i < Global.m_pAdsMyBmpArray.size(); i++) {
				
				MyBitmap bmp = (MyBitmap)Global.m_pAdsMyBmpArray.get(i);
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
		
	
	public void DeleteAdsBitmap() {
		if (_mAdsBitmap != null)
			if (!_mAdsBitmap.isRecycled())
				_mAdsBitmap.recycle();
		_mAdsBitmap = null;
	}
	
	public boolean MakeAdsBitmap() {
		if (_mAdsBitmap != null)
			if (!_mAdsBitmap.isRecycled())
				_mAdsBitmap.recycle();
		_mAdsBitmap = null;
		
		try {
			
			this.setDrawingCacheEnabled(true);
			_mAdsBitmap = Bitmap.createBitmap(this.getDrawingCache());
		    this.setDrawingCacheEnabled(false);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	
	public Bitmap getM_adsBitmap() {
		
		return _mAdsBitmap;
	}
}
