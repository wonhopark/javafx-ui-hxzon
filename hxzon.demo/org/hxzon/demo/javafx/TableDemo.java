package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.EditEvent;
import javafx.scene.control.TextBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableDemo extends Application {

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data = 
        FXCollections.observableArrayList(
            new Person("Jacob", "Smith", "jacob.smith@example.com"),
            new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
            new Person("Ethan", "Williams", "ethan.williams@example.com"),
            new Person("Emma", "Jones", "emma.jones@example.com"),
            new Person("Michael", "Brown", "michael.brown@example.com")
        );
    
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(400);
        stage.setHeight(450);

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setStyle("-fx-base: #b6e7c9;");
        Callback<TableColumn, TableCell> cellFactory =
           new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn p) {
                    return new EditingCell();
                }
        };
        
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setProperty("firstName");
        firstNameCol.setCellFactory(cellFactory);
        firstNameCol.setOnEditCommit(new EventHandler<EditEvent<String>>() {
            @Override public void handle(EditEvent<String> t) {
                ((Person)t.getTableView().getItems().get(
                 t.getTablePosition().getRow())).setFirstName(t.getNewValue());
            }
        });
        

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setProperty("lastName");
        lastNameCol.setCellFactory(cellFactory);
        lastNameCol.setOnEditCommit(new EventHandler<EditEvent<String>>() {
            @Override public void handle(EditEvent<String> t) {
                ((Person)t.getTableView().getItems().get(
                t.getTablePosition().getRow())).setLastName(t.getNewValue());
            }
        });       

        TableColumn nameCol = new TableColumn("Name");
        nameCol.getColumns().addAll(firstNameCol,lastNameCol);
        
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setProperty("email");
        emailCol.setCellFactory(cellFactory);
        emailCol.setOnEditCommit(new EventHandler<EditEvent<String>>() {
            @Override public void handle(EditEvent<String> t) {
                ((Person)t.getTableView().getItems().get(
                t.getTablePosition().getRow())).setEmail(t.getNewValue());
            }
        });
                                      
        table.setItems(data);
        table.getColumns().addAll(nameCol, emailCol);
      
        final TextBox addFirstName = new TextBox();
        addFirstName.setPromptText("Last Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        final TextBox addLastName = new TextBox();
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextBox addEmail = new TextBox();
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addEmail.setPromptText("Email");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                data.add(new Person(
                        addFirstName.getText(),
                        addLastName.getText(),
                        addEmail.getText()
                        ));
                addFirstName.clear();
                addLastName.clear();
                addEmail.clear();
            }
        });

        hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.setVisible(true);
    }
    
    public static class Person {
        private final StringProperty firstName;
        private final StringProperty lastName;
        private final StringProperty email;

        private Person(String fName, String lName, String email) {
            this.firstName = new StringProperty(fName);
            this.lastName = new StringProperty(lName);
            this.email = new StringProperty(email);
        }

        public String getFirstName() {
            return firstName.get();
        }
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
        
        public String getLastName() {
            return lastName.get();
        }
        public void setLastName(String fName) {
            lastName.set(fName);
        }
        
        public String getEmail() {
            return email.get();
        }
        public void setEmail(String fName) {
            email.set(fName);
        }
        
    }
    
    class EditingCell extends TableCell<String> {

        private final Label label;
        private TextBox textBox;

        public EditingCell() {
            this.label = new Label();
        }

        @Override public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }

            if (textBox == null) {
                createTextBox();
            } else {
                textBox.setText(getItem());
            }
            setNode(textBox);
            textBox.requestFocus();
            textBox.selectAll();
        }

        @Override public void cancelEdit() {
            super.cancelEdit();
            setNode(label);
        }

        @Override public void commitEdit(String t) {
            super.commitEdit(t);
            setNode(label);
        }

        @Override public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                if (textBox != null) {
                    textBox.setText(item);
                }
                label.setText(item);
                setNode(label);
            }
        }

        private void createTextBox() {
            textBox = new TextBox(getItem());
            textBox.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textBox.getRawText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }
    }
}