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
package puzzlepieces;

import javafx.event.EventHandler;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static puzzlepieces.Util.*;

public class TextButton extends Parent {

    public static final float WIDTH = 100f;
    public static final float HEIGHT = 40f;
    public static final float DEFAULT_OPACITY = 0.5f;
    public static final float HIGHLIGHT_OPACITY = 0.9f;
    
    private final String text;
    private Timeline t;
    private final Text textNode = new Text();
    private final Rectangle background = new Rectangle();
    
    public TextButton(String text) {
        this.text = text;
        configureText();
        configureBackground();
        addListeners();
        initEffects();
    }

    private void addListeners() {
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (t != null) t.stop();
                t = new Timeline();
                KeyFrame kf = new KeyFrame(Duration.millis(1000*0.3),
                        new KeyValue(TextButton.this.opacityProperty(), HIGHLIGHT_OPACITY , Interpolator.EASE_OUT));
                t.getKeyFrames().add(kf);
                t.playFromStart();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (t != null) t.stop();
                t = new Timeline();
                KeyFrame kf = new KeyFrame(Duration.millis(1000*0.3),
                        new KeyValue(TextButton.this.opacityProperty(), DEFAULT_OPACITY , Interpolator.EASE_OUT)
                        );
                t.getKeyFrames().add(kf);
                t.playFromStart();
            }
        });
        getChildren().addAll(background, textNode);
    }

    private void configureText() {
        textNode.setFill(GRAY_COLOR);
        textNode.setText(text);
        textNode.setFont(new Font(Font.getDefault().getFamily(), 24));
        textNode.setLayoutX((WIDTH - textNode.getBoundsInLocal().getWidth())/2 + CORRECTION_VISUAL);
        textNode.setLayoutY((HEIGHT - textNode.getBoundsInLocal().getHeight())/2 );
        textNode.setTextOrigin(VPos.TOP);
    }

    private void configureBackground() {
        background.setArcHeight(20);
        background.setArcWidth(20);
        background.setWidth(WIDTH);
        background.setHeight(HEIGHT);
        background.setFill(Color.WHITE);
        background.setStroke(DARK_GRAY_COLOR);
        background.setStrokeWidth(1f);
    }

    private void initEffects() {
       setOpacity(0.5f);
       setEffect(new InnerShadow());
    }

}

