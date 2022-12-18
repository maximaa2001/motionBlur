package by.bsuir;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Kernel kernel = new Kernel(5);
        Image image = new Image("/home/maks/a.jpg");
        BlurService blurService = new BlurService(image, kernel);
        blurService.imposeKernel();
    }
}
