package me.macnerland.dephender;

/**
 * Created by Doug on 10/30/2015.
 */
public class Character extends Rectangle{

    private char c;

    /*16 characters per line*/
    private final float StepSize = (1.0f)/(16.0f);

    Character(char ch, int program, float[] verts, int textureHandle){
        super(program, verts, textureHandle);
        c = ch;
        findTile();
    }

    Character(char ch, int ProgramHandle, int MVPMatrixH, int posHandle, int colHandle, int texCoordHandle, int texUniHandle, float[] verts, int textureHandle){
        super(ProgramHandle, MVPMatrixH, posHandle, colHandle, texCoordHandle, texUniHandle, verts, textureHandle);
        c = ch;
        findTile();
    }

    public void findTile(){
        float xCoord = (((int)(c))%16);
        float yCoord = ((((int)(c))/16)-1.0f);
        uvs.put(0, (xCoord)*StepSize);
        uvs.put(1, (yCoord+1)*StepSize);
        uvs.put(2, (xCoord+1)*StepSize);
        uvs.put(3, (yCoord+1)*StepSize);
        uvs.put(4, (xCoord+1)*StepSize);
        uvs.put(5, (yCoord)*StepSize);
        uvs.put(6, (xCoord)*StepSize);
        uvs.put(7, (yCoord)*StepSize);
    }
    public void put(char ch){
        c = ch;
        findTile();
    }

}
