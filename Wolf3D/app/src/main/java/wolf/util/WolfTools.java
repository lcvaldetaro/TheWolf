

package wolf.util;

import com.peterpan.wolfs3d.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class WolfTools 
{
	static final String TAG = "WolfTools";
	
	public static final String WOLF_FOLDER = "/data/data" + File.separator;

	public static final String GAME_ZIP = "wolfsw.zip";
	
	// One of wolf, wolfsw, sodemo, sod, sodm2, sodm3
	public static final String GAME_ID = "wolfsw";
	
	/**
	 * JNI lib. To be downloaded into /data/data/APP_PKG/files
	 */
	public static final String WOLF_LIB = "cacau.wolf"; //"libwolf_jni.so";
	//public static final String WOLF_LIB = "wolf"; //"libwolf.so";
	
	
	/**
	 * Message box
	 * @param text
	 */
	
	public static  void MessageBox (Context ctx, String title, String text) {
		AlertDialog d = createAlertDialog(ctx
				, title
				, text);
			
			d.setButton("Dismiss", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK so do some stuff */
                }
            });
			d.show();
	}
	
    /**
     * Create an alert dialog
     * @param ctx App context
     * @param message Message
     * @return
     */
    static public AlertDialog createAlertDialog (Context ctx, String title, String message) {
        return new AlertDialog.Builder(ctx)
	        .setIcon(R.drawable.icon)
	        .setTitle(title)
	        .setMessage(message)
	        .create();
    }
	
    /**
     * Launch web browser
     */
    static public void launchBrowser(Context ctx, String url) {
    	ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));     	
    }
	
	static public void hardExit ( int code) {
		System.exit(code);
	}
    
    /**
     * Unzip utility
     * @param is
     * @param dest
     * @throws IOException
     */
    public static void unzip (InputStream is, File dest) throws IOException
    {
    	if ( !dest.isDirectory()) 
    		throw new IOException("Invalid Unzip destination " + dest );
    	
    	ZipInputStream zip = new ZipInputStream(is);
    	
    	ZipEntry ze;
    	
    	while ( (ze = zip.getNextEntry()) != null ) {
    		final String path = dest.getAbsolutePath() 
    			+ File.separator + ze.getName();
    		
    		// Create any entry folders
    		if ( ze.getName().indexOf("/") != -1) 
    		{
    			File parent = new File(path).getParentFile();
    			if ( !parent.exists() )
    				if ( !parent.mkdirs() )
    					throw new IOException("Unable to create folder " + parent);
    		}
    		
    		FileOutputStream fout = new FileOutputStream(path);
    		byte[] bytes = new byte[1024];
    		
            for (int c = zip.read(bytes); c != -1; c = zip.read(bytes)) {
              fout.write(bytes,0, c);
            }
            zip.closeEntry();
            fout.close();    		
    	}
    }
    

    
	/**
	 * SDCARD?
	 * @return
	 */
//	static public boolean hasSDCard() {
//		try {
//			File f = new File(WOLF_FOLDER);
//			
//			// Does /sdcard/doom exist?
//			if ( f.exists()) return true;
//			
//			// Can we write into it?
//			return  f.mkdir();
//		} catch (Exception e) {
//			System.err.println(e.toString());
//			return false;
//		}
//	}
    
	/**
	 * Ckech 4 sdcard. diaply a msg box if not found
	 * @return
	 */
//	public static  boolean checkSDCard(Context ctx) {
//		boolean sdcard = hasSDCard();
//		
//		if ( ! sdcard) {
//			DialogTool.MessageBox(ctx, "No SDCARD", "An SDCARD is required to store game files.");
//			return false;
//		}		
//		return true;
//	}
	
	public static void installGame(Context ctx, String directory, File dir) throws IOException {
		Log.d(TAG, "Installing game " + GAME_ZIP + " in " + dir);
		unzip(ctx.getAssets().open(GAME_ZIP), dir);
		
		// install saved game files
		try {
			 for (int i = 0; i < 10; i++) {
				 String name = directory + File.separator + "savegam" + i + ".wl1";
				 File file = new File(name);
				 if (file.exists())
					 continue;
				 FileOutputStream os = new FileOutputStream(file);
				 Log.d(TAG, "installing " + name);
				 if (copyFile(ctx.getAssets().open("savegam.wl1"), os) == -1)
					 file.delete();
			 }
		} catch (Exception e) {
		     e.printStackTrace();
		}


	}
	
	

	public static int copyFile(InputStream in, OutputStream out)
		    throws IOException {
			byte[] bytes = new byte[2048];

			try {
				for (int c = in.read(bytes); c != -1; c = in.read(bytes)) {
					out.write(bytes, 0, c);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			return 0;
	}

	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
		}
	}
}
