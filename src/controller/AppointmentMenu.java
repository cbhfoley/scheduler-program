package controller;

import dao.AppointmentsDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import utils.alertUtils;
import utils.dateTimeUtils;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller class to display appointment information in a table view. Users can interact in various ways to add, delete,
 * or edit an appointment. Also allows for filtering utilizing radio buttons.
 *
 */
public class AppointmentMenu {
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
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;


    private Appointments selectedAppointment;

    /**
     * Initializes the AppointmentMenu controller.
     * Sets up the cell value factories for table columns and then calls the loadAppointmentsData method.
     *
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
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

        loadAppointmentsData();
    }

    /**
     * Calls the convertToLocal which converts the passed timestamp to the users local time (based on their machine settings).
     *
     * @param utcTimestamp
     * @return
     */
    private String convertToLocal(String utcTimestamp) {
        return dateTimeUtils.convertToLocal(utcTimestamp);
    }

    /**
     * Method to load the appointments data into the table view.
     * When called it checks which radio button is selected and fills the table with the correct appointments.
     * When the form is first initialized the "All" radio button is selected which loads all appointments in the database.
     *
     * @throws SQLException
     */
    private void loadAppointmentsData() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        ObservableList<Appointments> appointmentsList;

        if (weekRadioButton.isSelected()) {
            LocalDateTime startOfWeek = dateTimeUtils.getStartOfWeek();
            LocalDateTime endOfWeek = dateTimeUtils.getEndOfWeek();
            appointmentsList = appointmentsDAO.getAppointmentsFiltered(startOfWeek, endOfWeek);
        }
        else if (monthRadioButton.isSelected()) {
            LocalDateTime startOfMonth = dateTimeUtils.getStartOfMonth();
            LocalDateTime endOfMonth = dateTimeUtils.getEndOfMonth();
            appointmentsList = appointmentsDAO.getAppointmentsFiltered(startOfMonth, endOfMonth);
        }
        else {
            appointmentsList = appointmentsDAO.getAllAppointments();
        }


        for (Appointments appointments : appointmentsList) {
            appointments.setStart(convertToLocal(appointments.getStart()));
            appointments.setEnd(convertToLocal(appointments.getEnd()));
        }

        appointmentsTableView.setItems(appointmentsList);
    }

    /**
     * Handler that calls the loadAppointmentsData() method when a new radio button is selected.
     *
     * @throws SQLException
     */
    @FXML
    private void handleRadioButtonAction() throws SQLException {
        loadAppointmentsData();
    }

    /**
     * Loads the mainMenu scene when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the addAppointment scene when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addAppointmentButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Checks to make sure an appointment is selected when clicked. If no appointment is selected displays an alert indicating as such.
     * If an appointment is selected it loads the editAppointment scene and passes the selected appointment.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void editAppointmentButtonAction(ActionEvent actionEvent) throws IOException {
        selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            alertUtils.alertDisplay(11);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editAppointment.fxml"));
            Parent parent = loader.load();
            // Passes the selected appointment to the EditAppointment controller
            EditAppointment editAppointmentController = loader.getController();
            editAppointmentController.setAppointmentToEdit(selectedAppointment);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            generalUtils.centerOnScreen(stage);
            stage.show();
        }
    }

    /**
     * Checks to make sure an appointment is selected when clicked. If no appointment is selected displays an alert indicating as such.
     * If an appointment is selected it calls the method do delete the selected appointment from the database.
     *
     * @throws SQLException
     */
    public void deleteAppointmentButtonAction() throws SQLException {
        selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            alertUtils.alertDisplay(11);
        } else {
            // Alert that confirms if the user wants to do this by either pressing YES or NO. If X is pressed it's the same as NO.
            Alert alertConf = new Alert(Alert.AlertType.CONFIRMATION);
            alertConf.setTitle("Confirmation");
            alertConf.setHeaderText("Deletion Confirmation");
            alertConf.setContentText("This will delete the selected appointment. " +
                    "Are you sure you want to do this?");
            alertConf.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alertConf.showAndWait();
            if (result.orElse(ButtonType.NO) == ButtonType.YES){
                // Deletes the appointment. Displays an alert indicating as such, and reloads the appointments data to reflect the deletion.
                deleteAppointment();
                // Displays an alert indicating success of deleted appoint with the appointment ID and type
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Success");
                alertSuccess.setHeaderText("Appointment Deleted");
                alertSuccess.setContentText("Appointment #" + selectedAppointment.getApptId() + " of type (" + selectedAppointment.getType() +
                        ") has been successfully deleted.");
                alertSuccess.showAndWait();
                loadAppointmentsData();
            }
        }
    }

    /**
     * Method that calls the delete appointment method in the appointmentsDAO. Passes the selected appointments appointment ID
     * for the query to locate and delete the appointment.
     *
     * @throws SQLException
     */
    private void deleteAppointment() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        appointmentsDAO.deleteAppointment(selectedAppointment.getApptId());
    }
}
