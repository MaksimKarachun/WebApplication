package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import main.exception.UploadImageException;
import main.projectEnum.AllowedImageType;
import main.service.interfaces.ImageResizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static main.stringConst.StringConstant.NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE;
import static main.projectEnum.AllowedImageType.JPG;


@Service
@RequiredArgsConstructor
public class ImageService {

  private final StringUtilsService stringUtilsService;

  private final ImageResizer imageResizer;

  @Value("${upload.path}")
  private String uploadPath;

  @Value("${upload.directory}")
  private String directory;

  @Value("${profileImage.type}")
  private String imageType;

  public ResponseEntity<String> uploadCommentImage(MultipartFile image)
      throws UploadImageException, IOException {
    if (!imageTypeCheck(image)) {
      throw new UploadImageException(NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE);
    }
    String imagePath = createImagePath();
    image.transferTo(Path.of(imagePath));
    return new ResponseEntity<>(getShortPathOfImage(imagePath), HttpStatus.OK);
  }

  public String uploadProfileImage(MultipartFile image) throws IOException, UploadImageException {
    BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
    BufferedImage resizeImage = imageResizer.resizeImage(bufferedImage);
    String imagePath = createImagePath();
    ImageIO.write(resizeImage, imageType, new File(imagePath));
    return getShortPathOfImage(imagePath);
  }

  private String createImagePath() throws IOException {
    String randomStr = stringUtilsService.getRandomNumString(12);
    String directoryStrPath = uploadPath + "/" +
        randomStr.substring(0, 4) + "/" +
        randomStr.substring(4, 8) + "/" +
        randomStr.substring(8);
    Path directoryPath = Files.createDirectories(Path.of(directoryStrPath));
    return directoryPath + "/" + stringUtilsService.getRandomNumString(4) + JPG.getFormat();
  }

  private String getShortPathOfImage(String pathOfImage) {
    String shortPath = "";
    Pattern pattern = Pattern.compile(directory + ".+");
    Matcher matcher = pattern.matcher(pathOfImage);
    if (matcher.find()) {
      shortPath = matcher.group();
    }
    return shortPath;
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
