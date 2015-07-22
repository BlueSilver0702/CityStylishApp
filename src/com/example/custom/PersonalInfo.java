package com.example.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class PersonalInfo implements Serializable{
	
	private static final long serialVersionUID = 46543445;
	
	// User photo
	public Bitmap userPhoto;
	
	// Array of Outfits.
	public ArrayList<OutfitInfo> arrOutfits = new ArrayList<OutfitInfo>();
	
	// Array of Tops.
	public ArrayList<WearInfo> arrTops = new ArrayList<WearInfo>();
	
	// Array of Bottoms.
	public ArrayList<WearInfo> arrBottoms = new ArrayList<WearInfo>();
	
	// Array of Dresses.
	public ArrayList<WearInfo> arrDresses = new ArrayList<WearInfo>();
	
	// Array of Accessories.
	public ArrayList<WearInfo> arrAccessories = new ArrayList<WearInfo>();
	
	// Array of FootWears.
	public ArrayList<WearInfo> arrFootWears = new ArrayList<WearInfo>();
	
	// Array of JacketWears.
	public ArrayList<WearInfo> arrJacketWears = new ArrayList<WearInfo>();
	
	// Array of Wishes.
	public ArrayList<WishInfo> arrWishes = new ArrayList<WishInfo>();	
		
	
	private void writeObject(ObjectOutputStream out) throws IOException{
	    
		out.writeObject(arrOutfits);
	    out.writeObject(arrTops);
	    out.writeObject(arrBottoms);
	    out.writeObject(arrDresses);
	    out.writeObject(arrAccessories);
	    out.writeObject(arrFootWears);
	    out.writeObject(arrJacketWears);
	    out.writeObject(arrWishes);

	    if(userPhoto != null) {
		    
	    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    userPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    BitmapDataObject bitmapDataObject = new BitmapDataObject();     
		    bitmapDataObject.imageByteArray = stream.toByteArray();
	
		    out.writeObject(bitmapDataObject);
		    
		    SharedPreferences settings = Global.g_context.getSharedPreferences("GlobalStates", 0);
			SharedPreferences.Editor editor = settings.edit();     
		    editor.putBoolean("up_save", true);
		    editor.commit();
	    }
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    
		arrOutfits = (ArrayList<OutfitInfo>)in.readObject();
		arrTops = (ArrayList<WearInfo>)in.readObject();
		arrBottoms = (ArrayList<WearInfo>)in.readObject();
		arrDresses = (ArrayList<WearInfo>)in.readObject();
		arrAccessories = (ArrayList<WearInfo>)in.readObject();
		arrFootWears = (ArrayList<WearInfo>)in.readObject();
		arrJacketWears = (ArrayList<WearInfo>)in.readObject();
		arrWishes = (ArrayList<WishInfo>)in.readObject();
		
		SharedPreferences settings = Global.g_context.getSharedPreferences("GlobalStates", 0);		
		boolean isSaved = settings.getBoolean("up_save",false);
		//if(in.available() > 0) {
		if(isSaved) {
		    BitmapDataObject bitmapDataObject = (BitmapDataObject)in.readObject();
		    if(bitmapDataObject != null) {
		    	
		    	userPhoto = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
		    }
		}
	}
	
}
