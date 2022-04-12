package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;
import java.lang.Override;
import java.lang.Void;
import java.math.BigDecimal;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private TableView<Review> in_reviews;
    private Consumer<Book> onSave;
    private Consumer<Void> onCancel;

    public JFXooFormBook() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        _layout();
    }

    @Override
    public void setOnSave(Consumer<Book> onSave) {
        this.onSave = onSave;
    }
    @Override
    public void setOnCancel(Consumer<Void> onCancel) {
        this.onCancel = onCancel;
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
        GridPane.setValignment(label_summary, VPos.TOP);
        in_summary = new TextArea();
        grid.add(label_summary, 0, 5);
        grid.add(in_summary, 1, 5);

        Label label_isInPublicDomain = new Label("Is In Public Domain");
        in_isInPublicDomain = new CheckBox();
        grid.add(label_isInPublicDomain, 0, 6);
        grid.add(in_isInPublicDomain, 1, 6);

        Label label_reviews = new Label("Reviews");
        JFXooTableReview jfxooTable_reviews = new JFXooTableReview();
        in_reviews = jfxooTable_reviews.table();
        grid.add(label_reviews, 0, 7, 2, 1);
        grid.add(jfxooTable_reviews.control(), 0, 8, 2, 1);
        grid.add(in_reviews, 0, 9, 2, 1);

        Button btn_save = new Button("Save");
        btn_save.setOnMouseClicked(evt -> {
            if (onSave != null) onSave.accept(value());
        });
        Button btn_cancel = new Button("Cancel");
        btn_cancel.setOnMouseClicked(evt -> {
            if (onCancel != null) onCancel.accept(null);
        });
        HBox hBox_control = new HBox();
        hBox_control.setSpacing(4);
        hBox_control.setAlignment(Pos.BASELINE_RIGHT);
        hBox_control.getChildren().addAll(btn_cancel, btn_save);
        grid.add(hBox_control, 0, 10, 2, 1);
    }

    @Override
    public void init(Book book) {
        in_title.setText(book.getTitle());
        in_author.setText(book.getAuthor());
        in_publishedDate.setValue(book.getPublishedDate());
        in_price.setText(book.getPrice().toString());
        in_summary.setText(book.getSummary());
        in_isInPublicDomain.setSelected(book.getIsInPublicDomain());
        in_reviews.getItems().setAll(book.getReviews());
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
        t.setReviews(in_reviews.getItems());
        return t;
    }
}