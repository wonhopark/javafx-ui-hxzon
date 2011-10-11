/*
 * Copyright (c) 2008, 2011 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.javafx.experiments.ensemble2.samples.scenegraph.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import com.javafx.experiments.ensemble2.Sample;

/**
 * This sample shows different mouse events and their usage: you can drag the circles across the screen, resize them by mouse wheel and see all other mouse events logged in the console
 *
 * @see javafx.scene.Cursor
 * @see javafx.scene.input.MouseEvent
 * @see javafx.event.EventHandler
 */
public class MouseEventsSample extends Sample {
    //create a console for logging mouse events
    final ListView<String> console = new ListView<String>();
    //create a observableArrayList of logged events that will be listed in console
    final ObservableList<String> consoleObservableList = FXCollections.observableArrayList();
    {
        //set up the console
        console.setItems(consoleObservableList);
        console.setLayoutY(305);
        console.setPrefSize(500, 195);
    }
    //create a rectangle - (500px X 300px) in which our circles can move
    final Rectangle rectangle = new Rectangle(500, 300, new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(1, Color.rgb(156, 216, 255)),
            new Stop(0, Color.rgb(156, 216, 255, 0.5)) }));
    {
        //set rectangle stroke
        rectangle.setStroke(Color.BLACK);
    }
    //variables for storing initial position before drag of circle
    private double initX;
    private double initY;

    public MouseEventsSample() {
        super(500, 500);
        // create circle with method listed below: paramethers: name of the circle, color of the circle, radius
        Circle circleSmall = createCircle("Blue circle", Color.DODGERBLUE, 25);
        circleSmall.setTranslateX(200);
        circleSmall.setTranslateY(80);

        // and a second, bigger circle
        Circle circleBig = createCircle("Orange circle", Color.CORAL, 40);
        circleBig.setTranslateX(300);
        circleBig.setTranslateY(150);

        // we can set mouse event to any node, also on the rectangle
        rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //log mouse move to console, method listed below
                showOnConsole("Mouse moved, x: " + me.getX() + ", y: " + me.getY());
            }
        });
        // show all the circle , rectangle and console
        getChildren().addAll(rectangle, circleBig, circleSmall, console);
    }

    private Point2D dragAnchor;

    private Circle createCircle(final String name, final Color color, int radius) {
        //create a circle with desired name,  color and radius
        final Circle circle = new Circle(radius, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.rgb(250, 250, 255)), new Stop(1, color) }));

        //add a shadow effect
        circle.setEffect(new InnerShadow(7, color.darker().darker()));

        //change a cursor when it is over circle
        circle.setCursor(Cursor.HAND);

        //add a mouse listeners
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                showOnConsole("Clicked on" + name + ", " + me.getClickCount() + "times");
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                double dragX = me.getSceneX() - dragAnchor.getX();
                double dragY = me.getSceneY() - dragAnchor.getY();
                //calculate new position of the circle
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;

                //if new position do not exceeds borders of the rectangle, translate to this position
                if ((newXPosition >= circle.getRadius()) && (newXPosition <= 500 - circle.getRadius())) {
                    circle.setTranslateX(newXPosition);
                }
                if ((newYPosition >= circle.getRadius()) && (newYPosition <= 300 - circle.getRadius())) {
                    circle.setTranslateY(newYPosition);
                }
                showOnConsole(name + " was dragged (x:" + dragX + ", y:" + dragY + ")");
            }
        });

        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                circle.toFront();
                showOnConsole("Mouse entered " + name);
            }
        });

        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                showOnConsole("Mouse exited " + name);
            }
        });

        circle.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                //when mouse is pressed, store initial position
                initX = circle.getTranslateX();
                initY = circle.getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                showOnConsole("Mouse pressed above " + name);
            }
        });

        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                showOnConsole("Mouse released above " + name);
            }
        });

        circle.impl_setOnMouseWheelRotated(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //limit radius to <10, 60>
                double newRadius = circle.getRadius() + me.impl_getWheelRotation();
                if ((newRadius <= 60) && (newRadius >= 10)) {
                    //set new radius
                    circle.setRadius(newRadius);
                }
                showOnConsole("Mouse wheel rotated above " + name + ", with wheel rotation: " + me.impl_getWheelRotation());
            }
        });

        return circle;
    }

    private void showOnConsole(String text) {
        //if there is 8 items in list, delete first log message, shift other logs and  add a new one to end position
        if (consoleObservableList.size() == 8) {
            consoleObservableList.remove(0);
        }
        consoleObservableList.add(text);
    }

    // REMOVE ME
    public static Node createIconContent() {
        final ImageView sample = new ImageView(new Image("http://java.com/images/jv0_oracle.gif"));
        final double iconInitPosX = 11.5;
        final double iconInitPosY = 46;
        sample.setTranslateX(iconInitPosX);
        sample.setTranslateY(iconInitPosY);

        final Polygon cursor = com.javafx.experiments.ensemble2.GraphicsHelper.createArrow();
        final double cursorCorrX = 43;
        final double cursorCorrY = 12;
        cursor.setLayoutX(iconInitPosX + cursorCorrX);
        cursor.setLayoutY(iconInitPosY + cursorCorrY);
        cursor.setRotate(-25);
        cursor.setScaleX(0.7);
        cursor.setScaleY(0.7);
        cursor.setFill(Color.WHITE);
        cursor.setStroke(Color.BLACK);

        Rectangle mouseRect = new Rectangle(0, 0, 114, 114);
        mouseRect.setFill(Color.TRANSPARENT);

        mouseRect.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                cursor.setVisible(false);
            }
        });

        mouseRect.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                sample.setTranslateX(e.getX() - cursorCorrX);
                sample.setTranslateY(e.getY() - cursorCorrY);
            }
        });

        mouseRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                sample.setTranslateX(iconInitPosX);
                sample.setTranslateY(iconInitPosY);
                cursor.setVisible(true);
            }
        });

        return new Group(sample, cursor, mouseRect);
    }
    // END REMOVE ME
}