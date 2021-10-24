package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;

import java.lang.Override;
import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class JFXooFormBook implements JFXooForm<Book> {

    private GridPane grid;
    private TextField in_title;
    private TextField in_author;
    private TextField in_published;
    private TextField in_price;

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
        in_title = new TextField();
        grid.add(label_title, 0, 0);
        grid.add(in_title, 1, 0);

        Label label_author = new Label("Author");
        in_author = new TextField();
        grid.add(label_author, 0, 1);
        grid.add(in_author, 1, 1);

        Label label_published = new Label("Published");
        in_published = new TextField();
        grid.add(label_published, 0, 2);
        grid.add(in_published, 1, 2);

        Label label_price = new Label("Price");
        in_price = new TextField();
        grid.add(label_price, 0, 3);
        grid.add(in_price, 1, 3);
    }

    @Override
    public void init(Book book) {
        in_title.setText(book.getTitle());
        in_author.setText(book.getAuthor());
        in_published.setText(book.getPublished().toString());
        in_price.setText(book.getPrice().toString());
    }

    @Override
    public Book value() {
        Book t = new Book();
        t.setTitle(in_title.getText());
        t.setAuthor(in_author.getText());
        t.setPublished(LocalDate.parse(in_published.getText()));
        t.setPrice(new BigDecimal(in_price.getText()));
        return t;
    }
}