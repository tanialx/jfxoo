package io.github.tanialx.jfxoo;

public interface JFXooCreator {

    <T> JFXooForm<T> form(String name, Class<T> T);

    <T> JFXooTable<T> table(String name, Class<T> T);
}
