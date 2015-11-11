package me.macnerland.dephender;

/**
 * Created by Doug on 10/30/2015.
 *
 * This class represents a Rectangle with a character mapped onto it.
 * Uses typical sprite mapping to use the same texture with all characters. 
 */
public class Character extends Rectangle{

    /*value held by this Character*/
    private char c;

    /*16 characters per line in the sprite map*/
    private final float StepSize = (1.0f)/(16.0f);
    
    /*Finds render information from program handle*/
    Character(char ch, int program, float[] verts, int textureHandle){
	/*Generate a Rectangle*/
        super(program, verts, textureHandle);

	/*set character*/
        c = ch;
        findTile();
    }

    /*explicit constructor, uses no openGL-ES2.0 function calls*/
    Character(char ch, int ProgramHandle, int MVPMatrixH, int posHandle, int colHandle, int texCoordHandle, int texUniHandle, float[] verts, int textureHandle){
        super(ProgramHandle, MVPMatrixH, posHandle, colHandle, texCoordHandle, texUniHandle, verts, textureHandle);
        c = ch;
        findTile();
    }

    /*change the UV coordinates to map the char
 *  from the texture to the Character*/
    public void findTile(){
        float xCoord = (((int)(c))%16);
        float yCoord = ((((int)(c))/16)-1.0f);
        uv.put(0, (xCoord)*StepSize);
        uv.put(1, (yCoord+1)*StepSize);
        uv.put(2, (xCoord+1)*StepSize);
        uv.put(3, (yCoord+1)*StepSize);
        uv.put(4, (xCoord+1)*StepSize);
        uv.put(5, (yCoord)*StepSize);
        uv.put(6, (xCoord)*StepSize);
        uv.put(7, (yCoord)*StepSize);
    }

    /*change to a new character*/
    public void put(char ch){
        c = ch;
        findTile();
    }

}
