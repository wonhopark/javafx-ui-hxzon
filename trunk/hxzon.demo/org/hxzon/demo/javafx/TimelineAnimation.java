package org.hxzon.demo.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimelineAnimation extends Application {

    @Override
    public void start(Stage stage) {
        Group p = new Group();
        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.setWidth(500);
        stage.setHeight(500);
        p.setTranslateX(80);
        p.setTranslateY(80);

        final Rectangle rect = new Rectangle(100, 50, 100, 50);
        rect.setFill(Color.BROWN);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(rect.xProperty(), 300);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        p.getChildren().add(rect);

        stage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
