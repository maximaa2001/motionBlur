package by.bsuir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private final BufferedImage bufferedImage;
    private final int width;
    private final int height;

    public Image(String path) throws IOException {
        bufferedImage = ImageIO.read(new File(path));
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
