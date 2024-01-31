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

import java.io.IOException;
import java.sql.SQLException;

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
    private ComboBox startTimeComboBox;
    @FXML
    private ComboBox endTimeComboBox;
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField userIdTextField;
    private ContactsDAO contactsDAO;

    @FXML
    private void initialize() throws SQLException {
        contactsDAO = new ContactsDAO();
        loadContactsData();
    }

    private void loadContactsData() throws SQLException {
        ObservableList<String> contacts = contactsDAO.getAllContacts();
        contactComboBox.setItems(contacts);
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
        // Figure out the start/end times, for testing purposes will just use current timestamp
        String user = "admin"; //will need a method to get user, can reuse one from the customer.
        String time = dateTimeUtils.getCurrentTimestamp();
        int customerId = Integer.parseInt(customerIdTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());
        String contactId = "1"; // Will need to add some methods to the corresponding DAO to turn the combo box contact
        //name into the corresponding int, similar to the division I did for customer.

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty()) {
            alertDisplay(1);
        } else {
            Appointments appointment = new Appointments(-1, title, description, location, type, time, time, time, user, time, user, customerId, userId, contactId);

            AppointmentsDAO.addAppointment(appointment);
            alertDisplay(2);
            Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }

    private void alertDisplay(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Action invalid");
                alert.setContentText("Please enter text in ALL fields and select a start and end time as well as a contact.");
                alert.showAndWait();
            }
            case 2 -> {
                alert.setTitle("Saved");
                alert.setHeaderText("Appointment Saved");
                alert.setContentText("Press OK to continue.");
                alert.showAndWait();
            }
        }
    }
}
