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
package com.javafx.experiments.ensemble2.samples.controls.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import com.javafx.experiments.ensemble2.Sample;

/**
 * Demonstrates a Table.
 *
 * @see javafx.scene.control.TableCell
 * @see javafx.scene.control.TableColumn
 * @see javafx.scene.control.TablePosition
 * @see javafx.scene.control.TableRow
 * @see javafx.scene.control.TableView
 */
public class TableSample extends Sample {
    public TableSample() {
        final ObservableList<Person> data = FXCollections.observableArrayList(new Person("Jacob", "Smith", "jacob.smith@example.com"),
                new Person("Isabella", "Johnson", "isabella.johnson@example.com"), new Person("Ethan", "Williams", "ethan.williams@example.com"),
                new Person("Emma", "Jones", "emma.jones@example.com"), new Person("Michael", "Brown", "michael.brown@example.com"));
        TableColumn<Person, String> firstNameCol = new TableColumn<Person, String>();
        firstNameCol.setText("First");
        firstNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person,String>,ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Person,String> p) {
                return new SimpleStringProperty(p.getValue().firstName);
            }
        });
//        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        TableColumn<Person, String> lastNameCol = new TableColumn<Person, String>();
        lastNameCol.setText("Last");
        lastNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person,String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Person,String> p) {
                return new SimpleStringProperty(p.getValue().lastName);
            }
        });
//        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        TableColumn<Person, String> emailCol = new TableColumn<Person, String>();
        emailCol.setText("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person,String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Person,String> p) {
                return new SimpleStringProperty(p.getValue().email);
            }
        });
//        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        TableView<Person> tableView = new TableView<Person>();
        tableView.setItems(data);
        tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        getChildren().add(tableView);
    }

    public static class Person {
        String firstName;
        String lastName;
        String email;

        private Person(String fName, String lName, String email) {
            this.firstName = fName;
            this.lastName = lName;
            this.email = email;
        }
    }
}
