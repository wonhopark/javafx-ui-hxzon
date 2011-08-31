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
package chartssampler;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * PropertySheet for editing groups of bean properties
 */
public class PropertySheet extends GridPane {

    private final List<PropertyGroup> propertyGroups;

    public PropertySheet(PropertyGroup ... propertyGroups) {
        getStyleClass().add("property-sheet");
        this.propertyGroups = Arrays.asList(propertyGroups);
        int row = 0;
        for(PropertyGroup pg: propertyGroups) row = pg.create(this,row);
    }

    public static Property createProperty(String name, EventHandler<ActionEvent> action) {
        return new ActionPropertyImpl(name, action);
    }
    public static Property createProperty(String name, StringProperty prop) {
        return new StringPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, IntegerProperty prop) {
        return new IntegerPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, DoubleProperty prop) {
        return new DoublePropertyImpl(name, prop);
    }
    public static Property createProperty(String name, BooleanProperty prop) {
        return new BooleanPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, ObjectProperty prop) {
        if (prop.getValue() instanceof Enum) {
            return new EnumPropertyImpl(name, prop);
        } else {
            return null;
        }
    }

    public static class PropertyGroup {
        private final String title;
        private final Property[] properties;

        public PropertyGroup(String title,Property ... properties) {
            this.title = title;
            this.properties = properties;
        }

        private int create(GridPane grid, int row) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("property-sheet-header");
            titleLabel.setMaxWidth(Double.MAX_VALUE);
            GridPane.setMargin(titleLabel, new Insets((row==0)?0:5,0,5,0));
            GridPane.setColumnIndex(titleLabel, 0);
            GridPane.setColumnSpan(titleLabel, 2);
            GridPane.setHgrow(titleLabel, Priority.ALWAYS);
            GridPane.setRowIndex(titleLabel, row);
            grid.getChildren().add(titleLabel);
            row ++;
            for(Property prop: properties) {
                if (prop == null){
                    continue;
                } else if (prop instanceof ActionPropertyImpl) {
                    Label propLabel = new Label(prop.getName()+":");
                    Node editor = prop.getEditor();
                    GridPane.setHalignment(editor, HPos.CENTER);
                    GridPane.setHgrow(editor, Priority.ALWAYS);
                    GridPane.setMargin(editor, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(editor, 0);
                    GridPane.setColumnSpan(editor, 2);
                    GridPane.setRowIndex(editor, row);
                    grid.getChildren().add(editor);
                    row ++;
                } else {
                    Label propLabel = new Label(prop.getName()+":");
                    propLabel.setMaxWidth(Double.MAX_VALUE);
                    propLabel.setAlignment(Pos.CENTER_RIGHT);
                    GridPane.setHalignment(propLabel, HPos.RIGHT);
                    GridPane.setMargin(propLabel, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(propLabel,0);
                    GridPane.setRowIndex(propLabel, row);
                    grid.getChildren().add(propLabel);
                    Node editor = prop.getEditor();
                    GridPane.setHgrow(editor, Priority.ALWAYS);
                    GridPane.setMargin(editor, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(editor,1);
                    GridPane.setRowIndex(editor, row);
                    grid.getChildren().add(editor);
                    row ++;
                }
            }
            return row;
        }
    }

    public static abstract class Property {
        private String name;

        protected Property(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract Node getEditor();
    }

    static class ActionPropertyImpl extends Property {
        private Hyperlink editor = new Hyperlink();

        ActionPropertyImpl(String name, EventHandler<ActionEvent> action) {
            super(name);
            editor.setText(name);
            editor.setOnAction(action);
        }

        @Override public Node getEditor() {
            return editor;
        }
    }
    static class StringPropertyImpl extends Property {
        private TextBox editor = new TextBox();
        private String value = null;

        StringPropertyImpl(String name, final StringProperty sp) {
            super(name);
            editor.setText(sp.getValue());
            sp.addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> valueModel, String oldValue, String newValue) {
                    if (value == null || !value.equals(newValue))
                            editor.setText(valueModel.getValue());
                }
            });
            editor.rawTextProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> valueModel, String oldValue, String newValue) {
                    if (value == null || !value.equals(newValue))
                         sp.setValue(valueModel.getValue());
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    static class DoublePropertyImpl extends Property {
        private TextBox editor = new TextBox();

        DoublePropertyImpl(String name, final DoubleProperty dp) {
            super(name);
            editor.setText(Double.toString(dp.getValue()));
            dp.addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> valueModel, Number oldValue, Number newValue) {
                    editor.setText(newValue.toString());
                }
            });
            editor.rawTextProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> valueModel, String oldValue, String newValue) {
                    try {
                        dp.set(Double.parseDouble(newValue));
                    } catch (NumberFormatException e) {}
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    static class IntegerPropertyImpl extends Property {
        private TextBox editor = new TextBox();

        IntegerPropertyImpl(String name, final IntegerProperty ip) {
            super(name);
            editor.setText(Double.toString(ip.getValue()));
            ip.addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> valueModel, Number oldValue, Number newValue) {
                    editor.setText(newValue.toString());
                }
            });
            editor.rawTextProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> valueModel, String oldValue, String newValue) {
                    try {
                        ip.set(Integer.parseInt(newValue));
                    } catch (NumberFormatException e) {}
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    static class BooleanPropertyImpl extends Property {
        private CheckBox editor = new CheckBox();

        BooleanPropertyImpl(String name, final BooleanProperty bp) {
            super(name);
            editor.setSelected(bp.getValue());
            bp.addListener(new ChangeListener<Boolean>() {
                @Override public void changed(ObservableValue<? extends Boolean> valueModel, Boolean oldValue, Boolean newValue) {
                    editor.setSelected(newValue);
                }
            });
            editor.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> valueModel, Boolean oldValue, Boolean newValue) {
                    bp.setValue(newValue);
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    static class EnumPropertyImpl extends Property {
        private ChoiceBox<Enum> editor = new ChoiceBox<Enum>();

        EnumPropertyImpl(String name, final ObjectProperty<Enum> ep) {
            super(name);
            Enum val =  ep.get();
            for (Object enumOpt : val.getDeclaringClass().getEnumConstants()) {
                editor.getItems().add((Enum)enumOpt);
            }
            editor.getSelectionModel().select(val);
            ep.addListener(new ChangeListener<Enum>() {
                @Override public void changed(ObservableValue<? extends Enum> valueModel, Enum oldValue, Enum newValue) {
                    editor.getSelectionModel().select(newValue);
                }
            });
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Enum>() {
                @Override public void changed(ObservableValue<? extends Enum> valueModel, Enum oldValue, Enum newValue) {
                    ep.setValue(newValue);
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }
}
