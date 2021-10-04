package io.github.tanialx.jfxoo.processor.gnrt;

public class Helper {

    public static String labelFormat(String s) {
        if (s == null || s.isBlank()) {
            return s;
        }
        char f = s.charAt(0);
        String F = String.valueOf(Character.toUpperCase(f));
        return (s.length() > 1) ? F + s.substring(1) : F;
    }
}
