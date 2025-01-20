import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientApp extends Application {

    private Database database = new Database();
    private ObservableList<Client> clientList = FXCollections.observableArrayList();
    private ObservableList<Car> carList = FXCollections.observableArrayList();
    private ObservableList<Service> serviceList = FXCollections.observableArrayList();
    private ObservableList<Insurance> insuranceList = FXCollections.observableArrayList();
    private ObservableList<Opinion> opinionList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    private TableView<Client> clientTableView = new TableView<>();
    private TableView<Car> carTableView = new TableView<>();
    private TableView<Service> serviceTableView = new TableView<>();
    private TableView<Insurance> insuranceTableView = new TableView<>();
    private TableView<Opinion> opinionTableView = new TableView<>();
    private TableView<Booking> bookingTableView = new TableView<>();
    private TableView<Payment> paymentTableView = new TableView<>(); 

    private double cost=0.0;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Client and Car Management");

        // Menu tab
        TabPane tabPane = new TabPane();
        Tab clientTab = new Tab("Klient", createClientForm());
        Tab carTab = new Tab("Samochód", createCarForm());
        Tab serviceTab = new Tab("Serwis", createServiceForm());
        Tab insuranceTab = new Tab("Ubezpieczenia", createInsuranceForm());
        Tab opinionTab = new Tab("Opinie", createOpinionForm());
        Tab bookingTab = new Tab("Rezerwacje", createBookingForm());
        Tab paymentTab = new Tab("Płatności",createPaymentForm());
        Tab statisticsTab = new Tab("Statystyki",createStatisticsForm());

        // Ensure tabs are closable only programmatically
        clientTab.setClosable(false);
        carTab.setClosable(false);
        serviceTab.setClosable(false);
        insuranceTab.setClosable(false);
        opinionTab.setClosable(false);
        bookingTab.setClosable(false);
        paymentTab.setClosable(false);
        statisticsTab.setClosable(false);

        tabPane.getTabs().addAll(clientTab, carTab,serviceTab,insuranceTab,opinionTab,bookingTab,paymentTab,statisticsTab);

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

        Label nameLabel = new Label("Imię:");
        TextField nameField = new TextField();
        Label surnameLabel = new Label("Nazwisko:");
        TextField surnameField = new TextField();
        Label phoneLabel = new Label("Numer telefonu:");
        TextField phoneField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Button addButton = new Button("Dodaj Klienta");
        ComboBox<Client> clientComboBox = new ComboBox<>();
        clientComboBox.setItems(clientList); 
        clientComboBox.setPromptText("Wybierz klienta");
        Button deleteButton = new Button("Usuń Klienta");
        Button updateButton = new Button("Zaktualizuj dane");

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(surnameLabel, 0, 1);
        form.add(surnameField, 1, 1);
        form.add(phoneLabel, 0, 2);
        form.add(phoneField, 1, 2);
        form.add(emailLabel, 0, 3);
        form.add(emailField, 1, 3);
        form.add(addButton, 1, 4);
        form.add(clientComboBox,5,0);
        form.add(deleteButton,5,1);
        form.add(updateButton,5,2);

        // Table for displaying clients
        TableColumn<Client, String> nameColumn = new TableColumn<>("Imię");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

        TableColumn<Client, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));

        TableColumn<Client, String> phoneColumn = new TableColumn<>("Numer telefonu");
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
        
        Button refreshButton = new Button("Odśwież klientów");
        VBox clientLayout = new VBox(10);
        clientLayout.setPadding(new Insets(10));
        clientLayout.getChildren().addAll(form, refreshButton, clientTableView);
        
        refreshButton.setOnAction(e -> {
            clientList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Client()); // Dummy client to fetch data
            
                for (ArrayList<String> row : rows) {
                    if (row.size() == 5) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Client client = new Client(row.get(0), row.get(1), row.get(2), row.get(3),Integer.parseInt(row.get(4)));
                        clientList.add(client);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        refreshButton.fire();

        deleteButton.setOnAction(e->{
            Client selectedClient = clientComboBox.getValue();
            if(selectedClient != null){
                try (Connection conn = database.connect()) {
                    database.delate(selectedClient);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                clientList.remove(selectedClient);

                clientComboBox.getSelectionModel().clearSelection();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });
        updateButton.setOnAction(e->{
            Client selectedClient = clientComboBox.getValue();
            String updateFields ="";
            if(! nameField.getText().isEmpty())
                updateFields+="imie = '"+nameField.getText()+"', ";
            if(! surnameField.getText().isEmpty())
                updateFields+="nazwisko = '"+surnameField.getText()+"', ";
            if(! phoneField.getText().isEmpty())
                updateFields+="numer_telefonu = '"+phoneField.getText()+"', ";
            if(! emailField.getText().isEmpty())
                updateFields+="email = '"+emailField.getText()+"', ";
            if(selectedClient != null && !updateFields.equals("")){
                updateFields=updateFields.substring(0, updateFields.length()-2);
                try (Connection conn = database.connect()) {
                    database.update(selectedClient,updateFields);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                clientComboBox.getSelectionModel().clearSelection();
                nameField.clear();
                surnameField.clear();
                phoneField.clear();
                emailField.clear();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                alert.showAndWait();
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
        Button addButton = new Button("Dodaj Samochód");
        ComboBox<Car> carComboBox = new ComboBox<>();
        carComboBox.setItems(carList);
        carComboBox.setPromptText("Wybierz samochód");
        Button deleteButton = new Button("Usuń Samochód");
        Button updateButton = new Button("Zaktualizuj dane");

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
        form.add(carComboBox,5,0);
        form.add(deleteButton,5,1);
        form.add(updateButton,5,2);

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

        Button refreshCarsButton = new Button("Odśwież Samochody");
        VBox carLayout = new VBox(10);
        carLayout.setPadding(new Insets(10));
        carLayout.getChildren().addAll(form,refreshCarsButton ,carTableView);
        
        refreshCarsButton.setOnAction(e -> {
            carList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Car()); // Dummy car to fetch data
            
                for (ArrayList<String> row : rows) {
                    if (row.size() == 9) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Car car = new Car(row.get(0),
                         row.get(1), 
                         row.get(2), 
                         Integer.parseInt(row.get(3)),
                         row.get(4),
                         Integer.parseInt( row.get(5)),
                         row.get(6),
                         Double.parseDouble(row.get(7)),
                         Integer.parseInt(row.get(8))
                        );
                        carList.add(car);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        refreshCarsButton.fire();

        deleteButton.setOnAction(e->{
            Car selectedCar = carComboBox.getValue();
            if(selectedCar != null){
                try (Connection conn = database.connect()) {
                    database.delate(selectedCar);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                carList.remove(selectedCar);

                carComboBox.getSelectionModel().clearSelection();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });

        updateButton.setOnAction(e->{
            Car selectedCar = carComboBox.getValue();
            String updateFields ="";
            if(! brandField.getText().isEmpty())
                updateFields+="marka = '"+brandField.getText()+"', ";
            if(! modelField.getText().isEmpty())
                updateFields+="model = '"+modelField.getText()+"', ";
            if(! categoryField.getText().isEmpty())
                updateFields+="kategoria = '"+categoryField.getText()+"', ";
            if(! yearField.getText().isEmpty())
                updateFields+="rok = '"+yearField.getText()+"', ";
            if(! registrationNumberField.getText().isEmpty())
                updateFields+="numer_rejestracyjny = '"+registrationNumberField.getText()+"', ";
            if(! mileageField.getText().isEmpty())
                updateFields+="przebieg = '"+mileageField.getText()+"', ";
            if(! dailyFeeField.getText().isEmpty())
                updateFields+="dzienna_oplata = '"+dailyFeeField.getText()+"', ";
            if(selectedCar != null && !updateFields.equals("")){
                updateFields=updateFields.substring(0, updateFields.length()-2);
                try (Connection conn = database.connect()) {
                    database.update(selectedCar,updateFields);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                carComboBox.getSelectionModel().clearSelection();   
                brandField.clear();
                modelField.clear();
                categoryField.clear();
                yearField.clear();
                registrationNumberField.clear();
                mileageField.clear();
                dailyFeeField.clear();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                alert.showAndWait();
            }
        });
        return carLayout;
    }

    // Create form and table for service
    private VBox createServiceForm() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        Label carLabel = new Label("Car:");
        ComboBox<Car> carComboBox = new ComboBox<>();
        ComboBox<Service> serviceComboBox = new ComboBox<>();
        Label serviceLabel = new Label("Service");
        carComboBox.setItems(carList); // Populate ComboBox with the car list
        carComboBox.setPromptText("Wybierz samochód");
        serviceComboBox.setItems(serviceList);
        serviceComboBox.setPromptText("Wybierz samochód z serwisu");


        Label descriptionLabel = new Label("opis:");
        TextField descriptionField = new TextField();
        Label costLabel = new Label("koszt serwisu:");
        TextField costField = new TextField();
        Button addButton = new Button("Dodaj samochód");
        Button deleteButton = new Button("Usuń samochód z serwisu");
        Button updateButton = new Button("Zaktualizuj dane");
        Button endServiceButton = new Button("Zakończ serwis");

    
        form.add(carLabel, 0, 0);
        form.add(carComboBox, 1, 0);
        form.add(descriptionLabel, 0, 1);
        form.add(descriptionField, 1, 1);
        form.add(costLabel, 0, 2);
        form.add(costField, 1, 2);
        form.add(addButton,0,3);
        form.add(serviceLabel,9,0);
        form.add(serviceComboBox,10,0);
        form.add(deleteButton,10,1);
        form.add(updateButton,10,2);
        form.add(endServiceButton,10,3);

        // Table for displaying car in service
        TableColumn<Service, String> brandColumn = new TableColumn<>("Marka");
        brandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));

        TableColumn<Service, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));

        TableColumn<Service, Integer> yearColumn = new TableColumn<>("rok");
        yearColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().year));

        TableColumn<Service, String> descriptionColumn = new TableColumn<>("Opis");
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().description));

        TableColumn<Service, Double> costColumn = new TableColumn<>("koszt");
        costColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().cost).asObject());

        serviceTableView.getColumns().addAll(brandColumn, modelColumn, yearColumn, descriptionColumn,costColumn);
        serviceTableView.setItems(serviceList);

        addButton.setOnAction(e -> {
            Car selectedCar = carComboBox.getValue();
            String description = descriptionField.getText();
            String cost = costField.getText();
          

            if (selectedCar != null && !description.isEmpty() && !cost.isEmpty() ) {
                Service service = new Service(selectedCar.id,description,Double.parseDouble(cost));
                try (Connection conn = database.connect()) {
                    database.insert(service);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                serviceList.add(service);

                carComboBox.getSelectionModel().clearSelection();
                descriptionField.clear();
                costField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });

        Button refreshButton = new Button("Odśwież serwis");
        VBox serviceLayout = new VBox(10);
        serviceLayout.setPadding(new Insets(10));
        serviceLayout.getChildren().addAll(form, refreshButton, serviceTableView);
        
        refreshButton.setOnAction(e -> {
            serviceList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Service()); // Dummy service to fetch data
                for (ArrayList<String> row : rows) {
                    if (row.size() == 6) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Service service = new Service(row.get(0), row.get(1),Integer.parseInt( row.get(2)), row.get(3),Double.parseDouble( row.get(4)),Integer.parseInt(row.get(5)));
                        serviceList.add(service);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        refreshButton.fire();

        deleteButton.setOnAction(e->{
            Service selectedCar = serviceComboBox.getValue();
            if(selectedCar != null){
                try (Connection conn = database.connect()) {
                    database.delate(selectedCar);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                serviceList.remove(selectedCar);

                serviceComboBox.getSelectionModel().clearSelection();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });
        updateButton.setOnAction(e->{
            Service selectedCar = serviceComboBox.getValue();
            String updateFields ="";
            if(! descriptionField.getText().isEmpty())
                updateFields+="opis = '"+descriptionField.getText()+"', ";
            if(! costField.getText().isEmpty())
                updateFields+="koszt = '"+costField.getText()+"', ";
            if(selectedCar != null && !updateFields.equals("")){
                updateFields=updateFields.substring(0, updateFields.length()-2);
                try (Connection conn = database.connect()) {
                    database.update(selectedCar,updateFields);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                carComboBox.getSelectionModel().clearSelection();
                descriptionField.clear();
                costField.clear();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                alert.showAndWait();
            }
        });
        
        endServiceButton.setOnAction(e->{
            Service selectedCar = serviceComboBox.getValue();
            if(selectedCar != null)
            {
                Car car= new Car(selectedCar.car_id);
                String updateField ="status = 'dostepny'";
                try (Connection conn = database.connect()) {
                    database.update(car,updateField);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                carComboBox.getSelectionModel().clearSelection();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                alert.showAndWait();
            }
        });
        return serviceLayout;
    }
    // Create form and table for insurance
    private VBox createInsuranceForm() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        Label carLabel = new Label("Car:");
        ComboBox<Car> carComboBox = new ComboBox<>();
        carComboBox.setItems(carList); // Populate ComboBox with the car list
        carComboBox.setPromptText("Wybierz samochód");
        Label typeLabel = new Label("Rodzaj:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("OC", "AC", "Assistance", "Full");
        typeComboBox.setPromptText("Wybierz rodzaj");
        Label startDateLabel = new Label("Data rozpoczęcia:");
        DatePicker startDatePicker = new DatePicker();
        Label endDateLabel = new Label("Data zakończenia:");
        DatePicker endDatePicker = new DatePicker();
        Label costLabel = new Label("koszt:");
        TextField costField = new TextField();
        Label companyLabel = new Label("firma:");
        TextField companyField = new TextField();
        Button addButton = new Button("Wykup ubezpieczenie");
        ComboBox<Insurance> insuranceComboBox = new ComboBox<>();
        insuranceComboBox.setItems(insuranceList);
        insuranceComboBox.setPromptText("Wybierz ubezpieczenie");
        Button deleteButton= new Button("Usuń ubezpieczenie");
        Button updateButton = new Button("Zaktualizuj dane");
        

        form.add(carLabel, 0, 0);
        form.add(carComboBox, 1, 0);
        form.add(typeLabel, 0, 1);
        form.add(typeComboBox, 1, 1);
        form.add(startDateLabel,0,2);
        form.add(startDatePicker,1,2);
        form.add(endDateLabel,0,3);
        form.add(endDatePicker,1,3);
        form.add(costLabel, 0, 4);
        form.add(costField, 1, 4);
        form.add(companyLabel,0,5);
        form.add(companyField,1,5);
        form.add(addButton,0,6);
        form.add(insuranceComboBox,5,0);
        form.add(deleteButton,5,1);
        form.add(updateButton,5,2);

        TableColumn<Insurance, String> brandColumn = new TableColumn<>("Marka");
        brandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));

        TableColumn<Insurance, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));

        TableColumn<Insurance, String> registrationNumberColumn = new TableColumn<>("nr rejestracyjny");
        registrationNumberColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().registration_number));

        TableColumn<Insurance, LocalDate> startDateColumn = new TableColumn<>("Data rozpoczęcia");
        startDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().start_date));

        TableColumn<Insurance, LocalDate> endDateColumn = new TableColumn<>("Data zakończenia");
        endDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().end_date));

        TableColumn<Insurance, Double> costColumn = new TableColumn<>("koszt");
        costColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().cost).asObject());

        TableColumn<Insurance, String> companyColumn = new TableColumn<>("firma");
        companyColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().company));

        insuranceTableView.getColumns().addAll(brandColumn, modelColumn, registrationNumberColumn, startDateColumn, endDateColumn,costColumn,companyColumn);
        insuranceTableView.setItems(insuranceList);

        addButton.setOnAction(e -> {
            Car selectedCar = carComboBox.getValue();
            String type = typeComboBox.getValue();
            LocalDate startDate= startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String cost = costField.getText();
            String company =companyField.getText();
          

            if (selectedCar != null && type!=null && startDate != null && endDate != null &&!cost.isEmpty() && !company.isEmpty() ) {
                Insurance insurance = new Insurance(selectedCar.id,InsuranceType.valueOf(type),startDate,endDate,Double.parseDouble(cost),company);
                try (Connection conn = database.connect()) {
                    database.insert(insurance);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                insuranceList.add(insurance);

                carComboBox.getSelectionModel().clearSelection();
                typeComboBox.getSelectionModel().clearSelection();
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                costField.clear();
                companyField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });

        Button refreshButton = new Button("Odśwież ubezpieczenia");
        VBox insuranceLayout = new VBox(10);
        insuranceLayout.setPadding(new Insets(10));
        insuranceLayout.getChildren().addAll(form, refreshButton, insuranceTableView);
        
        refreshButton.setOnAction(e -> {
            insuranceList.clear();
            try (Connection conn = database.connect()) {
                System.out.println("Connected to database!");
                ArrayList<ArrayList<String>> rows = database.read(new Insurance()); // Dummy insurance to fetch data
                for (ArrayList<String> row : rows) {
                    if (row.size() == 9) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                        Insurance insurance = new Insurance(row.get(0), row.get(1),row.get(2),LocalDate.parse(row.get(3)),LocalDate.parse(row.get(4)),InsuranceType.valueOf( row.get(5)),Double.parseDouble( row.get(6)),row.get(7),Integer.parseInt(row.get(8)));
                        insuranceList.add(insurance);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        });
        refreshButton.fire();
         
        deleteButton.setOnAction(e->{
            Insurance selectedInsurance = insuranceComboBox.getValue();
            if(selectedInsurance != null){
                try (Connection conn = database.connect()) {
                    database.delate(selectedInsurance);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                insuranceList.remove(selectedInsurance);

                insuranceComboBox.getSelectionModel().clearSelection();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
            }
        });
        updateButton.setOnAction(e->{
            Insurance selectedInsurance = insuranceComboBox.getValue();
            String updateFields ="";
            if( typeComboBox.getValue() != null)
                updateFields+="rodzaj = '"+typeComboBox.getValue()+"', ";
            if( startDatePicker.getValue() != null)
                updateFields+="data_rozpoczecia = '"+startDatePicker.getValue()+"', ";
            if( endDatePicker.getValue() != null)
                updateFields+="data_zakonczenia = '"+endDatePicker.getValue()+"', ";
            if(! costField.getText().isEmpty())
                updateFields+="koszt = '"+costField.getText()+"', ";
            if(! companyField.getText().isEmpty())
                updateFields+="nazwa_ubezpieczyciela = '"+companyField.getText()+"', ";
            if(selectedInsurance != null && !updateFields.equals("")){
                updateFields=updateFields.substring(0, updateFields.length()-2);
                try (Connection conn = database.connect()) {
                    database.update(selectedInsurance,updateFields);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                carComboBox.getSelectionModel().clearSelection();
                typeComboBox.getSelectionModel().clearSelection();
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                costField.clear();
                companyField.clear();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                alert.showAndWait();
            }
        });
        return insuranceLayout;
    }
        // Create form and table for opinion
        private VBox createOpinionForm() {
            GridPane form = new GridPane();
            form.setPadding(new Insets(10));
            form.setHgap(10);
            form.setVgap(10);
    
            Label carLabel = new Label("Car:");
            ComboBox<Car> carComboBox = new ComboBox<>();
            carComboBox.setItems(carList); // Populate ComboBox with the car list
            carComboBox.setPromptText("Wybierz samochód");
            Label clientLabel = new Label("Klient:");
            ComboBox<Client> clientComboBox = new ComboBox<>();
            clientComboBox.setItems(clientList); 
            clientComboBox.setPromptText("wybierz klienta");
            Label opinionLabel = new Label("Opinia:");
            TextArea opinionField = new TextArea();
            opinionField.setPromptText("Wpisz opinie");
            Button addButton = new Button("Dodaj opinie");
            ComboBox<Opinion> opinionComboBox = new ComboBox<>();
            opinionComboBox.setPromptText("Wybierz opinie");
            opinionComboBox.setItems(opinionList);
            Button deleteButton = new Button("Usun opinie");

            Label gradeLabel = new Label("Ocena:");
            ComboBox<Integer> gradeComboBox = new ComboBox<>();
            gradeComboBox.getItems().addAll(1,2,3,4,5);
            gradeComboBox.setPromptText("wybierz ocenę");
            Button updateButton = new Button("Zaktualizuj dane");

            form.add(carLabel, 0, 0);
            form.add(carComboBox, 1, 0);
            form.add(clientLabel, 0, 1);
            form.add(clientComboBox, 1, 1);
            form.add(opinionLabel,0,2);
            form.add(opinionField,1,2);
            form.add(gradeLabel,0,3);
            form.add(gradeComboBox,1,3);
            form.add(addButton,0,4);
            form.add(opinionComboBox,5,0);
            form.add(deleteButton,5,1);
            form.add(updateButton,5,2);
    
            TableColumn<Opinion, String> brandColumn = new TableColumn<>("Marka");
            brandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));
    
            TableColumn<Opinion, String> modelColumn = new TableColumn<>("Model");
            modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));
    
            TableColumn<Opinion, String> categoryColumn = new TableColumn<>("Kategoria");
            categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().category));
            
            TableColumn<Opinion, String> nameColumn = new TableColumn<>("Imie");
            nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

            TableColumn<Opinion, String> surnameColumn = new TableColumn<>("Nazwisko");
            surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));
            
            TableColumn<Opinion, Integer> gradeColumn = new TableColumn<>("Ocena");
            gradeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().grade));

            TableColumn<Opinion, String> opinionColumn = new TableColumn<>("Opinia");
            opinionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().opinion));

            TableColumn<Opinion, LocalDate> dateColumn = new TableColumn<>("Data wystawienia");
            dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().date));
    
           
            opinionTableView.getColumns().addAll(brandColumn, modelColumn, categoryColumn, nameColumn, surnameColumn,gradeColumn,opinionColumn,dateColumn);
            opinionTableView.setItems(opinionList);
    
            addButton.setOnAction(e -> {
                Car selectedCar = carComboBox.getValue();
                Client selectedClient = clientComboBox.getValue();
                String clientOpinon = opinionField.getText();
                LocalDate currentDate = LocalDate.now();
                int selectedGrade = gradeComboBox.getValue();
    
                if (selectedCar != null && selectedClient!=null && !clientOpinon.isEmpty() && currentDate != null  ) {
                    Opinion opinion = new Opinion(selectedClient.id,selectedCar.id,clientOpinon,selectedGrade,currentDate);
                    try (Connection conn = database.connect()) {
                        database.insert(opinion);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    opinionList.add(opinion);
    
                    carComboBox.getSelectionModel().clearSelection();
                    clientComboBox.getSelectionModel().clearSelection();
                    opinionField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                    alert.showAndWait();
                }
            });
    
            Button refreshButton = new Button("Odśwież opinie");
            VBox opinionLayout = new VBox(10);
            opinionLayout.setPadding(new Insets(10));
            opinionLayout.getChildren().addAll(form, refreshButton, opinionTableView);
            
            refreshButton.setOnAction(e -> {
                opinionList.clear();
                try (Connection conn = database.connect()) {
                    System.out.println("Connected to database!");
                    ArrayList<ArrayList<String>> rows = database.read(new Opinion()); // Dummy insurance to fetch data
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 9) { // Upewnij się, że wiersz ma odpowiednią liczbę kolumn
                            Opinion opinion = new Opinion(row.get(0), row.get(1),row.get(2),row.get(3),row.get(4),Integer.parseInt( row.get(5)),row.get(6),LocalDate.parse(row.get(7)),Integer.parseInt(row.get(8)));
                            opinionList.add(opinion);
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
            });
            refreshButton.fire();
            
            deleteButton.setOnAction(e->{
                Opinion selectedOpinion = opinionComboBox.getValue();
                if(selectedOpinion != null){
                    try (Connection conn = database.connect()) {
                        database.delate(selectedOpinion);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    opinionList.remove(selectedOpinion);
                    opinionComboBox.getSelectionModel().clearSelection();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                    alert.showAndWait();
                }
            });
            updateButton.setOnAction(e->{
                Opinion selectedOpinion = opinionComboBox.getValue();
                String updateFields ="";
                if( carComboBox.getValue() != null)
                    updateFields+="id_samochodu = '"+carComboBox.getValue().id+"', ";
                if( clientComboBox.getValue() != null)
                    updateFields+="id_klienta = '"+clientComboBox.getValue().id+"', ";
                if(! opinionField.getText().isEmpty())
                    updateFields+="opinia = '"+opinionField.getText()+"', ";
                if(gradeComboBox !=null)
                    updateFields+="ocena = '"+gradeComboBox.getValue()+"', ";
                if(selectedOpinion != null && !updateFields.equals("")){
                    updateFields=updateFields.substring(0, updateFields.length()-2);
                    try (Connection conn = database.connect()) {
                        database.update(selectedOpinion,updateFields);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    carComboBox.getSelectionModel().clearSelection();
                    clientComboBox.getSelectionModel().clearSelection();
                    gradeComboBox.getSelectionModel().clearSelection();
                    opinionField.clear();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "missing fields");
                    alert.showAndWait();
                }
            });
            return opinionLayout;
        }
        // Create form and table for opinion
        private VBox createBookingForm() {
            ObservableList<Car> availableCarsList = FXCollections.observableArrayList();

            GridPane form = new GridPane();
            form.setPadding(new Insets(10));
            form.setHgap(10);
            form.setVgap(10);
    
            Label startDateLabel = new Label("Data rozpoczęcia:");
            DatePicker startDatePicker = new DatePicker();
            Label endDateLabel = new Label("Data zakończenia:");
            DatePicker endDatePicker = new DatePicker();
            Label clientLabel = new Label("Klient:");
            ComboBox<Client> clientComboBox = new ComboBox<>();
            clientComboBox.setItems(clientList); 
            clientComboBox.setPromptText("wybierz klienta");
            Label availableCarsLabel = new Label("dostepne samochody");
            ComboBox<Car> availableCarsComboBox = new ComboBox<>();
            availableCarsComboBox.setItems(availableCarsList); 
            availableCarsComboBox.setPromptText("wybierz samochód");
            Button showButton = new Button("Wyświelt samochody");
            Label costLabel = new Label("Całkowity koszt "+cost);
            Button addButton = new Button("Wypożycz");
            ComboBox<Booking> bookingComboBox = new ComboBox<>();
            bookingComboBox.setPromptText("Wybierz rezerwację");
            bookingComboBox.setItems(bookingList);
            Button deleteButton = new Button("Usuń");

            ComboBox<String> paymentMethodComboBox=new ComboBox<>();
            paymentMethodComboBox.getItems().addAll("Karta", "Gotowka");
            paymentMethodComboBox.setPromptText("Wybierz metode płatności");
            
    
            form.add(startDateLabel, 0, 0);
            form.add(startDatePicker, 1, 0);
            form.add(endDateLabel, 0, 1);
            form.add(endDatePicker, 1, 1);
            form.add(showButton,0,2);
            form.add(availableCarsLabel,0,3);
            form.add(availableCarsComboBox,1,3);
            form.add(costLabel,0,4);
            form.add(clientLabel,0,5);
            form.add(clientComboBox,1,5);
            form.add(paymentMethodComboBox,0,6);
            form.add(addButton,0,7);
            form.add(bookingComboBox,5,0);
            form.add(deleteButton,5,1);

            TableColumn<Booking, String> brandColumn = new TableColumn<>("Marka");
            brandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));
    
            TableColumn<Booking, String> modelColumn = new TableColumn<>("Model");
            modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));
    
            TableColumn<Booking, String> categoryColumn = new TableColumn<>("Kategoria");
            categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().category));
            
            TableColumn<Booking, String> nameColumn = new TableColumn<>("Imie");
            nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

            TableColumn<Booking, String> surnameColumn = new TableColumn<>("Nazwisko");
            surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));
            
            TableColumn<Booking, String> emailColumn = new TableColumn<>("Nazwisko");
            emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().email));

            TableColumn<Booking, LocalDate> bookingDateColumn = new TableColumn<>("Data rezerwacji");
            bookingDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().bookingDate));

            TableColumn<Booking, LocalDate> startBookingDateColumn = new TableColumn<>("Data rozpoczęcia rezerwacji");
            startBookingDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().startDate));

            TableColumn<Booking, LocalDate> endBookingDateColumn = new TableColumn<>("Data końca rezerwacji");
            endBookingDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().endDate));

            TableColumn<Booking, String> statusColumn = new TableColumn<>("Status");
            statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().status));

            TableColumn<Booking, Double> costColumn = new TableColumn<>("całkowity koszt");
            costColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().cost).asObject());

           
            bookingTableView.getColumns().addAll(brandColumn, modelColumn, categoryColumn, nameColumn, surnameColumn,emailColumn,bookingDateColumn,startBookingDateColumn,endBookingDateColumn,statusColumn,costColumn);
            bookingTableView.setItems(bookingList);
    
            showButton.setOnAction(e -> {
                LocalDate starDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
    
                if (starDate != null && endDate!=null) {
                    availableCarsList.clear();
                    AvailableCar availableCar = new AvailableCar(starDate,endDate);
                    try (Connection conn = database.connect()) {
                        System.out.println("Connected to database!");
                        ArrayList<ArrayList<String>> rows = database.read(availableCar);
                        for (ArrayList<String> row : rows) {
                            if (row.size() == 6) { 
                                AvailableCar car = new AvailableCar(row.get(0), row.get(1),row.get(2),Integer.parseInt(row.get(3)),Double.parseDouble( row.get(4)),Integer.parseInt(row.get(5)));
                                availableCarsList.add(car);
                            }
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                
                }
            });
            availableCarsComboBox.setOnAction(e->{
                Car selectedCar = availableCarsComboBox.getValue();
                long days =  endDatePicker.getValue().toEpochDay()-startDatePicker.getValue().toEpochDay();
    
                if(selectedCar != null){
                    cost=days*selectedCar.daily_fee;
                    costLabel.setText("Całkowity koszt "+cost);
                }
            });
            
            addButton.setOnAction(e -> {
                Car selectedCar = availableCarsComboBox.getValue();
                Client selectedClient = clientComboBox.getValue();
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate =endDatePicker.getValue();
                LocalDate currentDate = LocalDate.now();
                String paymentMethod = paymentMethodComboBox.getValue();
    
                if (selectedCar != null && selectedClient!=null && startDate != null && endDate !=null && currentDate !=null && paymentMethod!=null) {
                    Booking booking = new Booking(selectedClient.id, selectedCar.id, currentDate, startDate, endDate,cost);
                    Payment payment = new Payment();
                    try (Connection conn = database.connect()) {
                        database.insert(booking);
                        ArrayList<ArrayList<String>> rows= database.read(new Booking());
                        int lastBookingId=Integer.parseInt(rows.get(rows.size()-1).get(11));
                        payment = new Payment(lastBookingId,currentDate,cost,PaymentMethod.valueOf(paymentMethod));
                        database.insert(payment);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    bookingList.add(booking);
                    paymentList.add(payment);

                    availableCarsComboBox.getSelectionModel().clearSelection();
                    clientComboBox.getSelectionModel().clearSelection();
                    paymentMethodComboBox.getSelectionModel().clearSelection();
                    startDatePicker.setValue(null);
                    endDatePicker.setValue(null);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                    alert.showAndWait();
                }
            });
            Button refreshButton = new Button("Odśwież rezerwacje");
            VBox bookingLayout = new VBox(10);
            bookingLayout.setPadding(new Insets(10));
            bookingLayout.getChildren().addAll(form, refreshButton, bookingTableView);
            
            refreshButton.setOnAction(e -> {
                bookingList.clear();
                try (Connection conn = database.connect()) {
                    System.out.println("Connected to database!");
                    ArrayList<ArrayList<String>> rows = database.read(new Booking()); 
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 12) {
                            Booking booking = new Booking(row.get(0), row.get(1),row.get(2),row.get(3),row.get(4),row.get(5),LocalDate.parse(row.get(6)),LocalDate.parse(row.get(7)),LocalDate.parse(row.get(8)),row.get(9),Double.parseDouble(row.get(10)),Integer.parseInt(row.get(11)));
                            bookingList.add(booking);
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
            });
            refreshButton.fire();
            
            deleteButton.setOnAction(e->{
                Booking selectedBooking = bookingComboBox.getValue();
                if(selectedBooking != null){
                    try (Connection conn = database.connect()) {
                        database.delate(selectedBooking);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    bookingList.remove(selectedBooking);
    
                    bookingComboBox.getSelectionModel().clearSelection();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                    alert.showAndWait();
                }
            });
            return bookingLayout;
        }
        private VBox createStatisticsForm(){
            ObservableList<AvgOpinion> avgOpinionList = FXCollections.observableArrayList();
            ObservableList<ServiceStats> serviceStatsList = FXCollections.observableArrayList();

            TableView<AvgOpinion>   avgOpinionTableView = new TableView<>();
            TableView<ServiceStats>  serviceStatsTableView = new TableView<>();

            TableColumn<AvgOpinion, String> brandColumn = new TableColumn<>("Marka");
            brandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));

            TableColumn<AvgOpinion, String> modelColumn = new TableColumn<>("Model");
            modelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));

            TableColumn<AvgOpinion, String> categoryColumn = new TableColumn<>("Kategoria");
            categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().category));

            TableColumn<AvgOpinion, Double> avgGradeColumn = new TableColumn<>("Średnia ocena");
            avgGradeColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().avgGrade).asObject());

            TableColumn<AvgOpinion, Integer> numOfOpinionsColumn = new TableColumn<>("Liczba opinii");
            numOfOpinionsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().numOfOpinions));

            avgOpinionTableView.getColumns().addAll(brandColumn, modelColumn, categoryColumn, avgGradeColumn, numOfOpinionsColumn);
            avgOpinionTableView.setItems(avgOpinionList);

            // Configure ServiceStats TableView
            TableColumn<ServiceStats, String> serviceBrandColumn = new TableColumn<>("Marka");
            serviceBrandColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().brand));

            TableColumn<ServiceStats, String> serviceModelColumn = new TableColumn<>("Model");
            serviceModelColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().model));

            TableColumn<ServiceStats, Integer> yearColumn = new TableColumn<>("Rok");
            yearColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().year));

            TableColumn<ServiceStats, Integer> mileageColumn = new TableColumn<>("Przebieg");
            mileageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().mileage));

            TableColumn<ServiceStats, Double> sumCostColumn = new TableColumn<>("Suma kosztów");
            sumCostColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().sumCost).asObject());

            TableColumn<ServiceStats, Double> maxCostColumn = new TableColumn<>("Maks. koszt");
            maxCostColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().maxCost).asObject());

            TableColumn<ServiceStats, String> maxCostDescColumn = new TableColumn<>("Opis maks. usterki");
            maxCostDescColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().maxCostDescription));

            serviceStatsTableView.getColumns().addAll(serviceBrandColumn, serviceModelColumn, yearColumn, mileageColumn, sumCostColumn, maxCostColumn, maxCostDescColumn);
            serviceStatsTableView.setItems(serviceStatsList);

            // Configure paymentStats Chart
            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Miesiąc");
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Przychód");

            BarChart<String, Number> incomeChart = new BarChart<>(xAxis, yAxis);
            incomeChart.setTitle("Miesięczne Przychody");
            XYChart.Series<String, Number> year = new XYChart.Series<>();
            year.setName(String.valueOf(LocalDate.now().getYear()));

            Button refreshButton = new Button("Odśwież");

            refreshButton.setOnAction(e -> {
                avgOpinionList.clear();
                serviceStatsList.clear();
                incomeChart.getData().clear();
                year.getData().clear();


                try (Connection conn = database.connect()) {
                    System.out.println("Connected to database!");
                    ArrayList<ArrayList<String>> rows = database.read(new AvgOpinion()); 
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 5) { 
                            AvgOpinion avgOpinion = new AvgOpinion(row.get(0), row.get(1),row.get(2),Double.parseDouble(row.get(3)),Integer.parseInt(row.get(4)));
                            avgOpinionList.add(avgOpinion);
                        }
                    }
    
                    rows = database.read(new ServiceStats()); 
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 7) { 
                            ServiceStats stats = new ServiceStats(row.get(0), row.get(1),Integer.parseInt(row.get(2)),Integer.parseInt(row.get(3)),Double.parseDouble(row.get(4)),Double.parseDouble(row.get(5)),row.get(6));
                            serviceStatsList.add(stats);
                        }
                    }

                    rows=database.read(new PaymentStats());
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 2) { 
                            year.getData().add(new XYChart.Data<>(row.get(0),Double.parseDouble(row.get(1))));
                        }
                    }
                    incomeChart.getData().add(year);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
            });
            refreshButton.fire();

            VBox avgOpinionBox = new VBox(10, new Label("Średnie Opinie"), avgOpinionTableView);
            VBox serviceStatsBox = new VBox(10, new Label("Statystyki Serwisowe"), serviceStatsTableView);

            HBox tablesBox = new HBox(20, avgOpinionBox, serviceStatsBox);
            tablesBox.setPadding(new Insets(10));


            VBox layout = new VBox(20, tablesBox,  incomeChart,refreshButton);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            return layout;
        }
        private VBox createPaymentForm(){
            GridPane form = new GridPane();
            form.setPadding(new Insets(10));
            form.setHgap(10);
            form.setVgap(10);

            TableColumn<Payment, String> nameColumn = new TableColumn<>("Imie");
            nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().name));

            TableColumn<Payment, String> surnameColumn = new TableColumn<>("Nazwisko");
            surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().surname));

            TableColumn<Payment, Double> costColumn = new TableColumn<>("Kwota");
            costColumn.setCellValueFactory(data ->new javafx.beans.property.SimpleDoubleProperty(data.getValue().cost).asObject());

            TableColumn<Payment, String> paymentMethodColumn = new TableColumn<>("Nazwisko");
            paymentMethodColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().paymentMethod)));

            TableColumn<Payment, LocalDate> paymentDateColumn = new TableColumn<>("Data płatności");
            paymentDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().paymentDate));

            paymentTableView.getColumns().addAll( nameColumn, surnameColumn,costColumn,paymentMethodColumn,paymentDateColumn);
            paymentTableView.setItems(paymentList);

            Button refreshButton = new Button("odśwież");
            VBox paymentLayout = new VBox(10);
            paymentLayout.setPadding(new Insets(10));
            paymentLayout.getChildren().addAll(form, refreshButton, paymentTableView);
            
            refreshButton.setOnAction(e -> {
                paymentList.clear();
                try (Connection conn = database.connect()) {
                    System.out.println("Connected to database!");
                    ArrayList<ArrayList<String>> rows = database.read(new Payment()); 
                    for (ArrayList<String> row : rows) {
                        if (row.size() == 5) {
                            Payment payment = new Payment(row.get(0), row.get(1),Double.parseDouble(row.get(2)),PaymentMethod.valueOf(row.get(3)),LocalDate.parse(row.get(4)));
                            paymentList.add(payment);
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
            });
            refreshButton.fire();
            return paymentLayout;

        }
}
