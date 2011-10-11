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
package com.javafx.experiments.ensemble2;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Base class for all Ensemble samples
 */
public class Sample extends Pane {
    protected static final Image ICON_48 = new Image(Ensemble2.class.getResourceAsStream("images/icon-48x48.png"));
    protected static final Image BRIDGE = new Image(Ensemble2.class.getResourceAsStream("images/sanfran.jpg"));
    protected static final Image BOAT = new Image(Ensemble2.class.getResourceAsStream("images/boat.jpg"));
    private static final NumberFormat twoDp = new DecimalFormat("0.##");

    private double width = -1;
    private double height = -1;
    private Node controls = null;

    public Sample() {
    }

    public Sample(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getSampleHeight() {
        return height;
    }

    public double getSampleWidth() {
        return width;
    }

    public void play() {
    }

    public void stop() {
    }

    protected void setControls(PropDesc... properties) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("sample-control-grid");
        grid.setVgap(10);
        grid.setHgap(10);
        int row = 0;
        for (PropDesc property : properties) {
            final PropDesc prop = property;
            Label propName = new Label(prop.name + ":");
            propName.getStyleClass().add("sample-control-grid-prop-label");
            GridPane.setConstraints(propName, 0, row);
            grid.getChildren().add(propName);
            if (prop.valueModel instanceof DoubleProperty) {
                final Label valueLabel = new Label(twoDp.format(prop.initialValue));
                GridPane.setConstraints(valueLabel, 2, row);
                final Slider slider = new Slider();
                slider.setMin(prop.min);
                slider.setMax(prop.max);
                slider.setValue(((Number) prop.initialValue).doubleValue());
                GridPane.setConstraints(slider, 1, row);
                slider.setMaxWidth(Double.MAX_VALUE);
                slider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable ov) {
                        set(prop.valueModel, slider.getValue());
                        valueLabel.setText(twoDp.format(slider.getValue()));
                    }
                });
                grid.getChildren().addAll(slider, valueLabel);
            } else { //if (prop.property.getType() == Color.class || prop.property.getType() == Paint.class) {
                // FIXME we assume anything that isn't a double property is now a colour property
                final Rectangle colorRect = new Rectangle(20, 20, (Color) prop.initialValue);
                colorRect.setStroke(Color.GRAY);
                final Label valueLabel = new Label(formatWebColor((Color) prop.initialValue));
                valueLabel.setGraphic(colorRect);
                valueLabel.setContentDisplay(ContentDisplay.LEFT);
                GridPane.setConstraints(valueLabel, 2, row);
                final ColorPicker colorPicker = new ColorPicker();
                GridPane.setConstraints(colorPicker, 1, row);
                colorPicker.getColor().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable valueModel) {
                        Color c = colorPicker.getColor().get();
                        set(prop.valueModel, c);
                        valueLabel.setText(formatWebColor(c));
                        colorRect.setFill(c);
                    }
                });
                grid.getChildren().addAll(colorPicker, valueLabel);
            }
            row++;
        }
        controls = grid;
    }

    public Node getControls() {
        return controls;
    }

    private String formatWebColor(Color c) {
        String r = Integer.toHexString((int) (c.getRed() * 255));
        if (r.length() == 1)
            r = "0" + r;
        String g = Integer.toHexString((int) (c.getGreen() * 255));
        if (g.length() == 1)
            g = "0" + g;
        String b = Integer.toHexString((int) (c.getBlue() * 255));
        if (b.length() == 1)
            b = "0" + b;
        return "#" + r + g + b;
    }

    public static Object get(ObservableValue valueModel) {
        if (valueModel instanceof DoubleProperty) {
            return ((DoubleProperty) valueModel).get();
        } else if (valueModel instanceof ObjectProperty) {
            return ((ObjectProperty) valueModel).get();
        }

        return null;
    }

    public static void set(ObservableValue valueModel, Object value) {
        if (valueModel instanceof DoubleProperty) {
            ((DoubleProperty) valueModel).set((Double) value);
        } else if (valueModel instanceof ObjectProperty) {
            ((ObjectProperty) valueModel).set(value);
        }
    }

    protected class PropDesc {
        private String name;
        private Double min;
        private Double max;
        private Object initialValue;
        private ObservableValue valueModel;

        public PropDesc(String name, ObservableValue valueModel) {
            this.name = name;
            this.valueModel = valueModel;
            this.initialValue = get(valueModel);
        }

        public PropDesc(String name, DoubleProperty valueModel, Double min, Double max) {
            this.name = name;
            this.valueModel = valueModel;
            this.initialValue = valueModel.get();
            this.min = min;
            this.max = max;
        }
    }

    protected class ColorPicker extends Region {
        private final ObjectProperty<Color> color = new SimpleObjectProperty<Color>();
        private Rectangle hsbRect = new Rectangle(200, 30, buildHueBar());
        private Rectangle lightRect = new Rectangle(200, 30, new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.WHITE),
                new Stop(0.5, Color.rgb(255, 255, 255, 0)), new Stop(0.501, Color.rgb(0, 0, 0, 0)), new Stop(1, Color.BLACK), }));

        public ColorPicker() {
            getChildren().addAll(hsbRect, lightRect);
            lightRect.setStroke(Color.GRAY);
            lightRect.setStrokeType(StrokeType.OUTSIDE);
            EventHandler<MouseEvent> ml = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    double w = getWidth();
                    double h = getHeight();
                    double x = Math.min(w, Math.max(0, e.getX()));
                    double y = Math.min(h, Math.max(0, e.getY()));
                    double hue = (360 / w) * x;
                    double vert = (1 / h) * y;
                    double sat = 0;
                    double bright = 0;
                    if (vert < 0.5) {
                        bright = 1;
                        sat = vert * 2;
                    } else {
                        bright = sat = 1 - ((vert - 0.5) * 2);
                    }
                    // convert back to color
                    Color c = Color.hsb((int) hue, sat, bright);
                    color.set(c);
                    e.consume();
                }
            };
            lightRect.setOnMouseDragged(ml);
            lightRect.setOnMouseClicked(ml);
        }

        @Override
        protected double computeMinWidth(double height) {
            return 200;
        }

        @Override
        protected double computeMinHeight(double width) {
            return 30;
        }

        @Override
        protected double computePrefWidth(double height) {
            return 200;
        }

        @Override
        protected double computePrefHeight(double width) {
            return 30;
        }

        @Override
        protected double computeMaxWidth(double height) {
            return Double.MAX_VALUE;
        }

        @Override
        protected double computeMaxHeight(double width) {
            return Double.MAX_VALUE;
        }

        @Override
        protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            hsbRect.setX(1);
            hsbRect.setY(1);
            hsbRect.setWidth(w - 2);
            hsbRect.setHeight(h - 2);
            lightRect.setX(1);
            lightRect.setY(1);
            lightRect.setWidth(w - 2);
            lightRect.setHeight(h - 2);
        }

        public ObjectProperty<Color> getColor() {
            return color;
        }

        private LinearGradient buildHueBar() {
            double offset;
            Stop[] stops = new Stop[255];
            for (int y = 0; y < 255; y++) {
                offset = (double) (1.0 / 255) * y;
                int h = (int) ((y / 255.0) * 360);
                stops[y] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
            }
            return new LinearGradient(0f, 0f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
        }
    }
}
