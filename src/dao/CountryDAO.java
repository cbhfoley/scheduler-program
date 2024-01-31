package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

public class CountryDAO {



    /**
     * Method that executes an SQL statement to retrieve all the country names and returns the "Country" column, so it
     * can be displayed for the user.
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getAllCountryNames() throws SQLException {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        String query = "SELECT Country FROM countries";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                countryNames.add(resultSet.getString("Country"));
            }
        }
        return countryNames;

    }

    /**
     * Method to retrieve the country ID based on the country name. It accepts the country name as a string and returns
     * its ID as an int based on the executed SQL statement.
     *
     * @param countryName
     * @return
     * @throws SQLException
     */
    public int getCountryIdByName(String countryName) throws SQLException {
        int countryId = -1;

        String query = "SELECT Country_ID from countries where Country = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, countryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    countryId = resultSet.getInt("Country_ID");
                }
            }
        }

        return countryId;
    }


}
