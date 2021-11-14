package main.service.implementation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import main.service.interfaces.ImageResizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ImageResizerImpl implements ImageResizer {

  @Value("${profileImage.wight}")
  private int standardImageWight;

  @Value("${profileImage.height}")
  private int standardImageHeight;

  @Override
  public BufferedImage resizeImage(BufferedImage originalImage) {
    Image resultingImage = originalImage
        .getScaledInstance(standardImageWight, standardImageHeight, Image.SCALE_DEFAULT);
    BufferedImage outputImage = new BufferedImage(standardImageWight, standardImageHeight,
        BufferedImage.TYPE_INT_RGB);
    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
    return outputImage;
  }
}
