package by.bsuir;

import java.util.Arrays;

public class Kernel {
    private final int border;

    private int[][] kernel;

    public Kernel(int border) {
        this.border = border;
        createKernel(border);
    }

    public int getBorder() {
        return border;
    }

    public int[][] getKernel() {
        return kernel;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int[] ints : kernel) {
            builder.append(Arrays.toString(ints)).append("\n");
        }
        return builder.toString();
    }

    private void createKernel(int border) {
        kernel = new int[border][border];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[i].length; j++) {
                kernel[i][j] = (i == j) ? 1 : 0;
            }
        }
    }
}
