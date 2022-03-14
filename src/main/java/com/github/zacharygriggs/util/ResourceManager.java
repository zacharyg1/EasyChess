package com.github.zacharygriggs.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ResourceManager {

    private static final Map<String, Image> cache = new HashMap<>();

    public static Image loadResourceAsCachedImage(String fileName) {
        if(cache.containsKey(fileName)) {
            return cache.get(fileName);
        }
        Image img = new Image(loadResourceAsStream(fileName));
        cache.put(fileName, img);
        return img;
    }

    /**
     * Gets the resource as a file, or throws exception if not exists
     *
     * @param fileName File name to load
     * @return Valid input stream of file
     */
    public static InputStream loadResourceAsStream(String fileName) {
        fileName = fileName.replace("\n", "");
        fileName = fileName.replace("\r", "");
        InputStream resourceStream = ClassLoader.getSystemResourceAsStream(fileName);
        if (resourceStream == null) {
            // Try with both slash types.
            fileName = fileName.replace("/", "\\");
            resourceStream = ClassLoader.getSystemResourceAsStream(fileName);
            if (resourceStream == null) {
                fileName = fileName.replace("\\", "/");
                resourceStream = ClassLoader.getSystemResourceAsStream(fileName);
                if (resourceStream == null) {
                    // It really doesn't exist.
                    throw new RuntimeException("No file found at: " + fileName);
                }
            }
        }
        return resourceStream;
    }

}
