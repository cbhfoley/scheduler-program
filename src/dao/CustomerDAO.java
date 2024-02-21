package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

/**
 * DAO class responsible for database operations related to the customers table.
 *
 */
public class CustomerDAO {

    /**
     * Method to create a list of all the customers that are in the SQL database. Executes an SQL query to retrieve
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

    /**
     * Method to update a customer based on user input information. It accepts a Customer created via the Customer model
     * and executes an SQL statement to update it within the database.
     *
     * @param customerId
     * @param updatedCustomer
     * @throws SQLException
     */
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

    /**
     * Method to delete a customer from the customers table based on their Customer ID.
     *
      * @param customerId
     * @throws SQLException
     */
    public void deleteCustomer(int customerId) throws SQLException {
        String query = "DELETE FROM customers WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    /**
     * Method that retrieves all the customer names to display in combo boxes for the add/edit appointment view.
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getAllCustomerNames() throws SQLException {
        ObservableList<String> customerNames = FXCollections.observableArrayList();

        String query = "SELECT * FROM customers";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                customerNames.add(resultSet.getString("Customer_Name"));
            }
        }
        return customerNames;
    }

    /**
     * Method that retrieves a customer ID based on the passed customer name.
     *
     * @param customer
     * @return
     * @throws SQLException
     */
    public int getCustomerIdByName(String customer) throws SQLException {
        int customerId = -1;

        String query = "SELECT Customer_ID FROM customers WHERE Customer_Name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customer);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    customerId = resultSet.getInt("Customer_ID");
                }
            }
        }
        return customerId;
    }

    /**
     * Method to retrieve the customer name based on the passed customer ID.
     *
     * @param customerId
     * @return
     * @throws SQLException
     */
    public String getCustomerNameById(int customerId) throws SQLException {
        String customer = null;

        String query = "SELECT Customer_name FROM customers WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(customerId));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    customer = resultSet.getString("Customer_Name");
                }
            }
        }
        return  customer;
    }
}
