package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;


public class CustomerDAO {

    /**
     * Method to create a list of all the customers that are in the SQL database. It executes an SQL query to retrieve
     * all the customer information. It displays the division as plain text as opposed to the division ID.
     *
     * @return
     * @throws SQLException
     */
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
                        resultSet.getString("Last_Update"),
                        resultSet.getString("Last_Updated_By"),
                        resultSet.getString("Division")
                );
                customers.add(customer);
            }
        }
        return customers;
    }

    /**
     * Method to add a customer based on user input information. It accepts a Customer created via the Customer model
     * and executes an SQL statement to add it to the database.
     *
     * **** CURRENTLY SOME OF THE CUSTOMER INFORMATION IS MANUALLY FED INTO THIS STATEMENT will add a fix for as the
     * program develops.
     *
     * @param customer
     * @throws SQLException
     */
    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPostalCode());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getCreateDate());
            preparedStatement.setString(6, customer.getCreatedBy());
            preparedStatement.setString(7, customer.getLastUpdate());
            preparedStatement.setString(8, customer.getLastUpdatedBy());
            preparedStatement.setString(9, customer.getDivision());

            preparedStatement.executeUpdate();
        }
    }

    public void updateCustomer(int customerId, Customer updatedCustomer) throws SQLException {
        String query = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedCustomer.getCustomerName());
            statement.setString(2, updatedCustomer.getAddress());
            statement.setString(3, updatedCustomer.getPostalCode());
            statement.setString(4, updatedCustomer.getPhone());
            statement.setString(5, updatedCustomer.getLastUpdate());
            statement.setString(6, updatedCustomer.getLastUpdatedBy());
            statement.setString(7, updatedCustomer.getDivision());
            statement.setInt(8, customerId);

            statement.executeUpdate();
            }
        }
}
