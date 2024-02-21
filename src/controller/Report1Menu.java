package controller;

import dao.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.generalUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;

/**
 * Controller class responsible for displaying Report 1. Allows users to select a type, month, or both and updates gives
 * information based on the user selected parameters.
 *
 */
public class Report1Menu {
    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private ComboBox<Month> reportMonthComboBox;
    @FXML
    private TextField byTypeTextField;
    @FXML
    private TextField byMonthTextField;
    @FXML
    private TextField byBothTextField;
    @FXML
    private Label currentYearLabel;

    /**
     * Initializes the Report1Menu controller. Sets the items in the reportTypeComboBox and reportMonthComboBox respectively.
     * Updates the current year label to the current year.
     *
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        populateReportTypeComboBox();
        populateReportMonthComboBox();
        int year = java.time.LocalDate.now().getYear();
        currentYearLabel.setText(String.valueOf(year));
    }

    /**
     * Method to populate the reportMonthComboBox with the 12 months.
     *
     */
    private void populateReportMonthComboBox() {
        ObservableList<Month> months = FXCollections.observableArrayList();
        months.addAll(Month.values());
        reportMonthComboBox.setItems(months);
    }

    /**
     * Method to populate the reportTypeComboBox with the current appointment types in the database.
     *
     * @throws SQLException
     */
    private void populateReportTypeComboBox() throws SQLException {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList(AppointmentsDAO.getDistinctAppointmentTypes());
        reportTypeComboBox.setItems(appointmentTypes);
    }

    /**
     * Loads the ReportsMenu when clicked.
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
     * Event handler for the reportTypeComboBox. Updates the byTypeTextField with the number of appointments based on
     * the users selection. Calls the handleBothSelection() method.
     *
     * @throws SQLException
     */
    public void handleTypeSelection() throws SQLException {
        String selectedType = reportTypeComboBox.getValue();
        int count = AppointmentsDAO.getAppointmentCountByType(selectedType);
        byTypeTextField.setText(Integer.toString(count));
        handleBothSelection();
    }

    /**
     * Event handler for the reportMonthComboBox. Updates the byMonthTextField with the number of appointments based on
     * the users selection. Calls the handleBothSelection() method.
     *
     * @throws SQLException
     */
    public void handleMonthSelection() throws SQLException {
        Month selectedMonth = reportMonthComboBox.getValue();
        int year = java.time.LocalDate.now().getYear();
        int month = selectedMonth.getValue();
        int count = AppointmentsDAO.getAppointmentCountByMonth(year, month);
        byMonthTextField.setText(String.valueOf(count));
        handleBothSelection();
    }

    /**
     * Method to update the byBothTextField as long as there is a selection made in both combo boxes.
     * Displays the number of appointments by type AND month by setting the byBothTextField to that value.
     *
     * @throws SQLException
     */
    private void handleBothSelection() throws SQLException {
        String selectedType = reportTypeComboBox.getValue();
        Month selectedMonth = reportMonthComboBox.getValue();


        if (selectedType != null && selectedMonth != null) {
            int year = java.time.LocalDate.now().getYear();
            int month = selectedMonth.getValue();
            int count = AppointmentsDAO.getAppointmentCountByTypeAndMonth(selectedType, month, year);
            byBothTextField.setText(String.valueOf(count));
        }
    }
}
