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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import utils.alertUtils;
import utils.dateTimeUtils;

import java.io.IOException;
import java.sql.SQLException;


import static utils.generalUtils.getLoginUsername;


/**
 * Controller class for handling the adding of new customers.
 * Allows users to input customer parameters and save them as new customers to the database.
 *
 */
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

    /**
     * Initializes the AddCustomer controller. The createdByTextField filled out with the logged-in user. This cannot be edited.
     * Also sets the countryComboBox to the countries listed in the database. Once a country is selected it will
     * load the corresponding divisions into the divisionsComboBox.
     *
     * @throws SQLException
     */
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

    /**
     * Method to load the country data into the combo box.
     *
     * @throws SQLException
     */
    private void loadCountriesData() throws SQLException {
        ObservableList<String> countries = countryDAO.getAllCountryNames();

        countryComboBox.setItems(countries);
    }

    /**
     * Method to load the division data into the combo box.
     *
     * @param selectedCountry
     * @throws SQLException
     */
    private void loadDivisionsData(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = divisionsDAO.getDivisionByCountry(selectedCountry);
        divisionComboBox.setItems(divisions);
        divisionComboBox.setPromptText(" ");
    }

    /**
     * Loads the customerMenu scene if the cancel button is clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Saves the suggested customer to the database when clicked. Validates that the information is input correctly by
     * checking if the user filled out the text fields/combo boxes properly.
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void saveButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String customerName = customerNameTextField.getText();
        String phone = customerPhoneTextField.getText();
        String address = customerAddressTextField.getText();
        String postalCode = customerPostalTextField.getText();
        String division = divisionComboBox.getValue();
        String user = getLoginUsername();

        String localTimestamp = dateTimeUtils.getCurrentTimestamp();
        String utcTimeStamp = dateTimeUtils.convertToUTC(localTimestamp);


        int divisionId = divisionsDAO.getDivisionIdByName(division);
        String divisionIdString = String.valueOf(divisionId);


        if (customerName.isEmpty() || phone.isEmpty() || address.isEmpty() || postalCode.isEmpty() || division == null) {
            alertUtils.alertDisplay(3);
        } else {
            // Customer ID is set to -1 since the SQL is set to auto-increment by 1 from the last entered customer.
            Customer customer = new Customer(-1, customerName, address, postalCode, phone, utcTimeStamp, user, utcTimeStamp, user, divisionIdString);
            // Adds the customer to the database and displays an alert indicating that this was successful. Reloads the customer menu once "OK" is pressed.
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
