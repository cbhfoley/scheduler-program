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
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;
/**
 * Controller class for handling the updating of customers.
 * Allows users to input customer parameters and update them in the database.
 *
 */
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
    @FXML
    private TextField customerPhoneTextField;
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerPostalTextField;

    private CountryDAO countryDAO;
    private DivisionsDAO divisionsDAO;
    private CustomerDAO customerDAO;
    private Customer customerToEdit;

    /**
     * Initializes the AddCustomer controller. The createdByTextField filled out with the logged-in user. This cannot be edited.
     * Also sets the countryComboBox to the countries listed in the database. Once a country is selected it will
     * load the corresponding divisions into the divisionsComboBox. The user editable fields are also prefilled out
     * with the selected customer from the CustomerMenu controller.
     *
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        countryDAO = new CountryDAO();
        divisionsDAO = new DivisionsDAO();
        customerDAO = new CustomerDAO();

        editedByTextField.setPromptText(generalUtils.getLoginUsername());

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
    }

    /**
     * Loads the customerMenu scene if the cancel button is clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Updates the customer to the database when clicked. Validates that the information is input correctly by
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
        String user = generalUtils.getLoginUsername();

        String localTimeStamp = dateTimeUtils.getCurrentTimestamp();
        String utcTimeStamp = dateTimeUtils.convertToUTC(localTimeStamp);

        int divisionId = divisionsDAO.getDivisionIdByName(division);
        String divisionIdString = String.valueOf(divisionId);


        if (customerName.isEmpty() || phone.isEmpty() || address.isEmpty() || postalCode.isEmpty() || division == null) {
            alertUtils.alertDisplay(7);
        } else {
            Customer customer = new Customer(customerToEdit.getCustomerId(), customerName, address, postalCode, phone, customerToEdit.getCreateDate(), customerToEdit.getCreatedBy(), utcTimeStamp, user, divisionIdString);

            customerDAO.updateCustomer(customerToEdit.getCustomerId(), customer);
            alertUtils.alertDisplay(8);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Sets the customer to edit to the one that was selected from the CustomerMenu controller.
     * Calls the populateFieldsWithCustomerData() method.
     *
     * @param customerToEdit
     */
    public void setCustomerToEdit(Customer customerToEdit) {
        this.customerToEdit = customerToEdit;
        populateFieldsWithCustomerData();
    }

    /**
     * Fills the form out with the selected customer attributes. Pre-selects the combo boxes and fills out the
     * text fields accordingly.
     *
     */
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
