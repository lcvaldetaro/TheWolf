package wolf.util;

import com.peterpan.wolfs3d.WolfLauncher;
import com.peterpan.wolfs3d.WolfLauncher.eNavMethod;

import com.peterpan.wolfs3d.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;



public class DialogTool 
{

	/**
	 * Select a nav method dialog
	 * @param ctx
	 */
	static public void showNavMethodDialog(final Context ctx) { //, final SensorManager mSensorManager, final SensorListener mSensor) {
		final String[] mListItems = new String[] {"Keyboard", "Game Pad" };
		
		AlertDialog dialog = new AlertDialog.Builder(ctx)
	        .setIcon(R.drawable.icon)
	        .setTitle("Navigation Method")
	        .setItems(mListItems, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	boolean port = ((WolfLauncher)ctx).isPortrait();
                	
                	if (!port) {
                        final View v = ((Activity)ctx).findViewById(R.id.snes);
                        final View v1 = ((Activity)ctx).findViewById(R.id.other_ctls);
                        final View v2 = ((Activity)ctx).findViewById(R.id.another_ctls);

                        switch (which) {
                        	case 0:
    	                		v.setVisibility(View.GONE);
    	                		v1.setVisibility(View.GONE);
    	                		v2.setVisibility(View.GONE);
    	                		WolfLauncher.mNavMethod = eNavMethod.KBD;
                        		break;
                        	case 1:
    	                		v.setVisibility(View.VISIBLE);
    	                		v1.setVisibility(View.VISIBLE);
    	                		v2.setVisibility(View.VISIBLE);
    	                		WolfLauncher.mNavMethod = eNavMethod.PANEL;
    	                		break;
                        }
                	}
                	else {
	                    final View v = ((Activity)ctx).findViewById(R.id.snes);
                        final View v1 = ((Activity)ctx).findViewById(R.id.other_ctls);
                        final View v2 = ((Activity)ctx).findViewById(R.id.another_ctls);
	                    switch (which) {
	                    	case 0:
	                    		// KBD Hide ctls
	                			Toast(ctx, "Cannot use keyboard in portrait mode");
	                    		break;
							case 1:
		                		WolfLauncher.mNavMethod = eNavMethod.PANEL;
		                		// show panel ctls
		                		v.setVisibility(View.VISIBLE);
    	                		v1.setVisibility(View.VISIBLE);
    	                		v2.setVisibility(View.VISIBLE);
		                		break;
						}
                	}
                }
            })
            .create();
		dialog.show();
	}

	
	static public void showExitDialog(final Context ctx) {
		final String[] mListItems = new String[] { "Exit", "Don't Exit" };

		AlertDialog dialog = new AlertDialog.Builder(ctx)
				.setIcon(R.drawable.icon).setTitle("Are you sure?")
				.setItems(mListItems, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:
							WolfTools.hardExit(0);
							break;

						case 1:
							break;
						}
					}
				}).create();
		dialog.show();
	}


	
	
	/**
	 * Load spinner
	 * @param ctx
	 * @param spinner
	 * @param resID
	 * @param idx
	 */
	public static void loadSpinner(Context ctx, Spinner spinner, int resID, int idx) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, resID
				, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(idx);
	}
	
	/**
	 * MessageBox
	 * @param ctx
	 * @param text
	 */
	public static void MessageBox (Context ctx, String text) {
		MessageBox(ctx, ctx.getString(com.peterpan.wolfs3d.R.string.app_name), text);
	}

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
	 * Messagebox
	 * @param ctx
	 * @param text
	 */
	public static void PostMessageBox (final Context ctx, final String text) {
		WolfLauncher.mHandler.post(new Runnable() {
			public void run() {
				MessageBox(ctx, ctx.getString(R.string.app_name), text);
			}
		});
	}
	
    /**
     * Create an alert dialog
     * @param ctx App context
     * @param message Message
     * @return
     */
    static public AlertDialog createAlertDialog (Context ctx, String title, String message) {
        return new AlertDialog.Builder(ctx)
	        .setIcon(com.peterpan.wolfs3d.R.drawable.icon)
	        .setTitle(title)
	        .setMessage(message)
	        .create();
    }

    static public AlertDialog createAlertDialog (Context ctx, String title, String message, View view) {
        return new AlertDialog.Builder(ctx)
	        .setIcon(com.peterpan.wolfs3d.R.drawable.icon)
	        .setTitle(title)
	        .setView(view)
	        .setMessage(message)
	        .create();
    }
    
    /**
     * Launch web browser
     */
    static public void launchBrowser(Context ctx, String url) {
    	ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));     	
    }
	
    public static void Toast( final Context ctx, final String text) {
	    Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }

    public static void Toast( Handler handler ,final Context ctx, final String text) {
    	handler.post(new Runnable() {
			public void run() {
				Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
			}
		});
    }
    
}
