package main.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class StringUtilsService {

    private final Pattern NAME_REGISTER_PATTERN = Pattern.compile("^[A-ZА-Я][a-zа-я]{1,25}$");

    public String getRandomString(int strLength) {
        String dictionary = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "1234567890";
        StringBuilder randomString = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            int index = (int) (dictionary.length() * Math.random());
            randomString.append(dictionary.charAt(index));
        }
        return randomString.toString();
    }

    public String getRandomNumString(int strLength) {
        String dictionary = "1234567890";
        StringBuilder randomString = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            int index = (int) (dictionary.length() * Math.random());
            randomString.append(dictionary.charAt(index));
        }
        return randomString.toString();
    }

    public Boolean checkRegisterName(String name) {
        return NAME_REGISTER_PATTERN.matcher(name).find();
    }
}
