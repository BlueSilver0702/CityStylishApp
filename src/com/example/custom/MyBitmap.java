package com.example.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MyBitmap implements Serializable{
	
	private static final long serialVersionUID = 46543445;
	
	private int m_componentMark;  //	1 : user photo. 
								  //	2 : top.
								  //  	3 : bottom.
								  //  	4 : dress.
								  //  	5 : jacket
								  //  	6 : footwear
								  //  	7 : accessary
								  //    8 : text
	private Bitmap m_pBitmap;
	private float	m_fScale;
	private float	m_fAngle;
	private int		m_centerX;
	private int		m_centerY;
	private int		m_nWidth;
	private int		m_nHeight;
	
	public MyBitmap() {
		m_fScale = 1;
		m_fAngle = 0;
		m_nHeight = 0;
		m_nWidth = 0;
		m_centerX = 0;
		m_centerY = 0;
	}
	
	public int getM_componentMark() {		
		return m_componentMark;
	}
	public void setM_componentMark(int componentMark) {		
		m_componentMark = componentMark;
	}
	
	public Bitmap getM_pBitmap() {
		return m_pBitmap;
	}
	public float getM_fAngle() {
		return m_fAngle;
	}
	public int getM_centerX() {
		return m_centerX;
	}
	public int getM_centerY() {
		return m_centerY;
	}
	public void setM_pBitmap(Bitmap pBitmap) {
		if (this.m_pBitmap != null)
			if (!this.m_pBitmap.isRecycled())
				this.m_pBitmap.recycle();
		this.m_pBitmap = null;
		try {
			this.m_pBitmap = pBitmap.copy(pBitmap.getConfig(), true);
		}catch(Exception e) {
			System.gc();
			this.m_pBitmap = pBitmap.copy(pBitmap.getConfig(), true);
		}
		m_nWidth = pBitmap.getWidth();
		m_nHeight = pBitmap.getHeight();
	}
	public void setM_fAngle(float m_fAngle) {
		this.m_fAngle = m_fAngle;
	}
	public void setM_centerX(int m_centerX) {
		this.m_centerX = m_centerX;
	}
	public void setM_centerY(int m_centerY) {
		this.m_centerY = m_centerY;
	}
	public void setCenter(int x, int y) {
		m_centerX = x;
		m_centerY = y;
	}
	public float getM_fScale() {
		return m_fScale;
	}
	public void setM_fScale(float m_fScale) {
		if (m_fScale >= 0.1 && m_fScale <= 10.0f)
			this.m_fScale = m_fScale;
	}
	public int getM_nWidth() {
		return m_nWidth;
	}
	public int getM_nHeight() {
		return m_nHeight;
	}
	public void setM_nWidth(int m_nWidth) {
		this.m_nWidth = m_nWidth;
	}
	public void setM_nHeight(int m_nHeight) {
		this.m_nHeight = m_nHeight;
	}
	public void addAngle(float angle) {
		this.m_fAngle += angle;
	}
	public void addScale(float scale) {
		if (m_fScale + scale >= 0.1 && m_fScale + scale <= 5.0f)
			this.m_fScale += scale;
		
	}
	public void addCenterX(int x) {
		this.m_centerX += x;
	}
	public void addCenterY(int y) {
		this.m_centerY += y;
	}
	/**
	 * indicate the point(x,y) is in the bitmap area
	 * @return
	 */
	public boolean isPtInRect(int x, int y) {
		float x1 = x - m_centerX;
        float y1 = y - m_centerY;
        int w1 = (int)(m_nWidth * m_fScale);
        int h1 = (int)(m_nHeight * m_fScale);
        float d = (float)(Math.sqrt(x1 * x1 + y1 * y1));
        float alpha;
//        if (x1 == 0)
//        	if (y1 > 0)
//        		alpha = 90;
//        	else if (y1 < 0)
//        		alpha = -90;
//        	else
//        		alpha = 0;
//        else
        	alpha = (float)(Math.atan2(y1 , x1));
        
        float x2 = d  * (float)(Math.cos(alpha - m_fAngle * Math.PI / 180)) + m_centerX;
        float y2 = d  * (float)(Math.sin(alpha - m_fAngle * Math.PI / 180)) + m_centerY;
        
        if (x2 >= m_centerX - w1 / 2 && x2 <= m_centerX + w1 / 2 && y2 >= m_centerY - h1 / 2 && y2 <= m_centerY + h1 / 2)
        	return true;
		return false;
	}
	
	public void dealloc() {
		if (m_pBitmap != null)
			if (!m_pBitmap.isRecycled()) {
				m_pBitmap.recycle();
			}
		m_pBitmap = null;
	}
	
	// Serialization
	private void writeObject(ObjectOutputStream out) throws IOException{
			
	    if(m_pBitmap != null) {
		    
	    	out.writeInt(m_componentMark);
	    	
	    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    	m_pBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    BitmapDataObject bitmapDataObject = new BitmapDataObject();     
		    bitmapDataObject.imageByteArray = stream.toByteArray();
	
		    out.writeObject(bitmapDataObject);
		    
		    out.writeFloat(m_fScale);
		    out.writeFloat(m_fAngle);
		    out.writeInt(m_centerX);
		    out.writeInt(m_centerY);
		    out.writeInt(m_nWidth);
		    out.writeInt(m_nHeight);
	    }    
	    
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		
		if(in.available() > 0) {
		
			m_componentMark = in.readInt();
			BitmapDataObject bitmapDataObject = (BitmapDataObject)in.readObject();
			if(bitmapDataObject != null) {				
				
				m_pBitmap = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
				m_fScale = in.readFloat();
				m_fAngle = in.readFloat();
				m_centerX = in.readInt();
				m_centerY = in.readInt();
				m_nWidth = in.readInt();
				m_nHeight = in.readInt();			
				
			}	
			
		}
	}
	
	
}
