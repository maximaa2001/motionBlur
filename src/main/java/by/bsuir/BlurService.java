package by.bsuir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BlurService {
    private Image image;
    private Kernel kernel;
    private Map<Point, int[][]> point2Impose;
    private String outputPath;

    public BlurService(Image image, Kernel kernel, String outputPath) {
        this.image = image;
        this.kernel = kernel;
        this.outputPath = outputPath;
        point2Impose = new HashMap<>();
    }

    public void imposeKernel() throws IOException {
        int[][] newBufferedImagePixels = new int[image.getHeight()][image.getWidth()];
        int additionalCoordinates = kernel.getBorder() / 2;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                point2Impose.put(new Point(j, i), imposeKernel(kernel, j, i, additionalCoordinates));
            }
        }
        point2Impose.forEach((key, value) -> {
            int[][] redColorMatrix = getColorMatrix(value, this::getRed);
            int[][] greenColorMatrix = getColorMatrix(value, this::getGreen);
            int[][] blueColorMatrix = getColorMatrix(value, this::getBlue);
            int red = multipleOnKernel(redColorMatrix) / kernel.getBorder();
            int green = multipleOnKernel(greenColorMatrix) / kernel.getBorder();
            int blue = multipleOnKernel(blueColorMatrix) / kernel.getBorder();
            int rgb = (red << 16 | green << 8 | blue);
            newBufferedImagePixels[key.getY()][key.getX()] = rgb;
        });
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), 1);
        for (int i = 0; i < newBufferedImagePixels.length; i++) {
            for (int j = 0; j < newBufferedImagePixels[i].length; j++) {
                newBufferedImage.setRGB(j, i, newBufferedImagePixels[i][j]);
            }
        }
        createFile(newBufferedImage);
    }

    private int[][] imposeKernel(Kernel kernel, int x, int y, int additionalCoordinates) {
        List<Integer> values = new ArrayList<>();
        int leftBorder = x - additionalCoordinates;
        int rightBorder = x + additionalCoordinates;
        int upBorder = y - additionalCoordinates;
        int bottomBorder = y + additionalCoordinates;
        for (int i = upBorder; i <= bottomBorder; i++) {
            for (int j = leftBorder; j <= rightBorder; j++) {
                if (i < 0 || j < 0 || i >= image.getHeight() || j >= image.getWidth()) {
                    values.add(findMirrorPixel(j, i));
                } else {
                    values.add(image.getBufferedImage().getRGB(j, i));
                }
            }
        }
        return generateFromList(values, kernel);
    }

    private int findMirrorPixel(int x, int y) {
        if (y < 0) {
            y = Math.abs(y) - 1;
        } else if (y >= image.getHeight()) {
            int tempY = image.getHeight() - 1;
            int constantY = tempY + tempY + 1;
            y = constantY - y;
        }
        if (x < 0) {
            x = Math.abs(x) - 1;
        } else if (x >= image.getWidth()) {
            int tempX = image.getWidth() - 1;
            int constantX = tempX + tempX + 1;
            x = constantX - x;
        }
        return image.getBufferedImage().getRGB(x, y);
    }

    private int[][] generateFromList(List<Integer> values, Kernel kernel) {
        int[][] arrayForPixel = new int[kernel.getBorder()][kernel.getBorder()];
        for (int i = 0; i < arrayForPixel.length; i++) {
            for (int j = 0; j < arrayForPixel[i].length; j++) {
                arrayForPixel[i][j] = values.remove(0);
            }
        }
        return arrayForPixel;
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    private int multipleOnKernel(int[][] pixelMatrix) {
        int result = 0;
        for (int i = 0; i < pixelMatrix.length; i++) {
            for (int j = 0; j < pixelMatrix[i].length; j++) {
                result += pixelMatrix[i][j] * kernel.getKernel()[i][j];
            }
        }
        return result;
    }

    private int[][] getColorMatrix(int[][] rgbMatrix, Function<Integer, Integer> getConcreteColor) {
        int[][] concreteColorMatrix = new int[rgbMatrix.length][rgbMatrix.length];
        for (int i = 0; i < concreteColorMatrix.length; i++) {
            for (int j = 0; j < concreteColorMatrix[i].length; j++) {
                concreteColorMatrix[i][j] = getConcreteColor.apply(rgbMatrix[i][j]);
            }
        }
        return concreteColorMatrix;
    }

    private void createFile(BufferedImage newBufferedImage) throws IOException {
        Path path = Paths.get(outputPath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        ImageIO.write(newBufferedImage, "jpeg", new File(outputPath));
    }
}
