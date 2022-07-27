package editor;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.lang.Math;

public class ImageEditor {

    public static void main(String[] args){

        File file = new File(args[0]);
        Image image;
        try(Scanner scan = new Scanner(file)){
            image = makeImage(scan);
            //System.out.println("Image width: " + image.width);
            //System.out.println("Image height: " + image.height);
            //System.out.println(image.toString());
            if (args[2].equals("invert")){
                invertImage(image);
            }
            //System.out.println(image.toString());

            else if (args[2].equals("grayscale")){
                grayscaleImage(image);
            }

            else if (args[2].equals("emboss")){
                image = embossImage(image); //because emboss does not edit the original Image object, I set it equal to the return
            }

            else if (args[2].equals("motionblur")){
                int blurVal = Integer.parseInt(args[3]);
                motionblurImage(image, blurVal);
            }

            else{
                System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length");
            }

            //output ppm to output file
            FileWriter f = new FileWriter(args[1]);
            PrintWriter writer = new PrintWriter(f);
            writer.print(image.toString());
            writer.close();

        }
        catch(IOException ex) {
            System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length");
        }

    }

    //is passed the scanner connected to the ppm file, and returns an image object made from said file
    public static Image makeImage(Scanner scan) throws IOException{
        Image image;
        String imageType = scan.next();
        int width = scan.nextInt();
        int height = scan.nextInt();
        int maxColorVal = scan.nextInt();
        image = new Image(width, height, maxColorVal, imageType);
        int red;
        int green;
        int blue;

        while(scan.hasNextInt()){
            red = scan.nextInt();
            green = scan.nextInt();
            blue = scan.nextInt();
            Pixel p = new Pixel(red, green, blue);
            image.addPixel(p);
        }
        //System.out.println("Successfully read image");
        return image;
    }

    public static Image invertImage(Image image){
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        for (int i = 0; i < image.height; i++){
            for (int j = 0; j < image.width; j++){
                r = image.image[i][j].red - 127.5;
                g = image.image[i][j].green - 127.5;
                b = image.image[i][j].blue - 127.5;
                r *= 2;
                g *= 2;
                b *= 2;
                image.image[i][j].red -= r;
                image.image[i][j].green -= g;
                image.image[i][j].blue -= b;
            }
        }
        return image;
    }

    public static Image grayscaleImage(Image image){
        int newColor = 0;

        for (int i = 0; i < image.height; i++){
            for (int j = 0; j < image.width; j++){
                newColor = (image.image[i][j].red + image.image[i][j].green + image.image[i][j].blue) / 3;
                image.image[i][j].red = newColor;
                image.image[i][j].green = newColor;
                image.image[i][j].blue = newColor;
            }
        }

        return image;
    }

    public static Image motionblurImage(Image image, int blurVal){


        for (int i = 0; i < image.height; i++){
            for (int j = 0 ;j < image.width; j++){
                int newValRed = 0;
                int newValGreen = 0;
                int newValBlue = 0;
                int toBlur = blurVal;
                if (blurVal + j > image.width){//if the blur goes past the edge of the image, readjust
                    toBlur -= ((blurVal + j) - image.width);
                }

                for (int k = 0 ; k < toBlur; k++){
                    newValRed += image.image[i][j + k].red;
                    newValGreen += image.image[i][j + k].green;
                    newValBlue += image.image[i][j + k].blue;
                }
                image.image[i][j].red = newValRed/toBlur;
                image.image[i][j].green = newValGreen/toBlur;
                image.image[i][j].blue = newValBlue/toBlur;
            }
        }


        return image;
    }

    public static Image embossImage(Image image){
        Image img = new Image(image.width, image.height, image.maxColorVal, image.imageType);
        for (int i = 0; i < image.height; i++){
            for (int j = 0 ;j < image.width; j++){
                int V = 0; //the value I will set each color to at the end
                if (i == 0 || j == 0){ //if it is on an edge
                    V = 128;
                }
                else{
                    int redDifference = image.image[i][j].red - image.image[i-1][j-1].red;
                    int greenDifference = image.image[i][j].green - image.image[i-1][j-1].green;
                    int blueDifference = image.image[i][j].blue - image.image[i-1][j-1].blue;
                    if (Math.abs(blueDifference) > Math.abs(redDifference) && Math.abs(blueDifference) > Math.abs(greenDifference)){
                        V = blueDifference + 128;
                    }
                    else if (Math.abs(greenDifference) > Math.abs(redDifference)){
                        V = greenDifference + 128;
                    }
                    else{
                        V = redDifference + 128;
                    }

                }
                if (V < 0){
                    V = 0;
                }
                else if (V > 255){
                    V = 255;
                }
                Pixel p = new Pixel(V, V, V);
                img.addPixel(p);
            }
        }





        return img;
    }
}
