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
package pathanim.scenario;

import javafx.animation.Interpolator;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.Region;

public abstract class Scenario extends Region {

    private boolean animInProgress;

    private static final Duration DURATION = Duration.seconds(2);
    private FadeTransition fadeIn;
    private FadeTransition fadeOut;

    public Scenario() {
        init();
    }

    private void init() {
        createFadeInTransition();
        createFadeOutTransition();
    }

    private void createFadeInTransition() {
        fadeIn = new FadeTransition();
        fadeIn.setDuration(DURATION);
        fadeIn.setNode(this);
        fadeIn.setFromValue(0f);
        fadeIn.setToValue(1f);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);
    }

    private void createFadeOutTransition() {
        fadeOut = new FadeTransition();
        fadeOut.setDuration(DURATION);
        fadeOut.setNode(this);
        fadeOut.setFromValue(1f);
        fadeOut.setToValue(0f);
        fadeOut.setInterpolator(Interpolator.EASE_OUT);
    }

    public abstract void play();

    public abstract void pause();

    public void fadeIn() {
        if(animInProgress){
            play();
        }
        fadeIn.play();
        fadeOut.stop();
    }

    public void fadeOut() {
        fadeIn.stop();
        fadeOut.play();
        pause();
    }

    public boolean isAnimInProgress() {
        return animInProgress;
    }

    public void setAnimInProgress(boolean animInProgress) {
        this.animInProgress = animInProgress;
    }
    
}
