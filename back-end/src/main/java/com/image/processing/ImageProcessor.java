package com.image.processing;

import java.awt.Color;
import java.util.Random;
import static com.image.processing.ImageUtils.*;

public class ImageProcessor {
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        pixelCount = Math.max(0, pixelCount);
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Nao é possivel cortar tantos pixels da imagem.");
            return imageTwoD;
        }
    }

    public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] negativeImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                negativeImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return negativeImage;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                invertedImage[i][j] = imageTwoD[imageTwoD.length - 1 - i][imageTwoD[i].length - 1 - j];
            }
        }
        return invertedImage;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] stretchedImage = new int[imageTwoD.length][imageTwoD[0].length * 2];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                stretchedImage[i][2 * j] = imageTwoD[i][j];
                stretchedImage[i][2 * j + 1] = imageTwoD[i][j];
            }
        }
        return stretchedImage;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] shrunkImage = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length / 2; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                shrunkImage[i][j] = imageTwoD[2 * i][j];
            }
        }
        return shrunkImage;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] filteredImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = Math.min(255, Math.max(0, rgba[0] + redChangeValue));
                rgba[1] = Math.min(255, Math.max(0, rgba[1] + greenChangeValue));
                rgba[2] = Math.min(255, Math.max(0, rgba[2] + blueChangeValue));
                filteredImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return filteredImage;
    }

    public static int[][] grayscale(int[][] imageTwoD) {
        int[][] out = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int gray = (int)Math.round(0.299 * rgba[0] + 0.587 * rgba[1] + 0.114 * rgba[2]);
                rgba[0] = gray;
                rgba[1] = gray;
                rgba[2] = gray;
                out[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return out;
    }

    public static int[][] paintRandomImage(int[][] canvas) {
        Random random = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)).getRGB();
                canvas[i][j] = randomColor;
            }
        }
        return canvas;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
        for (int i = rowPosition; i < rowPosition + height && i < canvas.length; i++) {
            for (int j = colPosition; j < colPosition + width && j < canvas[0].length; j++) {
                canvas[i][j] = color;
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random random = new Random();
        int rows = canvas.length;
        int cols = canvas[0].length;
        for (int i = 0; i < numRectangles; i++) {
            int width = random.nextInt(cols / 3) + 10;
            int height = random.nextInt(rows / 3) + 10;
            int rowPosition = random.nextInt(rows - height);
            int colPosition = random.nextInt(cols - width);
            int randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)).getRGB();
            canvas = paintRectangle(canvas, width, height, rowPosition, colPosition, randomColor);
        }
        return canvas;
    }
}
