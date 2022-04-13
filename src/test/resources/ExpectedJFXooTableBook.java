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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JFXooTableBook implements JFXooTable<Book> {

    private VBox node;
    private HBox control;
    private TableView<Book> table;

    public JFXooTableBook() {
        node = new VBox();
        node.setSpacing(4);
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
        table.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty() && evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2) {
                    Book selected = row.getItem();
                    Stage s = new Stage();
                    JFXooFormBook f = new JFXooFormBook();
                    f.init(selected);
                    f.setOnSave(_f -> {
                        int idx = table.getItems().indexOf(selected);
                        table.getItems().remove(selected);
                        table.getItems().add(idx, _f);
                        s.close();
                    });
                    f.setOnCancel(Void -> s.close());
                    Scene scene = new Scene((GridPane) f.node());
                    s.setScene(scene);
                    s.setTitle("Edit");
                    s.show();
                }
            });
            return row ;
        });
        control = new HBox();
        control.setSpacing(4);
        Button btnADD = new Button("Add");
        Button btnEDT = new Button("Edit");
        Button btnREM = new Button("Remove");
        btnADD.setOnMouseClicked(evt -> {
            Stage s = new Stage();
            JFXooFormBook f = new JFXooFormBook();
            f.setOnSave(_f -> {
                table.getItems().add(_f);
                s.close();
            });
            f.setOnCancel(Void -> s.close());
            Scene scene = new Scene((GridPane) f.node());
            s.setScene(scene);
            s.setTitle("Add");
            s.show();
        });
        btnEDT.setOnMouseClicked(evt -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            Stage s = new Stage();
            JFXooFormBook f = new JFXooFormBook();
            f.init(selected);
            f.setOnSave(_f -> {
                int idx = table.getItems().indexOf(selected);
                table.getItems().remove(selected);
                table.getItems().add(idx, _f);
                s.close();
            });
            f.setOnCancel(Void -> s.close());
            Scene scene = new Scene((GridPane) f.node());
            s.setScene(scene);
            s.setTitle("Edit");
            s.show();
        });
        btnREM.setOnMouseClicked(evt -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                table.getItems().remove(selected);
            }
        });
        control.getChildren().addAll(btnADD, btnEDT, btnREM);
        node.getChildren().addAll(control, table);
    }

    @Override
    public TableView<Book> table() {
        return table;
    }

    @Override
    public HBox control() {
        return control;
    }

    @Override
    public ObservableList<Book> data() {
        return table.getItems();
    }

    @Override
    public VBox node() {
        return node;
    }
}