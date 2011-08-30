package org.hxzon.demo.javafx;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ParallelTransitionDemo extends Application {

	@Override
	public void start(Stage stage) {
		Group p = new Group();
		Scene scene = new Scene(p);
		stage.setScene(scene);
		stage.setWidth(500);
		stage.setHeight(500);
		p.setTranslateX(80);
		p.setTranslateY(80);

		//rect
		Rectangle rect = new Rectangle(10, 200, 50, 50);
		rect.setArcHeight(15);
		rect.setArcWidth(15);
		rect.setFill(Color.DARKBLUE);
		rect.setTranslateX(50);
		rect.setTranslateY(75);
		//fade transition
		FadeTransition fadeTransition = new FadeTransition(Duration.valueOf(3000), rect);
		fadeTransition.setFromValue(1.0f);
		fadeTransition.setToValue(0.3f);
		fadeTransition.setCycleCount(2);
		fadeTransition.setAutoReverse(true);
		//translate transition
		TranslateTransition translateTransition = new TranslateTransition(Duration.valueOf(2000), rect);
		translateTransition.setFromX(50);
		translateTransition.setToX(350);
		translateTransition.setCycleCount(2);
		translateTransition.setAutoReverse(true);
		//rotate transition
		RotateTransition rotateTransition = new RotateTransition(Duration.valueOf(3000), rect);
		rotateTransition.setByAngle(180f);
		rotateTransition.setCycleCount(4);
		rotateTransition.setAutoReverse(true);
		//scale transition
		ScaleTransition scaleTransition = new ScaleTransition(Duration.valueOf(2000), rect);
		scaleTransition.setToX(2f);
		scaleTransition.setToY(2f);
		scaleTransition.setCycleCount(2);
		scaleTransition.setAutoReverse(true);
		//parallel transition
		ParallelTransition parallelTransition = new ParallelTransition();
		parallelTransition.getChildren().addAll(fadeTransition, translateTransition, rotateTransition, scaleTransition);
		parallelTransition.setCycleCount(Timeline.INDEFINITE);
		parallelTransition.play();

		p.getChildren().add(rect);

		stage.setVisible(true);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
