package io.github.tanialx.jfxoo;

import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public interface JFXooForm<T> {

    VBox node();

    void init(T t);

    T value();

    void setOnSave(Consumer<T> onSave);

    void setOnCancel(Consumer<Void> onCancel);
}
