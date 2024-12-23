import java.sql.Connection;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class ClientApp extends Application {

    private Database database = new Database();
    private ObservableList<Clinet> clientList = FXCollections.observableArrayList();
    private TableView<Clinet> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Client Management");

        // Form for adding a new client
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label surnameLabel = new Label("Surname:");
        TextField surnameField = new TextField();
        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Button addButton = new Button("Add Client");

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(surnameLabel, 0, 1);
        form.add(surnameField, 1, 1);
        form.add(phoneLabel, 0, 2);
        form.add(phoneField, 1, 2);
        form.add(emailLabel, 0, 3);
        form.add(emailField, 1, 3);
        form.add(addButton, 1, 4);

        // Table for displaying clients
        TableColumn<Clinet, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

        TableColumn<Clinet, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));

        TableColumn<Clinet, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().phone_number));

        TableColumn<Clinet, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().email));

        tableView.getColumns().addAll(nameColumn, surnameColumn, phoneColumn, emailColumn);
        tableView.setItems(clientList);

        Button refreshButton = new Button("Refresh Clients");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(form, refreshButton, tableView);

        // Button actions
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (!name.isEmpty() && !surname.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                Clinet client = new Clinet(name, surname, phone, email);
                try (Connection conn = database.connect()) {
                    System.out.println("Connected to database!");
                    database.insert(client);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                clientList.add(client);

                nameField.clear();
                surnameField.clear();
                phoneField.clear();
                emailField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });

        refreshButton.setOnAction(e -> {
            clientList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Clinet("", "", "", "")); // Dummy client to fetch data
            
                for (ArrayList<String> row : rows) {
                    if (row.size() == 4) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Clinet client = new Clinet(row.get(0), row.get(1), row.get(2), row.get(3));
                        clientList.add(client);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });

        primaryStage.setScene(new Scene(layout, 600, 400));
        primaryStage.show();
    }

}