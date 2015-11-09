package me.macnerland.dephender;

import android.opengl.GLES20;

/**
 * Created by Doug on 9/13/2015.
 */
public class Text {
    protected Character[] myString;
    protected char[] str;
    private final int texture;

    private int programHandle;
    private int MVPMatrixHandle;
    private int PositionHandle;
    private int ColorHandle;
    private int TextureCoordinateHandle;
    private int TextureUniformHandle;

    protected float vertStep;
    private float[] textBox;
    private int alignment;

    public Text(int program, String s, float[] boxVerts, float charX, int alignType, int textTexture){
        texture = textTexture;
        textBox = boxVerts;
        alignment = alignType;
        programHandle = program;
        MVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        PositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        ColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        TextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        TextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        putString(s, charX);
    }
    public void putString(String newString, float charX){
        str = newString.toCharArray();
        Character[] newChara = new Character[str.length];
        if((textBox[3]-textBox[0])<(charX*str.length)){
            vertStep = (textBox[3]-textBox[0])/newString.length();
        }else {
            vertStep = charX;
        }
        switch(alignment){
            default:
                alignLeft(newChara);
                break;
        }
        myString = newChara;
    }
    /*Hi Doug*/

    public void putString(char[] newString, float charX){
        str = newString;
        Character[] newCharact = new Character[str.length];
        /*if((textBox[3]-textBox[0])<(charX*str.length)){
            vertStep = (textBox[3]-textBox[0])/newString.length;
        }else {
            vertStep = charX;
        }*/
        switch(alignment){
            default:
                alignLeft(newCharact);
                break;
        }
        myString = newCharact;
    }

    private void alignLeft(Character[] newChara){
        for(int i = 0; i < newChara.length; i++){
            newChara[i] = new Character(str[i], programHandle, MVPMatrixHandle, PositionHandle, ColorHandle, TextureCoordinateHandle, TextureUniformHandle,
                                                                      new float[]{(textBox[0]+(vertStep*i)), textBox[1], textBox[2],
                                                                                  (textBox[0]+(vertStep*(i+1))), textBox[4], textBox[5],
                                                                                  (textBox[0]+(vertStep*(i+1))), textBox[7], textBox[8],
                                                                                  (textBox[0]+(vertStep*i)), textBox[10], textBox[11]}, texture);
        }
    }

    public void render(float[] mMM, float[] mVM, float[] mPM, float[] mMVPM){
        for(int i = 0; i<myString.length; i++){
            myString[i].render(mMM, mVM, mPM, mMVPM);
        }
    }
    public void resetVerts(float[] vert){
        textBox = vert;
        putString(str, vertStep);
    }
    public void update(char up, int index){
        myString[index].put(up);
        str[index] = up;
    }
    /*public void findTile(Rectangle rend, char c){
        float xCoord = (((int)(c))%16);
        float yCoord = ((((int)(c))/16)-1.0f);
        rend.uvs.put(0, (xCoord)*StepSize);
        rend.uvs.put(1, (yCoord+1)*StepSize);
        rend.uvs.put(2, (xCoord+1)*StepSize);
        rend.uvs.put(3, (yCoord+1)*StepSize);
        rend.uvs.put(4, (xCoord+1)*StepSize);
        rend.uvs.put(5, (yCoord)*StepSize);
        rend.uvs.put(6, (xCoord)*StepSize);
        rend.uvs.put(7, (yCoord)*StepSize);
    }*/
}
