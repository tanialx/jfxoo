package io.github.tanialx.jfxoo.processor.gnrt;

public class Helper {

    private static final String SPACE = "\u0020";

    public static String labelFormat(String s) {
        if (s == null || s.isBlank()) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (i == 0) {
                sb.append(Character.toUpperCase(c));
            } else {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        sb.append(SPACE);
                    }
                    sb.append(c);
                } else {
                    // symbol or digit
                    sb.append(SPACE);
                    sb.append(c);
                    if (i != len - 1) {
                        sb.append(SPACE);
                    }
                }
            }
        }
        return sb.toString();
    }
}
