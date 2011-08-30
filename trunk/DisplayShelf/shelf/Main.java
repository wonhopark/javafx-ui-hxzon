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
package shelf;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static final String[] IMAGE_NAMES = new String[]{
        "DSC_0026_2.jpg",
        "DSC_0040_2.jpg",
        "DSC_0068_2.jpg",
        "DSC_0083_2.jpg",
        "DSC_0094_2.jpg",
        "DSC_0129_2.jpg",
        "DSC_0152_2.jpg",
        "DSC_0162_2.jpg",
        "DSC_0172_2.jpg",
        "DSC_0178_2.jpg",
        "DSC_0199_2.jpg",
        "DSC_0277_2.jpg",
        "DSC_0290_2.jpg",
        "DSC_0425_2.jpg"
    };

    @Override public void start(Stage stage) {
        stage.setTitle("Display Shelf Demo");
        // create scene
        final Scene scene = new Scene(new Group(), 600, 300);
        stage.setScene(scene);
        // load css stylesheet
        scene.getStylesheets().add("/shelf/displayshelf.css");
        // load images
        Image[] images = new Image[IMAGE_NAMES.length];
        for (int i = 0; i < images.length; i++) {
            Image image = new Image(Main.class.getResource("images/"+IMAGE_NAMES[i]).toExternalForm(), 200, 200, true, true, false);
            images[i] = image;
        }
        // create display shelf
        scene.setRoot(new DisplayShelf(images));
        // show stage
        stage.setVisible(true);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
