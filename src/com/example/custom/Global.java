package com.example.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.citystylishapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import android.content.SharedPreferences;


@SuppressLint("DefaultLocale")
public class Global {
	
	
	// Facebook
	public static String FACEBOOK_APP_ID = "346538702141076";
	
	public static float _scaleX 		= 1f;
	public static float _scaleY 		= 1f;
	public static float WIN_W;
	public static float WIN_H;
	public static boolean hpdi          = false;
	
	public static float _getX(float x) {
		return _scaleX*x;
	}

	public static float _getY(float y) {
		return _scaleY*y;
	}
	
	public static Context g_context = null;
	
	public static float DEFAULT_W = 480f; 
	public static float DEFAULT_H = 320;
	public static float PTM_RATIO = 32f; // pixels to metre ratio
	
	public static final int INIT_ACCESSORIES_COUNT = 3;
	public static final int INIT_BOTTOM_COUNT = 2;
	public static final int INIT_DRESS_COUNT = 3;
	public static final int INIT_FOOTER_COUNT = 4;
	public static final int INIT_JACKET_COUNT = 4;
	public static final int INIT_TOP_COUNT = 6;
	
	public static PersonalInfo personalInfo;
	public static boolean      startWithUserPhoto;
	
	public static int          wearTopIndexForOutfit;
	public static int          wearBottomIndexForOutfit;
	public static int          wearIndexForOutfit;
	
	// Profile Camera
	public static Bitmap fPhoto;
	public static int    iSelectedPhoto;
	public static String cameraFilePath;
	public static String settingCameraFilePath;
	public static File   settingPhotoFile;
	public static String selectedImageStringPath;	
	
	// Outfit constants.
	public static int    OT_NONE = 0;
	public static int    OT_LADIES_AM = 10;
	public static int    OT_LADIES_PM = 20;
	public static int    OT_MENS = 30;
	public static int    OT_ALL = 40;
	public static int    OT_DUMMY = 50;
	public static float  OUTFIT_WIDTH = 240;
	public static float  OUTFIT_HEIGHT = 320;
	
	public static int    selectedOutfitCounts;
	
	// Tidy Outfits constants.
	public static int TIDY_SELECT;
	public static int TIDY_NONE = 99;
	public static int TIDY_TOPS = 100;
	public static int TIDY_BOTTOMS = 101;
	public static int TIDY_DRESSES = 102;
	public static int TIDY_ACCESSORIES = 103;
	public static int TIDY_FOOTWEARS = 104;
	public static int TIDY_JACKETS = 105;
	
	// AddToCloset constants.
	public static int ADDTOSET_VS_TYPE;
	public static int ADDTOSET_VS_GUY = 201;
	public static int ADDTOSET_VS_GIRL = 202;
	
	// Visit Store.
	public static int VS_CATEGORY;
	public static int VS_CATEGORY_TOPS = 301;
	public static int VS_CATEGORY_BOTTOMS = 302;
	public static int VS_CATEGORY_DRESSES = 303;
	public static int VS_CATEGORY_ACCESSORIES = 304;
	public static int VS_CATEGORY_FOOTWEAR = 305;
	public static int VS_CATEGORY_JACKETS = 306;
	public static int VS_IMAGEBASE = 1000;
	public static int VS_SIZEBTNBASE = 2000;
	
	// Wish list page
	public static int WL_IMAGEBASE = 3000;
	public static int WL_BUTTONBASE = 4000;
	
	public static String serverUrl = "http://mms-gen1.isprime.com/awards";
	
	// Specified Outfit images for finallook page.	
	public static ArrayList<MyBitmap> m_pOutfitMyBmpArray = new ArrayList<MyBitmap>();
	public static ArrayList<MyBitmap> m_pAdsMyBmpArray = new ArrayList<MyBitmap>();
		
	public static PersonalInfo loadSerializedObject(Context context)
    {
		
		PersonalInfo p;
		
		try
        {			
			
			// Check if CityStylist.bin exists on sd card.
			File file = new File(Environment.getExternalStorageDirectory().toString(), "CityStylist.bin" );
			if (file.exists()) {			
				
				// Check if file size is zero.
				if(file.length() == 0) {
					
					p = new PersonalInfo();
					
					g_context = context;
					SharedPreferences settings = context.getSharedPreferences("GlobalStates", 0);
					SharedPreferences.Editor editor = settings.edit();     
				    editor.putBoolean("isFirst", true);
				    editor.commit();
				    
				}else {
				
					//ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath() + "CityStylist.bin")));
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
					
					// Load PersonalInfo.            
		            p =(PersonalInfo) ois.readObject();
		            if(p == null) {
		            	
		            	p = new PersonalInfo();
		            }
				}
	            
			}else {
				
				// Create the file.
				File newFile = new File(Environment.getExternalStorageDirectory().toString()
				         +File.separator				         
				         +"CityStylist.bin"); //file name
				newFile.createNewFile();
				
				// Create personal Info object.
				p = new PersonalInfo();
			}		
			
            return p;
            
        }		
        catch(Exception ex)
        {
	        
        	Log.v("Serialization Read Error : ", ex.getMessage());
	            ex.printStackTrace();
        }
        return null;
    }
	
	public static void saveObject(PersonalInfo p){
       
		try
        {			
		   File outFile = new File(Environment.getExternalStorageDirectory().toString(), "CityStylist.bin");	
           ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile)); //Select where you wish to save the file...
           oos.writeObject(p); // write the class as an 'object'
           oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
           oos.close();// close the stream
        }
        catch(Exception ex)
        {
           Log.v("Serialization Save Error : ",ex.getMessage());
           ex.printStackTrace();
        }
   }
	
	public static void initFirstData(Context context) {
		
		// Add Dress								
		WearInfo dressModel1 = new WearInfo();
		dressModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.dresses_01);
		dressModel1.wearInStore = false;						
		Global.addClosetWithModel(dressModel1, 303);
		
		WearInfo dressModel2 = new WearInfo();
		dressModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.dresses_02);
		dressModel2.wearInStore = false;						
		Global.addClosetWithModel(dressModel2, 303);
		
		WearInfo dressModel3 = new WearInfo();
		dressModel3.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.dresses_03);
		dressModel3.wearInStore = false;						
		Global.addClosetWithModel(dressModel3, 303);		
		
		// Add Tops
		WearInfo topModel1 = new WearInfo();
		topModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_01);
		topModel1.wearInStore = false;						
		Global.addClosetWithModel(topModel1, 301);
		
		WearInfo topModel2 = new WearInfo();
		topModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_02);
		topModel2.wearInStore = false;						
		Global.addClosetWithModel(topModel2, 301);
		
		WearInfo topModel3 = new WearInfo();
		topModel3.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_03);
		topModel3.wearInStore = false;						
		Global.addClosetWithModel(topModel3, 301);
		
		WearInfo topModel4 = new WearInfo();
		topModel4.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_04);
		topModel4.wearInStore = false;						
		Global.addClosetWithModel(topModel4, 301);
		
		WearInfo topModel5 = new WearInfo();
		topModel5.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_05);
		topModel5.wearInStore = false;						
		Global.addClosetWithModel(topModel5, 301);
		
		WearInfo topModel6 = new WearInfo();
		topModel6.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.tops_06);
		topModel6.wearInStore = false;						
		Global.addClosetWithModel(topModel6, 301);
		
		// Add Accessories
		WearInfo accessoryModel1 = new WearInfo();
		accessoryModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.accessories_01);
		accessoryModel1.wearInStore = false;						
		Global.addClosetWithModel(accessoryModel1, 304);
		
		WearInfo accessoryModel2 = new WearInfo();
		accessoryModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.accessories_02);
		accessoryModel2.wearInStore = false;						
		Global.addClosetWithModel(accessoryModel2, 304);
		
		WearInfo accessoryModel3 = new WearInfo();
		accessoryModel3.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.accessories_03);
		accessoryModel3.wearInStore = false;						
		Global.addClosetWithModel(accessoryModel3, 304);
		
		// Add Bottoms
		WearInfo bottomModel1 = new WearInfo();
		bottomModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.bottoms_01);
		bottomModel1.wearInStore = false;						
		Global.addClosetWithModel(bottomModel1, 302);
		
		WearInfo bottomModel2 = new WearInfo();
		bottomModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.bottoms_02);
		bottomModel2.wearInStore = false;						
		Global.addClosetWithModel(bottomModel2, 302);
		
		
		// Add Footer
		WearInfo footWearModel1 = new WearInfo();
		footWearModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.footwear_01);
		footWearModel1.wearInStore = false;						
		Global.addClosetWithModel(footWearModel1, 305);
		
		WearInfo footWearModel2 = new WearInfo();
		footWearModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.footwear_02);
		footWearModel2.wearInStore = false;						
		Global.addClosetWithModel(footWearModel2, 305);
		
		WearInfo footWearModel3 = new WearInfo();
		footWearModel3.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.footwear_03);
		footWearModel3.wearInStore = false;						
		Global.addClosetWithModel(footWearModel3, 305);
		
		WearInfo footWearModel4 = new WearInfo();
		footWearModel4.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.footwear_04);
		footWearModel4.wearInStore = false;						
		Global.addClosetWithModel(footWearModel4, 305);
		
		// Add Jacket
		WearInfo jacketModel1 = new WearInfo();
		jacketModel1.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.jackets_01);
		jacketModel1.wearInStore = false;						
		Global.addClosetWithModel(jacketModel1, 306);
		
		WearInfo jacketModel2 = new WearInfo();
		jacketModel2.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.jackets_02);
		jacketModel2.wearInStore = false;						
		Global.addClosetWithModel(jacketModel2, 306);
		
		WearInfo jacketModel3 = new WearInfo();
		jacketModel3.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.jackets_03);
		jacketModel3.wearInStore = false;						
		Global.addClosetWithModel(jacketModel3, 306);
		
		WearInfo jacketModel4 = new WearInfo();
		jacketModel4.imageOfWear = BitmapFactory.decodeResource(context.getResources(), 
			    R.drawable.jackets_04);
		jacketModel4.wearInStore = false;						
		Global.addClosetWithModel(jacketModel4, 306);
		
	}
	
	public static void addClosetWithModel(WearInfo model, int type) {
		
		switch(type) {
		
			case 301:				
				Global.personalInfo.arrTops.add(model);
				break;
			case 302:				
				Global.personalInfo.arrBottoms.add(model);
				break;
			case 303:				
				Global.personalInfo.arrDresses.add(model);
				break;
			case 304:				
				Global.personalInfo.arrAccessories.add(model);
				break;
			case 305:				
				Global.personalInfo.arrFootWears.add(model);
				break;
			case 306:				
				Global.personalInfo.arrJacketWears.add(model);
				break;
			
			default:
				
				break;
		}
	}
	
	public static void addClosetWithWish(WishInfo wish) {		
		
		Global.personalInfo.arrWishes.add(wish);
	}
	
}


/* Singleton pattern */
/*

   public class Singleton {
	   private static final Singleton INSTANCE = new Singleton();
	
	   // Private constructor prevents instantiation from other classes
	   private Singleton() {}
	
	   public static Singleton getInstance() {
	      return INSTANCE;
	   }
 	}
 	
 	Singleton singleton = Singleton.getInstance();
  
*/
