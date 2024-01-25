package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {

    public ObservableList<String> getAllCountryNames() throws SQLException {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        String query = "SELECT Country FROM countries";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                countryNames.add(resultSet.getString("Country"));
            }
        }
        return countryNames;

    }

    public int getCountryIdByName(String countryName) throws SQLException {
        int countryId = -1;

        String query = "SELECT Country_ID from countries where Country = ?";
        try (PreparedStatement statement = JDBC.connection.prepareStatement(query)) {
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
