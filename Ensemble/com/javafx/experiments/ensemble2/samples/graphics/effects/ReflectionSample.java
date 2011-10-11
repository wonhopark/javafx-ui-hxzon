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

package com.javafx.experiments.ensemble2.samples.graphics.effects;

import javafx.scene.Node;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.javafx.experiments.ensemble2.Sample;

/**
 * This sample how to use Reflection Effect on the circle.
 *
 * @see javafx.scene.effect.Reflection
 * @see javafx.scene.effect.Effect
 */
public class ReflectionSample extends Sample {

    public ReflectionSample() {
        super(100, 200);
        ImageView sample = new ImageView(BOAT);
        sample.setPreserveRatio(true);
        sample.setFitHeight(100);
        final Reflection reflection = new Reflection();
        sample.setEffect(reflection);
        getChildren().add(sample);
        // REMOVE ME
        setControls(new PropDesc("Reflection Bottom Opacity", reflection.bottomOpacityProperty(), 0d, 1d), new PropDesc("Reflection Top Opacity", reflection.topOpacityProperty(), 0d, 1d),
                new PropDesc("Reflection Fraction", reflection.fractionProperty(), 0d, 1d), new PropDesc("Reflection Top Offset", reflection.topOffsetProperty(), -10d, 10d));
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Text sample = new Text("FX");
        sample.setFont(Font.font("Amble", FontWeight.BOLD, 60));
        sample.setFill(Color.web("#333333"));
        final Reflection reflection = new Reflection();
        reflection.setTopOffset(-28d);
        reflection.setFraction(0.5);
        sample.setEffect(reflection);
        return sample;
    }
    // END REMOVE ME
}