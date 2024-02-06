package controller;

import dao.AppointmentsDAO;
import dao.ContactsDAO;
import dao.CustomerDAO;
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
import java.util.Objects;


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
    private ComboBox<String> customerIdComboBox;

    private ContactsDAO contactsDAO;
    private CustomerDAO customerDAO;

    @FXML
    private void initialize() throws SQLException {
        contactsDAO = new ContactsDAO();
        customerDAO = new CustomerDAO();
        loadContactsData();
        populateTimeComboBoxes();
        populateCustomerComboBox();

        // Sets the end date DatePicker to the value of the start date DatePicker.
        // The only scenario where the start and end date would be different would be if an appointment was scheduled
        // in a time zone that was within business hours, but it overlapped at midnight in the local users' timezone.
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDatePicker.setValue(newValue);
        });
    }

    private void populateCustomerComboBox() throws SQLException {
        ObservableList<String> customerNames = customerDAO.getAllCustomerNames();
        customerIdComboBox.setItems(customerNames);
    }

    private void loadContactsData() throws SQLException {
        ObservableList<String> contacts = contactsDAO.getAllContacts();
        contactComboBox.setItems(contacts);
    }

    private void populateTimeComboBoxes() {
        for (int hour = 0; hour <24; hour++) {
            for (int minute = 0; minute <60; minute += 15) {
                String formattedTime = String.format("%02d:%02d", hour,minute);
                startTimeComboBox.getItems().add(formattedTime);
                endTimeComboBox.getItems().add(formattedTime);
            }
        }
    }

    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    public void saveButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        String customer = customerIdComboBox.getValue();
        String contact = contactComboBox.getValue();
        LocalDate selectedStartDate = startDatePicker.getValue();
        LocalDate selectedEndDate = endDatePicker.getValue();
        String startTime = startTimeComboBox.getValue();
        String endTime = endTimeComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()  || customer == null
                || contact == null || selectedStartDate == null || selectedEndDate == null || startTime == null ||endTime == null) {
            alertUtils.alertDisplay(1);
            return;
        }
        if (!dateTimeUtils.isStartBeforeEnd(selectedStartDate, startTime, selectedEndDate, endTime)) {
            alertUtils.alertDisplay(9);
            return;
        }
        if (!dateTimeUtils.isWithinBusinessHours(selectedStartDate, startTime, selectedEndDate, endTime)) {
            alertUtils.alertDisplay(10);
        }
        else {
            int customerId = customerDAO.getCustomerIdByName(customer);
            int contactId = contactsDAO.getContactIdByName(contact);
            String contactIdString = String.valueOf(contactId);
            String startTimeStamp = dateTimeUtils.convertToUTC(dateTimeUtils.combineDateTime(selectedStartDate, startTime));
            String endTimeStamp = dateTimeUtils.convertToUTC(dateTimeUtils.combineDateTime(selectedEndDate, endTime));
            String user = generalUtils.getLoginUsername();
            String localTimeStamp = dateTimeUtils.getCurrentTimestamp();
            String utcTimeStamp = dateTimeUtils.convertToUTC(localTimeStamp);

            Appointments appointment = new Appointments(-1, title, description, location, type, startTimeStamp, endTimeStamp, utcTimeStamp, user, utcTimeStamp, user, customerId, 1, contactIdString);

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
