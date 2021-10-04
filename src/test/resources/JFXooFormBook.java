package io.github.tanialx.jfxoo.test;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class JFXooFormBook extends GridPane {

    public JFXooFormBook() {
        super();

        Label label_title = new Label("Title");
        TextField txtF_title = new TextField();
        this.add(label_title, 0, 0);
        this.add(txtF_title, 1, 0);

        Label label_author = new Label("Author");
        TextField txtF_author = new TextField();
        this.add(label_author, 0, 1);
        this.add(txtF_author, 1, 1);

        Label label_published = new Label("Published");
        TextField txtF_published = new TextField();
        this.add(label_published, 0, 2);
        this.add(txtF_published, 1, 2);

        Label label_price = new Label("Price");
        TextField txtF_price = new TextField();
        this.add(label_price, 0, 3);
        this.add(txtF_price, 1, 3);

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
    }
}