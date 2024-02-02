package controller;

import dao.AppointmentsDAO;
import dao.ContactsDAO;
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


public class AddAppointment {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea descriptionTextArea;
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
    private TextField customerIdTextField;
    @FXML
    private TextField userIdTextField;
    private ContactsDAO contactsDAO;

    @FXML
    private void initialize() throws SQLException {
        contactsDAO = new ContactsDAO();
        loadContactsData();
        populateTimeComboBoxes();

        // Sets the end date DatePicker to the value of the start date DatePicker.
        // The only scenario where the start and end date would be different would be if an appointment was scheduled
        // an appointment from a time zone that was within business hours, but it was near midnight.
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDatePicker.setValue(newValue); // Set endDatePicker to the selected date of startDatePicker
        });
    }

    private void loadContactsData() throws SQLException {
        ObservableList<String> contacts = contactsDAO.getAllContacts();
        contactComboBox.setItems(contacts);
    }

    private void populateTimeComboBoxes() {
        for (int hour = 0; hour <24; hour++) {
            for (int minute = 0; minute <60; minute += 15) {
                String formattedTime = String.format("%02d;%02d", hour,minute);
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
        String description = descriptionTextArea.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();

        LocalDate selectedStart = startDatePicker.getValue();

        // Figure out the start/end times, for testing purposes will just use current timestamp
        String user = generalUtils.getLoginUsername();
        String currentTime = dateTimeUtils.getCurrentTimestamp();
        int customerId = Integer.parseInt(customerIdTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());
        String contactId = "1"; // Will need to add some methods to the corresponding DAO to turn the combo box contact
        //name into the corresponding int, similar to the division I did for customer.

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()) {
            alertUtils.alertDisplay(1);
        } else {
            Appointments appointment = new Appointments(-1, title, description, location, type, currentTime, currentTime, currentTime, user, currentTime, user, customerId, userId, contactId);

            AppointmentsDAO.addAppointment(appointment);
            alertUtils.alertDisplay(2);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }


}
