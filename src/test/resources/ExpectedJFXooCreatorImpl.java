package io.github.tanialx.jfxoo;

import io.github.tanialx.jfxoo.test.JFXooFormBook;
import io.github.tanialx.jfxoo.test.JFXooTableBook;
import io.github.tanialx.jfxoo.test.JFXooTableReview;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;

public class JFXooCreatorImpl implements JFXooCreator {

    @Override
    public <T> JFXooForm<T> form(String name, Class<T> T) {
        JFXooForm<T> form = null;
        switch(name){
            case "Book" -> form = (JFXooForm<T>) new JFXooFormBook();
            default -> form = null;
        }
        return form;
    }

    @Override
    public <T> JFXooTable<T> table(String name, Class<T> T) {
        JFXooTable<T> table = null;
        switch(name){
            case "Book" -> table = (JFXooTable<T>) new JFXooTableBook();
            case "Review" -> table = (JFXooTable<T>) new JFXooTableReview();
            default -> table = null;
        }
        return table;
    }
}
