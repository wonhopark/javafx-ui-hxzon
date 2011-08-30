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

import javafx.event.EventHandler;
import pathanim.scenario.Scenario;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import static pathanim.Util.*;

public class ControlPanel extends Parent {

    public static final float HEIGHT = 166f;
    private static final float TEXT_Y = 20f;
    private static final float IMAGES_Y = 60f;
    private static final float PLAY_BUTTON_X = (float)(WIDTH - IMAGE_PLAY.getWidth() - 40);
    private static final Font DEFAULT_FONT = new Font("Arial", 24);
    private Text chooseText;
    private Text playText;
    private ScenarioButton carButton;
    private ScenarioButton boatButton;
    private ImageView playButton;
    private Scenario currentScenario;
    private Scenario carScenario;
    private Scenario boatScenario;

    public ControlPanel(Scenario carScenario, Scenario boatScenario) {
        this.carScenario = carScenario;
        this.boatScenario = boatScenario;
        currentScenario = carScenario;

        init();

        getChildren().addAll(chooseText, playText, carButton, boatButton, playButton);
    }

    private void init() {
        createChooseText();
        createPlayText();
        createCarButton();
        createBoatButton();
        createPlayButton();
    }

    private void createChooseText() {
        chooseText = new Text();
        chooseText.setText("Choose Your Vehicle");
        chooseText.setBoundsType(TextBoundsType.VISUAL);
        chooseText.setFill(Color.WHITE);
        chooseText.setFont(DEFAULT_FONT);
        chooseText.setTranslateY(TEXT_Y + chooseText.getBoundsInLocal().getHeight());
        chooseText.setTranslateX((chooseText.getBoundsInLocal().getWidth() - 196) / 2);
    }

    private void createPlayText() {
        playText = new Text();
        playText.setText("Play");
        playText.setBoundsType(TextBoundsType.VISUAL);
        playText.setFill(Color.WHITE);
        playText.setFont(DEFAULT_FONT);
        playText.setTranslateY(TEXT_Y + chooseText.getBoundsInLocal().getHeight());
        updatePlayerTextTrY();
    }

    private void createCarButton() {
        carButton = new ScenarioButton(1, IMAGE_CAR_THUMB);
        carButton.setTranslateY(IMAGES_Y);
        carButton.setTranslateX(40f);
        carButton.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                boatScenario.fadeOut();
                carScenario.fadeIn();
                currentScenario = carScenario;
                carButton.setActive(true);
                boatButton.setActive(false);
                setUpPlayButton();
            }
        });
    }

    private void createBoatButton() {
        boatButton = new ScenarioButton(2, IMAGE_BOAT_THUMB);
        boatButton.setTranslateY(IMAGES_Y);
        boatButton.setTranslateX(60f + ScenarioButton.SIZE);
        boatButton.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                carScenario.fadeOut();
                boatScenario.fadeIn();
                currentScenario = boatScenario;
                boatButton.setActive(true);
                carButton.setActive(false);
                setUpPlayButton();
            }
        });
    }

    private void createPlayButton() {
        playButton = new ImageView();
        playButton.setImage(IMAGE_PLAY);
        playButton.setTranslateY(IMAGES_Y - 5);
        playButton.setTranslateX(PLAY_BUTTON_X);
        playButton.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (playButton.getImage() == IMAGE_PLAY) {
                    playButton.setImage(IMAGE_PAUSE);
                    playText.setText("Pause");
                    currentScenario.play();
                } else {
                    playButton.setImage(IMAGE_PLAY);
                    playText.setText("Play");
                    currentScenario.setAnimInProgress(false);
                    currentScenario.pause();
                }
                updatePlayerTextTrY();
            }
        });
    }

    private void setUpPlayButton() {
        if (currentScenario.isAnimInProgress()) {
            playButton.setImage(IMAGE_PAUSE);
            playText.setText("Pause");
        } else {
            playButton.setImage(IMAGE_PLAY);
            playText.setText("Play");
        }
        updatePlayerTextTrY();
    }

    private void updatePlayerTextTrY(){
        playText.setTranslateX(PLAY_BUTTON_X + (IMAGE_PLAY.getWidth() - playText.getBoundsInLocal().getWidth()) / 2);
    }
}
