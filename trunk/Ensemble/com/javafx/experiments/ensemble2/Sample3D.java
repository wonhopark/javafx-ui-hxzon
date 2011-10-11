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

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Base class for all Ensemble 3D samples
 */
public abstract class Sample3D extends Sample {

    private Group root3d;
    private Rectangle root3dClip;
    private Node content3d;
    private Region background;
    private boolean isPlaying = false;

    protected Sample3D(double width, double height) {
        super(width, height);
        root3d = Ensemble2.getEnsemble2().getRoot3d();
        root3dClip = new Rectangle();
        root3dClip.setSmooth(false);
        content3d = create3dContent();
        background = new Region() {
            @Override
            protected void layoutChildren() {
                if (isPlaying)
                    updateRoot3dPosition();
            }
        };
        background.setPrefSize(width, height);
        background.setStyle(getBackgroundCss());
        getChildren().add(background);
    }

    /**
     * Update the position of the 3D root content to align with the background region.
     */
    private void updateRoot3dPosition() {
        Point2D positionInScene = background.localToScene(0, 0);
        root3d.setTranslateX(positionInScene.getX() + (background.getWidth() / 2));
        root3d.setTranslateY(positionInScene.getY() + (background.getHeight() / 2));
        root3dClip.setX(-background.getWidth() / 2);
        root3dClip.setY(-background.getHeight() / 2);
        root3dClip.setWidth(background.getWidth());
        root3dClip.setHeight(background.getHeight());
    }

    /** Get the css style to use on the background for 3D content */
    protected String getBackgroundCss() {
        return "-fx-background-color: #dddddd;-fx-border-color: #cccccc;";
    }

    /**
     * Called when the sample is is being shown. Subclasses that override this must call super.
     */
    @Override
    public void play() {
        super.play();
        root3d.setClip(root3dClip);
        root3d.getTransforms().add(new Rotate(180, Rotate.X_AXIS));
        updateRoot3dPosition();
        root3d.getChildren().setAll(content3d);
        isPlaying = true;
    }

    /**
     * Called when the sample is not going to be shown any more. Subclasses that override this must call super.
     */
    @Override
    public void stop() {
        super.stop();
        isPlaying = false;
        root3d.setClip(null);
        root3d.getChildren().clear();
    }

    /**
     * Called to create 3D content for this sample. The origin for the 3D content is the center of the sample area
     * defined by the width and height passed into the Sample3D constructor. Also the scene is rotated so that positive
     * Y is up. This is because most 3D work is done in a coordinate space where origin is center and Y is up.
     *
     * @return The nodes to display in 3D area with depth test on
     */
    public abstract Node create3dContent();

}
