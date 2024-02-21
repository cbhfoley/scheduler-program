package controller;

import dao.AppointmentsDAO;
import dao.ContactsDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Appointments;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;

import static utils.dateTimeUtils.convertToLocal;

/**
 * Controller class responsible for displaying Report 2. Allows a user to select a contact and displays appointment
 *  information for that contact in a table view.
 *
 */
public class Report2Menu {
    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> apptIdColumn;
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    @FXML
    private TableColumn<Appointments, String> descriptionColumn;
    @FXML
    private TableColumn<Appointments, String> locationColumn;
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    @FXML
    private TableColumn<Appointments, String> startColumn;
    @FXML
    private TableColumn<Appointments, String> endColumn;
    @FXML
    private TableColumn<Appointments, Integer> custIdColumn;
    @FXML
    private TableColumn<Appointments, Integer> userIdColumn;
    @FXML
    private TableColumn<Appointments, String> contactColumn;
    @FXML
    private ComboBox<String> contactComboBox;

    private ContactsDAO contactsDAO;

    public void initialize() throws SQLException {
        contactsDAO = new ContactsDAO();
        // Set up cell value factories for table columns using lambda expressions
        apptIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getApptId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        custIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        contactColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        // Each lambda expression extracts a property value from the cell data and creates a corresponding object property

        populateContactsComboBox();
    }

    private void populateContactsComboBox() throws SQLException {
        ObservableList<String> contacts = contactsDAO.getAllContacts();
        contactComboBox.setItems(contacts);
    }

    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/reportsMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();

    }

    /**
     * On action event for the contact combo box that calls the loadAppointmentData() method.
     *
     * @throws SQLException
     */
    public void handleContactComboBoxAction() throws SQLException {
        loadAppointmentData();
    }

    /**
     * Method to load the appointment data into the table. This passes in the contact to the method to retrieve the
     * desired appointments.
     *
     * @throws SQLException
     */
    private void loadAppointmentData() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        ObservableList<Appointments> appointmentsList;
        String contact = contactComboBox.getValue();

        appointmentsList = appointmentsDAO.getAllAppointmentsByContact(contact);

        for (Appointments appointments : appointmentsList) {
            appointments.setStart(convertToLocal(appointments.getStart()));
            appointments.setEnd(convertToLocal(appointments.getEnd()));
        }

        appointmentsTableView.setItems(appointmentsList);

    }
}
