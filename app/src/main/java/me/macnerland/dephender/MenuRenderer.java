package me.macnerland.dephender;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Doug on 8/12/2015.
 * This was tough to write
 */
public class MenuRenderer implements GLSurfaceView.Renderer{
    private float[] mModelMatrix = new float[16];//Model
    private float[] mViewMatrix = new float[16];//View
    private float[] mProjectionMatrix = new float[16];//Projection
    private float[] mMVPMatrix = new float[16];//Model, View, Projection matrix

    private int programHandle;
    private static Attacker[] invader = new Attacker[10];
    private static Rectangle back;//do these need to be static?
    private static Rectangle newGame;
    private static Rectangle hiScore;

    private static Rectangle target;
    private boolean yOsc;
    private boolean xOsc;

    private static Rectangle currentScore;
    private static Rectangle pauseMenu;
    private static Rectangle pauseButton;
    private static int GameState;
    private static float Ratio;
    private boolean newHigh;

    private SharedPreferences pref;
    private int high;

    private Counter HighScore;
    private Counter score;

    private int pauseButtonHandle;
    private int pauseMenuHandle;
    private int whiteHandle;
    private int illiniHandle;
    private int hiScoreHandle;
    private int currentScoreHandle;
    private int textHandle;
    private int newGameHandle;

    private float left;
    private float right;
    private float nRatio;
    private Context cont;

    public MenuRenderer(Context c){
        pref = c.getSharedPreferences("score", 0);
        cont = c;
        GameState = 0;
        high = pref.getInt("highScore", 0);
        xOsc = false;
        yOsc = false;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config){
// Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 2.0f;
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -10.0f;
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = Gleshelp.getVertexShader(1);

        final String fragmentShader = Gleshelp.getFragmentShader(1);

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        GLES20.glShaderSource(vertexShaderHandle, vertexShader);
        GLES20.glCompileShader(vertexShaderHandle);

        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
        GLES20.glCompileShader(fragmentShaderHandle);


        programHandle = GLES20.glCreateProgram();

        GLES20.glAttachShader(programHandle, vertexShaderHandle);
        GLES20.glAttachShader(programHandle, fragmentShaderHandle);

        GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
        GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
        GLES20.glLinkProgram(programHandle);

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
        newGameHandle = Gleshelp.loadTexture(cont, R.drawable.start);
        whiteHandle = Gleshelp.loadTexture(cont, R.drawable.white);
        illiniHandle = Gleshelp.loadTexture(cont, R.drawable.chief_illiniwek);
        textHandle = Gleshelp.loadTexture(cont, R.drawable.arial_small);
        hiScoreHandle = Gleshelp.loadTexture(cont, R.drawable.high_score);
        currentScoreHandle = Gleshelp.loadTexture(cont, R.drawable.score);
        pauseButtonHandle = Gleshelp.loadTexture(cont, R.drawable.pause_a);
        pauseMenuHandle = Gleshelp.loadTexture(cont, R.drawable.paused);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height){
        GLES20.glViewport(0, 0, width, height);
        Ratio = (float) width / height;
        nRatio = Ratio;
        if(nRatio>1.0f){
            nRatio = 1/nRatio;
        }
        nRatio = nRatio*nRatio;

        left = -Ratio;
        right = Ratio;

        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 0.0f;
        final float far = 5.0f;

        if(back == null) {
            back = new Rectangle(programHandle, new float[]{left, -1.0f, -1.0f,
                    right, -1.0f, -1.0f,
                    right, 1.0f, -1.0f,
                    left, 1.0f, -1.0f}, 0, whiteHandle);
        }else{
            back.resetVerts(new float[]{left, -1.0f, -1.0f,
                                        right, -1.0f, -1.0f,
                                        right, 1.0f, -1.0f,
                                        left, 1.0f, -1.0f});
        }

        if(currentScore == null) {
            currentScore = new Rectangle(programHandle, new float[]{left+(0.009f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                                    left+(0.24f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                                    left+(0.24f/nRatio), -1.0f+(0.07f/nRatio), -0.5f,
                                                                    left+(0.009f/nRatio), -1.0f+(0.07f/nRatio), -0.5f}, 1, currentScoreHandle);
        }else{
            currentScore.resetVerts(new float[]{left+(0.009f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                left+(0.24f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                left+(0.24f/nRatio), -1.0f+(0.07f/nRatio), -0.5f,
                                                left+(0.009f/nRatio), -1.0f+(0.07f/nRatio), -0.5f});
        }

        hiScore = new Rectangle(programHandle, new float[]{left+(0.009f/nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                           left+(0.25f/nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                           left+(0.25f/nRatio), 1.0f-(0.009f/nRatio), -0.5f,
                                                           left+(0.009f/nRatio), 1.0f-(0.009f/nRatio), -0.5f}, 1, hiScoreHandle);
        if(newGame == null) {
            newGame = new Rectangle(programHandle, new float[]{-0.2f, 0.0f, -0.5f,
                    0.2f, 0.0f, -0.5f,
                    0.2f, 0.2f, -0.5f,
                    -0.2f, 0.2f, -0.5f}, 1, newGameHandle);
        }
        target = new Rectangle(programHandle, new float[]{left*0.2f*nRatio, -Ratio*0.2f*nRatio, -0.3f,
                right*0.2f*nRatio, -Ratio*0.2f*nRatio, -0.3f,
                right*0.2f*nRatio, Ratio*0.2f*nRatio, -0.3f,
                left*0.2f*nRatio, Ratio*0.2f*nRatio, -0.3f}, 1, whiteHandle);

        if(score == null) {
            score = new Counter(programHandle, "0", new float[]{left+(0.25f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                                right-(0.009f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                                                right-(0.009f/nRatio), -1.0f+(0.07f/nRatio), -0.5f,
                                                                left+(0.25f/nRatio), -1.0f+(0.07f/nRatio), -0.5f}, 0.07f, 1, textHandle);
        }else{
            score.resetVerts(new float[]{left+(0.25f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                         right-(0.009f/nRatio), -1.0f+(0.009f/nRatio), -0.5f,
                                         right-(0.009f/nRatio), -1.0f+(0.07f/nRatio), -0.5f,
                                         left+(0.25f/nRatio), -1.0f+(0.07f/nRatio), -0.5f});
        }

        if(HighScore==null) {
            HighScore = new Counter(programHandle, "" + high, new float[]{left + (0.26f / nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                                          right - (0.009f / nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                                          right - (0.009f / nRatio), 1.0f-(0.009f/nRatio), -0.5f,
                                                                          left + (0.26f / nRatio), 1.0f-(0.009f/nRatio), -0.5f}, 0.07f, 1, textHandle);
        }else{
            HighScore.resetVerts(new float[]{left + (0.26f / nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                                         right - (0.009f / nRatio), 1.0f-(0.07f/nRatio), -0.5f,
                                                                         right - (0.009f / nRatio), 1.0f-(0.009f/nRatio), -0.5f,
                                                                         left + (0.26f / nRatio), 1.0f-(0.009f/nRatio), -0.5f});
        }
            for (int i = 0; i < invader.length; i++) {
                invader[i] = new Attacker(programHandle, left, right, 1.0f, -1.0f, 0.08f, 0, illiniHandle);
            }

            pauseButton = new Rectangle(programHandle, new float[]{right - (0.05f/nRatio), 1.0f - (0.05f/nRatio), -0.5f,
                                                                    right - (0.01f/nRatio), 1.0f - (0.05f/nRatio), -0.5f,
                                                                    right - (0.01f/nRatio), 1.0f - (0.01f/nRatio), -0.5f,
                                                                    right - (0.05f/nRatio), 1.0f - (0.01f/nRatio), -0.5f}, 1, pauseButtonHandle);

        if(pauseMenu == null){
            pauseMenu = new Rectangle(programHandle, new float[]{-0.3f, -0.2f, -0.5f,
                                                                 0.3f, -0.2f, -0.5f,
                                                                 0.3f, 0.2f, -0.5f,
                                                                 -0.3f, 0.2f, -0.5f}, 1, pauseMenuHandle);
        }

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programHandle);
        Matrix.setIdentityM(mModelMatrix, 0);

        back.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
        hiScore.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
        HighScore.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
        back.smoothShimmer(0.0013f, 0.0007f, 0.0017f);
        switch(GameState) {
            case 0:
                newGame.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
                break;
            case 1:
                pauseButton.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);

                score.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);

                currentScore.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);

                target.smoothShimmer(0.13f, 0.07f, 0.05f);
                if(!xOsc&&score.getNumber()>=300&&(Math.sin((SystemClock.uptimeMillis()*0.001))<=0.1&&(Math.sin((SystemClock.uptimeMillis()*0.001))>=-0.1f))){
                    yOsc=true;
                }
                if(yOsc){
                    float Y = (float)((0.2f)*Math.sin((SystemClock.uptimeMillis()*0.001)));
                    target.moveTo(target.centerX, Y);
                }
                if(score.getNumber()>=500&&((0.5f)*Math.cos((SystemClock.uptimeMillis()*0.001))<=0.1&&Math.cos((SystemClock.uptimeMillis()*0.001))>=-0.1)){
                    yOsc=false;
                    xOsc=true;
                }
                if(xOsc){
                    float Y = (float)((0.2f)*Math.sin((SystemClock.uptimeMillis()*0.001)));
                    float X = (float)((Ratio/2.5f)*Math.cos((SystemClock.uptimeMillis()*0.001)));
                    target.moveTo(X, Y);
                }

                target.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);

                for (Attacker i : invader) {
                    if (target.isWithin(i.centerX, i.centerY, 0.09f, 0.09f)) {
                        score.reset();
                        xOsc=false;
                        yOsc=false;
                        GameState = 0;
                        for (Attacker j : invader) {
                            j.genCoords();
                            j.moveType=0;
                            j.fadeType=0;
                            j.fadeReset();
                        }
                        target.moveTo(0.0f,0.0f);
                        break;
                    } else {
                        //int moveType = (int)(Math.random()*score.getNumber()*0.015);
                        i.move(0.0001f + (0.0000001f * score.getNumber()), target.centerX, target.centerY);
                        i.fade(target.centerX, target.centerY, 50.0f, 0.75f);
                        i.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
                    }
                }
                break;
            case 2:
                pauseMenu.render(mModelMatrix, mViewMatrix, mProjectionMatrix, mMVPMatrix);
                break;

        }
    }

    private float logistic(float max, float steepness, float x, float mid){
        return (float)(max/(1.0f+(Math.exp((-1.0f*steepness*(x-mid))))));
    }
    public void onPause(){
        if(GameState != 0)
        GameState = 2;
        high = HighScore.getNumber();
        SharedPreferences.Editor e = pref.edit();
        e.putInt("highScore", high);
        e.commit();
    }
    public void onDestroy(){
        high = HighScore.getNumber();
        SharedPreferences.Editor e = pref.edit();
        e.putInt("highScore", high);
        e.commit();
    }
    public void onTouch(float bx, float y) {
        float x = bx * Ratio;
        switch (GameState) {
            case 0://menu
                if(newGame.isWithin(x, y, 0.0f, 0.0f)){
                    for(Attacker i : invader){
                        i.resetBirth();
                    }
                    GameState = 1;
                }
                break;

            case 1://active game

                for (int i = invader.length - 1; i >= 0; i--) {
                    if ((invader[i].isWithin(x, y, 0.1f, 0.1f))) {

                        invader[i].genCoords();
                        invader[i].fadeReset();
                        invader[i].moveType=(int)(Math.random()*logistic(4.0f, 0.01f, score.getNumber(), 250.0f));
                        invader[i].fadeType = (int)(Math.random()*logistic(7.0f, 0.005f, score.getNumber(), 250.0f));
                        invader[i].fadeInit(0.0f, 0.0f);//needed?
                        invader[i].moveInit(target.centerX, target.centerY);

                        score.increment(1);
                        if(score.getNumber()>HighScore.getNumber()){
                            HighScore.increment(1);
                        }
                        return;
                    }
                }
                if(pauseButton.isWithin(x, y, 0.0f, 0.0f)){
                    GameState = 2;
                }
                break;
            case 2:
                for (int i = invader.length - 1; i >= 0; i--) {
                    invader[i].resetBirth();
                }
                GameState = 1;
                break;
        }
    }
}
