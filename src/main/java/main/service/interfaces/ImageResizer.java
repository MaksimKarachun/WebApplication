package main.service.interfaces;

import java.awt.image.BufferedImage;

public interface ImageResizer {

  BufferedImage resizeImage(BufferedImage originalImage);
}
