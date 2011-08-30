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

import javafx.scene.image.Image;

public final class Util {

    private Util() {
    }
    public static final float WIDTH = 480f;
    public static final float HEIGHT = 640f;
    public static final float HEIGHT_CORRECTION = 28f;
    
    public static final String TITLE = "Path Animation Demo";
    private static final String URL_PREFIX = Util.class.getClassLoader().getResource("images").toExternalForm();

    public static Image createImage(String filename) {
        return new Image(URL_PREFIX + "/" + filename);
    }

    public static Image createImage(String filename, float width, float height) {
        return new Image(URL_PREFIX + "/" + filename, width, height, true, true, false);
    }
    
    public static final Image IMAGE_CAR_THUMB = createImage("car.png", 70f, 40f);
    public static final Image IMAGE_BOAT_THUMB = createImage("boat-thumb.png", 60f, 70f);
    public static final Image IMAGE_BOAT = createImage("boat.png", 118f, 282f);
    public static final Image IMAGE_PLAY = createImage("play.png", 91f, 91f);
    public static final Image IMAGE_PAUSE = createImage("pause.png", 91f, 91f);

}
