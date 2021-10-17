package io.github.tanialx.jfxoo;

import javafx.scene.Node;

public interface JFXooForm<T> {

    Node node();

    void init(T t);

    T value();
}
