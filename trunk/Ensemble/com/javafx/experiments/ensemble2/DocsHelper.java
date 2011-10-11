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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.javafx.experiments.ensemble2.pages.CategoryPage;
import com.javafx.experiments.ensemble2.pages.DocPage;

/**
 * DocsHelper
 *
 */
public class DocsHelper {
    private static final Pattern findClassUrl = Pattern.compile("A\\s+HREF=\\\"([^\\\"]+)\\\"");
    private static final String DOCS_DIR_URL = "http://download.oracle.com/javafx/2.0/api/";

    public static String getDocsUrl(String packagePath) {
        return DOCS_DIR_URL + packagePath.replace('.', '/') + ".html";
    }

    public static String getPagePath(String url) {
        String docsDirUrl = DOCS_DIR_URL.replaceAll("/+", "/");
//        System.out.println("DocsHelper.getPagePath("+url+")");
        String cleanUrl = url.replaceAll("/+", "/");
//        System.out.println("cleanUrl = " + cleanUrl);
        String relativePath = Pages.API_DOCS + '/' + cleanUrl.substring(docsDirUrl.length(), cleanUrl.length() - 5);
        if (relativePath.endsWith("/package-summary")) {
            relativePath = relativePath.substring(0, relativePath.length() - "/package-summary".length());
        }
//        System.out.println("relativePath = " + relativePath);
        return relativePath;
    }

    public static void getDocs(CategoryPage rootPage) {
//        System.out.println("docsDirUrl = " + docsDirUrl);
        // parse allclasses-frame.html
        Map<String, DocPage> packagePageMap = new HashMap<String, DocPage>();
        try {
            StringBuilder builder = new StringBuilder();
            URI uri = new URI(DOCS_DIR_URL + "allclasses-frame.html");
//            System.out.println("uri = " + uri);
            InputStream in = uri.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            reader.close();
            // parse package
            Matcher matcher = findClassUrl.matcher(builder);
            while (matcher.find()) {
                String classUrl = matcher.group(1);
//                System.out.println("classUrl = " + classUrl);
                DocPage parent = createPackagePage(classUrl.substring(0, classUrl.lastIndexOf('/')), rootPage, packagePageMap);
                String className = classUrl.substring(classUrl.lastIndexOf('/') + 1, classUrl.lastIndexOf('.'));
//                System.out.println("className = " + className);
                DocPage docPage = new DocPage(className, DOCS_DIR_URL + classUrl);
                parent.getChildren().add(docPage);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DocPage createPackagePage(String path, CategoryPage rootPage, Map<String, DocPage> packagePageMap) {
        DocPage packagePage = packagePageMap.get(path);
        if (packagePage == null) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash == -1) {
                // found root package
                packagePage = new DocPage(path, DOCS_DIR_URL + path + "/package-summary.html");
                rootPage.getChildren().add(packagePage);
            } else {
                DocPage parent = createPackagePage(path.substring(0, lastSlash), rootPage, packagePageMap);
                packagePage = new DocPage(path.substring(lastSlash + 1, path.length()), DOCS_DIR_URL + path + "/package-summary.html");
                parent.getChildren().add(packagePage);
            }
            packagePageMap.put(path, packagePage);
        }
        return packagePage;
    }
}
