package main.model.appEnum;

import lombok.Getter;

@Getter
public enum AllowedImageType {

  PNG("image/png", ".png"),
  JPG("image/jpg", ".jpg"),
  JPEG("image/jpeg", ".jpeg");

  private final String contentType;
  private final String format;

  AllowedImageType(String contentType, String format) {
    this.contentType = contentType;
    this.format = format;
  }
}
