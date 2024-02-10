package controller;

import dao.AppointmentsDAO;
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

    /**
     * Method to load the customer data from the SQL database into the customer table view.
     * It converts their created date to the local time the user is viewing it in as well.
     *
     * @throws SQLException
     */
    private void loadCustomerData() throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        ObservableList<Customer> customerList = customerDAO.getAllCustomers();

        for (Customer customer : customerList) {
            customer.setCreateDate(dateTimeUtils.convertToLocal(customer.getCreateDate()));
        }
        customerTableView.setItems(customerList);
    }

    /**
     * Loads the main menu view when pressed.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the add customer view when pressed.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addCustomerButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the edit customer view when pressed.
     * If a customer is not selected it displays an error stating as such.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void editCustomerButtonAction(ActionEvent actionEvent) throws IOException {
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

    /**
     * Deletes the selected customer, if no customer selected it displays a message indicating as such.
     * If also deletes any appointments the customer might have before deleting the customer. It deletes their appointments
     * based off of the customer ID.
     * <p>
     * The user first must confirm they want to delete the customer (and any of their appointments) by pressing YES.
     * If NO is pressed, or they click the X it will not delete any records.
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteCustomerButtonAction(ActionEvent actionEvent) throws SQLException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            alertUtils.alertDisplay(6);
        } else {
            // Try to find solution to place the below code within the alertUtils class, if not, it works as intended.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Deletion Confirmation");
            alert.setContentText("This will delete the selected customer, INCLUDING their appointments. " +
                    "Are you sure you want to do this?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(ButtonType.NO) == ButtonType.YES){
                deleteCustomerAppointments();
                deleteCustomer();
                alertUtils.alertDisplay(13);
                loadCustomerData();
            }
        }
    }

    /**
     * Method to delete customer appointments based on their customer ID before a customer is deleted due to foreign key constraints.
     *
     * @throws SQLException
     */
    private void deleteCustomerAppointments() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        appointmentsDAO.deleteCustomerAppointments(selectedCustomer.getCustomerId());
    }

    /**
     * Method to delete a customer based on their customer ID.
     *
     * @throws SQLException
     */
    public void deleteCustomer() throws SQLException {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.deleteCustomer(selectedCustomer.getCustomerId());
    }
}