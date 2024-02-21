package controller;

import dao.AppointmentsDAO;
import dao.UserDAO;
import javafx.application.Platform;
import javafx.beans.Observable;
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
import utils.alertUtils;
import utils.dateTimeUtils;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller class for the main menu of the application. Displays upcoming appointments in a table view for a user to view.
 * Handles navigation to the various subsections of the application.
 *
 */
public class MainMenu {
    @FXML
    private TableView<List<String>> appointmentsTableView;
    @FXML
    private TableColumn<List<String>, String> idColumn;
    @FXML
    private TableColumn<List<String>, String> startColumn;
    @FXML
    private TableColumn<List<String>, String> endColumn;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean alertShown = false;

    /**
     * MainMenu initializes with a TableView that displays upcoming appointments. An alert is displayed after the scene
     * is loaded indicating if a user has an appointment within 15 minutes, or if they have none.
     * It is only displayed ONCE per login so as not to inundate the user with an alert every time they load the main menu.
     *
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(15);
        String nowString = now.format(dateTimeFormatter);
        String nowStringUtc = dateTimeUtils.convertToUTC(nowString);
        String endTimeString = endTime.format(dateTimeFormatter);
        String endTimeStringUtc = dateTimeUtils.convertToUTC(endTimeString);
        String user = generalUtils.getLoginUsername();
        int userId = UserDAO.getUserIdByName(user);
        // Retrieves the list (if there are any) of upcoming appointments using the current time and current time + 15 minutes.
        ObservableList<List<String>> upcomingAppointments = AppointmentsDAO.getUpcomingAppointments(userId, nowStringUtc, endTimeStringUtc);
        // Displays the information converted to local time.
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateTimeUtils.convertToLocal(cellData.getValue().get(1))));
        endColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateTimeUtils.convertToLocal(cellData.getValue().get(2))));

        appointmentsTableView.setItems(upcomingAppointments);

        if (!alertShown) {
            // Displays an alert after the form is loaded indicating if there are any upcoming appointments or not.
            // Lambda used to keep code concise and simple for this one-off alert.
            Platform.runLater(() -> {
                int alertType = upcomingAppointments.isEmpty() ? 16 : 17;
                alertUtils.alertDisplay(alertType);
                alertShown = true;
            });
        }
    }

    /**
     * Loads the customer menu scene when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void customersButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/customerMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the appointment menu scene when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void appointmentsButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointmentMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the login menu scene when clicked. This will require a user to re-authenticate their login.
     * It also sets the alertShown to false so any new login will prompt the alert for upcoming appointments.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void signOutButtonAction(ActionEvent actionEvent) throws IOException {
        alertShown = false;
        Parent parent = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }

    /**
     * Loads the reports menu when clicked.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void reportsButtonAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/reportsMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        generalUtils.centerOnScreen(stage);
        stage.show();
    }
}
