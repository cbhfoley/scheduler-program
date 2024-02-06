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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Customer;
import utils.alertUtils;
import utils.dateTimeUtils;
import utils.generalUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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

    private Customer selectedCustomer;
    private Boolean confirmed = false;


    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        zipcodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode()));
        createdDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreateDate()));
        createdByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedBy()));
        regionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDivision()));

        loadCustomerData();
    }

    private String convertToLocal(String utcTimestamp) {
        return dateTimeUtils.convertToLocal(utcTimestamp);
    }

    private void loadCustomerData() throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        ObservableList<Customer> customerList = customerDAO.getAllCustomers();

        for (Customer customer : customerList) {
            customer.setCreateDate(convertToLocal(customer.getCreateDate()));
        }

        customerTableView.setItems(customerList);

    }

    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    public void addCustomerButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void editCustomerButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            alertUtils.alertDisplay(5);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editCustomer.fxml"));
            Parent parent = loader.load();

            // Get the controller instance and pass the selected customer
            EditCustomer editCustomerController = loader.getController();
            editCustomerController.setCustomerToEdit(selectedCustomer);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void deleteCustomerButtonAction(ActionEvent actionEvent) throws SQLException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            alertUtils.alertDisplay(6);
        } else {
            // Try to find solution to place the below code within the alertUtils class, if not, it works as intended.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Deletion Confirmation");
            alert.setContentText("This will delete all customer records, including appointments. " +
                    "Are you sure you want to do this?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(ButtonType.NO) == ButtonType.YES){
                deleteCustomer();
            }
        }
    }

    public void deleteCustomer() throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.deleteCustomer(selectedCustomer.getCustomerId());
        loadCustomerData();
    }
}