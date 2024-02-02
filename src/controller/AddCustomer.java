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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import utils.alertUtils;
import utils.dateTimeUtils;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static utils.generalUtils.getLoginUsername;

public class AddCustomer {
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> divisionComboBox;
    @FXML
    private TextField createdByTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerPhoneTextField;
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerPostalTextField;

    private CountryDAO countryDAO;
    private DivisionsDAO divisionsDAO;

    private final CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    public void initialize() throws SQLException {
        countryDAO = new CountryDAO();
        divisionsDAO = new DivisionsDAO();
        createdByTextField.setPromptText(getLoginUsername());

        // Loads countries into the first combo Box
        loadCountriesData();

        // Loads divisions based on which country is selected into the second combo box
        countryComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        loadDivisionsData(newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void loadCountriesData() throws SQLException {
        ObservableList<String> countries = countryDAO.getAllCountryNames();

        countryComboBox.setItems(countries);
    }

    private void loadDivisionsData(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = divisionsDAO.getDivisionByCountry(selectedCountry);
        divisionComboBox.setItems(divisions);
        divisionComboBox.setPromptText(" ");
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
            alertUtils.alertDisplay(3);
        } else {
            // Customer ID is set to -1 since the SQL is set to auto-increment by 1 from the last entered customer.
            Customer customer = new Customer(-1, customerName, address, postalCode, phone, time, user, time, user, divisionIdString);
            
            customerDAO.addCustomer(customer);
            alertUtils.alertDisplay(4);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
