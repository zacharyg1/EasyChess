package com.github.zacharygriggs.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageHelper {

    /**
     * Resizes the image
     * <p>
     * Workaround for the lacking functionality of the Image class is
     * to convert into a byte array, then convert the byte array into an
     * input stream and finally the input stream into an image.
     *
     * @param source    Source image
     * @param newWidth  Desired width
     * @param newHeight Desired height
     * @return Resized image
     */
    public static Image resize(Image source, int newWidth, int newHeight) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // Ugly conversion because JavaFX image is terrible.
            BufferedImage convertedImage = SwingFXUtils.fromFXImage(source, null);
            if (convertedImage != null) {
                ImageIO.write(convertedImage, "png", os);
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                return new Image(is, newWidth, newHeight, false, false);
            } else {
                return source;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Adjusts a color by an amount
     * Clamping at 1.0 and 0.0
     *
     * @param original Original color value [0.0, 1.0]
     * @param mod      Change in color
     * @return New color value [0.0, 1.0]
     */
    private static double adjust(double original, double mod) {
        original += mod;
        if (original > 1.0) {
            original = 1.0;
        }
        if (original < 0.0) {
            original = 0.;
        }
        return original;
    }

    /**
     * Changes the color of the image
     *
     * @param source   Source image
     * @param redMod   Change in red [-1.0, 1.0]
     * @param greenMod Change in green [-1.0, 1.0]
     * @param blueMod  Change in blue [-1.0, 1.0]
     * @return Changed color
     */
    public static Image recolor(Image source, double redMod, double greenMod, double blueMod) {
        WritableImage newImage = new WritableImage((int) source.getWidth(), (int) source.getHeight());
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                Color pixel = source.getPixelReader().getColor(x, y);
                double red = adjust(pixel.getRed(), redMod);
                double green = adjust(pixel.getGreen(), greenMod);
                double blue = adjust(pixel.getBlue(), blueMod);
                pixel = new Color(red, green, blue, pixel.getOpacity());
                newImage.getPixelWriter().setColor(x, y, pixel);
            }
        }
        return newImage;
    }
}
