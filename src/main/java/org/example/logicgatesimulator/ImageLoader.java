package org.example.logicgatesimulator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class ImageLoader {

    public static ImageView loadImage(String imagePath, double fitWidth) {
        ImageView imageView = new ImageView();

        if (imagePath.endsWith(".svg")) {
            // SVG: Lade als Stylesheet oder konvertiere zu PNG zur Laufzeit
            InputStream is = ImageLoader.class.getResourceAsStream("/images/" + imagePath);
            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
                imageView.setFitWidth(fitWidth);
                imageView.setPreserveRatio(true);
            }
        } else {
            // PNG/JPG: Standard Image Loading
            InputStream is = ImageLoader.class.getResourceAsStream("/images/" + imagePath);
            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
                imageView.setFitWidth(fitWidth);
                imageView.setPreserveRatio(true);
            }
        }

        return imageView;
    }

    public static Image loadImageAsImage(String imagePath) {
        InputStream is = ImageLoader.class.getResourceAsStream("/images/" + imagePath);
        if (is != null) {
            return new Image(is);
        }
        return null;
    }
}
