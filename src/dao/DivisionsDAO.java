package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;


public class DivisionsDAO {
    /**
     * Method to create a list of the divisions corresponding to the selected country. There are 3 countries and when
     * one is selected it returns a list of all divisions associating with the country ID.
     *
     * @param selectedCountry
     * @return
     * @throws SQLException
     */

    public ObservableList<String> getDivisionByCountry(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        int countryId = new CountryDAO().getCountryIdByName(selectedCountry);

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
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

    /**
     * Method to retrieve division ID by name. This is so when a customer is added, the selected division is converted
     * from plain text back into it's division ID. It allows the user to not have to memorize the different division ID's
     * and what they correspond too (e.g. Alabama's division ID = 1).
     *
     *
     * @param divisionName
     * @return
     * @throws SQLException
     */
    public int getDivisionIdByName(String divisionName) throws SQLException {
        int divisionId = -1; // Default value indicating failure or not found

        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, divisionName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    divisionId = resultSet.getInt("Division_ID");
                }
            }
        }

        return divisionId;
    }

    public String getCountryByDivision(String divisionName) throws SQLException {
        String country = null;
        int divisionId = getDivisionIdByName(divisionName);

        String query = "SELECT c.Country FROM countries c " +
                "JOIN first_level_divisions d ON c.Country_ID = d.Country_ID " +
                "WHERE d.Division_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, divisionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    country = resultSet.getString("Country");
                }
            }
        }
        return country;
    }
}
