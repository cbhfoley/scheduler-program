package controller;

import dao.CountryDAO;
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
    public void initialize() throws SQLException {
        loadCountriesData();
    }

    private void loadCountriesData() throws SQLException {
        CountryDAO countryDAO = new CountryDAO();
        ObservableList<String> countries = countryDAO.getAllCountryNames();

        countryComboBox.setItems(countries);



    }

    public void exitButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
