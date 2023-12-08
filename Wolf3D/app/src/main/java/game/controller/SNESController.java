package game.controller;

import wolf.jni.Natives;
import wolf.util.DialogTool;
import wolf.util.ScanCodes;

import com.peterpan.wolfs3d.WolfLauncher;
import com.peterpan.wolfs3d.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class SNESController  
{
	public static final String TAG = "SNESC";
	private Activity mView;
	private WolfLauncher wL;
	
	private ControllerListener mListener;
	

	public SNESController(Context context) {
		mView = (Activity)context;
		wL = (WolfLauncher)context;
		init();
	}

	public SNESController(Context context, AttributeSet attrs) {
		mView = (Activity)context;
		init();
	}
	public SNESController(Context context, AttributeSet attrs, int style) {
		mView = (Activity)context;
		init();
	}

	public void setListener (ControllerListener l) {
		mListener = l;
	}
	
	private void init() {
        //LayoutInflater factory = LayoutInflater.from(mContext);
        //mView =  factory.inflate(R.layout.wolf, null);
		setupControls();

	}

    private void setupControls() 
    {

        //trick
        mView.findViewById(R.id.btn_trick).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_UP) {
					checkTrick (true);
				}
				return true;
			}
        });
  
        //exit
        mView.findViewById(R.id.btn_exit).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_UP) {
					DialogTool.showExitDialog(mView);
				}
				return true;
			}
        });
  
        //menu
        mView.findViewById(R.id.btn_menu).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN, ScanCodes.sc_Escape); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP, ScanCodes.sc_Escape); 
				}
				return true;
			}
        });
  
        //quicksave
        mView.findViewById(R.id.btn_quicksave).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN, 0x42); //F8
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP, 0x42); // F8
				}
				return true;
			}
        });
  
  
        // switch guns 1
        mView.findViewById(R.id.btn_swguns1).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				Log.d(TAG,"presssed SG");
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN,  ScanCodes.sc_7); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP,  ScanCodes.sc_7);
				}
				return true;
			}
        });
  
        // switch guns 2
        mView.findViewById(R.id.btn_swguns2).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				Log.d(TAG,"presssed SG");
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN,  ScanCodes.sc_7); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP,  ScanCodes.sc_7);
				}
				return true;
			}
        });
  
        // switch guns 3
        mView.findViewById(R.id.btn_swguns3).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				Log.d(TAG,"presssed SG");
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN,  ScanCodes.sc_7); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP,  ScanCodes.sc_7);
				}
				return true;
			}
        });
  

        // 1
        mView.findViewById(R.id.btn_trans_1).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				Log.d(TAG,"presssed 1");
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN,  ScanCodes.sc_1); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP,  ScanCodes.sc_1);
				}
				return true;
			}
        });
  
        // 2
        mView.findViewById(R.id.btn_trans_2).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				Log.d(TAG,"presssed 2");
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN, ScanCodes.sc_2); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP, ScanCodes.sc_2);
				}
				return true;
			}
        });
  
        
        // 3
        mView.findViewById(R.id.btn_trans_3).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN, ScanCodes.sc_3); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP, ScanCodes.sc_3);
				}
				return true;
			}
        });
  
        // 4
        mView.findViewById(R.id.btn_trans_4).setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_DOWN) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_DOWN, ScanCodes.sc_4); 
				}
				else
				if ( action == MotionEvent.ACTION_UP) {
					Natives.sendNativeKeyEvent(MotionEvent.ACTION_UP, ScanCodes.sc_4);
				}
				return true;
			}
        });
  
        
    	//up
    	mView.findViewById(R.id.btn_up).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				//final ImageButton b = (ImageButton)v;
				
				int action = evt.getAction();
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_u1);
					//System.out.println("Up pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP); // ControllerListener.B_UP);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_u0);
					//System.out.println("Up released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP); // ControllerListener.B_UP);
				}
				return true;
			}
        });
        // down
    	mView.findViewById(R.id.btn_down).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				//final ImageButton b = (ImageButton)v;
				int action = evt.getAction();
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_d1);
					//System.out.println("Down pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN); // ControllerListener.B_DOWN);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_d0);
					//System.out.println("Down released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN);// ControllerListener.B_DOWN);
				}
				return true;
			}
        });
        // left
    	mView.findViewById(R.id.btn_left).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_l1);
					//System.out.println("Left pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT); // ControllerListener.B_LEFT);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_l0);
					//System.out.println("Left released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT);
				}
				return true;
			}
        });
        // Right
    	mView.findViewById(R.id.btn_right).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_r1);
					//System.out.println("Right pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_r0);
					//System.out.println("Right released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT);
				}
				return true;
			}
        });
        // select
    	mView.findViewById(R.id.btn_select).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_select1);
					//System.out.println("Select pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_select0);
					//System.out.println("Select released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER);
				}
				return true;
			}
        });
        // start
    	mView.findViewById(R.id.btn_start).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_start1);
					System.out.println("Start pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_start0);
					System.out.println("Start released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE);
				}
				return true;
			}
        });

        // X
    	mView.findViewById(R.id.btn_X).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_x1);
					//System.out.println("X pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_x0);
					//System.out.println("X released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_X);
				}
				return true;
			}
        });
        // Y
    	mView.findViewById(R.id.btn_Y).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_y1);
					//System.out.println("Y pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_Y);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_y0);
					//System.out.println("Y released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_Y);
				}
				return true;
			}
        });

        // A
    	mView.findViewById(R.id.btn_A).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_a1);
					//System.out.println("A pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_a0);
					//System.out.println("A released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_A);
				}
				return true;
			}
        });
        // B
    	mView.findViewById(R.id.btn_B).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent evt) {
				int action = evt.getAction();
				//final ImageButton b = (ImageButton)v;
				
				if ( action == MotionEvent.ACTION_DOWN) {
					//b.setImageResource(R.drawable.snes_b1);
					//System.out.println("B pressed");
					sendEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_B);
				}
				else if ( action == MotionEvent.ACTION_UP) {
					//b.setImageResource(R.drawable.snes_b0);
					//System.out.println("B released");
					sendEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_B);
				}
				return true;
			}
        });
    }

    /**
     * Send an event to the {@link ControllerListener}
     * @param state: Up (MotionEvent.ACTION_UP) or down (MotionEvent.ACTION_DOWN)
     * @param btn Android {@link KeyEvent}
     */
    private void sendEvent (int state, int btn) {
    	Log.d(TAG,"se btn = " + btn);
    	if (mListener != null) {
    		if ( state == MotionEvent.ACTION_UP)
    			mListener.ControllerUp(btn);
    		else
    			mListener.ControllerDown(btn);
    	}
    }

    public void checkTrick(boolean option) {
    	if (option) {
    		Log.d(TAG,"TRICKED!");
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, 'i');
    		//WolfTools.sleep (500);
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, 'l');
    		//WolfTools.sleep (500);
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYDOWN, 'm');
    		//WolfTools.sleep (1000);
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYUP, 'I');
    		//WolfTools.sleep (500);
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYUP, 'L');
    		//WolfTools.sleep (500);
    		//Natives.sendNativeKeyEvent(Natives.EV_KEYUP, 'M');
    		Natives.cheat(1);
    	}
    	else {
    		Log.d(TAG,"NOT TRICKED!");
    	}
    }


    
}
