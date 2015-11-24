package me.macnerland.dephender;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Doug on 8/11/2015.
 * Personal project
 */

public class MenuSurface extends GLSurfaceView{
    private MenuRenderer menu;

    public MenuSurface(Context context){
        super(context);

        setEGLContextClientVersion(2);

        menu = new MenuRenderer(context);
        setRenderer(menu);

        /*Interfaces are like objects that contain classes to override*/
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e != null) {
                    float x = (((e.getX() / ((float) v.getWidth())) * 2) - 1);
                    float y = -((e.getY() / ((float) v.getHeight())) * 2) + 1;
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        menu.onTouch(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public MenuSurface(Context context, AttributeSet attrSet){
        super(context, attrSet);

        setEGLContextClientVersion(2);

        menu = new MenuRenderer(context);
        setRenderer(menu);

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e != null) {
                    float x = (((e.getX() / ((float) v.getWidth())) * 2) - 1);
                    float y = -((e.getY() / ((float) v.getHeight())) * 2) + 1;
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        menu.onTouch(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void onPause(){
        menu.onPause();
    }

    public void onDestroy(){
        menu.onDestroy();
    }
}
