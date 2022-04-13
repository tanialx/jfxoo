package io.github.tanialx.jfxoo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class JFXooFormSnackBar {

    private final VBox node;

    public VBox node() { return node; }

    public JFXooFormSnackBar() {
        node = new VBox();
    }

    public void item(boolean isErr, String text) {
        HBox snackBarItem = new HBox();
        snackBarItem.setMinHeight(50);
        snackBarItem.setBackground(new Background(
                new BackgroundFill(
                        Paint.valueOf( isErr ? "#d5aba3" : "#c3dac1"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        ));
        snackBarItem.setSpacing(4);
        snackBarItem.setPadding(new Insets(10, 10, 10, 10));

        Text tf = new Text(text);
        tf.setDisable(true);
        tf.setFill(Color.BLACK);

        Region reg = new Region();
        HBox.setHgrow(reg, Priority.ALWAYS);

        snackBarItem.setAlignment(Pos.CENTER_LEFT);

        Button close = new Button("\u2715");
        close.setBackground(null);
        close.setFont(Font.font(null, 18));
        close.setOnMouseClicked(evt -> node.getChildren().remove(snackBarItem));

        snackBarItem.getChildren().addAll(tf, reg, close);
        node.getChildren().add(snackBarItem);
    }
}
