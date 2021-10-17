package main.projectEnum;

import lombok.Getter;

@Getter
public enum AllowedImageType {

  PNG("image/png", ".png"),
  JPG("image/jpg", ".jpg");

  private final String contentType;
  private final String format;

  AllowedImageType(String contentType, String format) {
    this.contentType = contentType;
    this.format = format;
  }
}
