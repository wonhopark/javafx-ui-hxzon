package com.javafx.experiments.ensemble2.controls;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.sun.javafx.Utils;

/**
 * Simple draggable area for the bottom right of a window to support resizing.
 */
public class WindowResizeButton extends Region {
    private double dragOffsetX, dragOffsetY;

    public WindowResizeButton(final Stage stage, final double stageMinimumWidth, final double stageMinimumHeight) {
        setId("window-resize-button");
        setPrefSize(11, 11);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                final double stageY = Utils.isMac() ? stage.getY() + 22 : stage.getY(); // TODO Workaround for RT-13980
                dragOffsetX = (stage.getX() + stage.getWidth()) - e.getScreenX();
                dragOffsetY = (stageY + stage.getHeight()) - e.getScreenY();
                e.consume();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                final double stageY = Utils.isMac() ? stage.getY() + 22 : stage.getY(); // TODO Workaround for RT-13980
                final Screen screen = Screen.getScreensForRectangle(stage.getX(), stageY, 1, 1).get(0);
                Rectangle2D visualBounds = screen.getVisualBounds();
                if (Utils.isMac())
                    visualBounds = new Rectangle2D(visualBounds.getMinX(), visualBounds.getMinY() + 22, visualBounds.getWidth(), visualBounds.getHeight()); // TODO Workaround for RT-13980
                double maxX = Math.min(visualBounds.getMaxX(), e.getScreenX() + dragOffsetX);
                double maxY = Math.min(visualBounds.getMaxY(), e.getScreenY() - dragOffsetY);
                stage.setWidth(Math.max(stageMinimumWidth, maxX - stage.getX()));
                stage.setHeight(Math.max(stageMinimumHeight, maxY - stageY));
                e.consume();
            }
        });
    }
}
