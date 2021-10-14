package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;
import java.lang.Object;
import java.lang.Override;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class JFXooFormBook implements JFXooForm {

    private GridPane grid;

    public JFXooFormBook() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        _layout();
    }

    @Override
    public Object node() {
        return grid;
    }

    private void _layout() {
        Label label_title = new Label("Title");
        TextField txtF_title = new TextField();
        grid.add(label_title, 0, 0);
        grid.add(txtF_title, 1, 0);

        Label label_author = new Label("Author");
        TextField txtF_author = new TextField();
        grid.add(label_author, 0, 1);
        grid.add(txtF_author, 1, 1);

        Label label_published = new Label("Published");
        TextField txtF_published = new TextField();
        grid.add(label_published, 0, 2);
        grid.add(txtF_published, 1, 2);

        Label label_price = new Label("Price");
        TextField txtF_price = new TextField();
        grid.add(label_price, 0, 3);
        grid.add(txtF_price, 1, 3);
    }
}