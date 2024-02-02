package controller;

import dao.CountryDAO;
import dao.CustomerDAO;
import dao.DivisionsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import utils.alertUtils;
import utils.dateTimeUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditCustomer {

    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> divisionComboBox;
    @FXML
    private TextField editedByTextField;
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML TextField customerPhoneTextField;
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerPostalTextField;

    private CountryDAO countryDAO;
    private DivisionsDAO divisionsDAO;
    private CustomerDAO customerDAO;
    private Customer customerToEdit;

    @FXML
    public void initialize() throws SQLException {
        countryDAO = new CountryDAO();
        divisionsDAO = new DivisionsDAO();
        customerDAO = new CustomerDAO();

        editedByTextField.setPromptText(getLoginUsername());

        // Loads countries into the first combo box
        loadCountriesData();

        // Loads divisions based on which country is selected into the second combo box
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadDivisionsData(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }

    private String getLoginUsername() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        try {
            Parent parent = loader.load();
            Login loginController = loader.getController();
            return loginController.getUsername();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void loadCountriesData() throws SQLException {
        ObservableList<String> countries = countryDAO.getAllCountryNames();

        countryComboBox.setItems(countries);
    }

    private void loadDivisionsData(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = divisionsDAO.getDivisionByCountry(selectedCountry);
        divisionComboBox.setItems(divisions);
    }

    public void exitButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void saveButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String customerName = customerNameTextField.getText();
        String phone = customerPhoneTextField.getText();
        String address = customerAddressTextField.getText();
        String postalCode = customerPostalTextField.getText();
        String division = divisionComboBox.getValue();
        String user = getLoginUsername();

        String time = dateTimeUtils.getCurrentTimestamp();
        int divisionId = divisionsDAO.getDivisionIdByName(division);
        String divisionIdString = String.valueOf(divisionId);


        if (customerName.isEmpty() || phone.isEmpty() || address.isEmpty() || postalCode.isEmpty() || division == null) {
            alertUtils.alertDisplay(7);
        } else {
            Customer customer = new Customer(customerToEdit.getCustomerId(), customerName, address, postalCode, phone, time, user, time, user, divisionIdString);

            customerDAO.updateCustomer(customerToEdit.getCustomerId(), customer);
            alertUtils.alertDisplay(8);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void setCustomerToEdit(Customer customerToEdit) {
        this.customerToEdit = customerToEdit;
        populateFieldsWithCustomerData();
    }

    private void populateFieldsWithCustomerData() {
        try {
            int customerId = customerToEdit.getCustomerId();
            String customerName = customerToEdit.getCustomerName();
            String customerPhone = customerToEdit.getPhone();
            String customerAddress = customerToEdit.getAddress();
            String customerPostal = customerToEdit.getPostalCode();
            String customerDivision = customerToEdit.getDivision();
            String customerCountry = divisionsDAO.getCountryByDivision(customerDivision);

            customerIdTextField.setPromptText(String.valueOf(customerId));
            customerNameTextField.setText(customerName);
            customerPhoneTextField.setText(customerPhone);
            customerAddressTextField.setText(customerAddress);
            customerPostalTextField.setText(customerPostal);
            divisionComboBox.setValue(customerDivision);
            countryComboBox.setValue(customerCountry);
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }




    }
}
