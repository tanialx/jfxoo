package io.github.tanialx.jfxoo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    private final ObservableList<HBox> scheduledToRemoved = FXCollections.observableArrayList();

    private class RemoveMessage extends TimerTask {
        private final HBox hBox;
        private final Timer timer;
        double co = -1;

        public RemoveMessage(HBox hBox, Timer timer) {
            this.hBox = hBox;
            this.timer = timer;
        }

        @Override
        public void run() {
            if (co == -1) co = hBox.getOpacity();
            if (co >= .4) {
                co -= .01;
                Platform.runLater(() -> {
                    if (node.getChildren().contains(hBox)) {
                        hBox.setOpacity(co);
                        hBox.getChildren().forEach(f -> f.setOpacity(co));
                    }
                });
                return;
            }
            Platform.runLater(() -> node.getChildren().remove(hBox));
            timer.cancel();
        }
    }

    public JFXooFormSnackBar() {
        scheduledToRemoved.addListener((ListChangeListener<HBox>) c ->
                scheduledToRemoved.forEach(prev -> {
                    final Timer timer = new Timer();
                    timer.schedule(new RemoveMessage(prev, timer), 200, 4);
                }));
    }

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

        node.getChildren().forEach(n -> {
            HBox hBox = (HBox) n;
            if (!scheduledToRemoved.contains(hBox)) {
                scheduledToRemoved.add(hBox);
            }
        });
        node.getChildren().add(snackBarItem);
    }
}
