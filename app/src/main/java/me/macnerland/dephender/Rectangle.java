package me.macnerland.dephender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * Created by Doug on 8/19/2015.
 */
public class Rectangle {

    private int programHandle;
    private int MVPMatrixHandle;
    private int PositionHandle;
    private int ColorHandle;
    private int TextureCoordinateHandle;
    private int TextureUniformHandle;
    private int TextureHandle;






    protected FloatBuffer vertices;
    protected FloatBuffer color;
    protected ShortBuffer indices;
    public FloatBuffer uvs;

    private float colorConstR;
    private float colorConstG;
    private float colorConstB;
    private float colorConstA;

    public float centerX;
    public float centerY;


    Rectangle(int ProgramHandle, float[] verts, float[] colors, int coloration, int textureHandle){
        this.programHandle = ProgramHandle;
        MVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        PositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        ColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        TextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        TextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        TextureHandle = textureHandle;
        centerX = (verts[0]+verts[3])/2;
        centerY = (verts[1]+verts[7])/2;
        this.vertices = ByteBuffer.allocateDirect(verts.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.vertices.put(verts).position(0);

        this.color = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.color.put(colors).position(0);

        short[] index = new short[]{0,1,2,0,2,3};
        indices = ByteBuffer.allocateDirect(index.length *2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.put(index).position(0);
        switch(coloration){
            case 0:
                colorRandomSoft();
                break;
            case 1:
                colorSolid(1.0f, 1.0f, 1.0f, 1.0f);//specifies values for texturization
                break;
            case 2:
                colorRandomSolid();
                break;
            default:
                break;
        }
        colorConstR = 1.0f;
        colorConstG = 1.0f;
        colorConstB = 1.0f;
        colorConstA = 1.0f;

        float[] uvCoord = new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };
        uvs = ByteBuffer.allocateDirect(uvCoord.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        uvs.put(uvCoord).position(0);
    }

    /*Specify the program, vertices in counter-clockwise order starting at the lower left,
    * coloration types and the handle to the texture to be used*/
    Rectangle(int ProgramHandle, float[] verts, int coloration, int textureHandle){
        this.programHandle = ProgramHandle;
        MVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        PositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        ColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        TextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        TextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        TextureHandle = textureHandle;

        TextureHandle=textureHandle;

        centerX = (verts[0]+verts[3])/2;

        centerY = (verts[1]+verts[7])/2;

        this.vertices = ByteBuffer.allocateDirect(verts.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.vertices.put(verts).position(0);

        float[] colors = new float[16];
        this.color = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.color.put(colors).position(0);

        short[] index = new short[]{0,1,2,0,2,3};
        indices = ByteBuffer.allocateDirect(index.length *2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.put(index).position(0);

        float[] uvCoord = new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };
        uvs = ByteBuffer.allocateDirect(uvCoord.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        uvs.put(uvCoord).position(0);

        switch(coloration){
            case 0:
                colorRandomSoft();
                break;
            case 1:
                colorSolid(1.0f, 1.0f, 1.0f, 1.0f);//specifies values for texturization
                break;
            default:
                colorRandomSolid();
                break;
        }
        colorConstR = 1.0f;
        colorConstG = 1.0f;
        colorConstB = 1.0f;
        colorConstA = 1.0f;
    }

    Rectangle(int ProgramHandle, float[] verts, int textureHandle){
        this.programHandle = ProgramHandle;
        MVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        PositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        ColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        TextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        TextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        TextureHandle = textureHandle;

        TextureHandle=textureHandle;

        centerX = (verts[0]+verts[3])/2;

        centerY = (verts[1]+verts[7])/2;

        this.vertices = ByteBuffer.allocateDirect(verts.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.vertices.put(verts).position(0);

        float[] colors = new float[16];
        this.color = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.color.put(colors).position(0);

        short[] index = new short[]{0,1,2,0,2,3};
        indices = ByteBuffer.allocateDirect(index.length *2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.put(index).position(0);

        float[] uvCoord = new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };
        uvs = ByteBuffer.allocateDirect(uvCoord.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        uvs.put(uvCoord).position(0);
        colorSolid(1.0f, 1.0f, 1.0f, 1.0f);//specifies values for texturization
        colorConstR = 1.0f;
        colorConstG = 1.0f;
        colorConstB = 1.0f;
        colorConstA = 1.0f;
    }

    Rectangle(int ProgramHandle, int MVPMatrixH, int posHandle, int colHandle, int texCoordHandle, int texUniHandle, float[] verts, int textureHandle){
        this.programHandle = ProgramHandle;
        MVPMatrixHandle = MVPMatrixH;
        PositionHandle = posHandle;
        ColorHandle = colHandle;
        TextureCoordinateHandle = texCoordHandle;
        TextureUniformHandle = texUniHandle;
        TextureHandle = textureHandle;

        TextureHandle=textureHandle;

        centerX = (verts[0]+verts[3])/2;

        centerY = (verts[1]+verts[7])/2;

        this.vertices = ByteBuffer.allocateDirect(verts.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.vertices.put(verts).position(0);

        float[] colors = new float[16];
        this.color = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.color.put(colors).position(0);

        short[] index = new short[]{0,1,2,0,2,3};
        indices = ByteBuffer.allocateDirect(index.length *2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.put(index).position(0);

        float[] uvCoord = new float[]{
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };
        uvs = ByteBuffer.allocateDirect(uvCoord.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        uvs.put(uvCoord).position(0);
        colorSolid(1.0f, 1.0f, 1.0f, 1.0f);//specifies values for texturization
        colorConstR = 1.0f;
        colorConstG = 1.0f;
        colorConstB = 1.0f;
        colorConstA = 1.0f;
    }

    public void render(float[] mModelMatrix, float[] mViewMatrix, float[] mProjectionMatrix,
                       float[] mMVPMatrix){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureHandle);
        GLES20.glUniform1i(TextureUniformHandle, 0);
        GLES20.glEnableVertexAttribArray(TextureCoordinateHandle);
        this.vertices.position(0);
        GLES20.glVertexAttribPointer(PositionHandle, 3, GLES20.GL_FLOAT, true,
                0, this.vertices);
        GLES20.glEnableVertexAttribArray(PositionHandle);
        this.color.position(0);
        GLES20.glVertexAttribPointer(ColorHandle, 4, GLES20.GL_FLOAT, true,
                0, this.color);
        GLES20.glEnableVertexAttribArray(ColorHandle);
        this.uvs.position(0);
        GLES20.glVertexAttribPointer(TextureCoordinateHandle, 2, GLES20.GL_FLOAT, true, 0, this.uvs);
        GLES20.glEnableVertexAttribArray(TextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);


        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, this.indices);
        GLES20.glDisableVertexAttribArray(PositionHandle);
        GLES20.glDisableVertexAttribArray(ColorHandle);
        GLES20.glDisableVertexAttribArray(TextureCoordinateHandle);
    }
    /*Move directly to the give x, y coordinates.*/
    public void moveTo(float x, float y){
        float xLength = centerX - vertices.get(0);
        float yLength = centerY - vertices.get(1);
        float z = vertices.get(2);
        float[] puts = new float[]{x - xLength, y - yLength, z,
                                   x + xLength, y - yLength, z,
                                   x + xLength, y + yLength, z,
                                   x - xLength, y + yLength, z};
        /*vertices.put(0, x - xLength);
        vertices.put(1, y - yLength);
        vertices.put(3, x + xLength);
        vertices.put(4, y - yLength);
        vertices.put(6, x + xLength);
        vertices.put(7, y + yLength);
        vertices.put(9, x - xLength);
        vertices.put(10, y + yLength);*/
        vertices.put(puts).position(0);
        setCenter();
    }

    /*Specify the the dimensions at the new position*/
    public void moveTo(float x, float y, float xLength, float yLength){
        vertices.put(0, x - xLength);
        vertices.put(1, y - yLength);
        vertices.put(3, x + xLength);
        vertices.put(4, y - yLength);
        vertices.put(6, x + xLength);
        vertices.put(7, y + yLength);
        vertices.put(9, x - xLength);
        vertices.put(10, y + yLength);
        setCenter();
    }
    public void resetVerts(float[] v){
        vertices.put(v).position(0);
    }
    public void colorSolid(float r, float g, float b, float a){
        for(int i = 0; i < 4; i++){
            color.put((i * 4), r);
            color.put((i * 4) + 1, g);
            color.put((i * 4) + 2, b);
            color.put((i * 4) + 3, a);
        }
    }
    public void colorRandomSoft(){
        for (int i = 0; i < 4; i++) {
            color.put((i * 4), (float) Math.random());
            color.put((i * 4) + 1, (float) Math.random());
            color.put((i * 4) + 2, (float) Math.random());
            color.put((i * 4) + 3, 1.0f);
        }
    }
    public void colorRandomSolid(){
        float colorR = (float) Math.random();
        float colorG = (float) Math.random();
        float colorB = (float) Math.random();
        for (int i = 0; i < 4; i++) {
            color.put((i * 4), colorR);
            color.put((i * 4) + 1, colorG);
            color.put((i * 4) + 2, colorB);
            color.put((i * 4) + 3, 1.0f);
        }
    }
    public void smoothShimmer(float changeR, float changeG, float changeB){
        for (int i = 0; i < 4; i++) {
            float r = color.get((i * 4));
            if (Math.abs(r + (changeR * colorConstR)) > 1.0f) {
                colorConstR *= -1.0f;
            }
            float g = color.get((i * 4) + 1);
            if (Math.abs(g + (changeG * colorConstG)) > 1.0f) {
                colorConstG *= -1.0f;
            }
            float b = color.get((i * 4) + 2);
            if (Math.abs(b + (changeB * colorConstB)) > 1.0f) {
                colorConstB *= -1.0f;
            }
            color.put((i * 4), r + (changeR * colorConstR));
            color.put((i * 4) + 1, g + (changeG * colorConstG));
            color.put((i * 4) + 2, b + (changeB * colorConstB));
        }
    }
    public boolean isWithin(float x, float y, float xDist, float yDist){
        if((x >= vertices.get(0)-xDist)&&(x <= vertices.get(3)+xDist)&&(y >= vertices.get(1)-yDist)&&(y <= vertices.get(10)+yDist)){
            return true;
        }
        return false;
    }
    public void setCenter(){
        float center = 0.0f;
        for(int i = 0; i < 4; i++){
            center += vertices.get((i*3)+1);
        }
        center = center/4f;
        centerY = center;
        center = 0.0f;
        for(int i = 0; i < 4; i++){
            center += vertices.get(i*3);
        }
        center = center/4f;
        centerX = center;
    }
}
