package dao;

import helper.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    /** Method to validate login. It accepts the entered in username and password from the "login" view and returns
     * true/false depending on the user input
     *
     * ******** IT'S CURRENTLY NOT WORKING AS INTENDED.
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean validateLogin(String userName, String password) {



        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        try {Connection connection = JDBC.connection;
             PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}

