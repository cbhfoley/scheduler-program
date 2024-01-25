package controller;

import dao.CustomerDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerMenu {
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> zipcodeColumn;

    @FXML
    private TableColumn<Customer, String> createdDateColumn;

    @FXML
    private TableColumn<Customer, String> createdByColumn;

    @FXML
    private TableColumn<Customer, String> regionColumn;


    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        zipcodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        createdDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreateDate()));
        createdByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedBy()));
        regionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDivisionId()));

        loadCustomerData();
    }

    private void loadCustomerData() throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        ObservableList<Customer> customerList = customerDAO.getAllCustomers();

        customerTableView.setItems(customerList);

    }

    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void addCustomerButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void editCustomerButtonAction(ActionEvent actionEvent) throws IOException {

        Customer customerToEdit = customerTableView.getSelectionModel().getSelectedItem();

        if (customerToEdit == null) {
            alertDisplay((1));
        } else {

            Parent parent = FXMLLoader.load(getClass().getResource("/view/editCustomer.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


    }

    private void alertDisplay(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please select a customer to edit.");
                alert.showAndWait();

            }
        }
    }
}