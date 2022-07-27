package editor;

public class Pixel {
    public int red;
    public int green;
    public int blue;

    public Pixel(int r, int g, int b){
        red = r;
        green = g;
        blue = b;
    }

    @Override
    public String toString(){
        return red + " " + green + " " + blue;
    }

}
