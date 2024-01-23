package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentsDAO {

    public ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> appointment = FXCollections.observableArrayList();
        String query = "SELECT * FROM Appointments";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Appointments appointments = new Appointments(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getString("Start"),
                        resultSet.getString("End"),
                        resultSet.getString("Create_Date"),
                        resultSet.getString("Created_By"),
                        resultSet.getString("Last_Update"),
                        resultSet.getString("Last_Updated_By"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID")
                );
                appointment.add(appointments);
            }
        }
        return appointment;
    }
}
