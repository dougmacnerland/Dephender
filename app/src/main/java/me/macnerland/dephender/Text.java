package me.macnerland.dephender;

import android.opengl.GLES20;

/**
 * Created by Doug on 9/13/2015.
 *
 * Analogous to a string. Single line array of Characters
 * Has vertices for each character, supports render function 
 */
public class Text {
    protected Character[] myString;
    protected char[] str;
    private final int texture;

    /*This data must be stored here to allow dynamic
 *  creation of array elements. Array elements must not
 *  rely on openGL-ES2.0 */
    private int programHandle;
    private int MVPMatrixHandle;
    private int PositionHandle;
    private int ColorHandle;
    private int TextureCoordinateHandle;
    private int TextureUniformHandle;

    protected float vertStep;
    private float[] textBox;
    private int alignment;

    /*Constructor.*/
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

    /*Arrange the Character[] within the text box vertices
 *  if the characters cannot fit within the vertices, change
 *  the vertStep so that they do.*/
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

    /*Use VertStep to create a row of Characters*/
    public void putString(char[] newString, float charX){
        str = newString;
        Character[] newCharact = new Character[str.length];
        switch(alignment){
            default:
                alignLeft(newCharact);
                break;
        }
        myString = newCharact;
    }

    /*Create row aligned to the left */
    private void alignLeft(Character[] newChara){
        for(int i = 0; i < newChara.length; i++){
            newChara[i] = new Character(str[i], programHandle, MVPMatrixHandle, PositionHandle, ColorHandle, TextureCoordinateHandle, TextureUniformHandle,
                                                                      new float[]{(textBox[0]+(vertStep*i)), textBox[1], textBox[2],
                                                                                  (textBox[0]+(vertStep*(i+1))), textBox[4], textBox[5],
                                                                                  (textBox[0]+(vertStep*(i+1))), textBox[7], textBox[8],
                                                                                  (textBox[0]+(vertStep*i)), textBox[10], textBox[11]}, texture);
        }
    }

    /*Render each of the Characters*/
    public void render(float[] mMM, float[] mVM, float[] mPM, float[] mMVPM){
        for(int i = 0; i<myString.length; i++){
            myString[i].render(mMM, mVM, mPM, mMVPM);
        }
    }

    /*reset text box*/
    public void resetVerts(float[] vert){
        textBox = vert;
        putString(str, vertStep);
    }

    /*Update the character at index*/
    public void update(char up, int index){
        myString[index].put(up);
        str[index] = up;
    }
}
