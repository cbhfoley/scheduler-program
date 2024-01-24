package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;


public class CustomerDAO {

    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT customers.*, first_level_divisions.Division " +
                        "FROM customers " +
                        "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";

        try (PreparedStatement statement = connection.prepareStatement(query);
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

    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPostalCode());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getCreateDate());
            preparedStatement.setString(6, customer.getCreatedBy());
            preparedStatement.setString(7, customer.getDivisionId());

            preparedStatement.executeUpdate();
        }
    }
}
