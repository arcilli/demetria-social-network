package com.arrnaux.user.utils;

public class Base64Handler {
    public static String getImageType(char firstCharacter) {
        switch (firstCharacter) {
            case '/':
                return "jpeg";
            case 'i':
                return "png";
            case 'R':
                return "gif";
        }
        return null;
    }
}
