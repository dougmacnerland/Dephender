package me.macnerland.dephender;

import android.os.SystemClock;

/**
 * Created by Doug on 8/19/2015.
 * Object for creating an enemy
 */
public class Attacker extends Rectangle {

    private float size;
    private float ratio;
    private float left;
    private float right;
    private float bottom;
    private float top;
    private long birthTime;
    public int moveType;
    public float moveOffset;
    private float radius;

    public int fadeType;
    public long fadeBirth;
    public float frequency;

    Attacker(int program, float left_, float right_, float top_, float bottom_, float sizeRatio, int coloration, int tex){
        super(program, new float[]{-1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f}, coloration, tex);
        ratio=sizeRatio;
        bottom=bottom_;
        top=top_;
        right=right_;
        left=left_;
        if(right>1){
            size=ratio*(right*right);
        }else{
            size=ratio/(right*right);
        }
        moveOffset = 0.0f;
        genCoords();
        birthTime = SystemClock.uptimeMillis();
    }

    /*move depending on the movetype*/
    public void move(float speed, float toX, float toY){
        switch(moveType){
            case 1:
                moveAlt(speed, toX, toY);
                break;
            case 2:
                moveSideAlt(speed, toX, toY);
                break;
            case 3:
                moveCircle(speed, toX, toY);
                break;
            default:
                moveLinear(speed, toX, toY);
                break;
        }
    }

    /*Move directly towards the target*/
    public void moveLinear(float speed, float toX, float toY){
        long now = SystemClock.uptimeMillis();
        long timeDiff = now-birthTime;
        float distance = ((float)timeDiff)*speed;
        birthTime = now;
        setCenter();
        float distX = distance;
        float distY = distance;
        float signY = 1.0f;
        float signX = 1.0f;

        if(centerX < toX){
            signX = -1.0f;
        }

        if(centerY < toY){
            signY = -1.0f;
        }


        if(centerY == toY){
            distY = 0;
        }

        if(centerX == toX){
            distX = 0;
        }

        if(centerX != toX && centerY != toY){
            double theta = Math.abs(Math.atan((centerY - toY) / (centerX - toX)));
            distY = ((float) Math.sin(theta)) * distance * (signY * -1.0f);
            distX = ((float) Math.cos(theta)) * distance * (signX * -1.0f)*right;
        }
        moveTo(centerX+distX, centerY+distY);
    }

    /*move back and forth towards the object*/
    public void moveAlt(float speed, float toX, float toY){
        long now = SystemClock.uptimeMillis();
        long timeDiff = now-birthTime;
        float distance = ((float)timeDiff)*speed;
        birthTime = now;
        setCenter();
        float distX = distance;
        float distY = distance;
        float signY = 1.0f;
        float signX = 1.0f;

        if(centerX < toX){
            signX = -1.0f;
        }

        if(centerY < toY){
            signY = -1.0f;
        }


        if(centerY == toY){
            distY = 0;
        }

        if(centerX == toX){
            distX = 0;
        }

        if(centerX != toX && centerY != toY){
            double theta = Math.abs(Math.atan((centerY - toY) / (centerX - toX)));
            distY = ((float) Math.sin(theta)) * distance * (signY * -1.0f);
            distX = ((float) Math.cos(theta)) * distance * (signX * -1.0f);
            distY = 0.75f*distY+distY*(float)Math.sin((now/128.0f)+moveOffset);
            distX = (0.75f*distX+distX*(float)Math.sin((now / 128.0f) + moveOffset))*right;
        }
        moveTo(centerX+distX, centerY+distY);
    }

    /*move while adding the sine of time plus some offset*/
    public void moveSideAlt(float speed, float toX, float toY) {
        long now = SystemClock.uptimeMillis();
        long timeDiff = now - birthTime;
        float distance = ((float) timeDiff) * speed;
        birthTime = now;
        setCenter();
        float distX = distance;
        float distY = distance;
        float signY = 1.0f;
        float signX = 1.0f;

        if (centerX < toX) {
            signX = -1.0f;
        }

        if (centerY < toY) {
            signY = -1.0f;
        }
        if (centerY == toY) {
            distY = 0;
        }

        if (centerX == toX) {
            distX = 0;
        }

        if (centerX != toX && centerY != toY) {
            double theta = Math.abs(Math.atan((centerY - toY) / (centerX - toX)));
            distY = ((float) Math.sin(theta)) * distance * (signY * -1.0f);
            distX = ((float) Math.cos(theta)) * distance * (signX * -1.0f);
            distY = 0.75f * distY + distY * (float) Math.sin(now / 128.0f);
            distX = (0.75f * distX + distX * (float) Math.cos(now / 128.0f))*right;
        }
        moveTo(centerX + distX, centerY + distY);
    }

    /*move in a constant spiral inwards towards the target*/
    public void moveCircle(float speed, float toX, float toY){
        long now = SystemClock.uptimeMillis();
        long timeDiff = now-birthTime;
        float X = (float)(toX+(radius/(timeDiff*0.001f))*Math.cos((speed*timeDiff*5.0f)+moveOffset));
        float Y = (float)(toY+(radius/(timeDiff*0.001f))*Math.sin((speed*timeDiff*5.0f)+moveOffset));
        moveTo(X, Y);
    }

    /*initialize the movement of the object*/
    public void moveInit(float toX, float toY){
        switch(moveType){
            case 1:
                moveOffset = (float)(2.0f*Math.PI*Math.random());
                break;
            case 2:
                moveOffset = (float)(2.0f*Math.PI*Math.random());
                break;
            case 3:
                if(centerX - toX == 0){
                    moveOffset = 0;
                }else{
                    moveOffset = (float)Math.atan((centerY - toY)/(centerX - toX));
                }
                radius = (float)(Math.sqrt(((centerX-toX)*(centerX-toX))+((centerY-toY)*(centerY-toY))));
                break;
        }
    }

    /*Attacker will fade in given a speed*/
    public void fadeIn(float toX, float toY, float surprise, float revealDist){
        float rad = (float)Math.sqrt(((centerX-toX)*(centerX-toX))+((centerY-toY)*(centerY-toY)));
        float alpha = (float)(1.0f/(1.0f+Math.exp(-1.0*surprise*((1/rad)-(1/revealDist)))));
        for(int i=0; i<4; i++){
            color.put(((i*4)+3), alpha);
        }
    }

    /*Attacker will flash a speed at a frequency*/
    public void fadeBlink(){
        for(int i=0; i<4; i++){
            color.put(((i*4)+3), (float)Math.abs(Math.sin(frequency*0.02*SystemClock.uptimeMillis())));
        }
    }

    /*Attacker will disappear at a certain revealDist, with a speed of surprise*/
    public void fadeOut(float toX, float toY, float surprise, float revealDist){
        float rad = (float)Math.sqrt(((centerX-toX)*(centerX-toX))+((centerY-toY)*(centerY-toY)));
        float alpha = (float)(1.0f-(1.0f/(1.0f+Math.exp(-1.0*surprise*((1/rad)-(1/(revealDist/3)))))));
        for(int i=0; i<4; i++) {
            color.put(((i * 4) + 3), alpha);
        }
    }

    /*Return the Attacker to completely opaque*/
    public void fadeReset(){
        for(int i=0; i<4; i++) {
            color.put(((i * 4) + 3), 1.0f);
        }
    }

    /*Fade the Attacker*/
    public void fade(float toX, float toY, float surprise, float revealDist){
        switch(fadeType){
            case 4:
                fadeIn(toX, toY, surprise, revealDist);
                break;
            case 5:
                fadeBlink();
                break;
            case 6:
                fadeOut(toX, toY, surprise/3.0f, revealDist/1.3f);
                break;
        }
    }

    /*Initialize the fade abilities of the attacker*/
    public void fadeInit(float toX, float toY){
        switch(fadeType){
            case 4:
                frequency = 100000.0f;
                fadeBirth = SystemClock.uptimeMillis();
                break;
            case 5:
                frequency = (float)(1.0f+Math.random());
                fadeBirth = SystemClock.uptimeMillis();
                break;
        }
    }

    /*reset the birthtime of the Attacker*/
    public void resetBirth(){
        birthTime = SystemClock.uptimeMillis();
    }

    /*Generate new coordinates for the attacker*/
    public void genCoords(){
        float screenY = ((top-bottom)+size);
        float screenX = ((right-left)+size);

        double xOrY = Math.random();
        if(xOrY<0.5){
            centerY = (float)((Math.random()*screenY)+bottom-(size/2));
            if(xOrY>0.25){
                centerX=left-(size/2);
            }
            else{
                centerX=right+(size/2);
            }
        }
        else{
            centerX = (float)((Math.random()*screenX)+left-(size/2));
            if(xOrY>0.75){
                centerY = top+(size/2);
            }
            else{
                centerY = bottom-(size/2);
            }
        }
        moveTo(centerX, centerY, size / 2, size / 2);
        if(vertices.get(3) - vertices.get(0) != size){
            moveTo(centerX, centerY, size / 2, size / 2);
        }
        resetBirth();
    }
}
