package com.example.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WearInfo implements Serializable{

	private static final long serialVersionUID = 46543445;
	
	public boolean wearInStore;
	public Bitmap  imageOfWear;
	
	public int     wearId;
	public String  wearBrand;
	public String  wearStyleName;
	public String  wearColor;
	public String  wearCode;
	public String  wearSize;
	public String  wearPrice;
	
	public String  wearImageURL1;
	public String  wearImageURL2;
	
	public String  wearImage1;
	public String  wearImage2;
	
	private void writeObject(ObjectOutputStream out) throws IOException{
	    
		out.writeBoolean(wearInStore);
	    out.writeInt(wearId);
	    out.writeObject(wearBrand);
	    out.writeObject(wearStyleName);
	    out.writeObject(wearColor);
	    out.writeObject(wearCode);
	    out.writeObject(wearSize);
	    out.writeObject(wearPrice);
	    out.writeObject(wearImageURL1);
	    out.writeObject(wearImageURL2);

	    if(imageOfWear != null) {
		    
	    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    imageOfWear.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    BitmapDataObject bitmapDataObject = new BitmapDataObject();     
		    bitmapDataObject.imageByteArray = stream.toByteArray();
	
		    out.writeObject(bitmapDataObject);
	    }
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    
		wearInStore = in.readBoolean();
		wearId = in.readInt();
		wearBrand = (String)in.readObject();
		wearStyleName = (String)in.readObject();
		wearColor = (String)in.readObject();
		wearCode = (String)in.readObject();
		wearSize = (String)in.readObject();
		wearPrice = (String)in.readObject();
		wearImageURL1 = (String)in.readObject();
		wearImageURL2 = (String)in.readObject();

	    BitmapDataObject bitmapDataObject = (BitmapDataObject)in.readObject();
	    imageOfWear = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
	    
	}
	
}
