package com.example.citystylishapp;


import java.text.AttributedCharacterIterator.Attribute;

import com.example.custom.Global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawingView extends ImageView {

	// Size of Drawing area.
	int width, height;
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = Color.TRANSPARENT;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap = null;
	//brush sizes
	private float brushSize, lastBrushSize;
	//erase flag
	private boolean erase=true;

	public DrawingView(Context context, AttributeSet attr){
		super(context, attr);
		setupDrawing();
	}

	//setup drawing
	private void setupDrawing(){
	
		//prepare for drawing and setup paint stroke properties
		brushSize = 30.0f;
		lastBrushSize = brushSize;
		drawPath = new Path();
		
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAlpha(0);
		drawPaint.setDither(true);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		setErase(true);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		
	}

	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		width = w;
		height = h;
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		
		//this.setImageBitmap(canvasBitmap);
//		canvasBitmap = Bitmap.createScaledBitmap(Global.fPhoto, width, height, true);
		
		
		final Canvas canvas1 = new Canvas(canvasBitmap);
		Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		Bitmap tmp = Bitmap.createScaledBitmap(Global.fPhoto, width, height, true);
		canvas1.drawBitmap(tmp, 1, 1, sPaint);					
		drawCanvas = new Canvas(canvasBitmap);
		
	}

	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		
//		if(canvasBitmap == null) {
//			//canvasBitmap = Bitmap.createBitmap(480, 320, Bitmap.Config.ARGB_8888);
//			canvasBitmap = Bitmap.createScaledBitmap(Global.fPhoto, width, height, true);			
//			drawCanvas = new Canvas(canvasBitmap);
//			//drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//		}			
		
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);		
		canvas.drawPath(drawPath, drawPaint);
	}

	//register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		//respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			drawPath.lineTo(touchX, touchY);			
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		//redraw
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
	
	public Bitmap MakeClosetBitmap() {	
		
		this.setDrawingCacheEnabled(true);
		Bitmap capturedBmp = Bitmap.createBitmap(this.getDrawingCache());
		this.setDrawingCacheEnabled(false);
			
		return capturedBmp;
	}
	
	public Bitmap getM_closetBitmap() {
		
		return canvasBitmap;
	}

	//update color
	public void setColor(String newColor){
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	//set brush size
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	//get and set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
		return lastBrushSize;
	}

	//set erase true or false
	public void setErase(boolean isErase){
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}

	//start new drawing
	public void startNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
}
