import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientApp extends Application {

    private Database database = new Database();
    private ObservableList<Client> clientList = FXCollections.observableArrayList();
    private ObservableList<Car> carList = FXCollections.observableArrayList();
    private TableView<Client> clientTableView = new TableView<>();
    private TableView<Car> carTableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Client and Car Management");

        // Menu tab
        TabPane tabPane = new TabPane();
        Tab clientTab = new Tab("Klient", createClientForm());
        Tab carTab = new Tab("Samochód", createCarForm());

        // Ensure tabs are closable only programmatically
        clientTab.setClosable(false);
        carTab.setClosable(false);

        tabPane.getTabs().addAll(clientTab, carTab);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().add(tabPane);

        primaryStage.setScene(new Scene(layout, 800, 600));
        primaryStage.show();
    }

    // Create form and table for clients
    private VBox createClientForm() {
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
        TableColumn<Client, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

        TableColumn<Client, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));

        TableColumn<Client, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().phone_number));

        TableColumn<Client, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().email));

        clientTableView.getColumns().addAll(nameColumn, surnameColumn, phoneColumn, emailColumn);
        clientTableView.setItems(clientList);

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (!name.isEmpty() && !surname.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                Client client = new Client(name, surname, phone, email);
                try (Connection conn = database.connect()) {
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
        
        Button refreshButton = new Button("Refresh Clients");
        VBox clientLayout = new VBox(10);
        clientLayout.setPadding(new Insets(10));
        clientLayout.getChildren().addAll(form, refreshButton, clientTableView);
        
        refreshButton.setOnAction(e -> {
            clientList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Client("", "", "", "")); // Dummy client to fetch data
            
                for (ArrayList<String> row : rows) {
                    if (row.size() == 4) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Client client = new Client(row.get(0), row.get(1), row.get(2), row.get(3));
                        clientList.add(client);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        return clientLayout;
    }

    // Create form and table for cars
    private VBox createCarForm() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        Label barndLabel = new Label("marka:");
        TextField brandField = new TextField();
        Label modelLabel = new Label("Model:");
        TextField modelField = new TextField();
        Label categoryLabel = new Label("Kategoria:");
        TextField categoryField = new TextField();
        Label yearLabel = new Label("Rok:");
        TextField yearField = new TextField();
        Label  registrationNumberLabel = new Label("nr rejestracyjny:");
        TextField registrationNumberField = new TextField();
        Label mileageLabel = new Label("przebieg:");
        TextField mileageField = new TextField();
        Label dailyFeeLabel = new Label("dzienna_oplata:");
        TextField dailyFeeField = new TextField();
        Button addButton = new Button("Add Car");

        form.add(barndLabel, 0, 0);
        form.add(brandField, 1, 0);
        form.add(modelLabel, 0, 1);
        form.add(modelField, 1, 1);
        form.add(categoryLabel, 0, 2);
        form.add(categoryField, 1, 2);
        form.add(yearLabel, 0, 3);
        form.add(yearField, 1, 3);
        form.add(registrationNumberLabel, 0, 4);
        form.add(registrationNumberField, 1, 4);
        form.add(mileageLabel, 0, 5);
        form.add(mileageField, 1, 5);
        form.add(dailyFeeLabel, 0, 6);
        form.add(dailyFeeField, 1, 6);
        form.add(addButton, 1, 7);

        // Table for displaying cars
        TableColumn<Car, String> makeColumn = new TableColumn<>("Marka");
        makeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));

        TableColumn<Car, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));

        TableColumn<Car, String> categoryColumn = new TableColumn<>("Kategoria");
        categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().category));

        TableColumn<Car, Integer> yearColumn = new TableColumn<>("Rok");
        yearColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().year));

        TableColumn<Car, String> registrationColumn = new TableColumn<>("numer rejestracyjny");
        registrationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().registration_number));

        TableColumn<Car, Integer> mileageColumn = new TableColumn<>("przebieg");
        mileageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().mileage));

        TableColumn<Car, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().status));

        TableColumn<Car, Double> dailyFeeColumn = new TableColumn<>("opłata");
        dailyFeeColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().daily_fee).asObject());

        carTableView.getColumns().addAll(makeColumn, modelColumn,categoryColumn, yearColumn,registrationColumn,mileageColumn,statusColumn,dailyFeeColumn);
        carTableView.setItems(carList);

        addButton.setOnAction(e -> {
            String brand = brandField.getText();
            String model = modelField.getText();
            String category = categoryField.getText();
            String yearText = yearField.getText();
            String registrationNumber=registrationNumberField.getText();
            String mileageText=mileageField.getText();
            String dailyFeeText=dailyFeeField.getText();



            if (!brand.isEmpty() && !model.isEmpty() && !yearText.isEmpty() && !registrationNumber.isEmpty() && !mileageText.isEmpty() && !dailyFeeText.isEmpty()) {
                try {
                    int year = Integer.parseInt(yearText);
                    int mileage = Integer.parseInt(mileageText);
                    double dailyFee = Double.parseDouble(dailyFeeText);

                    Car car = new Car(brand, model, category, year, registrationNumber, mileage, dailyFee);
                    try (Connection conn = database.connect()) {
                        database.insert(car);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    carList.add(car);

                    brandField.clear();
                    modelField.clear();
                    categoryField.clear();
                    yearField.clear();
                    registrationNumberField.clear();
                    mileageField.clear();
                    dailyFeeField.clear();
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Year, mileage, and daily fee must be numbers.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });

        Button refreshCarsButton = new Button("Refresh Cars");
        VBox carLayout = new VBox(10);
        carLayout.setPadding(new Insets(10));
        carLayout.getChildren().addAll(form,refreshCarsButton ,carTableView);
        
        refreshCarsButton.setOnAction(e -> {
            carList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Car()); // Dummy car to fetch data
            
                for (ArrayList<String> row : rows) {
                    if (row.size() == 8) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Car car = new Car(row.get(0),
                         row.get(1), 
                         row.get(2), 
                         Integer.parseInt(row.get(3)),
                         row.get(4),
                         Integer.parseInt( row.get(5)),
                         row.get(6),
                         Double.parseDouble(row.get(7))
                        );
                        carList.add(car);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        return carLayout;
    }
}
