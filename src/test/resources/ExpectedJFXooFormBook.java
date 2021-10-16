package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;
import java.lang.Override;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class JFXooFormBook implements JFXooForm<Book> {

    private GridPane grid;
    private TextField txtF_title;
    private TextField txtF_author;
    private TextField txtF_published;
    private TextField txtF_price;

    public JFXooFormBook() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        _layout();
    }

    @Override
    public Node node() {
        return grid;
    }

    private void _layout() {
        Label label_title = new Label("Title");
        txtF_title = new TextField();
        grid.add(label_title, 0, 0);
        grid.add(txtF_title, 1, 0);

        Label label_author = new Label("Author");
        txtF_author = new TextField();
        grid.add(label_author, 0, 1);
        grid.add(txtF_author, 1, 1);

        Label label_published = new Label("Published");
        txtF_published = new TextField();
        grid.add(label_published, 0, 2);
        grid.add(txtF_published, 1, 2);

        Label label_price = new Label("Price");
        txtF_price = new TextField();
        grid.add(label_price, 0, 3);
        grid.add(txtF_price, 1, 3);
    }

    @Override
    public void init(Book book) {
        txtF_title.setText(book.getTitle());
        txtF_author.setText(book.getAuthor());
        txtF_published.setText(book.getPublished().toString());
        txtF_price.setText(book.getPrice().toString());
    }
}