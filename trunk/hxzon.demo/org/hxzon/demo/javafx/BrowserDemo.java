package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class BrowserDemo extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        final WebView browser = new WebView();
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setTitle("Browser Demo");
        stage.setWidth(570);
        stage.setHeight(550);

        browser.setPrefSize(550, 420);

        final TextField url = new TextField();
        url.setText("http://code.google.com/p/javafx-ui-hxzon/");
        Button button = new Button("open");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                browser.getEngine().load(url.getText());
            }
        });
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(url, button, browser);
        root.getChildren().add(vbox);

        stage.setScene(scene);
        stage.setVisible(true);
    }
}
