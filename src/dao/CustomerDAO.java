package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT customers.*, first_level_divisions.Division " +
                        "FROM customers " +
                        "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Create_Date"),
                        resultSet.getString("Created_By"),
                        resultSet.getString("Division")
                );
                customers.add(customer);
            }
        }
        return customers;
    }
}
