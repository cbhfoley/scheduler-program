package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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

    public static void editAppointment(int apptId, Appointments updatedAppointment) throws SQLException {
        String query = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type =?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedAppointment.getTitle());
            statement.setString(2, updatedAppointment.getDescription());
            statement.setString(3, updatedAppointment.getLocation());
            statement.setString(4, updatedAppointment.getType());
            statement.setString(5, updatedAppointment.getStart());
            statement.setString(6, updatedAppointment.getEnd());
            statement.setString(7, updatedAppointment.getLastUpdate());
            statement.setString(8, updatedAppointment.getLastUpdatedBy());
            statement.setInt(9, updatedAppointment.getCustomerId());
            statement.setInt(10, updatedAppointment.getUserId());
            statement.setString(11, updatedAppointment.getContact());
            statement.setInt(12, apptId);

            statement.executeUpdate();
        }
    }

    public static boolean isOverlap(int customerId, String startTimeStamp, String endTimeStamp) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments " +
                "WHERE Customer_ID = ? " +
                "AND Start < ? " +
                "AND End > ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.setString(2, endTimeStamp);
            statement.setString(3, startTimeStamp);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        }
    }

    public static boolean isOverlapForEdit(int customerId, int apptId, String startTimeStamp, String endTimeStamp) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments " +
                "WHERE Customer_ID = ? " +
                "AND Appointment_ID != ? " +
                "AND Start < ? " +
                "AND End > ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.setInt(2, apptId);
            statement.setString(3, endTimeStamp);
            statement.setString(4, startTimeStamp);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
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
                        "JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                        "ORDER BY Appointment_ID";


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

    public void deleteCustomerAppointments(int customerId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }

    }

    public void deleteAppointment(int apptId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, apptId);
            statement.executeUpdate();
        }
    }

    public ObservableList<Appointments> getAppointmentsFiltered(LocalDateTime start, LocalDateTime end) throws SQLException {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String query = "SELECT Appointments.*, Contacts.Contact_Name " +
                "FROM Appointments " +
                "JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                "WHERE Start >= ? AND End <= ? " +
                "ORDER BY Appointment_ID";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(start));
            statement.setString(2, String.valueOf(end));
            ResultSet resultSet = statement.executeQuery();
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
                    appointmentsList.add(appointments);
                }
        }
        return appointmentsList;
    }
}
