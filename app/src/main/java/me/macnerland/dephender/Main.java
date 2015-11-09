package me.macnerland.dephender;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
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
        SharedPreferences pref = this.getPreferences(MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        menu = new MenuRenderer(this, pref);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//runs fullscreen
        setContentView(R.layout.activity_main);

        AdView mAdView= (AdView) findViewById(R.id.gadView);
        AdRequest adRequest = new AdRequest.Builder().build();//do not use  for testing
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        //glSurfaceView = new MenuSurface(this);
        glSurfaceView = (MenuSurface)findViewById(R.id.mSurfaceView);
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2)
        {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(menu);//It is wicked important to do this here!!!!!!
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
