package wolf.util;

import android.util.Log;



public class LibraryLoader {

	static final String TAG = "LibLoader";
	
	static public boolean load (String name) {
		boolean result = false;
		final String LD_PATH = System.getProperty("java.library.path");
		
		Log.e(TAG, "Trying to load library " + name + " from LD_PATH: " + LD_PATH);
		
		try {
			System.loadLibrary(name);
			Log.e(TAG, "Library loaded");
			result = true;
		} catch (UnsatisfiedLinkError e) {
			Log.e(TAG, e.toString());
		}
		
//		Log.d(TAG, "Trying to load " + name + " using its absolute path.");
//		System.load(name);
		return result;
	}
}
