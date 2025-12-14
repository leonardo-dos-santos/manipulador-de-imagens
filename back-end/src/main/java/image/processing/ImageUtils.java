package image.processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            String s = inputFileOrLink == null ? "" : inputFileOrLink.trim();
            if (s.toLowerCase().startsWith("http")) {
                URL imageUrl = java.net.URI.create(s).toURL();
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Falha ao obter imagem da URL fornecida.");
                }
            } else {
                if (s.isEmpty()) return null;
                image = ImageIO.read(new File(s));
            }
            if (image == null) return null;

            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Falha ao carregar imagem: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static int[][] imgToTwoD(BufferedImage image) {
        if (image == null) return null;
        int imgRows = image.getHeight();
        int imgCols = image.getWidth();
        int[][] pixelData = new int[imgRows][imgCols];
        for (int i = 0; i < imgRows; i++) {
            for (int j = 0; j < imgCols; j++) {
                pixelData[i][j] = image.getRGB(j, i);
            }
        }
        return pixelData;
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        if (imgData == null || imgData.length == 0) return;
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
            System.out.println("Imagem salva com sucesso em: " + fileName);
        } catch (Exception e) {
            System.out.println("Falha ao salvar imagem: " + e.getLocalizedMessage());
        }
    }

    public static byte[] twoDToJpegBytes(int[][] imgData) {
        if (imgData == null || imgData.length == 0) return new byte[0];
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(result, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Numero incorreto de elementos no array RGBA.");
            return -1;
        }
    }
}

