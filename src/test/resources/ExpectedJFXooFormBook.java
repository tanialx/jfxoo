package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;
import java.lang.Override;
import java.math.BigDecimal;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JFXooFormBook implements JFXooForm<Book> {

    private GridPane grid;
    private TextField in_title;
    private TextField in_author;
    private DatePicker in_publishedDate;
    private TextField in_price;
    private TextArea in_summary;
    private CheckBox in_isInPublicDomain;

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
        Text heading = new Text("Book");
        heading.setFont(Font.font(null, FontWeight.NORMAL, 20));
        grid.add(heading, 0, 0, 2, 1);

        Label label_title = new Label("Title");
        in_title = new TextField();
        grid.add(label_title, 0, 1);
        grid.add(in_title, 1, 1);

        Label label_author = new Label("Author");
        in_author = new TextField();
        grid.add(label_author, 0, 2);
        grid.add(in_author, 1, 2);

        Label label_publishedDate = new Label("Published Date");
        in_publishedDate = new DatePicker();
        grid.add(label_publishedDate, 0, 3);
        grid.add(in_publishedDate, 1, 3);

        Label label_price = new Label("Price");
        in_price = new TextField();
        grid.add(label_price, 0, 4);
        grid.add(in_price, 1, 4);

        Label label_summary = new Label("Summary");
        in_summary = new TextArea();
        grid.add(label_summary, 0, 5);
        grid.add(in_summary, 1, 5);

        Label label_isInPublicDomain = new Label("Is In Public Domain");
        in_isInPublicDomain = new CheckBox();
        grid.add(label_isInPublicDomain, 0, 6);
        grid.add(in_isInPublicDomain, 1, 6);
    }

    @Override
    public void init(Book book) {
        in_title.setText(book.getTitle());
        in_author.setText(book.getAuthor());
        in_publishedDate.setValue(book.getPublishedDate());
        in_price.setText(book.getPrice().toString());
        in_summary.setText(book.getSummary());
        in_isInPublicDomain.setSelected(book.getIsInPublicDomain());
    }

    @Override
    public Book value() {
        Book t = new Book();
        t.setTitle(in_title.getText());
        t.setAuthor(in_author.getText());
        t.setPublishedDate(in_publishedDate.getValue());
        t.setPrice(new BigDecimal(in_price.getText()));
        t.setSummary(in_summary.getText());
        t.setIsInPublicDomain(in_isInPublicDomain.isSelected());
        return t;
    }
}