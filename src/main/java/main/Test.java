package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {


  public static void main(String[] args) {
    String result = getShortPathOfImage("/User/uploads/aasd/asd/asd/123/4123");
    System.out.println(result);
  }


  private static String getShortPathOfImage(String pathOfImage) {
    String shortPath = "";
    Pattern pattern = Pattern.compile("/upload.+");
    Matcher matcher = pattern.matcher(pathOfImage);
    if (matcher.find()) {
      shortPath = matcher.group();
    }
    return shortPath;
  }

}
