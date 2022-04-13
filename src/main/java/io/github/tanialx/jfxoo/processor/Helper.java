package io.github.tanialx.jfxoo.processor;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.List;

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

    public static List<TypeName> typeArgs(TypeName type) {
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).typeArguments;
        }
        return Collections.emptyList();
    }

    public static boolean isFromType(TypeName type, TypeName from) {
        if (type instanceof ParameterizedTypeName) {
            TypeName typeName = ((ParameterizedTypeName) type).rawType;
            return (typeName.equals(from));
        }
        return false;
    }
}
