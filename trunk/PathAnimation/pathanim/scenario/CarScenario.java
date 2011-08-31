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
package pathanim.scenario;

import javafx.animation.Animation;
import javafx.scene.Group;
import javafx.animation.Interpolator;
import static javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import pathanim.ControlPanel;
import pathanim.Util;
import static pathanim.Util.*;

public class CarScenario extends Scenario {

    private static final float PADDING = 2f;
    
    private PathElement[] path = new PathElement[11];
    private Path track;
    private Path line;
    private ImageView car;
    private Group content;

    private PathTransition anim;
    
    public CarScenario() {
        init();

        getChildren().addAll(createBackground(), content);
    }

    private void init() {
        createContent();
        createPathTransition();
    }

    private void createContent(){
        createPath();
        createTrack();
        createLine();
        createCar();

        content = new Group(track, line, car);
        content.setLayoutX((Util.WIDTH - track.getBoundsInLocal().getWidth()) / PADDING);
        content.setLayoutY(((Util.HEIGHT - ControlPanel.HEIGHT) + HEIGHT_CORRECTION / 2 - track.getBoundsInLocal().getHeight()) / 2 + ControlPanel.HEIGHT);
    }

    private void createPath() {
        final MoveTo moveTo1 = new MoveTo(32f, 178f);
        path[0] = moveTo1;

        final LineTo lineTo1 = new LineTo(92f, 10f);
        path[1] = lineTo1;

        final QuadCurveTo quadCurveTo1 = new QuadCurveTo(96f, 0f, 128f, 2f);
        path[2] = quadCurveTo1;

        final LineTo lineTo2 = new LineTo(146f, 8f);
        path[3] = lineTo2;

        final CubicCurveTo cubicCurveTo1 = new CubicCurveTo(228f, 48f, 44f, 204f, 168f, 262f);
        path[4] = cubicCurveTo1;

        final LineTo lineTo3 = new LineTo(214f, 288f);
        path[5] = lineTo3;

        final CubicCurveTo cubicCurveTo2 = new CubicCurveTo(240f, 308f, 218f, 354f, 194f, 344f);
        path[6] = cubicCurveTo2;

        final LineTo lineTo4 = new LineTo(100f, 310f);
        path[7] = lineTo4;

        final QuadCurveTo quadCurveTo2 = new QuadCurveTo(0f, 264f, 18f, 216f);
        path[8] = quadCurveTo2;

        final LineTo lineTo5 = new LineTo(32f, 178f);
        path[9] = lineTo5;

        final ClosePath closePath = new ClosePath();
        path[10] = closePath;
    }

    private ImageView createBackground(){
        final ImageView imageView = new ImageView();
        imageView.setImage(Util.createImage("car-bg.jpg"));
        return imageView;
    }

    private void createTrack() {
        track = new Path();
        track.setStroke(Color.rgb(51, 51, 51));
        track.setStrokeWidth(40f);
        track.getElements().addAll(path);
    }

    private void createLine(){
        line = new Path();
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(2);
        line.getStrokeDashArray().addAll(20.0, 15.0);
        line.getElements().addAll(path);
    }

    private void createCar(){
        car = new ImageView();
        car.setSmooth(true);
        car.setImage(IMAGE_CAR_THUMB);
        car.setTranslateX(32 - IMAGE_CAR_THUMB.getWidth() / PADDING);
        car.setTranslateY(188 - IMAGE_BOAT_THUMB.getHeight() / PADDING);
        car.setRotate(-70);
    }

    private void createPathTransition(){
        anim = new PathTransition();
        anim.setNode(car);
        anim.setPath(track);
        anim.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        anim.setInterpolator(Interpolator.LINEAR);
        anim.setDuration(Duration.seconds(6));
        anim.setCycleCount(Animation.INDEFINITE);
    }

    @Override
    public void play() {
        setAnimInProgress(true);
        anim.play();
    }

    @Override
    public void pause() {
        anim.pause();
    }
}
