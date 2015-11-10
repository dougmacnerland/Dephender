package me.macnerland.dephender;

/**
 * Created by Doug on 9/23/2015.
 *
 * Displays a count
 */
public class Counter extends Text {
    private int number;

    /*Constructor, must pass number as a string*/
    public Counter(int program, String counts, float[] boxVertices, float charX, int alignType, int texture){
        super(program, counts, boxVertices, charX, alignType, texture);
        this.str = counts.toCharArray();
        number = Integer.parseInt(counts);
    }

    /*return the number stored and displayed by the text*/
    public int getNumber(){
        return number;
    }

    /*set the number back to zero*/
    public void reset(){
        number = 0;
        putString("0", vertStep);
    }

    /*Increase the count and update the Text*/
    public void increment(int num){
        number = number + num;
        String s = Integer.toString(number);
        putString(s, vertStep);
    }
}
