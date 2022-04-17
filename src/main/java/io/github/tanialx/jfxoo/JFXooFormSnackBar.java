package io.github.tanialx.jfxoo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class JFXooFormSnackBar {

    private final VBox node = new VBox();

    public VBox node() {
        return node;
    }

    public void item(boolean isErr, String text) {
        HBox snackBarItem = new HBox();
        snackBarItem.setMinHeight(50);
        snackBarItem.setBackground(new Background(
                new BackgroundFill(
                        Paint.valueOf(isErr ? "#d5aba3" : "#c3dac1"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        ));
        snackBarItem.setSpacing(4);
        snackBarItem.setPadding(new Insets(10, 10, 10, 10));

        Text tf = new Text(text);
        tf.setFill(Color.BLACK);

        Region reg = new Region();
        HBox.setHgrow(reg, Priority.ALWAYS);

        snackBarItem.setAlignment(Pos.CENTER_LEFT);

        Text close = new Text("\u2715");
        tf.setFill(Color.BLACK);
        close.setFont(Font.font(null, 18));
        close.setOnMouseClicked(evt -> node.getChildren().remove(snackBarItem));

        snackBarItem.getChildren().addAll(tf, reg, close);
        node.getChildren().add(snackBarItem);

        if (node.getChildren().size() > 1) {
            Timer timer = new Timer();
            HBox prev = (HBox) node.getChildren().get(0);
            timer.schedule(new TimerTask() {

                double co = -1;

                @Override
                public void run() {
                    if (co == -1) co = prev.getOpacity();
                    if (co >= .4) {
                        co -= .01;
                        Platform.runLater(() -> {
                            if (node.getChildren().contains(prev)) {
                                prev.setOpacity(co);
                                prev.getChildren().forEach(f -> f.setOpacity(co));
                            }
                        });
                        return;
                    }
                    node.getChildren().remove(prev);
                    timer.cancel();
                }
            }, 1000, 5);
        }
    }
}
