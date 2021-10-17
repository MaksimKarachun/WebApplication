package main.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import main.exception.UploadImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static main.stringConst.StringConstant.NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE;
import static main.projectEnum.AllowedImageType.PNG;
import static main.projectEnum.AllowedImageType.JPG;


@Service
@RequiredArgsConstructor
public class ImageService {

  private final StringUtilsService stringUtilsService;

  @Value("${upload.path}")
  private String uploadPath;

  @Value("${upload.directory}")
  private String directory;

  public ResponseEntity<String> uploadImage(MultipartFile image)
      throws UploadImageException, IOException {
    if (!Objects.equals(image.getContentType(), PNG.getContentType()) &&
        !Objects.equals(image.getContentType(), JPG.getContentType())) {
      throw new UploadImageException(NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE);
    }
    Path imageDir = createImagePath();
    String imagePath = "";
    if (Objects.equals(image.getContentType(), PNG.getContentType())) {
      imagePath = imageDir + stringUtilsService.getRandomNumString(4) + PNG.getFormat();
    } else if (Objects.equals(image.getContentType(), JPG.getContentType())) {
      imagePath = imageDir + stringUtilsService.getRandomNumString(4) + JPG.getFormat();
    }
    image.transferTo(Path.of(imagePath));
    return new ResponseEntity<>(getShortPathOfImage(imagePath), HttpStatus.OK);
  }

  private Path createImagePath() throws IOException {
    String randomStr = stringUtilsService.getRandomNumString(12);
    String directoryPath = uploadPath + "/" +
        randomStr.substring(0, 4) + "/" +
        randomStr.substring(4, 8) + "/" +
        randomStr.substring(8) + "/";
    return Files.createDirectories(Path.of(directoryPath));
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
}
