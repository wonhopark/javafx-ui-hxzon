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
import javafx.animation.Interpolator;
import static javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import pathanim.Util;

public class BoatScenario extends Scenario {

    private static final Duration DURATION = Duration.seconds(8);

    private Group content;
    private Group seaContent;
    private ImageView boat;
    private Path path;
    private Path sea;

    private PathTransition anim;
    private TranslateTransition moveAnim;

    public BoatScenario() {
        init();

        getChildren().addAll(createBackground(), content);
    }

    private void init() {
        createContent();
        createPathTransition();
        createMoveTransition();
    }

    private void createContent() {
        createPath();
        createSeaContent();

        content = new Group(seaContent);
        content.setTranslateY(Util.HEIGHT * 0.75f);
    }

    private void createSeaContent() {
        createBoat();
        createSea();
        seaContent = new Group(boat, sea);
    }

    private void createPath() {
        final MoveTo moveTo1 = new MoveTo(140f, 60f);
        final CubicCurveTo cubicCurveTo1 = new CubicCurveTo(240f, 20f, 240f, 100f, 340f, 60f);
        final CubicCurveTo cubicCurveTo2 = new CubicCurveTo(440f, 20f, 510f, 140f, 590f, 60f);
        final CubicCurveTo cubicCurveTo3 = new CubicCurveTo(670f, 0f, 710f, 100f, 810f, 60f);

        path = new Path();
        path.getElements().addAll(moveTo1, cubicCurveTo1, cubicCurveTo2, cubicCurveTo3);
    }

    private ImageView createBackground() {
        final ImageView imageView = new ImageView();
        imageView.setImage(Util.createImage("boat-bg.jpg"));
        return imageView;
    }

    private void createBoat() {
        boat = new ImageView();
        boat.setImage(Util.IMAGE_BOAT);
        boat.setSmooth(true);
        boat.setTranslateX(100 - Util.IMAGE_BOAT.getWidth() / 2);
        boat.setTranslateY(68 - Util.IMAGE_BOAT.getHeight() / 2);
    }

    private void createSea() {
        final CubicCurveTo cubicCurveTo1 = new CubicCurveTo(910f, 20f, 910f, 100f, 1010f, 60f);
        final CubicCurveTo cubicCurveTo2 = new CubicCurveTo(1110f, 20f, 1180f, 140f, 1260f, 60f);
        final VLineTo vLineTo1 = new VLineTo(240f);
        final HLineTo hLineTo1 = new HLineTo(-80f);
        final VLineTo vLineTo2 = new VLineTo(60);
        final CubicCurveTo cubicCurveTo3 = new CubicCurveTo(0f, 0f, 40f, 100f, 140f, 60f);
        final ClosePath closePath = new ClosePath();

        sea = new Path();
        sea.setFill(Color.BLUE);
        sea.getElements().addAll(path.getElements());
        sea.getElements().addAll(cubicCurveTo1, cubicCurveTo2, vLineTo1, hLineTo1, vLineTo2, cubicCurveTo3, closePath);
    }

    // Animates ship along the path
    private void createPathTransition() {
        anim = new PathTransition();
        anim.setPath(path);
        anim.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        anim.setNode(boat);
        anim.setInterpolator(Interpolator.LINEAR);
        anim.setDuration(DURATION);
        anim.setCycleCount(Animation.INDEFINITE);
    }

    // Animates the path itself
    private void createMoveTransition() {
        moveAnim = new TranslateTransition();
        moveAnim.setNode(seaContent);
        moveAnim.setInterpolator(Interpolator.LINEAR);
        moveAnim.setDuration(DURATION);
        moveAnim.setCycleCount(Animation.INDEFINITE);
        moveAnim.setFromX(0f);
        moveAnim.setFromY(0f);
        moveAnim.setToX(-670f);
        moveAnim.setToY(0f);
    }

    @Override
    public void play() {
        setAnimInProgress(true);
        anim.play();
        moveAnim.play();
    }

    @Override
    public void pause() {
        anim.pause();
        moveAnim.pause();
    }
}
