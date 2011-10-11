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
package com.javafx.experiments.ensemble2.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.javafx.experiments.ensemble2.Ensemble2;
import com.javafx.experiments.ensemble2.Page;
import com.javafx.experiments.ensemble2.Pages;
import com.javafx.experiments.ensemble2.Sample;
import com.javafx.experiments.ensemble2.SampleHelper;

/**
 * SamplePage
 *
 */
public class SamplePage extends Page {
    private static final Pattern findPackage = Pattern.compile("[ \\t]*package[ \\t]*([^;]*);\\s*");
    private static final Pattern findJavaDocComment = Pattern.compile("\\/\\*\\*(.*)\\*\\/\\s*", Pattern.DOTALL);
    private static final Pattern findMultilineComment = Pattern.compile("\\/\\*(.*?)\\*\\/\\s*", Pattern.DOTALL);
    private static final Pattern findRemoveMeBlock = Pattern.compile("\\s+//\\s+REMOVE ME.*?END REMOVE ME", Pattern.DOTALL);
    private static final Pattern findEnsembleImport = Pattern.compile("\\nimport com.javafx.experiments.ensemble2.*?;");
    private static File tempSrc = null;

    private String sourceFileUrl;
    private Class sampleClass;
    private String description;
    private String[] apiClasspaths;
    private String[] relatesSamplePaths;
    private String[] resourceUrls;
    private String url;
    private static WebEngine engine = null;
    private static WebView webView = null;

    public SamplePage(String name, String sourceFileUrl) throws IllegalArgumentException {
        super(name);
        this.sourceFileUrl = sourceFileUrl;
        try {
            // load src into String
            StringBuilder builder = new StringBuilder();
            URI uri = new URI(sourceFileUrl);
            InputStream in = uri.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            reader.close();
            // parse package
            Matcher matcher = findPackage.matcher(builder);
            if (!matcher.find())
                throw new IllegalArgumentException("Failed to find package statement in sample file [" + sourceFileUrl + "]");
            String packageStr = matcher.group(1);
            // load class
//            System.out.println("packageStr+\".\"+name = " + packageStr + "." + name);
            String fileName = sourceFileUrl.substring(sourceFileUrl.lastIndexOf('/') + 1, sourceFileUrl.length() - 5);
            sampleClass = getClass().getClassLoader().loadClass(packageStr + "." + fileName);
            // parse the src file comment
            matcher = findJavaDocComment.matcher(builder);
            if (!matcher.find())
                throw new IllegalArgumentException("Failed to find java doc comment in sample file [" + sourceFileUrl + "]");
            String javaDocComment = matcher.group(1);
//            System.out.println("javaDocComment = " + javaDocComment);
            String[] lines = javaDocComment.split("\\n");
//            for (String jdocline:lines) {
//                System.out.println("jdocline = " + jdocline);
//            }
            String[] lines2 = javaDocComment.split("([ \\t]*\\n[ \\t]*\\*[ \\t]*)+");
            StringBuilder descBuilder = new StringBuilder();
            List<String> relatedList = new ArrayList<String>();
            List<String> seeList = new ArrayList<String>();
            List<String> resourceList = new ArrayList<String>();
            for (String jdocline : lines2) {
//                System.out.println("jdocline2 = " + jdocline);
                String trimedLine = jdocline.trim();
                if (trimedLine.length() != 0) {
                    if (trimedLine.startsWith("@related")) {
                        relatedList.add(trimedLine.substring(8).trim());
                    } else if (trimedLine.startsWith("@see")) {
                        seeList.add(trimedLine.substring(4).trim());
                    } else if (trimedLine.startsWith("@resource")) {
                        // todo resolve to a URL
                        resourceList.add(trimedLine.substring(9).trim());
                    } else {
                        descBuilder.append(trimedLine);
                        descBuilder.append(' ');
                    }
                }
            }
            description = descBuilder.toString();
            relatesSamplePaths = relatedList.toArray(new String[relatedList.size()]);
            apiClasspaths = seeList.toArray(new String[seeList.size()]);
            resourceUrls = resourceList.toArray(new String[resourceList.size()]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add API back references
        Ensemble2 ensemble2 = Ensemble2.getEnsemble2();
        for (String apiClassPath : apiClasspaths) {
            String path = Pages.API_DOCS + '/' + apiClassPath.replace('.', '/');
            DocPage docPage = (DocPage) ensemble2.getPages().getPage(path);
            if (docPage != null) {
                docPage.getRelatedSamples().add(this);
            } else {
                System.err.println("Failed to find documentation page for classpath [" + apiClassPath + "] from @see in sample [" + sourceFileUrl + "]");
            }
        }
    }

    public SamplePage(String name, Class sampleClass, String description, String[] apiClasspaths, String[] relatesSamplePaths, String[] resourceUrls) {
        super(name);
        this.sampleClass = sampleClass;
        this.description = description;
        this.apiClasspaths = apiClasspaths;
        this.relatesSamplePaths = relatesSamplePaths;
        this.resourceUrls = resourceUrls;
    }

    @Override
    public Node createView() {
        try {
            // create main grid
            //final FlowSafeVBox main = new FlowSafeVBox();
            final VBox main = new VBox(8);
            main.getStyleClass().add("sample-page");
            // create header
            Label header = new Label(getName());
            header.getStyleClass().add("page-header");
            main.getChildren().add(header);
            // create sample area
            final SampleArea sampleArea = new SampleArea();
            main.getChildren().add(sampleArea);
            // create sample
            final Sample sample = (Sample) sampleClass.newInstance();
            double sampleWidth = sample.getSampleWidth();
            if (sampleWidth > 0) {
                sample.setPrefWidth(sampleWidth);
                sample.setMinWidth(sampleWidth);
                sample.setMaxWidth(sampleWidth);
            }
            double sampleHeight = sample.getSampleHeight();
            if (sampleHeight > 0) {
                sample.setPrefHeight(sampleHeight);
                sample.setMinHeight(sampleHeight);
                sample.setMaxHeight(sampleHeight);
            }
            sampleArea.getChildren().add(sample);
            sample.play();
            // create sample controls
            Node sampleControls = sample.getControls();
            if (sampleControls != null) {
                Label subHeader = new Label("Play with these:");
                subHeader.getStyleClass().add("page-subheader");
                main.getChildren().add(subHeader);
                main.getChildren().add(sampleControls);
            }
            // create code view
            URL url = generateSourceUrl();
            WebView webView = getWebView();
            webView.setPrefWidth(300);
            setUrl(url.toString());
            // create border pane for main content and sidebar
            BorderPane borderPane = new BorderPane();
            borderPane.setRight(createSideBar());
            borderPane.setCenter(main);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.getStyleClass().add("noborder-scroll-pane");
            scrollPane.setContent(borderPane);
            scrollPane.setFitToWidth(true);
            // create tab pane
            final TabPane tabPane = new SamplePageView(sample);
            tabPane.setId("source-tabs");
            final Tab sampleTab = new Tab();
            sampleTab.setText("Sample");
            sampleTab.setContent(scrollPane);
            sampleTab.setClosable(false);
            final Tab sourceTab = new Tab();
            sourceTab.setText("Source Code");
            sourceTab.setContent(webView);
            sourceTab.setClosable(false);
            tabPane.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable ov) {
                    if (tabPane.getSelectionModel().getSelectedItem() == sampleTab) {
                        sample.play();
                    } else {
                        sample.stop();
                    }
                }
            });
            tabPane.getTabs().addAll(sampleTab, sourceTab);
            return tabPane;
        } catch (Exception e) {
            e.printStackTrace();
            return new Text("Failed to create sample because of [" + e.getMessage() + "]");
        }
    }

    private Node createSideBar() {
        GridPane sidebar = new GridPane();
        sidebar.getStyleClass().add("right-sidebar");
        sidebar.setMaxWidth(Double.MAX_VALUE);
        sidebar.setMaxHeight(Double.MAX_VALUE);
        int sideRow = 0;
        // create side bar content
        // description
        Label discTitle = new Label("Description");
        discTitle.getStyleClass().add("right-sidebar-title");
        GridPane.setConstraints(discTitle, 0, sideRow++);
        sidebar.getChildren().add(discTitle);
        Text disc = new Text(description);
        disc.setWrappingWidth(200);
        disc.getStyleClass().add("right-sidebar-body");
        GridPane.setConstraints(disc, 0, sideRow++);
        sidebar.getChildren().add(disc);
        // docs
        if (apiClasspaths != null && apiClasspaths.length > 0) {
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label docsTitle = new Label("API Documentation");
            docsTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(docsTitle, 0, sideRow++);
            sidebar.getChildren().add(docsTitle);
            for (String docPath : apiClasspaths) {
                Hyperlink link = new Hyperlink(docPath);
                link.setOnAction(new GoToPageEventHandler(Pages.API_DOCS + '/' + docPath.replace('.', '/')));
                GridPane.setConstraints(link, 0, sideRow++);
                sidebar.getChildren().add(link);
            }
        }
        // related
        if (relatesSamplePaths != null && relatesSamplePaths.length > 0) {
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label relatedTitle = new Label("Related");
            relatedTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(relatedTitle, 0, sideRow++);
            sidebar.getChildren().add(relatedTitle);
            for (String relatedPath : relatesSamplePaths) {
                String[] parts = relatedPath.split("/");
                Hyperlink link = new Hyperlink(parts[parts.length - 1]);
                //convert path
                String path = "";
                for (String part : parts) {
                    path = path + '/' + SampleHelper.formatName(part);
                }
                link.setOnAction(new GoToPageEventHandler(Pages.SAMPLES + path));
                GridPane.setConstraints(link, 0, sideRow++);
                sidebar.getChildren().add(link);
            }
        }
        // resources
        // TODO add back in later
//        if (resourceUrls!=null && resourceUrls.length>0) {
//            Separator separator = new Separator();
//            separator.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//            sidebar.getChildren().add(separator);
//            Label docsTitle = new Label("Resources");
//            docsTitle.getStyleClass().add("right-sidebar-title");
//            docsTitle.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//            sidebar.getChildren().add(docsTitle);
//            for (String resourceUrl:resourceUrls) {
//                String[] parts = resourceUrl.split("/");
//                Hyperlink link = new Hyperlink(parts[parts.length-1]);
//                link.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//                sidebar.getChildren().add(link);
//            }
//        }
        return sidebar;
    }

    private Node getIcon() {
        URL url = sampleClass.getResource(sampleClass.getSimpleName() + ".png");
        if (url != null) {
            ImageView imageView = new ImageView(new Image(url.toString()));
            return imageView;
        } else {
            ImageView imageView = new ImageView(new Image(Ensemble2.class.getResource("images/icon-overlay.png").toString()));
            imageView.setMouseTransparent(true);
//            Rectangle overlayHighlight = new Rectangle(114,114, new LinearGradient(0,0.6,0,1,true, CycleMethod.NO_CYCLE, new Stop[]{ new Stop(0,Color.BLACK), new Stop(1,Color.web("#a5a5a5"))}));
            Rectangle overlayHighlight = new Rectangle(-8, -8, 130, 130);
            overlayHighlight.setFill(new LinearGradient(0, 0.5, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.web("#444444")) }));
            overlayHighlight.setOpacity(0.8);
            overlayHighlight.setMouseTransparent(true);
//            Rectangle background = new Rectangle(114,114, Color.web("#006fb5"));
            Rectangle background = new Rectangle(-8, -8, 130, 130);
            background.setFill(Color.web("#b9c0c5"));
            Group contentGroup = new Group(background);
            Node content = createIconContent();
            if (content != null) {
                content.setTranslateX((int) ((114 - content.getBoundsInParent().getWidth()) / 2) - (int) content.getBoundsInParent().getMinX());
                content.setTranslateY((int) ((114 - content.getBoundsInParent().getHeight()) / 2) - (int) content.getBoundsInParent().getMinY());
                contentGroup.getChildren().add(content);
            }
            Group blendGroup = new Group(contentGroup, overlayHighlight);
            blendGroup.setBlendMode(BlendMode.ADD);
            Group group = new Group(blendGroup, imageView);
            Rectangle clipRect = new Rectangle(114, 114);
            clipRect.setArcWidth(38);
            clipRect.setArcHeight(38);
            group.setClip(clipRect);
            // Wrap in extra group as clip dosn't effect layout without it
            return new Group(group);
        }
    }

    public Node createIconContent() {
        try {
            Method createIconContent = sampleClass.getDeclaredMethod("createIconContent");
            return (Node) createIconContent.invoke(sampleClass);
        } catch (NoSuchMethodException e) {
            System.err.println("Sample [" + getName() + "] is missing a icon.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node createTile() {
        Button tile = new Button(getName().trim(), getIcon());
        tile.setMinSize(140, 140);
        tile.setPrefSize(140, 140);
        tile.setMaxSize(140, 140);
        tile.setContentDisplay(ContentDisplay.TOP);
        tile.getStyleClass().clear();
        tile.getStyleClass().add("sample-tile");
        tile.setOnAction(new EventHandler() {
            public void handle(Event event) {
                Ensemble2.getEnsemble2().goToPage(SamplePage.this);
            }
        });
        return tile;
    }

    protected void setUrl(String url) {
        if (engine != null && url != null) {
            engine.load(url);
        }
    }

    protected WebView getWebView() {
        if (engine == null) {
            webView = new WebView();
            engine = webView.getEngine();
        }
        return webView;
    }

    private URL generateSourceUrl() {
        URL url = null;
        try {
            if (tempSrc == null) {
                // create temp file
                tempSrc = File.createTempFile("EnsembleSource", ".html");
                tempSrc.deleteOnExit();
            }
            // get url
            url = tempSrc.toURI().toURL();
            // load source file
            StringBuilder source = new StringBuilder();
            URI uri = new URI(sourceFileUrl);
            InputStream in = uri.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line);
                source.append('\n');
            }
            reader.close();
            // remove comments and package statement
            String srcStr = findMultilineComment.matcher(source).replaceFirst("");
            srcStr = findEnsembleImport.matcher(srcStr).replaceAll("");
            srcStr = findJavaDocComment.matcher(srcStr).replaceFirst("");
            srcStr = findPackage.matcher(srcStr).replaceFirst("");
            // remove icon createion
            srcStr = srcStr.replaceFirst(" implements CanCreateIcon", "");
            // remove REMOVE ME ... END REMOVE ME blocks
            srcStr = findRemoveMeBlock.matcher(srcStr).replaceAll("");
            // escape < & >
            srcStr = srcStr.replaceAll("&", "&amp;");
            srcStr = srcStr.replaceAll("<", "&lt;");
            srcStr = srcStr.replaceAll(">", "&gt;");
            srcStr = srcStr.replaceAll("\"", "&quot;");
            srcStr = srcStr.replaceAll("\'", "&apos;");
            // create content
            PrintWriter writer = new PrintWriter(new FileWriter(tempSrc));
            writer.print("<html>\n" + "    <head>\n" + "    <style>\n" + "        body { background-color: #EEEEEE }\n"
                    + "        pre  { font-size: 10pt !important; font-family: Monospaced, monospace !important; }\n" + "    </style>\n" + "    </head>\n" + "<body>\n"
                    + "    <pre class=\"brush: java;gutter: false;toolbar: false;\">\n");
            writer.println(srcStr);
            writer.print("    </pre>\n" + "    <script type=\"text/javascript\"> SyntaxHighlighter.all(); </script>\n" + "</body>\n" + "</html>\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static class SamplePageView extends TabPane {
        private Sample sample;

        public SamplePageView(Sample sample) {
            super();
            this.sample = sample;
        }

        public void stop() {
            sample.stop();
        }
    }

    private static class SampleArea extends Pane {
        @Override
        protected void layoutChildren() {
            final double w = getWidth();
            final double h = getHeight();
            for (Node child : getChildren()) {
                double cw = child.prefWidth(-1);
                double ch = child.prefHeight(-1);
                if (cw > w) {
                    // shrink width
                    child.resizeRelocate(0, (h - ch) / 2, cw, ch);
                } else {
                    // center
                    child.resizeRelocate((w - cw) / 2, (h - ch) / 2, cw, ch);
                }
            }
        }
    }
}
