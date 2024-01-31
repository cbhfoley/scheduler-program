package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ContactsDAO {

    public ObservableList<String> getAllContacts() throws SQLException  {
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        String query = "SELECT * FROM contacts";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                contactNames.add(resultSet.getString("Contact_Name"));
            }
        }
        return contactNames;
    }
}
