package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuDemo extends Application {

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
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        menu1.getItems().addAll(new MenuItem("Open"), new MenuItem("Close"), new MenuItem("Exit"));
        Menu menu2 = new Menu("Options");
        menu2.getItems().addAll(new MenuItem("What"), new MenuItem("Why"), new MenuItem("How"));
        Menu menu3 = new Menu("Help");
        menuBar.getMenus().addAll(menu1, menu2, menu3);

        MenuButton menuButton = new MenuButton("Eats");
        menuButton.getItems().addAll(new MenuItem("Burger"), new MenuItem("Hot Dog"));

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(menuBar, menuButton);
        root.getChildren().addAll(vbox);
    }
}
