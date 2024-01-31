package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

public class AppointmentsDAO {

    public static void addAppointment(Appointments appointment) throws SQLException {
        String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, appointment.getTitle());
            preparedStatement.setString(2, appointment.getDescription());
            preparedStatement.setString(3, appointment.getLocation());
            preparedStatement.setString(4, appointment.getType());
            preparedStatement.setString(5, appointment.getStart());
            preparedStatement.setString(6, appointment.getEnd());
            preparedStatement.setString(7, appointment.getCreateDate());
            preparedStatement.setString(8, appointment.getCreatedBy());
            preparedStatement.setString(9, appointment.getLastUpdate());
            preparedStatement.setString(10, appointment.getLastUpdatedBy());
            preparedStatement.setInt(11, appointment.getCustomerId());
            preparedStatement.setInt(12, appointment.getUserId());
            preparedStatement.setString(13, appointment.getContact());

            preparedStatement.executeUpdate();

        }
    }

    /**
     * Method to create a list of all the appointments that are in the SQL database. It executes an SQL query to retrieve
     * all the appointment information.
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> appointment = FXCollections.observableArrayList();
        String query = "SELECT Appointments.*, Contacts.Contact_Name " +
                        "FROM Appointments " +
                        "JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID";

        try (PreparedStatement statement = connection.prepareStatement(query);
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
                        resultSet.getString("Contact_Name")
                );
                appointment.add(appointments);
            }
        }
        return appointment;
    }
}
