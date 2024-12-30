package com.peterpan.wolfs3d;

import game.controller.ControllerListener;
import game.controller.SNESController;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import wolf.audio.AudioManager;
import wolf.jni.Natives;
import wolf.util.DialogTool;
import wolf.util.LibraryLoader;
import wolf.util.ScanCodes;
import wolf.util.WolfTools;

public class WolfActivity extends BaseActivity
	implements Natives.EventListener, ControllerListener 
{
	private static final String TAG = "Wolf3D";
	
	public static final Handler mHandler = new Handler();

	private static boolean mGameStarted = false;

	private static Bitmap mBitmap;
	private static ImageView mView;
	
	// Audio Manager
	private AudioManager mAudioMgr;
	
	// Sound? ( yes by default)
	private boolean mSound = true;
	
	private String mGameDir = WolfTools.WOLF_FOLDER ;
	
	public WolfActivity dClient = this;
	
	// Navigation
	public static enum eNavMethod  {KBD, PANEL} ; //, ACC};
	public static eNavMethod mNavMethod = eNavMethod.KBD; 
	
	// Screen size
	public int height;
	public int pad_height;
	public int width;
	
	// Virtual Screen Size
	public final int HEIGHT     = 480;
	public final int PAD_HEIGHT = 240;
	public final int WIDTH      = 320;
	
	
	public boolean   portrait   = false;
	public int       api_level  = 0;
	
	private SNESController controller;
	
    /** 
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d (TAG,"Began onCreate");
        
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // No title
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        setContentView(R.layout.wolf);
        
        mView = (ImageView)findViewById(R.id.wolf_iv);

        api_level =  android.os.Build.VERSION.SDK_INT;
        Log.d (TAG,"API Level Detected = " + api_level);


		Log.d(TAG,"Old way");
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.d (TAG,"Screen detected is " + dm.widthPixels + "," + dm.heightPixels);
		if (dm.widthPixels < dm.heightPixels ) {
			portrait   = true;
			width      = dm.widthPixels;
			height     = dm.heightPixels;
			pad_height = (int) (width * 0.75);
		}
		else {
			portrait   = false;
			width      = dm.heightPixels;
			height     = dm.widthPixels;
			pad_height = (int) (width * 0.75);
		}

        if (isPortrait())
        	Log.d(TAG,"Portrait");
        else
        	Log.d(TAG,"Landscape");
        
    	Log.d (TAG,"Screen height is " + height + " width is " + width + "PH is " + pad_height ); 

    	initController();
                
        if (mGameStarted) {
        	setGameUI();
        	return;
        }

		mGameDir = App.packageFolder + "files" + File.separator;
		Log.d(TAG, "Running game from files installed on " + mGameDir);


        // Initial Image size
        Log.e(TAG,"Will set image size");
        if (isPortrait()) {
            setImageSize(2560, 1280);
            mNavMethod = eNavMethod.PANEL;
        }
        else {
            setImageSize(height, width);
            mNavMethod = eNavMethod.KBD;
        }
        
        // Start Game
        startGame(mGameDir, WolfTools.GAME_ID, true);
    }

    private void initController() {
    	Log.e(TAG,"InitController");
    	
    	if ( !isPortrait() ) {
    		setImageSize(height, width);
        	if ( controller == null) {
        		controller = new SNESController(this);
        		controller.setListener(this);
        	}
        	
        	findViewById(R.id.snes).setVisibility(View.VISIBLE);
        	findViewById(R.id.other_ctls).setVisibility(View.VISIBLE);
        	findViewById(R.id.another_ctls).setVisibility(View.VISIBLE);
        	findViewById(R.id.trick_ctls).setVisibility(View.VISIBLE);
        	mNavMethod = eNavMethod.PANEL;
        	
       		relocateAndResize (R.id.btn_up    ,48,48);
       		relocateAndResize (R.id.btn_down  ,48,48);
       		relocateAndResize (R.id.btn_left  ,48,48);
       		relocateAndResize (R.id.btn_right ,48,48);
       		relocateAndResize (R.id.btn_menu  ,48,48);
       		relocateAndResize (R.id.btn_select,48,48);
       		relocateAndResize (R.id.btn_start ,48,48);
       		relocateAndResize (R.id.btn_X     ,48,48);
       		relocateAndResize (R.id.btn_Y     ,48,48);
       		relocateAndResize (R.id.btn_A     ,48,48);
       		relocateAndResize (R.id.btn_B     ,48,48);
       		
       		relocateAndResize (R.id.btn_trans_1  ,48,48);
       		relocateAndResize (R.id.btn_trans_2  ,48,48);
       		relocateAndResize (R.id.btn_trans_3  ,48,48);
       		relocateAndResize (R.id.btn_trans_4  ,48,48);
       		
       		relocateAndResize (R.id.btn_trick    ,48,48);
       		relocateAndResize (R.id.btn_exit     ,48,48);
       		relocateAndResize (R.id.btn_help     ,48,48);
       		relocateAndResize (R.id.btn_quicksave,48,48);
    		return;
    	}
    	
    	// portrait
    	
    	setImageSize(2560, 1280);
    	
        // init controller 
    	if ( controller == null) {
    		controller = new SNESController(this); 
    		controller.setListener(this);
    	}
    	// resize snes layout
   		resizeLayout (R.id.snes,pad_height,width);
   		
   		// relocate/resize objects within it
   		relocateAndResize (R.id.btn_up    , 60, 48,48,48);
   		relocateAndResize (R.id.btn_down  ,141, 48,48,48);
   		relocateAndResize (R.id.btn_left  , 99,  0,48,48);
   		relocateAndResize (R.id.btn_right , 99, 96,48,48);
   		relocateAndResize (R.id.btn_menu  ,192,  2,48,48);
   		relocateAndResize (R.id.btn_select,192,110,48,48);
   		relocateAndResize (R.id.btn_start ,192,160,48,48);
   		relocateAndResize (R.id.btn_X     , 63,223,48,48);
   		relocateAndResize (R.id.btn_Y     , 99,174,48,48);
   		relocateAndResize (R.id.btn_A     , 99,270,48,48);
   		relocateAndResize (R.id.btn_B     ,141,223,48,48);
   		
   		relocateAndResize (R.id.btn_trans_1, 3, 12,48,48);
   		relocateAndResize (R.id.btn_trans_2, 3, 72,48,48);
   		relocateAndResize (R.id.btn_trans_3, 3,200,48,48);
   		relocateAndResize (R.id.btn_trans_4, 3,264,48,48);
   		
   		// resize image layout
   		resizeLayout (R.id.top_panel, pad_height, width);
   		
   		// resize buttons on image layout
   		relocateAndResize (R.id.btn_exit     ,48,48);
   		relocateAndResize (R.id.btn_trick    ,48,48);
   		relocateAndResize (R.id.btn_quicksave,48,48);

    	findViewById(R.id.snes).setVisibility(View.VISIBLE);
    	findViewById(R.id.other_ctls).setVisibility(View.VISIBLE);
    	findViewById(R.id.another_ctls).setVisibility(View.VISIBLE);
    	findViewById(R.id.trick_ctls).setVisibility(View.VISIBLE);
   		
    	findViewById(R.id.top_panel).invalidate();
    	findViewById(R.id.snes).invalidate();
    	
    	mNavMethod = eNavMethod.PANEL;
    }
    
    public void resizeLayout (int l, int h, int w) {
    	findViewById(l).getLayoutParams().height = h;
    	findViewById(l).getLayoutParams().width  = w;
    	Log.d(TAG,"Layout resized to " + w + "," + h);
    }
    
    public void relocateAndResize (int l, int y, int x, int h, int w) {
    	 View v = findViewById(l);
    	 
    	 // adjust positions 
    	 h = (h * pad_height ) / PAD_HEIGHT;
    	 y = (y * pad_height ) / PAD_HEIGHT;
    	 
    	 w = (w * width) / WIDTH;
    	 x = (x * width) / WIDTH;
    	 
    	 // relocate
     	 ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
         p.setMargins(x,y,0,0);
    	
         absoluteResize (l,h,w);
    }

    public void relocateAndResize (int l, int h, int w) {
   	    float p = w / h;
   	    Log.d (TAG,"Resized from " +w + "," + h);
   	    // adjust positions
   	    w = (w * width) / WIDTH;
   	    h = (int) (w * p);
   	    absoluteResize (l,h,w);
   }

    public void absoluteResize (int l, int h, int w) {
   	    View v = findViewById(l);
        // resize
        LayoutParams lp = v.getLayoutParams();
        lp.height = h; lp.width=w;
        v.setLayoutParams(lp);
        Log.d (TAG,"Resized to " +w + "," + h);
    }
    
    public boolean isPortrait() {
    	return portrait;
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }

    
    /**
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	//menu.add(0, 0, 0, "Toggle Screen").setIcon(R.drawable.view);
    	//menu.add(0, 1, 1, "Navigation").setIcon(R.drawable.nav);
    	menu.add(0, 2, 2, "Exit").setIcon(R.drawable.exit);

    	return true;
    }
    
    /**
     * Menu actions
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	
    	switch (item.getItemId()) {
		case 1:
			// navigation method
			DialogTool.showNavMethodDialog(this); //, mSensorManager, mSensor );
			return true;
    	
		case 2:
			// Exit
			WolfTools.hardExit(0);
			return true;
			
    	}
    	return false;
    }

    /**
     * This will set the size of the image view
     * @param w
     * @param h
     */
    private void setImageSize(int w, int h ) {
    	LayoutParams lp = mView.getLayoutParams();
    	lp.width = w;
    	lp.height = h;
    }
    
    /**
     * Key down
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// Ignore MENU
    	if (keyCode == KeyEvent.KEYCODE_MENU) 
    		return false;

    	try {
    		int sym = ScanCodes.keySymToScancode(keyCode);
    		// this is necessary to make the 1aqw keys behave like arrows
    		/*if (sym == 2) sym = 72;
    		if (sym == 30) sym = 80;
    		if (sym == 16) sym = 75;
    		if (sym == 17) sym = 77;*/
    		Log.d(TAG,"okd sym = " + sym);
    		Natives.keyPress(sym);
    		
		} catch (UnsatisfiedLinkError e) {
			System.err.println(e.toString());
		}
    	return false;
    }
    
    /**
     * Key Released
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	// Ignore MENU
    	if (keyCode == KeyEvent.KEYCODE_MENU) 
    		return false;
    	
    	try {
    		int sym = ScanCodes.keySymToScancode(keyCode);
    		// this is necessary to make the 1aqw keys behave like arrows
    		/*if (sym == 2) sym = 72;
    		if (sym == 30) sym = 80;
    		if (sym == 16) sym = 75;
    		if (sym == 17) sym = 77;*/
    		Log.d(TAG,"okun sym = " + sym);
    		Natives.keyRelease(sym);
    		
		} catch (UnsatisfiedLinkError e) {
			System.err.println(e.toString());
		}
    	return false;
    }
    
    /**
     * Touch eventcopcopid
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	try {
    		// This was a bug - everywhere t would fire. Commented out the sending of strokes.
        	if ( event.getAction() == MotionEvent.ACTION_DOWN) {
            	// Fire on tap R-CTL
        		Log.d(TAG,"ote-action down");
            	//Natives.keyPress(ScanCodes.sc_Control);  
        	}
        	else if ( event.getAction() == MotionEvent.ACTION_UP) {
        		Log.d(TAG,"ote-action up");
            	//Natives.keyRelease(ScanCodes.sc_Control);  
        	}
        	else if ( event.getAction() == MotionEvent.ACTION_MOVE) {
        		Log.d(TAG,"ote-actionmove");
        		// Motion event
        	}
        	return true;
		} 
    	catch (UnsatisfiedLinkError e) {
			// Should not happen!
    		Log.e(TAG, e.toString());
    		return false;
		}
    }

    /**
     * Trackball == Mouse
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) 
    {
    	return false;
    }
    
	/**
	 * 
	 * @param baseDir
	 * @param game
	 * @param portrait
	 */
    private void startGame(String baseDir, String game, boolean portrait) 
    {
    	File dir = new File(baseDir);
    	
    	if (! dir.exists() ) {
    		if (!dir.mkdir() ) {
        		MessageBox("Invalid game base folder: " + baseDir);
        		return;
    		}
    	}
    	
    	Log.d(TAG, "Start game base dir: " + baseDir + " game=" + game + " port:" + portrait);
    	
        // Args
        final String argv[] = {"wolf3d", game, "basedir" , baseDir}; //, "x2"};
        
		// Load lib 
		if (!loadLibrary()) {
			// this should not happen
			return;
		}

        Natives.setListener(this);
        
        // get rid of imageview background
        setGameUI();
        
        // Audio?
        if ( mSound)
        	mAudioMgr = AudioManager.getInstance(this);
        
        new Thread(new Runnable() {
			public void run() {
				mGameStarted = true;
		        Natives.WolfMain(argv);
			}
		}).start();
    }
    
    
    
	/**
	 * Hide main layout/show image vire (game)
	 */
	private void setGameUI() {
		switch (mNavMethod) {
		case KBD:
//			findViewById(R.id.snes).setVisibility(View.GONE);
//			findViewById(R.id.other_ctls).setVisibility(View.GONE);
//			findViewById(R.id.another_ctls).setVisibility(View.GONE);			
			break;
		case PANEL:
//			findViewById(R.id.snes).setVisibility(View.VISIBLE);
//			findViewById(R.id.other_ctls).setVisibility(View.VISIBLE);
//			findViewById(R.id.another_ctls).setVisibility(View.VISIBLE);			
			break;
//		case ACC:
//			
//			break;
		}
	}

	
    /******************************************************
     * Native Events
     ******************************************************/
	@Override
	public void OnSysError(final String text) 
	{
		
		mHandler.post(new Runnable() {
			public void run() {
				MessageBox("System Message", text);
			}
		});

		// Wait for the user to read the box
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			
		}
		// Ouch !
		WolfTools.hardExit(-1);
	}

	@Override
	public void OnImageUpdate(int[] pixels, int x ,int y, int w, int h) {
		mBitmap.setPixels(pixels, 0, w, x, y, w, h);
		
		mHandler.post(new Runnable() {
			public void run() {
				try {
					mView.setImageBitmap(mBitmap);
					
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void OnInitGraphics(int w, int h) {
		Log.d(TAG, "OnInitGraphics creating Bitmap of " + w + " by " + h);
		mBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
	}

	@Override
	public void OnMessage(String text) {
		System.out.println("** Wolf Message: " + text);
	}
	
	
    /**
     * Load JNI library. Lib must exist in /data/data/APP_PKG/files
     */
    private boolean loadLibrary() {
		
		Log.e(TAG, "Loading JNI library lib" + WolfTools.WOLF_LIB + ".so") ;//lib);
		LibraryLoader.load(WolfTools.WOLF_LIB); //lib.getAbsolutePath());
		
		// Listen for events
		Natives.setListener(this);
		return true;
    }
	
	void MessageBox (String text) {
		WolfTools.MessageBox(this, getString(R.string.app_name), text);
	}

	void MessageBox (String title, String text) {
		WolfTools.MessageBox(this, title, text);
	}
 

	@Override
	public void OnStartSound(int idx) {
		if ( mSound && mAudioMgr == null)
			Log.e(TAG, "Bug: Audio Mgr is NULL but sound is enabled!");
		
		try {
			if ( mSound && mAudioMgr != null)
				mAudioMgr.startSound( idx); 
			
		} catch (Exception e) {
			Log.e(TAG, "OnStartSound: " +  e.toString());
		}
	}
	
	public void OnStartMusic(int idx) {
		if ( mSound && mAudioMgr == null)
			Log.e(TAG, "Bug: Audio Mgr is NULL but sound is enabled!");
		
		try {
			if ( mSound && mAudioMgr != null)
				mAudioMgr.startMusic(this, idx); 
			
		} catch (Exception e) {
			Log.e(TAG, "OnStartSound: " +  e.toString());
		}
	}
	
	

	@Override
	public void ControllerDown(int btnCode) {
		switch (btnCode) {
		case KeyEvent.KEYCODE_Y:
			Log.d(TAG,"Y");
			// strafe left
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_Alt);
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_LeftArrow);
			break;

		case KeyEvent.KEYCODE_X:
			// strafe right
			Log.d(TAG,"X");
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_Alt);
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_RightArrow);
			break;

		case KeyEvent.KEYCODE_B:
			// Fire
			Log.d(TAG,"B");
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_Control);
			break;

		case KeyEvent.KEYCODE_A:
			// Rshift (Run)
			Log.d(TAG,"B");
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.sc_RShift);
			break;
			
		default:
			Log.d(TAG,"default. key = " + ScanCodes.keySymToScancode(btnCode) + " btn code = " + btnCode);
			Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, ScanCodes.keySymToScancode(btnCode));
			break;
		}
	}

	@Override
	public void ControllerUp(int btnCode) {
		switch (btnCode) {
		case KeyEvent.KEYCODE_Y:
			// strafe left
			Log.d(TAG,"Up-Y");
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_Alt);
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_LeftArrow);
			break;

		case KeyEvent.KEYCODE_X:
			// strafe right
			Log.d(TAG,"Up-X");
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_Alt);
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_RightArrow);
			break;

		case KeyEvent.KEYCODE_B:
			// Fire
			Log.d(TAG,"Up-B");
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_Control);
			break;
			
		case KeyEvent.KEYCODE_A:
			// shift (Run)
			Log.d(TAG,"Up-A");
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.sc_RShift);
			break;
			
		default:
			Log.d(TAG,"Updefault. key = " + ScanCodes.keySymToScancode(btnCode) + " btn code = " + btnCode);
			Natives.sendNativeKeyEvent(Natives.EV_KEYUP, ScanCodes.keySymToScancode(btnCode));
			break;
		}
	}
	
}