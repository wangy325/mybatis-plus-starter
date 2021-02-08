package com.wangy.common.utils;

import java.util.regex.Pattern;

/**
 * @author wangy
 * @date 2021-2-8 14:29
 */
public class StringUtils {
    /**
     * transfer first char of a string to upper case
     * <p>
     * "string" --> "String"
     */
    static String firstCharCap(String string) {
        return string.replaceFirst("\\w", String.valueOf(string.charAt(0)).toUpperCase());
    }

    /**
     * get the first string split by regex
     * <p>
     * ("string", "i") -> "str"
     */
    static String subStringBefore(String string, String regex) {
        return string.split(regex)[0];
    }

    /**
     * Turn a empty string to null
     * <p>
     * "" -> null<br>
     * "  " -> null<br>
     * "a b" -> "a b"<br>
     */
    static String emptyToNull(String string) {
        if (string.trim().isEmpty()) {
            System.out.println("is empty");
            string = null;
        }
        return string;
    }

    public static void main(String[] args) {
        String a = "a   b";

        System.out.println(emptyToNull(a));
    }
}
