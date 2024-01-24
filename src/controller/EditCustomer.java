package controller;

import dao.CountryDAO;
import dao.DivisionsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditCustomer {

    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> divisionComboBox;

    private CountryDAO countryDAO;
    private DivisionsDAO divisionsDAO;

    @FXML
    public void initialize() throws SQLException {
        countryDAO = new CountryDAO();
        divisionsDAO = new DivisionsDAO();

        // Loads countries into the first combo box
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
    }

    public void exitButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
