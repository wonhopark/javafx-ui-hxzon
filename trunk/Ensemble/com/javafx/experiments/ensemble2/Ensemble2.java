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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.javafx.experiments.ensemble2.controls.BreadcrumbBar;
import com.javafx.experiments.ensemble2.controls.WindowButtons;
import com.javafx.experiments.ensemble2.controls.WindowResizeButton;
import com.javafx.experiments.ensemble2.pages.SamplePage;

/**
 * Ensemble2
 */
public class Ensemble2 extends Application {
    static {
        // Enable using system proxy if set
        System.setProperty("java.net.useSystemProxies", "true");
    }

    private static Ensemble2 ensemble2;
    private Stage stage;
    private Scene scene;
    private BorderPane root2d;
    private ToolBar toolBar;
    private SplitPane splitPane;
    private TreeView pageTree;
    private Pane pageArea;
    private Pages pages;
    private Page currentPage;
    private Node currentPageView;
    private BreadcrumbBar breadcrumbBar;
    private Group root3d;
    private Stack<Page> history = new Stack<Page>();
    private Stack<Page> forwardHistory = new Stack<Page>();
    private boolean changingPage = false;
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    private WindowResizeButton windowResizeButton;

    public static Ensemble2 getEnsemble2() {
        return ensemble2;
    }

    @Override
    public void start(final Stage stage) {
        ensemble2 = this;
        stage.initStyle(StageStyle.UNDECORATED);
        // create window resize button
        windowResizeButton = new WindowResizeButton(stage, 1020, 700);
        // create 2d root
        root2d = new BorderPane() {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                windowResizeButton.autosize();
                windowResizeButton.setLayoutX(getWidth() - windowResizeButton.getLayoutBounds().getWidth());
                windowResizeButton.setLayoutY(getHeight() - windowResizeButton.getLayoutBounds().getHeight());
            }
        };
        root2d.setId("root");
        root2d.setDepthTest(DepthTest.DISABLE);
        // create 3d root
        root3d = new Group();
        // create root and scene
        Pane root = new Pane() {
            @Override
            protected void layoutChildren() {
                // fill whole area with 2d root
                root2d.resize(getWidth(), getHeight());
            }
        };
        root.getChildren().addAll(root2d, root3d);
        scene = new Scene(root, 1020, 700, true);
        scene.setCamera(new PerspectiveCamera());
        stage.setHeight(scene.getHeight());
        stage.setWidth(scene.getWidth());
        scene.getStylesheets().addAll("/com/javafx/experiments/ensemble2/ensemble2.css");
        // create main toolbar
        toolBar = new ToolBar();
        toolBar.setId("mainToolBar");
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("images/logo.png")));
        HBox.setMargin(logo, new Insets(0, 0, 0, 5));
        toolBar.getItems().add(logo);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        toolBar.setPrefHeight(66);
        toolBar.setMinHeight(66);
        toolBar.setMaxHeight(66);
        GridPane.setConstraints(toolBar, 0, 0);
        // add window dragging
        toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseDragOffsetX = event.getSceneX();
                mouseDragOffsetY = event.getSceneY();
            }
        });
        toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - mouseDragOffsetX);
                stage.setY(event.getScreenY() - mouseDragOffsetY);
            }
        });
        // add close min max
        toolBar.getItems().add(new WindowButtons(stage));
        // create page tree toolbar
        ToolBar pageTreeToolBar = new ToolBar();
        pageTreeToolBar.setId("page-tree-toolbar");
        pageTreeToolBar.setMinHeight(29);
        pageTreeToolBar.setPrefHeight(29);
        pageTreeToolBar.setMaxSize(Double.MAX_VALUE, 29);
        ToggleGroup pageButtonGroup = new ToggleGroup();
        final ToggleButton allButton = new ToggleButton("All");
        allButton.setToggleGroup(pageButtonGroup);
        allButton.setSelected(true);
        final ToggleButton samplesButton = new ToggleButton("Samples");
        samplesButton.setToggleGroup(pageButtonGroup);
        final ToggleButton docsButton = new ToggleButton("Document");
        docsButton.setToggleGroup(pageButtonGroup);
        InvalidationListener treeButtonNotifyListener = new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (allButton.isSelected()) {
                    pageTree.setRoot(pages.getRoot());
                } else if (samplesButton.isSelected()) {
                    pageTree.setRoot(pages.getSamples());
                } else if (docsButton.isSelected()) {
                    pageTree.setRoot(pages.getDocs());
                }
            }
        };
        allButton.selectedProperty().addListener(treeButtonNotifyListener);
        samplesButton.selectedProperty().addListener(treeButtonNotifyListener);
        docsButton.selectedProperty().addListener(treeButtonNotifyListener);
        pageTreeToolBar.getItems().addAll(allButton, samplesButton, docsButton);
        // create page tree
        pages = new Pages();
        pages.parseDocs();
        pages.parseSamples();
        pageTree = new TreeView();
        pageTree.setId("page-tree");
        pageTree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pageTree.setRoot(pages.getRoot());
        pageTree.setShowRoot(false);
        pageTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pageTree.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable value) {
//                value.getValue();
                if (!changingPage) {
                    Page selectedPage = (Page) pageTree.getSelectionModel().getSelectedItem();
                    if (selectedPage != pages.getRoot())
                        goToPage(selectedPage);
                }
            }
        });
        // create left split pane
        BorderPane leftSplitPane = new BorderPane();
        leftSplitPane.setTop(pageTreeToolBar);
        leftSplitPane.setCenter(pageTree);
        // create page toolbar
        ToolBar pageToolBar = new ToolBar();
        pageToolBar.setId("page-toolbar");
        pageToolBar.setMinHeight(29);
        pageToolBar.setPrefHeight(29);
        pageToolBar.setMaxSize(Double.MAX_VALUE, 29);
        Button backButton = new Button();
        backButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/back.png"))));
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                back();
            }
        });
        Button forwardButton = new Button();
        forwardButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/forward.png"))));
        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                forward();
            }
        });
        Button reloadButton = new Button();
        reloadButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("images/reload.png"))));
        reloadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                reload();
            }
        });
        breadcrumbBar = new BreadcrumbBar();
        pageToolBar.getItems().addAll(backButton, forwardButton, reloadButton, breadcrumbBar);
        // create page area
        pageArea = new Pane() {
            @Override
            protected void layoutChildren() {
                for (Node child : pageArea.getChildren()) {
                    child.resizeRelocate(0, 0, pageArea.getWidth(), pageArea.getHeight());
                }
            }
        };
        pageArea.setId("page-area");
        // create right split pane
        BorderPane rightSplitPane = new BorderPane();
        rightSplitPane.setTop(pageToolBar);
        rightSplitPane.setCenter(pageArea);
        // create split pane
        splitPane = new SplitPane();
        splitPane.setId("page-splitpane");
        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setConstraints(splitPane, 0, 1);
        splitPane.getItems().addAll(leftSplitPane, rightSplitPane);
        splitPane.setDividerPosition(0, 0.25);

        root2d.setTop(toolBar);
        root2d.setCenter(splitPane);
        // add window resize button so its on top
        windowResizeButton.setManaged(false);
        root2d.getChildren().add(windowResizeButton);
        // show stage
        for (TreeItem child : pages.getRoot().getChildren()) {
            child.setExpanded(true);
            for (TreeItem child2 : (ObservableList<TreeItem<String>>) child.getChildren()) {
                child2.setExpanded(true);
            }
        }
        // goto samples page
        pageTree.getSelectionModel().select(pages.getSamples());
        stage.setScene(scene);
        stage.show();
    }

    public Group getRoot3d() {
        return root3d;
    }

    public Pages getPages() {
        return pages;
    }

    /** Change to new pagePath without swaping views, assumes that the current view is already showing the new page */
    public void updateCurrentPage(String pagePath) {
        updateCurrentPage(pages.getPage(pagePath));
    }

    /** Change to new page without swaping views, assumes that the current view is already showing the new page */
    public void updateCurrentPage(Page page) {
        goToPage(page, true, false, false);
    }

    public void goToPage(String pagePath) {
        goToPage(pages.getPage(pagePath));
    }

    public void goToPage(Page page) {
        goToPage(page, true, false, true);
    }

    private void goToPage(Page page, boolean addHistory, boolean force, boolean swapViews) {
        if (page == null)
            return;
        if (!force && page == currentPage) {
            System.out.println("trying to go to current page and force=false");
            return;
        }
        System.out.println("Ensemble2.goToPage[" + page.getPath() + "]");
        changingPage = true;
        if (swapViews) {
            Node view = page.createView();
            if (view == null)
                view = new Region(); // todo temp workaround
            // replace view in pageArea if new
            if (force || view != currentPageView) {
                for (Node child : pageArea.getChildren()) {
                    if (child instanceof SamplePage.SamplePageView) {
                        System.out.println("STOPPING SAMPLE");
                        ((SamplePage.SamplePageView) child).stop();
                    }
                }
                pageArea.getChildren().setAll(view);
                currentPageView = view;
            }
        }
        // add page to history
        if (addHistory && currentPage != null) {
            history.push(currentPage);
            forwardHistory.clear();
        }
        currentPage = page;
        // expand tree to selected node
        Page p = page;
        while (p != null) {
            p.setExpanded(true);
            p = (Page) p.getParent();
        }
        // update tree selection
        pageTree.getSelectionModel().select(page);
        // update breadcrumb bar
        breadcrumbBar.setPath(currentPage.getPath());
        // done
//        if (addHistory && currentPage!=null) printHistory();
        changingPage = false;
    }

    public void back() {
        if (!history.isEmpty()) {
            Page prevPage = history.pop();
            forwardHistory.push(currentPage);
            goToPage(prevPage, false, false, true);
        }
    }

    private void printHistory() {
        System.out.print("   HISTORY = ");
        for (Page page : history) {
            System.out.print(page.getName() + "->");
        }
        System.out.print("   [" + currentPage.getName() + "]");
        for (Page page : forwardHistory) {
            System.out.print(page.getName() + "->");
        }
        System.out.print("\n");
    }

    public void forward() {
        if (!forwardHistory.isEmpty()) {
            Page prevPage = forwardHistory.pop();
            history.push(currentPage);
            goToPage(prevPage, false, false, true);
        }
    }

    public void reload() {
        goToPage(currentPage, false, true, true);
    }

    /**
     * Extracts a resource from the Jar into a tempory file that is deleted at JVM exit. Then returns a url to that
     * extracted file.
     *
     * @param resourcePath path to a resource relative to Ensemble2.class
     * @return a url to the temp extracted file
     */
    public static String extractFile(String resourcePath) {
        String extension = resourcePath.substring(resourcePath.lastIndexOf('.'));
        String url = null;
        try {
            File temp = File.createTempFile("Ensemble", extension);
            temp.deleteOnExit();
            // get url
            url = temp.toURI().toURL().toString();
            System.out.println("extracting file [" + resourcePath + "] to url = " + url);
            // write file
            FileOutputStream fout = new FileOutputStream(temp);
            InputStream in = Ensemble2.class.getResourceAsStream(resourcePath);
            if (in == null) {
                System.err.println("Could not extractFile [" + resourcePath + "] as resource was not found relative to Ensemble2.class.");
                fout.close();
                return null;
            }
            byte[] buf = new byte[4048];
            int bytesRead = 0;
            while ((bytesRead = in.read(buf)) > 0) {
                fout.write(buf, 0, bytesRead);
            }
            in.close();
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
        return url;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
