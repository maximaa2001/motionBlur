package by.bsuir;

import java.io.IOException;

public class CommandLineMain {
    public static void main(String[] args) throws IOException {
        Kernel kernel = new Kernel(Integer.parseInt(args[0]));
        Image image = new Image(args[1]);
        BlurService blurService = new BlurService(image, kernel, args[2]);
        blurService.imposeKernel();
    }
}
