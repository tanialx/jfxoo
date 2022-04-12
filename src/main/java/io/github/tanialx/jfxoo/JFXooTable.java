package io.github.tanialx.jfxoo;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public interface JFXooTable<T> {

    TableView<T> table();

    HBox control();

    ObservableList<T> data();
}
