package io.github.tanialx.jfxoo;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface JFXooTable<T> {

    VBox node();

    TableView<T> table();

    HBox control();

    ObservableList<T> data();
}
