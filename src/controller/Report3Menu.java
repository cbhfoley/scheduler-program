package controller;

import dao.AppointmentsDAO;
import dao.UserDAO;
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

public class Report3Menu {
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
    private TableColumn<Appointments, String> userColumn;
    @FXML
    private TableColumn<Appointments, String> contactColumn;
    @FXML
    private ComboBox<String> userComboBox;

    private UserDAO userDAO;

    public void initialize() throws SQLException {
        userDAO = new UserDAO();

        apptIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getApptId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        custIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        // Displays the User_Name as opposed to the User_ID to make viewing this report easier.
        userColumn.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserId();
            String userName = null;
            try {
                userName = userDAO.getUserNameById(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(userName);
        });
        contactColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));

        populateUsersComboBox();
    }

    /**
     * Sets the usersComboBox with the User_Names in the database.
     *
     * @throws SQLException
     */
    private void populateUsersComboBox() throws SQLException {
        ObservableList<String> users = userDAO.getAllUserNames();
        userComboBox.setItems(users);
    }

    /**
     * Goes back one screen (to the reports menu) when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/reportsMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();

    }

    /**
     * Handles when the combo box item (user) is selected. Calls the loadAppointmentsData() method.
     *
     * @throws SQLException
     */
    public void handleUserComboBoxAction() throws SQLException {
        loadAppointmentData();
    }

    /**
     * Method to load the appointments based off of selected User. Converts the User_Name to the User_ID and runs the
     * method in the UserDAO to return all appointments associated with that user.
     *
     * @throws SQLException
     */
    private void loadAppointmentData() throws SQLException {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        ObservableList<Appointments> appointmentsList;
        String user = userComboBox.getValue();
        int userId = UserDAO.getUserIdByName(user);

        appointmentsList = appointmentsDAO.getAllAppointmentsByUser(userId);

        for (Appointments appointments : appointmentsList) {
            appointments.setStart(convertToLocal(appointments.getStart()));
            appointments.setEnd(convertToLocal(appointments.getEnd()));
        }

        appointmentsTableView.setItems(appointmentsList);

    }
}
