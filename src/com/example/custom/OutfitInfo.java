package com.example.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public class OutfitInfo implements Serializable{
	
	private static final long serialVersionUID = 46543445;
	
	public MyBitmap modelPhoto;
	
	// Top Wear
	public MyBitmap topImage;
	
	// Bottom Wear
	public MyBitmap bottomImage;
	
	// Dress Wear
	public MyBitmap dressImage;
	
	// Accessory Wear
	public MyBitmap accessImage;
	
	// Foot Wear	
	public MyBitmap footImage;
	
	// Jacket Wear
	public MyBitmap textImage;
	
	// Jacket Wear
	public MyBitmap jacketImage;
	
	public MyBitmap imageModeWeared;	
	
	// Other Infos.
	public String outfitName;
	public String strOutfitDate;
	public Date   outfitDate;
	public int    outfitType; // 0 : NONE, 1 : ALL, 2 : GIRL_AM, 3 : GIRL_PM, 4 : GUYs. 
	
	private void writeObject(ObjectOutputStream out) throws IOException{
	    
	    out.writeObject(modelPhoto);
	    out.writeObject(topImage);
	    out.writeObject(bottomImage);
	    out.writeObject(dressImage);
	    out.writeObject(accessImage);
	    out.writeObject(footImage);
	    out.writeObject(textImage);
	    out.writeObject(jacketImage);
	    out.writeObject(imageModeWeared);
	    
	    // Other Infos.
		out.writeObject(outfitName);
		out.writeObject(strOutfitDate);
		out.writeObject(outfitDate);
		out.writeInt(outfitType);	    
	    
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    
		modelPhoto = (MyBitmap)in.readObject();
		topImage = (MyBitmap)in.readObject();
		bottomImage = (MyBitmap)in.readObject();
		dressImage = (MyBitmap)in.readObject();
		accessImage = (MyBitmap)in.readObject();
		footImage = (MyBitmap)in.readObject();
		textImage = (MyBitmap)in.readObject();
		jacketImage = (MyBitmap)in.readObject();
		imageModeWeared = (MyBitmap)in.readObject();
		
		// Other Infos.
		outfitName = (String)in.readObject();
		strOutfitDate = (String)in.readObject();
		outfitDate = (Date)in.readObject();
		outfitType = in.readInt();
		
	}
	
}
