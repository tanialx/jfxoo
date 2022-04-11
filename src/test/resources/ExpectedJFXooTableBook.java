package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooTable;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class JFXooTableBook implements JFXooTable<Book> {

    private TableView<Book> table;

    public JFXooTableBook() {
        table = new TableView<>();

        TableColumn<Book, String> c_title = new TableColumn<>("Title");
        c_title.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getTitle()));

        TableColumn<Book, String> c_author = new TableColumn<>("Author");
        c_author.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getAuthor()));

        TableColumn<Book, LocalDate> c_publishedDate = new TableColumn<>("Published Date");
        c_publishedDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getPublishedDate()));

        TableColumn<Book, BigDecimal> c_price = new TableColumn<>("Price");
        c_price.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getPrice()));

        TableColumn<Book, String> c_summary = new TableColumn<>("Summary");
        c_summary.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getSummary()));

        TableColumn<Book, Boolean> c_isInPublicDomain = new TableColumn<>("Is In Public Domain");
        c_isInPublicDomain.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getIsInPublicDomain()));

        table.getColumns().addAll(Arrays.asList(c_title, c_author, c_publishedDate, c_price, c_summary, c_isInPublicDomain));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(false);
    }

    @Override
    public TableView<Book> table() {
        return table;
    }

    @Override
    public ObservableList<Book> data() {
        return table.getItems();
    }
}