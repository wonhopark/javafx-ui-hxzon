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

import javafx.scene.Group;
import javafx.scene.effect.InnerShadow;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.shape.Line;
import static puzzlepieces.Util.*;

public class Desk extends Parent{

    public static final float DESK_WIDTH = Piece.SIZE * maxCol;
    public static final float DESK_HEIGHT = Piece.SIZE * maxRow;
    public static final float START_Y = 5f;


    private final Rectangle background = new Rectangle();

    Desk() {
        configureBackground();
        configureVerticalLines();
        configureHorizontalLines();
    }

    private void configureBackground() {
        background.setEffect(new InnerShadow());
        background.setFill(DARK_GRAY_COLOR);
        background.setStroke(STROKE_COLOR);
        background.setWidth(DESK_WIDTH);
        background.setHeight(DESK_HEIGHT);
        getChildren().add(background);
    }

    private void configureVerticalLines() {
         for (int col = 0; col < maxCol - 1; col++) {
            Line l = new Line();
            l.setStroke(STROKE_COLOR);
            l.setStartX(Piece.SIZE + Piece.SIZE * col);
            l.setStartY(START_Y);
            l.setEndX(Piece.SIZE + Piece.SIZE * col);
            l.setEndY(Piece.SIZE * maxRow - START_Y);
            getChildren().add(l);
        }
    }

    private void configureHorizontalLines() {
        for (int row = 0; row < maxRow - 1; row++) {
            Line l = new Line();
            l.setStroke(STROKE_COLOR);
            l.setStartX(START_Y);
            l.setStartY(Piece.SIZE + Piece.SIZE * row);
            l.setEndX(Piece.SIZE * maxCol - START_Y);
            l.setEndY(Piece.SIZE + Piece.SIZE * row);
            getChildren().add(l);
        }
    }

    void addPieces(Group pieceGroup) {
        getChildren().addAll(pieceGroup);
    }
}
