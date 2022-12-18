package by.bsuir;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Kernel kernel = new Kernel(5);
        Image image = new Image("/home/maks/a.jpg");
        BlurService blurService = new BlurService(image, kernel, "a.jpeg");
        blurService.imposeKernel();
    }
}
