package editor;

import editor.Pixel;
import java.lang.StringBuilder;

public class Image {
    public int width;
    public int height;
    public int maxColorVal;
    public String imageType;

    public Pixel[][] image;

    //used when filling the image with pixels, to know where to place the next pixel
    private int currentHeight;
    private int currentWidth;

    public Image(int w, int h, int maxC, String iT){
        width = w;
        height = h;
        maxColorVal = maxC;
        image = new Pixel[height][width];
        currentWidth = 0;
        currentHeight = 0;
        imageType = iT;
    }

    //places the passed pixel in the next location
    public void addPixel(Pixel p){
        if (currentWidth == width){
            //System.out.println("moving to next line");
            currentWidth = 0;
            currentHeight++;
        }
        //System.out.println("Adding pixel at location [" + currentHeight + "][" + currentWidth + "]");
        image[currentHeight][currentWidth] = p;
        currentWidth++;
    }

    @Override
    public String toString(){
        StringBuilder toReturn = new StringBuilder("");
        toReturn.append(imageType);
        toReturn.append(" ");
        toReturn.append(width);
        toReturn.append(" ");
        toReturn.append(height);
        toReturn.append(" ");
        toReturn.append(maxColorVal);
        toReturn.append(" ");

        //System.out.println(image[0].length);
        //System.out.println(image.length);


        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                //System.out.println("i + " " + j);
                toReturn.append(image[i][j].toString());
                toReturn.append(" ");
            }
            toReturn.append("\n");
        }

        //System.out.println(toReturn.toString());

        String r = toReturn.toString();
        return r;


    }

}
