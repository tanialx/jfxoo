package io.github.tanialx.jfxoo.test;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class JFXFormBook extends GridPane {

    public JFXFormBook() {
        super();

        final Label label_title = new Label("Title");
        final Label label_author = new Label("Author");
        final Label label_published = new Label("Published");
        final Label label_price = new Label("Price");
        final TextField txtF_title = new TextField();
        final TextField txtF_author = new TextField();
        final TextField txtF_published = new TextField();
        final TextField txtF_price = new TextField();

        int row = 0;
        int col = 0;
        this.add(label_title, col, row); col++;
        this.add(txtF_title, col, row);
        row++; col = 0;
        this.add(label_author, col, row); col++;
        this.add(txtF_author, col, row);
        row++; col = 0;
        this.add(label_published, col, row); col++;
        this.add(txtF_published, col, row);
        row++; col = 0;
        this.add(label_price, col, row); col++;
        this.add(txtF_price, col, row);

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
    }
}