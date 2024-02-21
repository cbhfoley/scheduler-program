package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

/**
 * DAO class responsible for database operations related to the contacts table.
 *
 */
public class ContactsDAO {
    /**
     * Method to retrieve the contact names from the contacts database for use in various combo boxes.
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getAllContacts() throws SQLException  {
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        String query = "SELECT * FROM contacts";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                contactNames.add(resultSet.getString("Contact_Name"));
            }
        }
        return contactNames;
    }

    /**
     * Method to retrieve contact ID based on contact name. Used so that contacts can be displayed as a name for the user, but easily
     * converted to their ID for use by the database.
     *
     * @param contactName
     * @return
     * @throws SQLException
     */
    public int getContactIdByName(String contactName) throws SQLException {
        int contactId = -1;

        String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contactName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    contactId = resultSet.getInt("Contact_ID");
                }
            }
        }
        return contactId;
    }
}
