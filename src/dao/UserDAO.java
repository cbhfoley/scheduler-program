package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

/**
 * DAO class responsible for database operations related to the users table.
 *
 */
public class UserDAO {
    /**
     * Returns User_ID from the database based on the User_Name passed to it.
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public static int getUserIdByName(String user) throws SQLException {
        int userId = -1;

        String query = "SELECT User_ID from users where User_Name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("User_ID");
                }
            }
        }
        return  userId;
    }


    /**
     * Method to validate login. It accepts the entered in username and password from the "login" view and returns
     * true/false depending on the user input
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean validateLogin(String userName, String password) {
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        try {PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Method to return all usernames within the database for displaying in various combo boxes.
     * @return
     * @throws SQLException
     */
    public ObservableList<String> getAllUserNames() throws SQLException {
        ObservableList<String> userNames = FXCollections.observableArrayList();

        String query = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                userNames.add(resultSet.getString("User_Name"));
            }
        }
        return userNames;
    }

    /**
     * Method to retrieve a username based on the passed user ID.
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public String getUserNameById(int userId) throws SQLException {
        String user = null;

        String query = "SELECT User_Name FROM users WHERE User_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(userId));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = resultSet.getString("User_Name");
                }
            }
        }
        return user;
    }
}

