package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LabelDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.ORANGE);
        primaryStage.setScene(scene);
        demo(scene, root);
        primaryStage.setVisible(true);
    }

    private void demo(Scene scene, Group root) {
        final Label label = new Label("A label that needs to be wrapped");
        label.setPrefWidth(100);
        label.setLayoutX(100);
        label.setLayoutY(100);
        label.setWrapText(true);

        label.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                label.setScaleX(1.5);
                label.setScaleY(1.5);
            }
        });

        label.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                label.setScaleX(1);
                label.setScaleY(1);
            }
        });

        root.getChildren().add(label);
    }
}
