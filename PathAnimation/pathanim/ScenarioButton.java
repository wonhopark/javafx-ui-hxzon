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
package pathanim;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class ScenarioButton extends Region {

    public static final float SIZE = 80f;
    private static final Stop[] STOPS = new Stop[]{new Stop(0.0f, Color.DARKBLUE), new Stop(0.8f, Color.BLACK)};
    private static final RadialGradient RADIAL_GRADIENT = new RadialGradient(0, 0, 0.5f, 0.5f, 1f, true, CycleMethod.NO_CYCLE, STOPS);

    private static final float PADDING = 2f;
    private static final float APP_PADDING = 4f;

    private int index;
    private Image icon;

    private Rectangle outerRectangle;
    private Rectangle innerRectangle;
    private ImageView imageView;

    public ScenarioButton(int index, Image icon) {
        this.index = index;
        this.icon = icon;
        init();
        getChildren().addAll(outerRectangle, innerRectangle, imageView);
    }

    private void init() {
        createOuterRectangle();
        createInnerRectangle();
        createImageView();
    }

    private void createOuterRectangle() {
        outerRectangle = new Rectangle();
        outerRectangle.setWidth(SIZE);
        outerRectangle.setHeight(SIZE);
        outerRectangle.setArcHeight(SIZE / APP_PADDING);
        outerRectangle.setArcWidth(SIZE / APP_PADDING);
        outerRectangle.setFill(null);
        if (index == 1) {
            outerRectangle.setStroke(Color.YELLOW);
        } else {
            outerRectangle.setStroke(Color.MEDIUMBLUE);
        }
        outerRectangle.setStrokeWidth(4f);
    }

    private void createInnerRectangle() {
        innerRectangle = new Rectangle();
        innerRectangle.setWidth(SIZE);
        innerRectangle.setHeight(SIZE);
        innerRectangle.setArcHeight(SIZE / APP_PADDING);
        innerRectangle.setArcWidth(SIZE / APP_PADDING);
        innerRectangle.setFill(RADIAL_GRADIENT);
    }

    private void createImageView() {
        imageView = new ImageView();
        imageView.setImage(icon);
        imageView.setLayoutX((SIZE - icon.getWidth()) / PADDING);
        imageView.setLayoutY((SIZE - icon.getHeight()) / PADDING);
    }

    public void setActive(boolean active) {
        if(active){
            outerRectangle.setStroke(Color.YELLOW);
        }else{
            outerRectangle.setStroke(Color.MEDIUMBLUE);
        }
    }
}
