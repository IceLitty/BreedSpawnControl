package com.gmail.litalways.BreedSpawnControl.Util;

public class Util {
    public static String padLeft(String string, int length, char padChar) {
        if (length - string.length() <= 0) return string;
        char[] result = new char[length];
        System.arraycopy(string.toCharArray(), 0, result, 0, string.length());
        for (int i = string.length(); i < result.length; i++) {
            result[i] = padChar;
        }
        return new String(result);
    }

    public static String padRight(String string, int length, char padChar) {
        int length2 = length - string.length();
        if (length2 <= 0) return string;
        char[] result = new char[length];
        System.arraycopy(string.toCharArray(), 0, result, length2, string.length());
        for (int i = 0; i < length2; i++) {
            result[i] = padChar;
        }
        return new String(result);
    }

    public static boolean isDigitOrDot(String string) {
        for (char c : string.toCharArray()) {
            if (isNotDigitOrDot(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotDigitOrDot(char character) {
        switch (character) {
            case '0': {
                return false;
            }
            case '1': {
                return false;
            }
            case '2': {
                return false;
            }
            case '3': {
                return false;
            }
            case '4': {
                return false;
            }
            case '5': {
                return false;
            }
            case '6': {
                return false;
            }
            case '7': {
                return false;
            }
            case '8': {
                return false;
            }
            case '9': {
                return false;
            }
            case '.': {
                return false;
            }
        }
        return true;
    }
}
