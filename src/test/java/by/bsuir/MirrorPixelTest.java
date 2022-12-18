package by.bsuir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MirrorPixelTest {
    private final Kernel kernel = new Kernel(5);
    private Image image;
    private BufferedImage bufferedImage;
    private int[][] imageRGB;


    @BeforeEach
    public void init() throws IOException {
        image = new Image("/home/maks/a.jpg");
        image.setKernel(kernel);
        bufferedImage = (BufferedImage) findPrivateField(image, "bufferedImage");
        imageRGB = new int[bufferedImage.getHeight()][bufferedImage.getWidth()];
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                imageRGB[i][j] = bufferedImage.getRGB(j, i);
            }
        }
    }

    @Test
    public void negativeY__negativeX__leftTopBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(1, 1), point2Impose.get(new Point(0, 0))[0][0]);
    }

    @Test
    public void negativeY__positiveX__leftTopBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(1, 1), point2Impose.get(new Point(0, 0))[0][3]);
    }

    @Test
    public void positiveY__negativeX__leftTopBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(1, 1), point2Impose.get(new Point(0, 0))[3][0]);
    }

    @Test
    public void positiveY__positiveX__rightTopBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(bufferedImage.getWidth()-1, 1), point2Impose.get(new Point(bufferedImage.getWidth()-2, 0))[0][4]);
    }

    @Test
    public void negativeY__positiveX__rightTopBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(bufferedImage.getWidth()-1, 0), point2Impose.get(new Point(bufferedImage.getWidth()-2, 0))[2][4]);
    }

    @Test
    public void positiveY__negativeX__leftBottomBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(1, bufferedImage.getHeight()-1), point2Impose.get(new Point(0, bufferedImage.getHeight() - 1))[3][0]);
    }

    @Test
    public void positiveY__negativeX__rightBottomBorder__test() {
        Map<Point, int[][]> point2Impose = image.getPoint2Impose();
        assertEquals(bufferedImage.getRGB(bufferedImage.getWidth() -1, bufferedImage.getHeight()-1), point2Impose.get(new Point(bufferedImage.getWidth() - 1, bufferedImage.getHeight() - 1))[3][3]);
    }

    private Object findPrivateField(Object object, String nameField) {
        try {
            Field declaredField = object.getClass().getDeclaredField(nameField);
            declaredField.setAccessible(true);
            Object o = declaredField.get(object);
            declaredField.setAccessible(false);
            return o;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
