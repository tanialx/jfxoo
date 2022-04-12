package io.github.tanialx.jfxoo;

import javafx.scene.Node;

import java.util.function.Consumer;

public interface JFXooForm<T> {

    Node node();

    void init(T t);

    T value();

    void setOnSave(Consumer<T> onSave);

    void setOnCancel(Consumer<Void> onCancel);
}
