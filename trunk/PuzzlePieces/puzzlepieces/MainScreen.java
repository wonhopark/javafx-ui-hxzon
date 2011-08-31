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
import javafx.scene.Group;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static puzzlepieces.Util.*;

public class MainScreen {

    private final Stage stage;
    private final Scene scene = new Scene(new Group(),SCENE_WIDTH, SCENE_HEIGHT+CORRECTION_STAGE_DECORATION);

    private static final float LAYOUT_Y_BUTTONS = SCENE_HEIGHT- TextButton.HEIGHT- SPACING;

    private final TextButton shuffleButton = new TextButton("Shuffle");
    private final TextButton solveButton = new TextButton("Solve");

    private final ObservableList<Piece> pieces  = FXCollections.<Piece>observableArrayList();
    private final Desk desk = new Desk();

    private Timeline timeline;

    public MainScreen(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        configureScene();
        configureStage();
        configureShuffleButton();
        configureSolveButton();
        configurePieces();
        configureDesk();
    }

    private void configureStage() {
        stage.setTitle("Puzzle Pieces");
        stage.setResizable(true);
        stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - scene.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - scene.getHeight()) / 2);
        stage.setScene(scene);
        stage.setVisible(true);
    }
    private void configureScene() {
        scene.setFill(GRAY_COLOR);
        scene.setRoot(new Group(shuffleButton, solveButton, desk));
    }
    private void configureShuffleButton() {
        shuffleButton.setLayoutX(SCENE_WIDTH/2 - 2*SPACING - TextButton.WIDTH  - CORRECTION_VISUAL);
        shuffleButton.setLayoutY(LAYOUT_Y_BUTTONS);
        shuffleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (timeline != null) timeline.stop();
                timeline = new Timeline();
                for (final Piece piece : pieces) {
                   piece.setActive();
                   float shuffleX = (float)( Math.random() * (Desk.DESK_WIDTH - Piece.SIZE + 48f ) - 24f - piece.getCorrectX());
                   float shuffleY = (float)( Math.random() * (Desk.DESK_HEIGHT - Piece.SIZE + 30f ) - 15f - piece.getCorrectY());
                    
                    KeyValue keyValueX = new KeyValue(piece.translateXProperty(), shuffleX);
                    KeyValue keyValueY = new KeyValue(piece.translateYProperty(), shuffleY);

                    KeyFrame kf2 = new KeyFrame(Duration.seconds(1), keyValueX, keyValueY);

                    timeline.getKeyFrames().add(kf2);
                }
                timeline.playFromStart();
            }
        });
    }
    private void configureSolveButton() {
        solveButton.setLayoutX(SCENE_WIDTH/2 + 2*SPACING  - CORRECTION_VISUAL);//bind
        solveButton.setLayoutY(LAYOUT_Y_BUTTONS);//bind
        solveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (timeline != null) timeline.stop();
                timeline = new Timeline();

                for (final Piece piece : pieces) {
                    piece.setInactive();
                    float shuffleX = 0f;
                    float shuffleY = 0f;

                    KeyValue keyValueX = new KeyValue(piece.translateXProperty(), shuffleX);
                    KeyValue keyValueY = new KeyValue(piece.translateYProperty(), shuffleY);

                    KeyFrame kf2 = new KeyFrame(Duration.seconds(1), keyValueX, keyValueY);

                    timeline.getKeyFrames().add(kf2);
                }
                timeline.playFromStart();
            }
        });
    }
    
    private void configureDesk() {
        Group pieceGroup = new Group();
        pieceGroup.getChildren().addAll(pieces);
        desk.addPieces(pieceGroup);
        desk.setLayoutX((SCENE_WIDTH - Desk.DESK_WIDTH)/2  - CORRECTION_VISUAL);
        desk.setLayoutY(30);
    }
    private void configurePieces() {
        for (int col = 0; col < maxCol; col++) {
            for (int row = 0; row < maxRow; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(image, x, y, row>0, col>0, row<maxRow -1, col < maxCol -1);
                piece.setInactive();
                piece.addListeners();
                pieces.add(piece);
            }
        }
    }
}
