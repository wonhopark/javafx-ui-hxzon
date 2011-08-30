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

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Util {

    public static final float SCENE_HEIGHT = 300f;
    public static final float SCENE_WIDTH = 600f;
    public static final float SPACING = 15f;
    public static final float CORRECTION_VISUAL = 3f;
    public static final float CORRECTION_STAGE_DECORATION = 30f;

    public static final Image image = new Image(
        Util.class.getClassLoader().getResourceAsStream(
            "puzzlepieces/puzzle_picture.jpg"));

    public static final int maxCol = (int) (image.getWidth() / Piece.SIZE);
    public static final int maxRow = (int) (image.getHeight() / Piece.SIZE);

    public static final Color STROKE_COLOR = Color.rgb(70, 70, 70);
    public static final Color GRAY_COLOR = Color.rgb(100,100,100);
    public static final Color DARK_GRAY_COLOR =  Color.rgb(80,80,80);

}
