package by.bsuir;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String pathToImage = System.getProperty("user.dir").concat("/flowers.jpg");
        String pathToMotionImage = System.getProperty("user.dir").concat("/motionFlowers.jpg");
        Kernel kernel = new Kernel(5);
        Image image = new Image(pathToImage);
        BlurService blurService = new BlurService(image, kernel, pathToMotionImage);
        blurService.imposeKernel();
    }
}
