package controller;

import dao.AppointmentsDAO;
import dao.ContactsDAO;
import dao.CustomerDAO;
import dao.UserDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;
import utils.dateTimeUtils;
import utils.generalUtils;
import utils.alertUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller class for handling the adding of new appointments.
 * Allows users to input appointment parameters and save them as new appointments to the database.
 *
 */

public class AddAppointment {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private TextField typeTextField;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private ComboBox<String> userComboBox;

    private ContactsDAO contactsDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;

    /**
     * Initializes the AddAppointment controller with the combo boxes populated with their respective data. Includes listeners to allow for a
     * better UX when choosing a date/time for an appointment.
     *
     * @throws SQLException
     */
    @FXML
    private void initialize() throws SQLException {
        contactsDAO = new ContactsDAO();
        customerDAO = new CustomerDAO();
        userDAO = new UserDAO();
        populateContactsComboBox();
        populateTimeComboBoxes();
        populateCustomerComboBox();
        populateUserComboBox();

        // Sets the end date DatePicker to the value of the start date DatePicker.
        // The most likely scenario where the start and end date would be different would be if an appointment was scheduled
        // in a time zone that was within business hours, but it overlapped at midnight in the local users' timezone.
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDatePicker.setValue(newValue);
        });
        // Sets the end time combo box to 1 hour after the start time combo box. End time can still be changed if need be.
        // This allows for the most common appointment length (1 hour) to be set up with ease.
        startTimeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalTime startTime = LocalTime.parse(newValue);
            LocalTime endTime = startTime.plusHours(1);
            endTimeComboBox.setValue(String.valueOf(endTime));
        });
    }

    /**
     * Method to set the items in the userComboBox to the users in the database.
     *
     * @throws SQLException
     */
    private void populateUserComboBox() throws SQLException {
        ObservableList<String> userNames = userDAO.getAllUserNames();
        userComboBox.setItems(userNames);
    }

    /**
     * Method to set the items in the customerComboBox to the customers in the database.
     *
     * @throws SQLException
     */
    private void populateCustomerComboBox() throws SQLException {
        ObservableList<String> customerNames = customerDAO.getAllCustomerNames();
        customerComboBox.setItems(customerNames);
    }

    /**
     * Method to set the items in the contactsComboBox to the contacts in the database.
     *
     * @throws SQLException
     */
    private void populateContactsComboBox() throws SQLException {
        ObservableList<String> contacts = contactsDAO.getAllContacts();
        contactComboBox.setItems(contacts);
    }

    /**
     * Method to set the timeComboBoxes with times incrementing every 15 minutes from 00:00 to 23:45.
     *
     */
    private void populateTimeComboBoxes() {
        for (int hour = 0; hour <24; hour++) {
            for (int minute = 0; minute <60; minute += 15) {
                String formattedTime = String.format("%02d:%02d", hour,minute);
                startTimeComboBox.getItems().add(formattedTime);
                endTimeComboBox.getItems().add(formattedTime);
            }
        }
    }

    /**
     * Reloads the appointmentMenu without saving anything to the database if the Cancel button is clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Saves the proposed appointment to the database when clicked. Has various data validation checks to ensure the suggested appointment
     * is allowed by the database and also checks for other various things such as the suggested time compared to other appointments
     * and the pre-defined business hours.
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void saveButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        // Variables to capture the input data
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        String customer = customerComboBox.getValue();
        String contact = contactComboBox.getValue();
        LocalDate selectedStartDate = startDatePicker.getValue();
        LocalDate selectedEndDate = endDatePicker.getValue();
        String startTime = startTimeComboBox.getValue();
        String endTime = endTimeComboBox.getValue();
        String user = userComboBox.getValue();
        // Checks that information is entered in all user input fields ONLY.
        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()  || customer == null
                || contact == null || selectedStartDate == null || selectedEndDate == null || startTime == null ||endTime == null || user == null) {
            alertUtils.alertDisplay(1);
            return;
        }
        // Checks that the start time entered makes logical sense (start time has to be before end).
        if (!dateTimeUtils.isStartBeforeEnd(selectedStartDate, startTime, selectedEndDate, endTime)) {
            alertUtils.alertDisplay(9);
            return;
        }
        // Checks that the suggested appointment time is within the defined business hours.
        if (!dateTimeUtils.isWithinBusinessHours(selectedStartDate, startTime, selectedEndDate, endTime)) {
            alertUtils.alertDisplay(10);
        }
        else {
            // Takes the input data and converts it as needed for database entry, such as combining the suggested time or returning contact ID based on selected contact.
            int customerId = customerDAO.getCustomerIdByName(customer);
            int contactId = contactsDAO.getContactIdByName(contact);
            String contactIdString = String.valueOf(contactId);
            String startTimeStamp = dateTimeUtils.convertToUTC(dateTimeUtils.combineDateTime(selectedStartDate, startTime));
            String endTimeStamp = dateTimeUtils.convertToUTC(dateTimeUtils.combineDateTime(selectedEndDate, endTime));
            String localTimeStamp = dateTimeUtils.getCurrentTimestamp();
            String utcTimeStamp = dateTimeUtils.convertToUTC(localTimeStamp);
            int userId = UserDAO.getUserIdByName(user);
            // Checks if there is any overlap based on the Customer ID. The suggested appointment cannot be scheduled if the Customer has an appointment already scheduled within that time frame.
            if (AppointmentsDAO.isOverlap(customerId, startTimeStamp, endTimeStamp)) {
                alertUtils.alertDisplay(14);
            } else {
                // Creates an appointment with the user suggested inputs and saves it to the database.
                Appointments appointment = new Appointments(-1, title, description, location, type, startTimeStamp, endTimeStamp, utcTimeStamp, user, utcTimeStamp, user, customerId, userId, contactIdString);
                // Calls the add appointment method which will run successfully since data validation above was in place. Displays an alert after the appointment is saved indicating as such.
                AppointmentsDAO.addAppointment(appointment);
                alertUtils.alertDisplay(2);
                Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                generalUtils.centerOnScreen(stage);
                stage.show();
            }
        }

    }


}
