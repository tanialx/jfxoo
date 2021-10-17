package io.github.tanialx.jfxoo;

public interface JFXooCreator {

    <T> JFXooForm<T> create(String name, Class<T> T);
}
