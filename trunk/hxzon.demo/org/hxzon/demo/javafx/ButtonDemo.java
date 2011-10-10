package org.hxzon.demo.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ButtonDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.WHITE);
        primaryStage.setScene(scene);
        demo(scene, root);
        primaryStage.show();
    }

    private void demo(Scene scene, Group root) {
        Button button = new Button();
        button.setText("OK");
        button.setFont(new Font("Tahoma", 24));
        button.setEffect(new Reflection());

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(button.opacityProperty(), 0);
        final KeyFrame kf = new KeyFrame(Duration.millis(600), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        root.getChildren().add(button);
    }
}
