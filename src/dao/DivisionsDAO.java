package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DivisionsDAO {
    public ObservableList<String> getDivisionByCountry(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        int countryId = new CountryDAO().getCountryIdByName(selectedCountry);

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(query)) {
            statement.setInt(1, countryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String division = resultSet.getString("Division");
                    divisions.add(division);
                }
            }
        }

        return divisions;
    }

    public int getDivisionIdByName(String divisionName) throws SQLException {
        int divisionId = -1; // Default value indicating failure or not found

        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try (PreparedStatement statement = JDBC.connection.prepareStatement(query)) {
            statement.setString(1, divisionName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    divisionId = resultSet.getInt("Division_ID");
                }
            }
        }

        return divisionId;
    }
}
