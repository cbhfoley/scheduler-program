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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Appointments;
import utils.dateTimeUtils;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;

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
        ObservableList<Appointments> appointmentsList = appointmentsDAO.getAllAppointments();

        for (Appointments appointments : appointmentsList) {
            appointments.setStart(convertToLocal(appointments.getStart()));
            appointments.setEnd(convertToLocal(appointments.getEnd()));
        }

        appointmentsTableView.setItems(appointmentsList);
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

    public void editAppointmentButtonAction(ActionEvent actionEvent) {
    }

    public void deleteAppointmentButtonAction(ActionEvent actionEvent) {
    }
}
