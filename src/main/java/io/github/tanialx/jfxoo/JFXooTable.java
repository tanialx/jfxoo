package io.github.tanialx.jfxoo;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public interface JFXooTable<T> {

    TableView<T> table();

    ObservableList<T> data();
}
