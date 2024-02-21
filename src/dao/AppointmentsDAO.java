package dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static helper.JDBC.connection;

/**
 * DAO class responsible for database operations related to the appointments table.
 *
 */
public class AppointmentsDAO {
    /**
     * Method to add an appointment to the SQL database based on the passed appointment.
     *
     * @param appointment
     * @throws SQLException
     */
    public static void addAppointment(Appointments appointment) throws SQLException {
        String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, appointment.getTitle());
            preparedStatement.setString(2, appointment.getDescription());
            preparedStatement.setString(3, appointment.getLocation());
            // Trimmed any leading or trailing white space in type as it was causing issues for report 1 displaying.
            preparedStatement.setString(4, appointment.getType().trim());
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
     * Method to update an appointment within the database based on the passed appointment.
     *
     * @param apptId
     * @param updatedAppointment
     * @throws SQLException
     */
    public static void editAppointment(int apptId, Appointments updatedAppointment) throws SQLException {
        String query = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type =?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedAppointment.getTitle());
            statement.setString(2, updatedAppointment.getDescription());
            statement.setString(3, updatedAppointment.getLocation());
            // Trimmed any leading or trailing white space in type as it was causing issues for report 1 displaying.
            statement.setString(4, updatedAppointment.getType().trim());
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

    /**
     * Method to check if there is an overlap for the passed customer (customerId).
     *
     * @param customerId
     * @param startTimeStamp
     * @param endTimeStamp
     * @return
     * @throws SQLException
     */
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

    /**
     * Method to check if there is an overlap for the passed customer (customerId). Excludes the appointment being edited (apptId).
     *
     * @param customerId
     * @param apptId
     * @param startTimeStamp
     * @param endTimeStamp
     * @return
     * @throws SQLException
     */
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
     * Method to retrieve any upcoming appointments for use in the MainMenu controller table.
     *
     * @param userId
     * @param nowString
     * @param endTimeString
     * @return
     * @throws SQLException
     */
    public static ObservableList<List<String>> getUpcomingAppointments(int userId, String nowString, String endTimeString) throws SQLException {
        ObservableList<List<String>> appointmentsList = FXCollections.observableArrayList();

        String query = "SELECT Appointment_ID, Start, End " +
                "FROM Appointments " +
                "WHERE User_ID = ? " +
                "AND Start >= ? AND Start <= ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, nowString);
            statement.setString(3, endTimeString);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<String> appointment = new ArrayList<>();
                appointment.add(resultSet.getString("Appointment_ID"));
                appointment.add(resultSet.getString("Start"));
                appointment.add(resultSet.getString("End"));
                appointmentsList.add(appointment);
            }
        }
        return appointmentsList;
    }

    /**
     * Method to retrieve the distinct appointment types from the database for use in Report 1.
     *
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getDistinctAppointmentTypes() throws SQLException {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT Type FROM Appointments";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                appointmentTypes.add(type);
            }
        }
       return appointmentTypes;
    }

    /**
     * Method to get the number of appointments by a user specified type.
     *
     * @param selectedType
     * @return
     * @throws SQLException
     */
    public static int getAppointmentCountByType(String selectedType) throws SQLException{
        int count = 0;
        String query = "SELECT COUNT(*) FROM appointments WHERE Type = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, selectedType);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    /**
     * Method to get the number of appointments by a user specified type. Year is predefined as the current year.
     *
     * @param year
     * @param month
     * @return
     * @throws SQLException
     */
    public static int getAppointmentCountByMonth(int year, int month) throws SQLException {
        int count = 0;

        String query = "SELECT COUNT(*) FROM appointments WHERE MONTH(Start) = ? AND YEAR(Start) = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, month);
            statement.setInt(2, year);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    /**
     * Method to get the number of appointments by a user specified type AND by month. Year is predefined as the current year.
     *
     * @param type
     * @param month
     * @param year
     * @return
     * @throws SQLException
     */
    public static int getAppointmentCountByTypeAndMonth(String type, int month, int year) throws SQLException {
        int count = 0;

        String query = "SELECT COUNT(*) FROM appointments WHERE Type = ? AND MONTH(Start) = ? AND YEAR(Start) = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            statement.setInt(2, month);
            statement.setInt(3, year);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    /**
     * Method to create a list of all the appointments that are in the SQL database. Returns the data to display.
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

    /**
     * Method to delete the customer appointments if the selected customer is attempting to be deleted. This is because of foreign key
     * restraints.
     *
     * @param customerId
     * @throws SQLException
     */
    public void deleteCustomerAppointments(int customerId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }

    }

    /**
     * Method to delete an appointment based on the passed appointment ID.
     *
     * @param apptId
     * @throws SQLException
     */
    public void deleteAppointment(int apptId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, apptId);
            statement.executeUpdate();
        }
    }

    /**
     * Method to filter appointments based on start/end times. Used to filter by week and month currently (based on radio button selection).
     * Could be further expanded to allow a user to input specified times.
     *
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
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

    /**
     * Method to retrieve all appointment data based on a user selected contact for display in the Report 2 menu.
     *
     * @param contact
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointments> getAllAppointmentsByContact(String contact) throws SQLException {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String query = "SELECT Appointments.*, Contacts.Contact_Name " +
                "FROM Appointments " +
                "JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                "WHERE Contact_Name = ? " +
                "ORDER BY Start";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact);

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

    /**
     * Method to retrieve all appointment data based on a user selected user for display in the Report 3 menu.
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointments> getAllAppointmentsByUser(int userId) throws SQLException {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String query = "SELECT Appointments.*, Contacts.Contact_Name " +
                "FROM Appointments " +
                "JOIN Contacts ON Appointments.Contact_ID = Contacts.Contact_ID " +
                "WHERE User_ID = ? " +
                "ORDER BY Start";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

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
