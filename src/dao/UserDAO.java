package dao;

import helper.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean validateLogin(String userName, String password) {
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        boolean isValid = false;

        try (Connection connection = JDBC.connection;
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                isValid = resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}

