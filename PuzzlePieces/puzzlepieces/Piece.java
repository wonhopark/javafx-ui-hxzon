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
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Piece extends Parent {

    public static final int SIZE = 100;

    private Image image;
    private final float correctX;
    private final float correctY;
    private final boolean hasTopTab;
    private final boolean hasLeftTab;
    private final boolean hasBottomTab;
    private final boolean hasRightTab;

    private double startDragX;
    private double startDragY;

    private Shape pieceStroke;
    private Shape pieceClip;
    private ImageView imageView = new ImageView();

    private Point2D dragAnchor;

    public Piece(Image image, float correctX, float correctY, boolean topTab, boolean leftTab, boolean bottomTab, boolean rightTab) {
        this.image = image;
        this.correctX = correctX;
        this.correctY = correctY;
        this.hasTopTab = topTab;
        this.hasLeftTab = leftTab;
        this.hasBottomTab = bottomTab;
        this.hasRightTab = rightTab;
        configurePieceClip();
        configurePieceStroke();
        configureImageView();
        init();
    }

    private void init() {
        setFocusTraversable(true);
        getChildren().addAll(imageView, pieceStroke);
        setCache(true);
    }

    private void configurePieceClip() {
        pieceClip = createPiece();
        pieceClip.setFill(Color.WHITE);
        pieceClip.setStroke(null);
    }

    private void configurePieceStroke() {
        pieceStroke = createPiece();
        pieceStroke.setFill(null);
        pieceStroke.setStroke(Color.BLACK);
    }

    private void configureImageView() {
        imageView.setImage(image);
        imageView.setClip(pieceClip);
    }

    private Shape createPiece() {
        Shape shape = createPieceRectangle();

        if (hasRightTab) {
            shape = Path.union(shape, createPieceTab(69.5f, 0f, 10f, 17.5f, 50f, -12.5f, 11.5f, 25f, 56.25f, -14f, 6.25f, 56.25f, 14f, 6.25f));
        }
        if (hasBottomTab) {
            shape = Path.union(shape, createPieceTab(0f, 69.5f, 17.5f, 10f, -12.5f, 50f, 25f, 11f, -14f, 56.25f, 6.25f, 14f, 56.25f, 6.25f));
        }
        if (hasLeftTab) {
            shape = Path.subtract(shape, createPieceTab(-31f, 0f, 10f, 17.5f, -50f, -12.5f, 11f, 25f, -43.75f, -14f, 6.25f, -43.75f, 14f, 6.25f));
        }
        if (hasTopTab) {
            shape = Path.subtract(shape, createPieceTab(0f, -31f, 17.5f, 10f, -12.5f, -50f, 25f, 12.5f, -14f, -43.75f, 6.25f, 14f, -43.75f, 6.25f));
        }

        shape.setTranslateX(correctX);
        shape.setTranslateY(correctY);
        shape.setLayoutX(50f);
        shape.setLayoutY(50f);

        return shape;
    }

    private Rectangle createPieceRectangle() {
        Rectangle rec = new Rectangle();
        rec.setX(-50);
        rec.setY(-50);
        rec.setWidth(SIZE);
        rec.setHeight(SIZE);
        return rec;
    }

    private Shape createPieceTab(float eclipseCenterX, float eclipseCenterY, float eclipseRadiusX, float eclipseRadiusY,
                                 float rectangleX, float rectangleY, float rectangleWidth, float rectangleHeight,
                                 float circle1CenterX, float circle1CenterY, float circle1Radius,
                                 float circle2CenterX, float circle2CenterY, float circle2Radius) {
        Ellipse e = new Ellipse();
        e.setCenterX(eclipseCenterX);
        e.setCenterY(eclipseCenterY);
        e.setRadiusX(eclipseRadiusX);
        e.setRadiusY(eclipseRadiusY);
        Rectangle r = new Rectangle();
        r.setX(rectangleX);
        r.setY(rectangleY);
        r.setWidth(rectangleWidth);
        r.setHeight(rectangleHeight);
        Shape tab = Path.union(e, r);
        Circle c1 = new Circle();
        c1.setCenterX(circle1CenterX);
        c1.setCenterY(circle1CenterY);
        c1.setRadius(circle1Radius);
        tab = Path.subtract(tab, c1);
        Circle c2 = new Circle();
        c2.setCenterX(circle2CenterX);
        c2.setCenterY(circle2CenterY);
        c2.setRadius(circle2Radius);
        tab = Path.subtract(tab, c2);
        return tab;
    }

    public void setActive() {
            setDisable(false);
            setEffect(new DropShadow());
            toFront();
    }

    public void setInactive() {
            setEffect(null);
            setDisable(true);
            toBack();
    }

    public float getCorrectX() {
        return correctX;
    }

    public float getCorrectY() {
        return correctY;
    }

    public void addListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                    toFront();
                    startDragX = getTranslateX();
                    startDragY = getTranslateY();
                    dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                    if (getTranslateX() < (10) && getTranslateX() > (- 10) &&
                        getTranslateY() < (10) && getTranslateY() > (- 10)) {
                        setTranslateX(0);
                        setTranslateY(0);
                        setInactive();
                }
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                double newTranslateX = startDragX
                                        + me.getSceneX() - dragAnchor.getX();
                double newTranslateY = startDragY 
                                        + me.getSceneY() - dragAnchor.getY();
                float minTranslateX = - 45f - correctX;
                float maxTranslateX = (Desk.DESK_WIDTH - Piece.SIZE + 50f ) - correctX;
                float minTranslateY = - 30f - correctY;
                float maxTranslateY = (Desk.DESK_HEIGHT - Piece.SIZE + 70f ) - correctY;

                if ((newTranslateX> minTranslateX ) &&
                    (newTranslateX< maxTranslateX) &&
                    (newTranslateY> minTranslateY) &&
                    (newTranslateY< maxTranslateY))
                {
                    setTranslateX(newTranslateX);
                    setTranslateY(newTranslateY);
                }
            }
        });

    }


}
