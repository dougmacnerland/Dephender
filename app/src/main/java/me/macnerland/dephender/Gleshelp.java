package me.macnerland.dephender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Doug on 8/28/2015.
 */
public class Gleshelp {
    public static int loadTexture(Context c, int res){
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; //openGL will not pre-scale

        Bitmap bit = BitmapFactory.decodeResource(c.getResources(), res, options);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bit, 0);
        bit.recycle();
        return textureHandle[0];

    }
    public static String getVertexShader(int type){
        switch(type){
            default://includes texturing
                return
                                  "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.

                                + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
                                + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
                                + "attribute vec2 a_TexCoordinate;\n" //Per-vertex texture data

                                + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
                                + "varying vec2 v_TexCoordinate;  \n"

                                + "void main()                    \n"		// The entry point for our vertex shader.
                                + "{                              \n"
                                + "   v_Color = a_Color;          \n"		// Pass the color through to the fragment shader.
                                + "   v_TexCoordinate = a_TexCoordinate;\n"
                                                                             // It will be interpolated across the triangle.
                                + "   gl_Position = u_MVPMatrix   \n" 	// gl_Position is a special variable used to store the final position.
                                + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                                + "}                              \n";    // normalized screen coordinates.
        }
    }
    public static String getFragmentShader(int type){
        switch (type){
            default:
                return
                     "precision mediump float;       \n"
                    +"uniform sampler2D u_Texture;    \n"
                    +"varying vec4 v_Color;           \n"
                    +"varying vec2 v_TexCoordinate;   \n"
                    + "void main()                    \n"
                    + "{                              \n"
                    +"      gl_FragColor = v_Color * texture2D(u_Texture, v_TexCoordinate);         \n"
                    + "}                              \n";
        }
    }
}
