package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.JFXooForm;
import io.github.tanialx.jfxoo.JFXooFormSnackBar;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JFXooFormBook implements JFXooForm<Book> {

    private VBox node;
    private JFXooFormSnackBar snackBar;
    private HBox hBox_control;
    private TextField in_title;
    private TextField in_author;
    private DatePicker in_publishedDate;
    private TextField in_price;
    private TextArea in_summary;
    private CheckBox in_isInPublicDomain;
    private TableView<Review> in_reviews;

    public JFXooFormBook() {
        node = new VBox();
        snackBar = new JFXooFormSnackBar();
        _layout();
    }

    @Override
    public void button(String buttonText, Consumer<Book> onClicked) {
        Button btn = new Button(buttonText);
        if (onClicked != null) {
            btn.setOnMouseClicked(evt -> onClicked.accept(value()));
        }
        hBox_control.getChildren().add(btn);
    }

    @Override
    public VBox node() {
        return node;
    }

    private void _layout() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Text heading = new Text("Book");
        heading.setFont(Font.font(null, FontWeight.NORMAL, 20));
        grid.add(heading, 0, 0, 2, 1);

        Label label_title = new Label("Title");
        in_title = new TextField();
        grid.add(label_title, 0, 1, 1, 1);
        grid.add(in_title, 1, 1, 1, 1);
        GridPane.setHgrow(in_title, Priority.ALWAYS);

        Label label_author = new Label("Author");
        in_author = new TextField();
        grid.add(label_author, 0, 2, 1, 1);
        grid.add(in_author, 1, 2, 1, 1);
        GridPane.setHgrow(in_author, Priority.ALWAYS);

        Label label_publishedDate = new Label("Published on");
        in_publishedDate = new DatePicker();
        grid.add(label_publishedDate, 0, 3, 1, 1);
        grid.add(in_publishedDate, 1, 3, 1, 1);
        GridPane.setHgrow(in_publishedDate, Priority.ALWAYS);

        Label label_price = new Label("Price");
        in_price = new TextField();
        grid.add(label_price, 0, 4, 1, 1);
        grid.add(in_price, 1, 4, 1, 1);
        GridPane.setHgrow(in_price, Priority.ALWAYS);

        Label label_summary = new Label("Summary");
        GridPane.setValignment(label_summary, VPos.TOP);
        in_summary = new TextArea();
        grid.add(label_summary, 0, 5, 1, 1);
        grid.add(in_summary, 1, 5, 1, 1);
        GridPane.setHgrow(in_summary, Priority.ALWAYS);

        Label label_isInPublicDomain = new Label("Public domain");
        in_isInPublicDomain = new CheckBox();
        grid.add(label_isInPublicDomain, 0, 6, 1, 1);
        grid.add(in_isInPublicDomain, 1, 6, 1, 1);
        GridPane.setHgrow(in_isInPublicDomain, Priority.ALWAYS);

        Label label_reviews = new Label("Reviews");
        JFXooTableReview jfxooTable_reviews = new JFXooTableReview();
        in_reviews = jfxooTable_reviews.table();
        grid.add(label_reviews, 0, 7, 2, 1);
        grid.add(jfxooTable_reviews.node(), 0, 8, 2, 1);
        GridPane.setHgrow(jfxooTable_reviews.node(), Priority.ALWAYS);

        hBox_control = new HBox();
        hBox_control.setSpacing(4);
        hBox_control.setPadding(new Insets(10, 10, 10, 10));
        hBox_control.setAlignment(Pos.BASELINE_RIGHT);

        ScrollPane sp = new ScrollPane(grid);
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);
        node.getChildren().addAll(snackBar.node(), sp, hBox_control);
    }

    @Override
    public void init(Book book) {
        in_title.setText(book.getTitle());
        in_author.setText(book.getAuthor());
        in_publishedDate.setValue(book.getPublishedDate());
        in_price.setText(Optional.ofNullable(book.getPrice()).map(o -> o.toString()).orElse(""));
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

    @Override
    public void info(String msg) {
        snackBar.item(false, msg);
    }

    @Override
    public void error(String msg) {
        snackBar.item(true, msg);
    }
}