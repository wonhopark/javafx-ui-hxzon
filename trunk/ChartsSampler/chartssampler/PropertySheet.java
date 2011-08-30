package chartssampler;

import javafx.beans.property.*;
import javafx.beans.value.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Arrays;
import java.util.List;

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
            sp.addListener(new InvalidationListener<String>() {
                @Override public void invalidated(ObservableValue<? extends String> valueModel) {
                    String newValue = valueModel.getValue();
                    if (value == null || !value.equals(newValue))
                            editor.setText(valueModel.getValue());
                }
            });
            editor.rawTextProperty().addListener(new InvalidationListener<String>() {
                @Override public void invalidated(ObservableValue<? extends String> valueModel) {
                    String newValue = valueModel.getValue();
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
            dp.addListener(new InvalidationListener<Number>() {
                @Override public void invalidated(ObservableValue<? extends Number> valueModel) {
                    editor.setText(valueModel.getValue().toString());
                }
            });
            editor.rawTextProperty().addListener(new InvalidationListener<String>() {
                @Override public void invalidated(ObservableValue<? extends String> valueModel) {
                    try {
                        dp.set(Double.parseDouble(valueModel.getValue()));
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
            ip.addListener(new InvalidationListener<Number>() {
                @Override public void invalidated(ObservableValue<? extends Number> valueModel) {
                    editor.setText(valueModel.getValue().toString());
                }
            });
            editor.rawTextProperty().addListener(new InvalidationListener<String>() {
                @Override public void invalidated(ObservableValue<? extends String> valueModel) {
                    try {
                        ip.set(Integer.parseInt(valueModel.getValue()));
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
            bp.addListener(new InvalidationListener<Boolean>() {
                @Override public void invalidated(ObservableValue<? extends Boolean> valueModel) {
                    editor.setSelected(valueModel.getValue());
                }
            });
            editor.selectedProperty().addListener(new InvalidationListener<Boolean>() {
                @Override
                public void invalidated(ObservableValue<? extends Boolean> valueModel) {
                    bp.setValue(valueModel.getValue());
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
            ep.addListener(new InvalidationListener<Object>() {
                @Override public void invalidated(ObservableValue<? extends Object> valueModel) {
                    editor.getSelectionModel().select((Enum)valueModel.getValue());
                }
            });
            editor.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener<Enum>() {
                @Override public void invalidated(ObservableValue<? extends Enum> valueModel) {
                    ep.setValue((Enum)valueModel.getValue());
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }
}
