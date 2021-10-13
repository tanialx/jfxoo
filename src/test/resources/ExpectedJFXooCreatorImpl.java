package io.github.tanialx.jfxoo;

import io.github.tanialx.jfxoo.test.JFXooFormBook;
import java.lang.Override;
import java.lang.String;

public class JFXooCreatorImpl implements JFXooCreator {
    @Override
    public JFXooForm create(String name) {
        JFXooForm form = null;
        switch(name){
            case "Book" -> form = new JFXooFormBook();
            default -> form = null;
        }
        return form;
    }
}
