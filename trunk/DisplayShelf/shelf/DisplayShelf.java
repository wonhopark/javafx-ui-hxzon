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
package shelf;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class DisplayShelf extends Region {

    private static final Duration DURATION = Duration.valueOf(500);
    private static final Interpolator INTERPOLATOR = Interpolator.EASE_BOTH;
    private static final double SPACING = 50;
    private static final double LEFT_OFFSET = -110;
    private static final double RIGHT_OFFSET = 110;
    private static final double SCALE_SMALL = 0.7;
    private Item[] items;
    private Group centered = new Group();
    private Group left = new Group();
    private Group center = new Group();
    private Group right = new Group();
    private int centerIndex = 0;
    private Timeline timeline;
    private ScrollBar scrollBar = new ScrollBar();
    private boolean localChange = false;

    public DisplayShelf(Image[] images) {
        getStyleClass().add("displayshelf");
        // create items
        items = new Item[images.length];
        for (int i=0; i<images.length; i++) {
            final Item item = items[i] = new Item(images[i]);
            final double index = i;
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    localChange = true;
                    scrollBar.setValue(index);
                    localChange = false;
                    shiftToCenter(item);
                }
            });
        }
        // setup scroll bar
        scrollBar.setMax(items.length-1);
        scrollBar.setVisibleAmount(1);
        scrollBar.setUnitIncrement(1);
        scrollBar.setBlockIncrement(1);
        scrollBar.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(ObservableValue ov) {
                if(!localChange)
                    shiftToCenter(items[(int)scrollBar.getValue()]);
            }
        });
        // create content
        centered.getChildren().addAll(left, right, center);
        getChildren().addAll(centered,scrollBar);
        // listen for keyboard events
        setFocusTraversable(true);
        setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.LEFT) {
                    shift(1);
                    localChange = true;
                    scrollBar.setValue(centerIndex);
                    localChange = false;
                } else if (ke.getCode() == KeyCode.RIGHT) {
                    shift(-1);
                    localChange = true;
                    scrollBar.setValue(centerIndex);
                    localChange = false;
                }
            }
        });
        // update
        update();
    }

    @Override protected void layoutChildren() {
        // keep centered centered
        centered.setLayoutY((getHeight() - Item.HEIGHT) / 2);
        centered.setLayoutX((getWidth() - Item.WIDTH) / 2);
        // position scroll bar at bottom
        scrollBar.setLayoutX(10);
        scrollBar.setLayoutY(getHeight()-25);
        scrollBar.resize(getWidth()-20,15);
    }

    private void update() {
        // move items to new homes in groups
        left.getChildren().clear();
        center.getChildren().clear();
        right.getChildren().clear();
        for (int i = 0; i < centerIndex; i++) {
            left.getChildren().add(items[i]);
        }
        center.getChildren().add(items[centerIndex]);
        for (int i = items.length - 1; i > centerIndex; i--) {
            right.getChildren().add(items[i]);
        }
        // stop old timeline if there is one running
        if (timeline!=null) timeline.stop();
        // create timeline to animate to new positions
        timeline = new Timeline();
        // add keyframes for left items
        final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        for (int i = 0; i < left.getChildren().size(); i++) {
            final Item it = items[i];
            double newX = -left.getChildren().size() * SPACING + SPACING * i + LEFT_OFFSET;
            keyFrames.add(new KeyFrame(DURATION,
                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR),
                    new KeyValue(it.scaleXProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.scaleYProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.angle, 45.0, INTERPOLATOR)));
        }
        // add keyframe for center item
        final Item centerItem = items[centerIndex];
        keyFrames.add(new KeyFrame(DURATION,
                new KeyValue(centerItem.translateXProperty(), 0, INTERPOLATOR),
                new KeyValue(centerItem.scaleXProperty(), 1.0, INTERPOLATOR),
                new KeyValue(centerItem.scaleYProperty(), 1.0, INTERPOLATOR),
                new KeyValue(centerItem.angle, 90.0, INTERPOLATOR)));
        // add keyframes for right items
        for (int i = 0; i < right.getChildren().size(); i++) {
            final Item it = items[items.length - i - 1];
            final double newX = right.getChildren().size() * SPACING - SPACING * i + RIGHT_OFFSET;
            keyFrames.add(new KeyFrame(DURATION,
                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR),
                    new KeyValue(it.scaleXProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.scaleYProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.angle, 135.0, INTERPOLATOR)));
        }
        // play animation
        timeline.play();
    }

    private void shiftToCenter(Item item) {
        for (int i = 0; i < left.getChildren().size(); i++) {
            if (left.getChildren().get(i) == item) {
                int shiftAmount = left.getChildren().size() - i;
                shift(shiftAmount);
                return;
            }
        }
        if (center.getChildren().get(0) == item) {
            return;
        }
        for (int i = 0; i < right.getChildren().size(); i++) {
            if (right.getChildren().get(i) == item) {
                int shiftAmount = -(right.getChildren().size() - i);
                shift(shiftAmount);
                return;
            }
        }
    }

    public void shift(int shiftAmount) {
        if (centerIndex <= 0 && shiftAmount > 0) return;
        if (centerIndex >= items.length - 1 && shiftAmount < 0) return;
        centerIndex -= shiftAmount;
        update();
    }
}
