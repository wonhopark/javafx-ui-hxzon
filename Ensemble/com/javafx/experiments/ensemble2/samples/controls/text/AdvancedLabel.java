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
package com.javafx.experiments.ensemble2.samples.controls.text;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import com.javafx.experiments.ensemble2.Sample;

/**
 * Demonstrates a label with the graphic and text displayed in different positions.
 *
 * @see javafx.scene.control.Label
 * @related controls/text/SimpleLabel
 * @related controls/buttons/GraphicButton
 * @resource images/icon-48x48.png
 */
public class AdvancedLabel extends Sample {
    public AdvancedLabel() {
        VBox box = new VBox(10);

        ImageView imageView = new ImageView(ICON_48);
        Label label = new Label("graphic on the top.", imageView);
        label.setContentDisplay(ContentDisplay.TOP);
        box.getChildren().add(label);

        imageView = new ImageView(ICON_48);
        label = new Label("graphic on the right.", imageView);
        label.setContentDisplay(ContentDisplay.RIGHT);
        box.getChildren().add(label);

        imageView = new ImageView(ICON_48);
        label = new Label("graphic on the bottom.", imageView);
        label.setContentDisplay(ContentDisplay.BOTTOM);
        box.getChildren().add(label);

        imageView = new ImageView(ICON_48);
        label = new Label("graphic on the left.", imageView);
        label.setContentDisplay(ContentDisplay.LEFT);
        box.getChildren().add(label);

        imageView = new ImageView(ICON_48);
        label = new Label("graphic on the center.", imageView);
        label.setContentDisplay(ContentDisplay.CENTER);
        box.getChildren().add(label);

        getChildren().add(box);
    }
}
