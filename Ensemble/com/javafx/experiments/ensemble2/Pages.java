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
package com.javafx.experiments.ensemble2;

import com.javafx.experiments.ensemble2.pages.AllPagesPage;
import com.javafx.experiments.ensemble2.pages.CategoryPage;

/**
 * Pages
 *
 */
public class Pages {
    public static final String SAMPLES = "SAMPLES";
    public static final String API_DOCS = "API DOCUMENTATION";
    private AllPagesPage root;
    private CategoryPage samples;
    private CategoryPage docs;

    public Pages() {
        // create all the pages
        root = new AllPagesPage();
        samples = new CategoryPage(SAMPLES);
        docs = new CategoryPage(API_DOCS);
        root.getChildren().add(samples);
        root.getChildren().add(docs);
    }

    public void parseDocs() {
        DocsHelper.getDocs(docs);
    }

    public void parseSamples() {
        SampleHelper.getSamples(samples);
    }

    public Page getPage(String name) {
        return root.getChild(name);
    }

    public CategoryPage getSamples() {
        return samples;
    }

    public CategoryPage getDocs() {
        return docs;
    }

    public Page getRoot() {
        return root;
    }
}
