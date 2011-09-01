package org.hxzon.demo.javafx;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
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
        Callback<TableColumn<String>, TableCell<String>> cellFactory =
           new Callback<TableColumn<String>, TableCell<String>>() {
                public TableCell<String> call(TableColumn<String> p) {
                    return new EditingCell();
                }
        };
        
        TableColumn<String> firstNameCol = new TableColumn<String>("First Name");
        firstNameCol.setProperty("firstName");
        firstNameCol.setCellFactory(cellFactory);
        firstNameCol.setOnEditCommit(new EventHandler<EditEvent<String>>() {
            @Override public void handle(EditEvent<String> t) {
                ((Person)t.getTableView().getItems().get(
                 t.getTablePosition().getRow())).setFirstName(t.getNewValue());
            }
        });
        

        TableColumn<String> lastNameCol = new TableColumn<String>("Last Name");
        lastNameCol.setProperty("lastName");
        lastNameCol.setCellFactory(cellFactory);
        lastNameCol.setOnEditCommit(new EventHandler<EditEvent<String>>() {
            @Override public void handle(EditEvent<String> t) {
                ((Person)t.getTableView().getItems().get(
                t.getTablePosition().getRow())).setLastName(t.getNewValue());
            }
        });       

        TableColumn<String> nameCol = new TableColumn<String>("Name");
        nameCol.getColumns().addAll(firstNameCol,lastNameCol);
        
        TableColumn<String> emailCol = new TableColumn<String>("Email");
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
      
        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("Last Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextField addEmail = new TextField();
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
                addFirstName.setText("");
                addLastName.setText("");
                addEmail.setText("");
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
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
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

		private TextField textBox;

		public EditingCell() {
			this.textBox = new TextField();
		}

		@Override
		public void startEdit() {
			super.startEdit();
			if (isEmpty()) {
				return;
			}
			setText("");
			setGraphic(textBox);
			//why no effect?
			textBox.requestFocus();
			textBox.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setGraphic(null);
			setText(getItem());
		}

		@Override
		public void commitEdit(String t) {
			super.commitEdit(t);
			setGraphic(null);
			setText(t);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				textBox.setText(item);
				setGraphic(null);
				setText(item);
			}
		}

	}
}