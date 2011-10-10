package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReflectionEffect extends Application {

	@Override
	public void start(Stage stage) {
		stage.show();

		Scene scene = new Scene(new Group(), 840, 680);
		ObservableList<Node> content = ((Group) scene.getRoot()).getChildren();
		content.add(reflectionText());
		content.add(reflectionButton());
		stage.setScene(scene);
	}

	static Node reflectionText() {
		Text text = new Text();
		text.setX(10.0f);
		text.setY(50.0f);
		text.setCache(true);
		text.setText("Reflection in JavaFX...");
		text.setFill(Color.RED);
		text.setFont(Font.font("null", FontWeight.BOLD, 30));

		Reflection r = new Reflection();
		r.setFraction(0.9);

		text.setEffect(r);

		text.setTranslateY(400);
		return text;
	}

	static Node reflectionButton() {
		Button button = new Button();
		button.setText("OK");
		button.setFont(new Font("Tahoma", 24));
		button.setEffect(new Reflection());
		return button;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
