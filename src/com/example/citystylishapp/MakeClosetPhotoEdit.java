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
import android.view.MotionEvent;
import android.graphics.Path;

public class MakeClosetPhotoEdit extends View {	
	
	public FinallookActivity containerActivity;
	
	public static int m_nOriginalWidth = 320;
	public static int m_nOriginalHeight = 480;

	public static Bitmap canvasBitmap;
	public static Canvas drawCanvas;
	public static ContentResolver mContentResolver;
	// private static Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static Paint m_bmpPaint;
	private Bitmap currentBmp = null;
	
	// drawing path	
	private Path drawPath;
	// drawing and canvas paint.
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF660000;
	
	
	Matrix mat = new Matrix();	

	public MakeClosetPhotoEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			
			setupDrawing();		
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void setupDrawing() {
		try {
			
			drawPath = new Path();
			drawPaint = new Paint();
			drawPaint.setColor(paintColor);
			
			// Set the initial path properties.
			drawPaint.setAntiAlias(true);
			drawPaint.setStrokeWidth(20);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeJoin(Paint.Join.ROUND);
			drawPaint.setStrokeCap(Paint.Cap.ROUND);
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			
			// instantiate the canvas Paint object.
			canvasPaint = new Paint(Paint.DITHER_FLAG);		
			
			//currentBmp = Global.fPhoto.copy(Config.ARGB_8888, true);
			//currentBmp = Global.fPhoto;
			
			//invalidate();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	
	@Override 
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		
		// instantiate the drawing canvas and bitmap using the width and height values.
		//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvasBitmap = Bitmap.createScaledBitmap(Global.fPhoto, this.getWidth(), this.getHeight(), true);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		try {		
						
//			mat.reset();
//			mat.preTranslate(-this.getWidth() / 2, -this.getHeight() / 2);
//			mat.postTranslate(this.getWidth() / 2, this.getHeight() / 2);
//			//float xScale = (float)currentBmp.getWidth()/this.getWidth();
//			float yScale = (float)currentBmp.getHeight()/this.getHeight();
//			mat.postScale(yScale, yScale,
//					this.getWidth()/2, this.getHeight()/2);
//			mat.postRotate(0, this.getWidth()/2, this.getHeight()/2);
//			canvas.drawBitmap(currentBmp, mat, m_bmpPaint);
			
			// draw the canvas and the drawing path.
			canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			//canvas.drawPath(drawPath, drawPaint);
			
		} catch (Exception e) {
			
			System.out.println(e.toString());
		}
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event) {
		
		float touchX = event.getX();
		float touchY = event.getY();
		
		switch(event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);			
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchX);
			break;
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;	
			
		}
		
		invalidate();
		
		return true;
		
	}
		
	public void Reset() {
		
		if (canvasBitmap != null)
			if (!canvasBitmap.isRecycled())
				canvasBitmap.recycle();
		canvasBitmap = null;
		
		if (drawCanvas != null)
			drawCanvas = null;
		
		canvasBitmap = Bitmap.createScaledBitmap(Global.fPhoto, this.getWidth(), this.getHeight(), true);
		drawCanvas = new Canvas(canvasBitmap);
		
		invalidate();
			
	}
	public void DeleteClosetBitmap() {
		if (canvasBitmap != null)
			if (!canvasBitmap.isRecycled())
				canvasBitmap.recycle();
		canvasBitmap = null;
		
		if (drawCanvas != null)
			drawCanvas = null;
	}
	
	public boolean MakeClosetBitmap() {
		if (canvasBitmap != null)
			if (!canvasBitmap.isRecycled())
				canvasBitmap.recycle();
		canvasBitmap = null;
		
		try {
			
			this.setDrawingCacheEnabled(true);
			canvasBitmap = Bitmap.createBitmap(this.getDrawingCache());
		    this.setDrawingCacheEnabled(false);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	
	public Bitmap getM_closetBitmap() {
		
		return canvasBitmap;
	}
}
