package com.example.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class WishInfo implements Serializable{
	
	private static final long serialVersionUID = 46543445;

	public WearInfo  wearInfo;
	public int       wearCount;
	public float     wishTotalPrice;
	public String    strSize;
	
	private void writeObject(ObjectOutputStream out) throws IOException{
	    		
	    out.writeInt(wearCount);
	    out.writeFloat(wishTotalPrice);
	    out.writeObject(strSize);
	    out.writeObject(wearInfo);	    
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    		
		wearCount = in.readInt();
		wishTotalPrice = in.readFloat();
		strSize = (String)in.readObject();
		wearInfo = (WearInfo)in.readObject();
	    
	}
	
}
