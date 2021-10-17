package io.github.tanialx.jfxoo;

import io.github.tanialx.jfxoo.test.JFXooFormBook;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;

public class JFXooCreatorImpl implements JFXooCreator {
    @Override
    public <T> JFXooForm<T> create(String name, Class<T> T) {
        JFXooForm<T> form = null;
        switch(name){
            case "Book" -> form = (JFXooForm<T>) new JFXooFormBook();
            default -> form = null;
        }
        return form;
    }
}
