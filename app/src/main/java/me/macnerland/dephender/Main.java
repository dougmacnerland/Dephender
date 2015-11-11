package me.macnerland.dephender;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Main extends Activity {
    private MenuSurface glSurfaceView;
    private MenuRenderer menu;


    /*When the program is first run, the Activity is initialized*/
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        /*Create an activity*/
        super.onCreate(savedInstanceState);
	
	/*Initialize OpenGLES renderer
 * 	with variables Activity and SharedPreferences*/
        menu = new MenuRenderer(this);

        /*Request fullscreen, cull menu bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

	/*Set the android draw content to main activity layout
 * 	Specified at res/layout/activity_main*/
        setContentView(R.layout.activity_main);

	/*Initialize new AdView, mAdview, from the gadView ID in the
 * 	activity_main layout*/
        AdView mAdView= (AdView) findViewById(R.id.gadView);
        
        /*Request an ad*/
	/*Actual deployment*/
        AdRequest adRequest = new AdRequest.Builder().build();
	/*Testing on Emulator*/
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        
	/*Load the request into the ad*/
        mAdView.loadAd(adRequest);

        //glSurfaceView = new MenuSurface(this);
        glSurfaceView = (MenuSurface)findViewById(R.id.mSurfaceView);
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2)
        {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(menu);//It is important to do this here!
            /*Add in the touch interface*/
            glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent e) {
                    if(e!=null){
                        float x = (((e.getX()/((float)v.getWidth()))*2)-1);
                        float y = -((e.getY()/((float)v.getHeight()))*2)+1;
                        if(e.getAction()== MotionEvent.ACTION_DOWN){
                            menu.touch(x, y);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        else
        {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        menu.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        menu.onDestroy();

    }
}
