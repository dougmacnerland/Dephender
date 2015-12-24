package me.macnerland.dephender;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Main extends Activity {
    private MenuSurface glSurfaceView;

    /*When the program is first run, the Activity is initialized*/
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        /*Create an activity*/
        super.onCreate(savedInstanceState);

        /*Request fullscreen, cull menu bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

	/*Set the android draw content to main activity layout
 * 	Specified at res/layout/activity_main*/
        setContentView(R.layout.activity_main);

	/*Initialize new AdView, mAdview, from the gadView ID in the
 * 	activity_main layout*/
        AdView mAdView = (AdView) findViewById(R.id.gadView);
        
        /*Request an ad*/
	/*Actual deployment*/
        AdRequest adRequest = new AdRequest.Builder().build();
	/*Testing on Emulator*/
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        
	/*Load the request into the ad*/
        mAdView.loadAd(adRequest);

        glSurfaceView = (MenuSurface)findViewById(R.id.mSurfaceView);
    }
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        glSurfaceView.onDestroy();
        super.onDestroy();
    }
}
