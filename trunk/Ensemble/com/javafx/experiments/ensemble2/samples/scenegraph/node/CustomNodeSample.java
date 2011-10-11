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
package com.javafx.experiments.ensemble2.samples.scenegraph.node;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import com.javafx.experiments.ensemble2.GraphicsHelper;
import com.javafx.experiments.ensemble2.Sample;

/**
 * Demonstrates a creation of specific custom node by extending Parent class
 *
 * @see javafx.scene.Parent
 * @see javafx.scene.Node
 */
public class CustomNodeSample extends Sample {

    public CustomNodeSample() {
        super(50, 80);
        //create an instane of clas MyNode listed below
        MyNode myNode = new MyNode("MyNode");
        myNode.setLayoutY(50);

        MyNode parent = new MyNode("Parent");

        // REMOVE ME
        Polygon arrow = GraphicsHelper.createUMLArrow();
        arrow.setLayoutY(20);
        arrow.setLayoutX(25 - 7.5);
        Text text = new Text("<<extends>>");
        text.setTextOrigin(VPos.TOP);
        text.setLayoutY(31);
        text.setLayoutX(30);
        getChildren().addAll(arrow, text);
        // END REMOVE ME

        getChildren().addAll(parent, myNode);
    }

    private static class MyNode extends Parent {
        private Text text;
        private Rectangle rectangle;

        public MyNode(String name) {
            text = new Text(name);
            text.setTextOrigin(VPos.TOP);
            text.setLayoutX(4);
            text.setLayoutY(2);
            rectangle = new Rectangle(50, 20, Color.WHITESMOKE);
            rectangle.setStroke(Color.BLACK);
            //add nodes as childrens, order matters, first is on the bottom
            getChildren().addAll(rectangle, text);
        }
    }

    // REMOVE ME
    public static Node createIconContent() {
        MyNode myNode = new MyNode("MyNode");
        myNode.setLayoutY(50);
        MyNode parent = new MyNode("Parent");
        Polygon arrow = GraphicsHelper.createUMLArrow();
        arrow.setLayoutY(20);
        arrow.setLayoutX(25 - 7.5);
        Text text = new Text("<<extends>>");
        text.setTextOrigin(VPos.TOP);
        text.setLayoutY(31);
        text.setLayoutX(30);
        return new Group(parent, arrow, text, myNode);
    }
    // END REMOVE ME
}
