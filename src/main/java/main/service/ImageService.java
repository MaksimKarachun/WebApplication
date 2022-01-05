package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import main.exception.UploadImageException;
import main.model.appEnum.AllowedImageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static main.config.StringConstant.NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE;
import static main.model.appEnum.AllowedImageType.JPG;


@Service
@RequiredArgsConstructor
public class ImageService {

  private final StringUtilsService stringUtilsService;

  private final ImageResizer imageResizer;

  @Value("${upload.directory}")
  private String directory;

  @Value("${profileImage.type}")
  private String imageType;

  public String uploadCommentImage(MultipartFile image)
      throws UploadImageException, IOException {
    if (!imageTypeCheck(image)) {
      throw new UploadImageException(NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE);
    }
    String imagePath = createImagePath();
    image.transferTo(Path.of(imagePath));
    return String.format("/%s", imagePath);
  }

  public String uploadProfileImage(MultipartFile image) throws IOException, UploadImageException {
    BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
    BufferedImage resizeImage = imageResizer.resizeImage(bufferedImage);
    String imagePath = createImagePath();
    ImageIO.write(resizeImage, imageType, new File(imagePath));
    return String.format("/%s", imagePath);
  }

  private String createImagePath() throws IOException {
    String randomStr = stringUtilsService.getRandomNumString(12);
    String directoryStrPath = directory + "/" +
        randomStr.substring(0, 4) + "/" +
        randomStr.substring(4, 8) + "/" +
        randomStr.substring(8);
    Files.createDirectories(Paths.get(directoryStrPath));
    return directoryStrPath + "/" + stringUtilsService.getRandomNumString(4)
        + JPG.getFormat();
  }

  private boolean imageTypeCheck(MultipartFile image) {
    for (AllowedImageType allowedImageType : AllowedImageType.values()) {
      if (allowedImageType.getContentType().equals(image.getContentType())) {
        return true;
      }
    }
    return false;
  }
}
