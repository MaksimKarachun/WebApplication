package main.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class StringUtilsService {

  private final Pattern NAME_REGISTER_PATTERN = Pattern.compile("^[A-ZА-Я][a-zа-я]{1,25}$");

  public Boolean checkRegisterName(String name) {
    return NAME_REGISTER_PATTERN.matcher(name).find();
  }

  public String getRandomString(int strLength) {
    String dictionary = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    return getRandomFromDictionary(strLength, dictionary);
  }

  public String getRandomNumString(int strLength) {
    String dictionary = "1234567890";
    return getRandomFromDictionary(strLength, dictionary);
  }

  public String cuttingText (String text, int finalLength) {
    String textWithoutHtml = Jsoup.parse(text).text();
    String finalText = null;
    if (textWithoutHtml.length() > finalLength) {
      finalText = textWithoutHtml.substring(0, finalLength);
      finalText = finalText.substring(0 , finalText.lastIndexOf(" "));
    }
    return finalText + "...";
  }

  private String getRandomFromDictionary(int strLength, String dictionary) {
    StringBuilder randomString = new StringBuilder(strLength);
    for (int i = 0; i < strLength; i++) {
      int index = (int) (dictionary.length() * Math.random());
      randomString.append(dictionary.charAt(index));
    }
    return randomString.toString();
  }
}
