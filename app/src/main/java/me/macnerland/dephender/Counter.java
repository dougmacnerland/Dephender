package me.macnerland.dephender;

/**
 * Created by Doug on 9/23/2015.
 */
public class Counter extends Text {
    private int number;

    public Counter(int program, String counts, float[] boxVertices, float charX, int alignType, int texture){
        super(program, counts, boxVertices, charX, alignType, texture);
        this.str = counts.toCharArray();
        number = Integer.parseInt(counts);
    }
    public int getNumber(){
        return number;
    }
    public void reset(){
        number = 0;
        putString("0", vertStep);
    }
    /*public void increment(int num){
        number = number + num;//TODO: only update changed digits
        char[] s = Integer.toString(number).toCharArray();
        int mod = s.length-str.length;
        if(mod>=0){
            for(int i = 0; i < mod; i++){

            }
            for(int i = 0; i < str.length; i++){
                if(s[i+mod] != str[i]){
                    myString[i].put(s[i]);
                }
            }
        }else{
            for(int i = 0; i < str.length; i++){
                if(s[i] != str[i]){
                    myString[i].put(s[i]);
                }
            }
        }*/
        public void increment(int num){
            number = number + num;
            String s = Integer.toString(number);
            putString(s, vertStep);
    }

    /**/
}
