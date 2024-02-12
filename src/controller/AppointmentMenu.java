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

    @FXML
    public void initialize() throws SQLException {
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

        loadAppointmentsData();
    }

    private String convertToLocal(String utcTimestamp) {
        return dateTimeUtils.convertToLocal(utcTimestamp);
    }

    private void loadAppointmentsData() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        ObservableList<Appointments> appointmentsList = null;

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

    @FXML
    private void handleRadioButtonAction() throws SQLException {
        loadAppointmentsData();
    }

    public void mainMenuButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    public void addAppointmentButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    public void editAppointmentButtonAction(ActionEvent actionEvent) throws IOException {
        selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            alertUtils.alertDisplay(11);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editAppointment.fxml"));
            Parent parent = loader.load();

            EditAppointment editAppointmentController = loader.getController();
            editAppointmentController.setAppointmentToEdit(selectedAppointment);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            generalUtils.centerOnScreen(stage);
            stage.show();
        }
    }

    public void deleteAppointmentButtonAction() throws SQLException {
        selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            alertUtils.alertDisplay(11);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Deletion Confirmation");
            alert.setContentText("This will delete the selected appointment. " +
                    "Are you sure you want to do this?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(ButtonType.NO) == ButtonType.YES){
                deleteAppointment();
                alertUtils.alertDisplay(12);
                loadAppointmentsData();
            }
        }        
    }

    private void deleteAppointment() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        appointmentsDAO.deleteAppointment(selectedAppointment.getApptId());
    }
}
