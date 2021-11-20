package main;

import org.jsoup.Jsoup;

public class Test {


  public static void main(String[] args) {

    int limit = 50;

    String str = "bla <b>hehe</b> <br> this is awesome simple";
    String strWithoutHtml = Jsoup.parse(str).text();


    //String str1 = str.substring(0, limit);
    //String str2 = str1.substring(0, str1.lastIndexOf(" "));

    System.out.println(Jsoup.parse(str).text());
  }

}
